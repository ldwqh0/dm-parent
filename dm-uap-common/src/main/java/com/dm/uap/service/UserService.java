package com.dm.uap.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.User;

public interface UserService extends UserDetailsService {

    /**
     * 判断系统中是否存在用户
     * 
     * @return
     */
    public boolean exist();

    /**
     * 保存一个用户
     * 
     * @param user
     * @return
     */
    public User save(UserDto user);

    public Optional<User> get(Long id);

    public void delete(Long id);

    public User update(long id, UserDto userDto);

    public Page<User> search(String key, Pageable pageable);

    public boolean checkPassword(Long id, String password);

    public User repassword(Long id, String password);

    public Page<User> search(Long department, Long role, Long roleGroup, String key, Pageable pageable);

}
