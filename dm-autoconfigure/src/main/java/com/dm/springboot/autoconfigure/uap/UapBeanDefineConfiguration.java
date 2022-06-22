package com.dm.springboot.autoconfigure.uap;

import com.dm.springboot.autoconfigure.DmEntityScan;
import com.dm.uap.controller.DepartmentController;
import com.dm.uap.controller.LoginLogController;
import com.dm.uap.controller.UserController;
import com.dm.uap.controller.UserRoleController;
import com.dm.uap.repository.DepartmentRepository;
import com.dm.uap.repository.LoginLogRepository;
import com.dm.uap.repository.UserRepository;
import com.dm.uap.repository.UserRoleRepository;
import com.dm.uap.security.controller.CurrentUserController;
import com.dm.uap.security.service.impl.UserDetailsServiceImpl;
import com.dm.uap.service.DepartmentService;
import com.dm.uap.service.LoginLogService;
import com.dm.uap.service.UserRoleService;
import com.dm.uap.service.UserService;
import com.dm.uap.service.impl.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
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
        return new UserRoleServiceImpl(userRoleRepository);
    }

    @Bean
    @ConditionalOnMissingBean(UserService.class)
    public UserServiceImpl userServiceImp(UserRepository userRepository,
                                          DepartmentRepository departmentRepository,
                                          UserRoleRepository userRoleRepository) {
        return new UserServiceImpl(
            userRepository,
            departmentRepository,
            userRoleRepository
        );
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

    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    @ConditionalOnClass(org.springframework.security.core.userdetails.UserDetailsService.class)
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new UserDetailsServiceImpl(userRepository);
    }

    @Bean
    @ConditionalOnMissingBean(CurrentUserController.class)
    @ConditionalOnClass(org.springframework.security.core.AuthenticatedPrincipal.class)
    public CurrentUserController currentUserController(UserService userService) {
        return new CurrentUserController(userService);
    }
}
