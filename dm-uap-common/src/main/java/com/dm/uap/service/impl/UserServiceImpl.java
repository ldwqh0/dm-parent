package com.dm.uap.service.impl;

import com.dm.collections.CollectionUtils;
import com.dm.common.exception.DataNotExistException;
import com.dm.common.exception.DataValidateException;
import com.dm.security.core.userdetails.UserDetailsDto;
import com.dm.uap.converter.UserConverter;
import com.dm.uap.dto.UserDto;
import com.dm.uap.dto.UserPostDto;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.service.UserService;
import com.dm.util.Assert;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentRepository departmentRepository;

    private final DepartmentRepository dpr;

    private final QUser qUser = QUser.user;

    @Override
    public boolean exist() {
        return userRepository.count() > 0;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "#username.toLowerCase()")
    public UserDetailsDto loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(username)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findOneByUsernameIgnoreCase)
            .map(this::toUserDetailsDto)
            .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "users", sync = true, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    public UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException {
        return Optional.ofNullable(mobile)
            .filter(StringUtils::isNotEmpty)
            .flatMap(userRepository::findByMobileIgnoreCase)
            .map(this::toUserDetailsDto)
            .orElseThrow(() -> new UsernameNotFoundException(mobile));
    }


    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public User save(UserDto userDto) {
        validationUser(userDto, null);
        String password = userDto.getPassword();
        // 密码不能为空
        Assert.notEmpty(password).orElseThrow(() -> new DataValidateException("用户密码不能为空"));
        User user = userConverter.copyProperties(new User(), userDto);
        user.setPassword(passwordEncoder.encode(password));
        addPostsAndRoles(user, userDto);
        user = userRepository.save(user);
        return user;
    }

    @Override
    public Optional<UserDto> findById(long id) {
        return userRepository.findById(id).map(userConverter::toDto);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public User delete(long id) {
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userRepository::delete);
        return user.orElseThrow(DataNotExistException::new);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public User update(long id, UserDto userDto) {
        validationUser(userDto, id);
        User user = userConverter.copyProperties(userRepository.getOne(id), userDto);
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
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public User resetPassword(long id, String password) {
        User user = userRepository.getOne(id);
        user.setPassword(passwordEncoder.encode(password));
        return user;
    }

    @Override
    public Page<User> search(Long department, Long role, String roleGroup, String key, Pageable pageable) {
        BooleanBuilder query = new BooleanBuilder();
        if (Objects.nonNull(department)) {
            Department dep = dpr.getOne(department);
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
        return userRepository.findAll(query, pageable);
    }

    // 添加用户的职务和角色信息
    private void addPostsAndRoles(User model, UserDto dto) {
        List<UserPostDto> posts = dto.getPosts();
        if (CollectionUtils.isNotEmpty(posts)) {
            Map<Department, String> posts_ = new HashMap<>();
            posts.forEach(entry -> posts_.put(departmentRepository.getOne(entry.getDepartment().getId()), entry.getPost()));
            model.setPosts(posts_);
        } else {
            model.setPosts(Collections.emptyMap());
        }
        model.setRoles(dto.getRoles());
    }

    @Override
    public boolean userExistsByUsername(String username, Long exclude) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.username.equalsIgnoreCase(username));
        if (Objects.nonNull(exclude)) {
            query.and(qUser.id.ne(exclude));
        }
        return userRepository.exists(query);
    }

    @Override
    public boolean userExistsByEmail(String email, Long exclude) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.email.equalsIgnoreCase(email));
        if (Objects.nonNull(exclude)) {
            query.and(qUser.id.ne(exclude));
        }
        return userRepository.exists(query);
    }

    @Override
    public boolean userExistsByMobile(Long exclude, String mobile) {
        BooleanBuilder query = new BooleanBuilder();
        query.and(qUser.mobile.equalsIgnoreCase(mobile));
        if (Objects.nonNull(exclude)) {
            query.and(qUser.id.ne(exclude));
        }
        return userRepository.exists(query);
    }

    @Override
    @Transactional
    @Caching(evict = {
        @CacheEvict(cacheNames = {"users"}, key = "#result.username.toLowerCase()"),
        @CacheEvict(cacheNames = {"users"}, key = "'M@_' + #result.mobile.toLowerCase()", condition = "#result.mobile!=null")
    })
    public User patch(long id, UserDto user) {
        User originUser = userRepository.getOne(id);
        if (Objects.nonNull(user.getEnabled())) {
            originUser.setEnabled(user.getEnabled());
        }
        return userRepository.save(originUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserDto saveOwnerInfo(Long userId, UserDto user) {
        User originUser = userRepository.getOne(userId);
        originUser.setBirthDate(user.getBirthDate());
        originUser.setDescription(user.getDescription());
        originUser.setEmail(user.getEmail());
        originUser.setFullname(user.getFullname());
        originUser.setMobile(user.getMobile());
        originUser.setNo(user.getNo());
        originUser.setRegionCode(user.getRegionCode());
        originUser.setScenicName(user.getScenicName());
        originUser.setUsername(user.getUsername());
        return userConverter.toDto(userRepository.save(originUser));
    }

    @Override
    public Optional<User> findByMobile(String mobile) {
        return userRepository.findByMobileIgnoreCase(mobile);
    }


    private <T extends User> UserDetailsDto toUserDetailsDto(T user) {
        UserDetailsDto dto = new UserDetailsDto();
        dto.setPassword(user.getPassword());
        dto.setAccountExpired(user.isAccountExpired());
        dto.setCredentialsExpired(user.isCredentialsExpired());
        dto.setEnabled(user.isEnabled());
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullname(user.getFullname());
        dto.setScenicName(user.getScenicName());
        dto.setRegionCode(user.getRegionCode());
        dto.setGrantedAuthority(user.getRoles());
        dto.setMobile(user.getMobile());
        return dto;
    }

    private void validationUser(UserDto user, Long exclude) {
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
}
