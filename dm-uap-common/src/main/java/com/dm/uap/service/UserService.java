package com.dm.uap.service;

import com.dm.uap.dto.UserDto;
import com.dm.uap.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    /**
     * 判断系统中是否存在用户
     *
     * @return
     */
    boolean exist();

    /**
     * 保存一个用户
     *
     * @param user
     * @return
     */
    User save(UserDto user);

    Optional<UserDto> findById(long id);

    User delete(long id);

    User update(long id, UserDto userDto);

    Page<User> search(String key, Pageable pageable);

    boolean checkPassword(long id, String password);

    User resetPassword(long id, String password);

    Page<User> search(Long department, Long role, String roleGroup, String key, Pageable pageable);

    Optional<User> findByMobile(String mobile);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于username,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param username
     * @param exclude
     */
    boolean userExistsByUsername(String username, Long exclude);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于email,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param email
     * @param id
     */
    boolean userExistsByEmail(String email, Long id);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于mobile,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param id
     * @param mobile
     */
    boolean userExistsByMobile(Long id, String mobile);

    User patch(long id, UserDto user);

    /**
     * 保存用户的一部分信息
     *
     * @param userId
     * @param user
     * @return
     */
    UserDto saveOwnerInfo(Long userId, UserDto user);
}
