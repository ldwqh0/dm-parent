package com.dm.uap.repository.impl;

import com.dm.collections.CollectionUtils;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.QDepartment;
import com.dm.uap.entity.QUser;
import com.dm.uap.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserRepositoryImpl {

    private final JPAQueryFactory queryFactory;

    private final QUser qUser = QUser.user;

    private final QDepartment qDepartment = QDepartment.department;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }
// todo 需要测试是否有用
//    public List<User> findByDepartment(Department department) {
//        return queryFactory.select(qUser).from(qUser).where(qUser.posts.containsKey(department)).fetch();
//    }

    /**
     * 查询某个部门的用户
     *
     * @param dp        要查询的部门
     * @param recursive 是否递归查询子部门的用户
     * @return 查询到的用户的列表
     */
    public List<User> findByDepartment(Department dp, boolean recursive) {
//        if (recursive) {
//            // 获取所有待查询的部门已经其子部门
//            List<User> result = new ArrayList<>();
//            List<Department> deps = new ArrayList<>();
//            deps.add(dp);
//            List<Department> children = findChildren(Collections.singleton(dp));
//            while (CollectionUtils.isNotEmpty(children)) {
//                deps.addAll(children);
//                children = findChildren(children);
//            }
//            deps.forEach(dep -> result.addAll(findByDepartment(dep)));
//            return result;
//        } else {
//            return findByDepartment(dp);
//        }
        return Collections.emptyList();
    }

    private List<Department> findChildren(Collection<Department> parents) {
        return queryFactory.select(qDepartment).from(qDepartment).where(qDepartment.parent.in(parents)).fetch();
    }
}
