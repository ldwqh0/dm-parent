package com.dm.uap.service;

import com.dm.uap.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {

    /**
     * 判断系统中是否存在用户
     */
    boolean exist();

    /**
     * 保存一个用户
     *
     * @param user 要保存的用户信息
     */
    UserDto save(UserDto user);

    Optional<UserDto> findById(long id);

    void delete(long id);

    UserDto update(long id, UserDto userDto);

    boolean checkPassword(long id, String password);

    UserDto resetPassword(long id, String password);

    Page<UserDto> search(Long department, Long role, String roleGroup, String key, Pageable pageable);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于username,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param username 要检测的用户名
     * @param excludes 要排除的用户
     */
    boolean userExistsByUsername(String username, Long... excludes);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于email,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param email    要检测的邮箱地址
     * @param excludes 要排队除的用户
     */
    boolean userExistsByEmail(String email, Long... excludes);

    /**
     * 检测是否存在同名用户<br>
     * 规则:用户等于mobile,但id不等于指定id的用户如果存在,则表示存在同名用户<br>
     * 用户新增用户或者修改用户名时的预检测
     *
     * @param mobile   要检测的手机号码
     * @param excludes 要排除的用户
     */
    boolean userExistsByMobile(String mobile, Long... excludes);

    UserDto patch(long id, UserDto user);

    /**
     * 保存用户的一部分信息（用户自身的私有信息）
     *
     * @param userId 要保存的用户的信息
     * @param user   要保存的用户信息
     */
    UserDto saveOwnerInfo(Long userId, UserDto user);

}
