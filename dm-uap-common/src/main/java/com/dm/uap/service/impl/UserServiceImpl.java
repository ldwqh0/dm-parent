package com.dm.uap.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.RoleDto;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.RoleRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.UserService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentRepository departmentRepository;

    private final DepartmentRepository dpr;

    private final QUser qUser = QUser.user;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserConverter userConverter, RoleRepository roleRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, DepartmentRepository dpr) {
        this.userRepository = userRepository;
        this.userConverter = userConverter;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.dpr = dpr;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = {"users"}, sync = true, key = "#username.toLowerCase()")
    public UserDetailsDto loadUserByUsername(String username) {
        return Optional.ofNullable(username)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findOneByUsernameIgnoreCase)
            .map(userConverter::toUserDetailsDto)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return Optional.ofNullable(mobile)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findByMobileIgnoreCase)
            .map(userConverter::toUserDetailsDto)
            .orElseThrow(() -> new UsernameNotFoundException(mobile));
    }

    @Override
    public boolean exist() {
        return userRepository.count() > 0;
    }

    @Override
    @Transactional
    public User save(UserDto userDto) {
        checkUsernameExists(userDto.getId(), userDto.getUsername());
        User user = new User();
        userConverter.copyProperties(user, userDto);
        String password = userDto.getPassword();
        if (StringUtils.isNotBlank(password)) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            user.setPassword(null);
        }
        addPostsAndRoles(user, userDto);
        user = userRepository.save(user);
        user.setOrder(user.getId());
        return user;
    }

    /**
     * 判断某个用户名是否被占用，检测用户ID!=指定ID
     *
     * @param id       用户ID
     * @param username 用户名称
     */
    private void checkUsernameExists(Long id, String username) {
        BooleanBuilder builder = new BooleanBuilder();
        if (Objects.nonNull(id)) {
            builder.and(qUser.id.ne(id));
        }

        if (StringUtils.isNotBlank(username)) {
            builder.and(qUser.username.eq(username));
        } else {
            throw new RuntimeException("The username can not be empty");
        }
        if (userRepository.exists(builder)) {
            throw new DataValidateException("用户名已被占用");
        }
    }

    @Override
    public Optional<User> get(long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()")
    public User delete(long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(a -> userRepository.deleteById(id));
        return user.orElseThrow(DataNotExistException::new);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()")
    public User update(long id, UserDto userDto) {
        checkUsernameExists(id, userDto.getUsername());
        User user = userRepository.getOne(id);
        userConverter.copyProperties(user, userDto);
        addPostsAndRoles(user, userDto);
        return user;
    }

    @Override
    public Page<User> search(String key, Pageable pageable) {
        if (StringUtils.isNotBlank(key)) {
            BooleanExpression expression = qUser.username.containsIgnoreCase(key)
                .or(qUser.fullname.containsIgnoreCase(key));
            return userRepository.findAll(expression, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    @Override
    public boolean checkPassword(long id, String password) {
        User user = userRepository.getOne(id);
        return !passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()")
    public User repassword(long id, String password) {
        User user = userRepository.getOne(id);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Override
    public Page<User> search(Long department, Long role, Long roleGroup, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (Objects.nonNull(department)) {
            Department dep = dpr.getOne(department);
            query.and(qUser.posts.containsKey(dep));
        }
        if (Objects.nonNull(role)) {
            query.and(qUser.roles.any().id.eq(role));
        }
        if (Objects.nonNull(roleGroup)) {
            query.and(qUser.roles.any().group.id.eq(roleGroup));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qUser.username.containsIgnoreCase(key)
                .or(qUser.fullname.containsIgnoreCase(key)));
        }
        return userRepository.findAll(query, pageable);
    }

    // 添加用户的职务和角色信息
    private void addPostsAndRoles(User model, UserDto dto) {
        List<UserPostDto> posts = dto.getPosts();
        List<RoleDto> _roles = dto.getRoles();
        if (CollectionUtils.isNotEmpty(posts)) {
            Map<Department, String> posts_ = new HashMap<>();
            posts.forEach(entry -> posts_.put(departmentRepository.getOne(entry.getDepartment().getId()), entry.getPost()));
            model.setPosts(posts_);
        } else {
            model.setPosts(Collections.emptyMap());
        }
        if (CollectionUtils.isNotEmpty(_roles)) {
            List<Role> roles = _roles.stream().map(RoleDto::getId).map(roleRepository::getOne)
                .collect(Collectors.toList());
            model.setRoles(roles);
        } else {
            model.setRoles(Collections.emptyList());
        }
    }

    @Override
    public boolean userExistsByUsername(Long id, String username) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.username.equalsIgnoreCase(username));
        if (Objects.nonNull(id)) {
            query.and(qUser.id.ne(id));
        }
        return userRepository.exists(query);
    }

    @Override
    public boolean userExistsByEmail(Long id, String email) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.email.equalsIgnoreCase(email));
        if (Objects.nonNull(id)) {
            query.and(qUser.id.ne(id));
        }
        return userRepository.exists(query);
    }

    @Override
    public boolean userExistsByMobile(Long id, String mobile) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.mobile.equalsIgnoreCase(mobile));
        if (Objects.nonNull(id)) {
            query.and(qUser.id.ne(id));
        }
        return userRepository.exists(query);
    }

    @Override
    @Transactional
    public User patch(long id, UserDto user) {
        User originUser = userRepository.getOne(id);
        if (Objects.nonNull(user.getEnabled())) {
            originUser.setEnabled(user.getEnabled());
        }
        return userRepository.save(originUser);
    }

    @Override
    public Optional<User> findByMobile(String mobile) {
        return userRepository.findByMobileIgnoreCase(mobile);
    }

}
