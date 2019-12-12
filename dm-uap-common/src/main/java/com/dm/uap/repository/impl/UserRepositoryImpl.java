package com.dm.uap.repository.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class UserRepositoryImpl {

    @Autowired
    private JPAQueryFactory queryFactory;

    private final QUser qUser = QUser.user;

    private final QDepartment qDepartment = QDepartment.department;

    public List<User> findByDepartment(Department department) {
        return queryFactory.select(qUser).from(qUser).where(qUser.posts.containsKey(department)).fetch();
    }

    /**
     * 查询某个部门的用户
     * 
     * @param dp        要查询的部门
     * @param recursive 是否递归查询子部门的用户
     * @return
     */
    public List<User> findByDepartment(Department dp, boolean recursive) {
        if (recursive) {
            // 获取所有待查询的部门已经其子部门
            List<User> result = new ArrayList<User>();
            List<Department> deps = new ArrayList<Department>();
            deps.add(dp);
            List<Department> children = findChildren(Collections.singleton(dp));
            while (CollectionUtils.isNotEmpty(children)) {
                deps.addAll(children);
                children = findChildren(children);
            }
            deps.forEach(dep -> {
                result.addAll(findByDepartment(dep));
            });
            return result;
        } else {
            return findByDepartment(dp);
        }
    }

    private List<Department> findChildren(Collection<Department> parents) {
        return queryFactory.select(qDepartment).from(qDepartment).where(qDepartment.parent.in(parents)).fetch();
    }
}
