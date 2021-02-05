package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends IdentifiableDtoRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findOneByUsernameIgnoreCase(String username);

    Optional<User> findByMobileIgnoreCase(String mobile);

    //  这三个是否钉钉里面有用
//    @Query("select u from User u where key(u.posts) = :department")
//    List<User> findByDepartment(@Param("department") Department dp);
//
//    /**
//     * 获取指定部门的用户
//     *
//     * @param dp        要获取用户的部门
//     * @param recursive 是否递归的获取下级部门的用户
//     * @return 查找到的用户
//     */
//    List<User> findByDepartment(Department dp, boolean recursive);
//
//    @Modifying
//    @Query("update User u set u.enabled = :enabled where u.id in (:deleteUsers)")
//    int batchSetEnabled(@Param("deleteUsers") List<Long> deleteUsers, @Param("enabled") boolean b);

}
