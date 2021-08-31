package com.dm.uap.service.impl;

import com.dm.collections.ArrayUtils;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Maps;
import com.dm.collections.Sets;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.dto.UserRoleDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.dm.uap.entity.UserRole;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.repository.UserRoleRepository;
import com.dm.uap.service.UserService;
import com.dm.util.Assert;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentRepository departmentRepository;

    private final UserRoleRepository userRoleRepository;


    private final QUser qUser = QUser.user;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, DepartmentRepository departmentRepository, UserRoleRepository userRoleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepository = departmentRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    public boolean exist() {
        return userRepository.count() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "#username.toLowerCase()")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findOneByUsernameIgnoreCase)
            .map(UserConverter::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    public org.springframework.security.core.userdetails.UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return Optional.ofNullable(mobile)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findByMobileIgnoreCase)
            .map(UserConverter::toUserDetails)
            .orElseThrow(() -> new UsernameNotFoundException(mobile));
    }


    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public UserDto save(UserDto userDto) {
        validationUser(userDto);
        String password = userDto.getPassword();
        // 密码不能为空
        Assert.notEmpty(password).orElseThrow(() -> new DataValidateException("用户密码不能为空"));
        User user = copyProperties(new User(), userDto);
        user.setPassword(passwordEncoder.encode(password));
        addPostsAndRoles(user, userDto);
        return UserConverter.toDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id).map(UserConverter::toDto);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public void delete(long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
        user.orElseThrow(DataNotExistException::new);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public UserDto update(long id, UserDto userDto) {
        validationUser(userDto, id);
        User user = this.copyProperties(userRepository.getById(id), userDto);
        addPostsAndRoles(user, userDto);
        return UserConverter.toDto(userRepository.save(user));
    }

    @Override
    public boolean checkPassword(long id, String password) {
        User user = userRepository.getById(id);
        return !passwordEncoder.matches(password, user.getPassword());
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public UserDto resetPassword(long id, String password) {
        User user = userRepository.getById(id);
        user.setPassword(passwordEncoder.encode(password));
        return UserConverter.toDto(user);
    }

    @Override
    public Page<UserDto> search(Long department, Long role, String roleGroup, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (Objects.nonNull(department)) {
            Department dep = departmentRepository.getById(department);
            query.and(qUser.posts.containsKey(dep));
        }
        if (Objects.nonNull(role)) {
            query.and(qUser.roles.any().id.eq(role));
        }
        if (Objects.nonNull(roleGroup)) {
            query.and(qUser.roles.any().group.eq(roleGroup));
        }
        if (StringUtils.isNotBlank(key)) {
            query.and(qUser.username.containsIgnoreCase(key)
                .or(qUser.fullname.containsIgnoreCase(key)));
        }
        return userRepository.findAll(query, pageable).map(UserConverter::toDto);
    }

    // 添加用户的职务和角色信息
    private void addPostsAndRoles(User model, UserDto dto) {
        List<UserPostDto> posts = dto.getPosts();
        if (CollectionUtils.isNotEmpty(posts)) {
            Map<Department, String> posts_ = Maps.map(posts, it -> departmentRepository.getByDto(it.getDepartment()), UserPostDto::getPost);
            model.setPosts(posts_);
        } else {
            model.setPosts(Collections.emptyMap());
        }
        model.setRoles(Sets.transform(dto.getRoles(), this::toModel));
    }

    /**
     * 根据dto获取role实体，如果role不存在，新增一个
     *
     * @param dto 要转换为model的role dto
     */
    private UserRole toModel(UserRoleDto dto) {
        Long id = dto.getId();
        UserRole role = userRoleRepository.existsById(id) ? userRoleRepository.getById(id) : new UserRole();
        role.setId(dto.getId());
        role.setGroup(dto.getGroup());
        role.setName(dto.getName());
        return userRoleRepository.save(role);
    }

    /**
     * 检测一个用户名是否已经存在
     *
     * @param username 要检测的用户名
     * @param excludes 要排除的user id
     */
    @Override
    public boolean userExistsByUsername(String username, Long... excludes) {
        BooleanExpression expression = qUser.username.equalsIgnoreCase(username);
        List<Long> excludesList = Arrays.stream(excludes).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(excludesList)) {
            expression = expression.and(qUser.id.notIn(excludesList));
        }
        return userRepository.exists(expression);
    }

    /**
     * 检测指定的email是否已经存在，并且
     *
     * @param email    要检测的email
     * @param excludes 要排除的用户
     */
    @Override
    public boolean userExistsByEmail(String email, Long... excludes) {
        BooleanExpression expression = qUser.email.equalsIgnoreCase(email);
        if (ArrayUtils.isNotEmpty(excludes)) {
            expression = expression.and(qUser.id.notIn(excludes));
        }
        return userRepository.exists(expression);
    }

    @Override
    public boolean userExistsByMobile(String mobile, Long... excludes) {
        BooleanExpression expression = qUser.mobile.equalsIgnoreCase(mobile);
        if (ArrayUtils.isNotEmpty(excludes)) {
            expression = expression.and(qUser.id.notIn(excludes));
        }
        return userRepository.exists(expression);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public UserDto patch(long id, UserDto user) {
        User originUser = userRepository.getById(id);
        if (Objects.nonNull(user.getEnabled())) {
            originUser.setEnabled(user.getEnabled());
        }
        return UserConverter.toDto(userRepository.save(originUser));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto saveOwnerInfo(Long userId, UserDto user) {
        User originUser = userRepository.getById(userId);
        originUser.setBirthDate(user.getBirthDate());
        originUser.setDescription(user.getDescription());
        originUser.setEmail(user.getEmail());
        originUser.setFullname(user.getFullname());
        originUser.setMobile(user.getMobile());
        originUser.setNo(user.getNo());
        originUser.setRegionCode(user.getRegionCode());
        originUser.setScenicName(user.getScenicName());
        originUser.setUsername(user.getUsername());
        return UserConverter.toDto(userRepository.save(originUser));
    }


    private void validationUser(UserDto user, Long... exclude) {
        String mobile = user.getMobile();
        String email = user.getEmail();
        Assert.from(user.getUsername(), v -> !userExistsByUsername(v, exclude)).orElseThrow(() -> new DataValidateException("用户名已经存在"));
        if (StringUtils.isNotBlank(mobile)) {
            Assert.from(mobile, v -> !userExistsByUsername(v, exclude)).orElseThrow(() -> new DataValidateException("手机号已经被占用"));
        }
        if (StringUtils.isNotBlank(email)) {
            Assert.from(email, v -> !userExistsByEmail(v, exclude)).orElseThrow(() -> new DataValidateException("邮箱已经被占用"));
        }
    }


    private User copyProperties(User user, UserDto userDto) {
        if (Objects.nonNull(user) && Objects.nonNull(userDto)) {
            user.setEnabled(userDto.getEnabled());
            user.setUsername(userDto.getUsername());
            user.setFullname(userDto.getFullname());
            user.setProfilePhoto(userDto.getProfilePhoto());
            user.setMobile(userDto.getMobile());
            user.setDescription(userDto.getDescription());
            user.setEmail(userDto.getEmail());
            user.setScenicName(userDto.getScenicName());
            user.setRegionCode(userDto.getRegionCode());
            user.setBirthDate(userDto.getBirthDate());
            user.setNo(userDto.getNo());
        }
        return user;
    }
}
