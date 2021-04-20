package com.dm.uap.repository;

import com.dm.common.repository.IdentifiableDtoRepository;
import com.dm.uap.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends IdentifiableDtoRepository<User, Long>, QuerydslPredicateExecutor<User> {

    Optional<User> findOneByUsernameIgnoreCase(String username);

    Optional<User> findByMobileIgnoreCase(String mobile);

    // TODO 这个方式不是很好，在下一个版本改进,不应该使用原生查询，应该把role变成一个实体
    // 这样，role这个实体会在两个地方出现
    @Query(value = "update dm_user_role_ set name_=:name,group_=:group where role_id_=:id", nativeQuery = true)
    @Modifying
    int updateRole(@Param("id") Long id, @Param("name") String name, @Param("group") String group);

}
