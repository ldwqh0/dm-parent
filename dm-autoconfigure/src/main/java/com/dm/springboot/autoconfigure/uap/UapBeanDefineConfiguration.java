package com.dm.springboot.autoconfigure.uap;

import com.dm.springboot.autoconfigure.DmEntityScan;
import com.dm.uap.controller.*;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.LoginLogRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.repository.UserRoleRepository;
import com.dm.uap.service.DepartmentService;
import com.dm.uap.service.LoginLogService;
import com.dm.uap.service.UserRoleService;
import com.dm.uap.service.UserService;
import com.dm.uap.service.impl.DepartmentServiceImpl;
import com.dm.uap.service.impl.LoginLogServiceImpl;
import com.dm.uap.service.impl.RoleServiceImpl;
import com.dm.uap.service.impl.UserServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManager;

@DmEntityScan("com.dm.uap")
public class UapBeanDefineConfiguration {

    @Bean
    public DepartmentRepository departmentRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(DepartmentRepository.class);
    }

    @Bean
    public LoginLogRepository loginLogRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(LoginLogRepository.class);
    }

    @Bean
    public UserRepository userRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(UserRepository.class);
    }

    @Bean
    public UserRoleRepository userRoleRepository(EntityManager em) {
        return new JpaRepositoryFactory(em).getRepository(UserRoleRepository.class);
    }


    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    @ConditionalOnMissingBean(DepartmentService.class)
    public DepartmentService departmentService(DepartmentRepository departmentRepository) {
        return new DepartmentServiceImpl(departmentRepository);
    }

    @Bean
    @ConditionalOnMissingBean(LoginLogService.class)
    public LoginLogService loginLogService(LoginLogRepository loginLogRepository) {
        return new LoginLogServiceImpl(loginLogRepository);
    }

    @Bean
    @ConditionalOnMissingBean(UserRoleService.class)
    public UserRoleService userRoleService(UserRoleRepository userRoleRepository) {
        return new RoleServiceImpl(userRoleRepository);
    }


    @Bean
    @ConditionalOnMissingBean(UserService.class)
    public UserServiceImpl userServiceImp(UserRepository userRepository,
                                          PasswordEncoder passwordEncoder,
                                          DepartmentRepository departmentRepository,
                                          UserRoleRepository userRoleRepository) {
        return new UserServiceImpl(
            userRepository,
            passwordEncoder,
            departmentRepository,
            userRoleRepository
        );
    }

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public UserDetailsService userDetailsService(UserService userService) {
        return (UserDetailsService) userService;
    }

    @Bean
    @ConditionalOnMissingBean(CurrentUserController.class)
    public CurrentUserController currentUserController(UserService userService) {
        return new CurrentUserController(userService);
    }

    @Bean
    public DepartmentController departmentController(DepartmentService departmentService) {
        return new DepartmentController(departmentService);
    }

    @Bean
    public LoginLogController loginLogController(LoginLogService loginLogService) {
        return new LoginLogController(loginLogService);
    }

    @Bean
    public UserController userController(UserService userService) {
        return new UserController(userService);
    }

    @Bean
    public UserRoleController userRoleController(UserRoleService userService) {
        return new UserRoleController(userService);
    }
}