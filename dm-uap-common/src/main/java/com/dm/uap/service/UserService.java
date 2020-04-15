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

    public User delete(Long id);

    public User update(long id, UserDto userDto);

    public Page<User> search(String key, Pageable pageable);

    public boolean checkPassword(Long id, String password);

    public User repassword(Long id, String password);

    public Page<User> search(Long department, Long role, Long roleGroup, String key, Pageable pageable);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于username,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     * 
     * @param id
     * @param username
     */
    public boolean userExistsByUsername(Long id, String username);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于email,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     * 
     * @param id
     * @param username
     */
    public boolean userExistsByEmail(Long id, String email);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于mobile,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     * 
     * @param id
     * @param username
     */
    public boolean userExistsByMobile(Long id, String mobile);

}
