package com.dm.auth.repository;

import com.dm.auth.entity.Menu;
import com.dm.auth.entity.Role;
import com.dm.auth.entity.Role.Status;
import com.dm.data.domain.Identifiable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>, QuerydslPredicateExecutor<Role> {

    List<Role> findByState(Status enabled);

    Optional<Role> findByGroupAndName(String group, String name);

    boolean existsByGroupAndName(String group, String name);

    boolean existsByGroupAndNameAndIdNot(String group, String name, Long id);

    @Query("select r from Role r left join r.menus m where m=:menu")
    List<Role> findByMenu(@Param("menu") Menu menu);

    @Query("select distinct(r.group) from Role r")
    List<String> listGroups();

    @Query("select max(r.id) from Role r")
    Optional<Long> findMaxId();

    default Role getByDto(Identifiable<Long> identifiable) {
        return getById(identifiable.getId());
    }
}
