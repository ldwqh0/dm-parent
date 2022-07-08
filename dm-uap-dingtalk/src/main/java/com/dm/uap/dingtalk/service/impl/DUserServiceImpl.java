package com.dm.uap.dingtalk.service.impl;

import com.dm.dingtalk.api.exception.DDepartmentNotFoundException;
import com.dm.dingtalk.api.request.OapiUserCreateRequest;
import com.dm.dingtalk.api.request.OapiUserUpdateRequest;
import com.dm.dingtalk.api.response.OapiUserCreateResponse;
import com.dm.dingtalk.api.response.OapiUserGetDeptMemberResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse;
import com.dm.dingtalk.api.response.OapiUserGetResponse.Roles;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DUserConverter;
import com.dm.uap.dingtalk.entity.DDepartment;
import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.repository.DUserRepository;
import com.dm.uap.dingtalk.service.DDepartmentService;
import com.dm.uap.dingtalk.service.DRoleGroupService;
import com.dm.uap.dingtalk.service.DUserService;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.Role;
import com.dm.uap.entity.User;
import com.dm.uap.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DUserServiceImpl implements DUserService {

    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private DDepartmentRepository dDepartmentRepository;

    @Autowired
    private DUserRepository dUserRepository;

    @Autowired
    private DUserConverter dUserConverter;

    @Autowired
    private DRoleRepository dRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DDepartmentService dDepartmentService;

    @Autowired
    private DRoleGroupService dRoleGroupService;

    @Autowired
    private DUserService dUserService;

    /**
     * 采用批处理同步的模式
     */
    @Override
    public void syncToUap() {
        dDepartmentService.syncToUap();
        dRoleGroupService.syncToUap();
        Collection<String> fetchedUsers = dUserService.fetchDUserState();
        // 同步用户信息
        log.info("开始同步用户信息，共{}条", fetchedUsers.size());
        fetchedUsers.forEach(it -> dUserService.syncToUap(it));
    }

    @Override
    public DUser save(DUser dUser) {
        try {
            // 如果用户被标记为删除，从钉钉通讯录中删除用户
            if (Boolean.TRUE.equals(dUser.getDeleted())) {
                dingTalkService.deleteUser(dUser.getUserid());
            } else {
                dUser = saveToDingTalk(dUser);
            }
            // 保存到钉钉服务器
        } catch (Exception e) {
            log.error("将用户信息保存到钉钉时出错", e);
        }
        // 更新关联的用户信息
        User user = toUser(dUser);
        dUser.setUser(user);
        // 将记录保存到数据库
        return dUserRepository.save(dUser);
    }

    /**
     * 保存用户信息到钉钉, <br >
     * 保存前根据userid判断是否在钉钉中已经存在相关用户, <br>
     * 以便区别是新增还是修改
     *
     * @param dUser
     * @return 创建成功之后的DUser对象，主要是更新创建的userid
     */
    private DUser saveToDingTalk(DUser dUser) {
        String userId = dUser.getUserid();
        if (StringUtils.isNotBlank(userId)) {
            OapiUserGetResponse response = dingTalkService.fetchUserById(userId);
            // 这个状态码代表找到了相关的用户信息
            if (Objects.equals(0L, response.getErrcode()) && StringUtils.isNotBlank(response.getUserid())) {
                // 更新钉钉用户到服务器
                return updateToDingTalk(dUser);
            }
        }
        // 创建用户到钉钉服务器
        return createToDingTalk(dUser);
    }

    private User toUser(DUser dUser) {
        User user = dUser.getUser();
        if (Objects.isNull(user)) {
            if (StringUtils.isNotBlank(dUser.getUserid())) {
                Optional<User> dUserOptional = userRepository.findOneByUsernameIgnoreCase(dUser.getUserid());
                user = dUserOptional.orElse(new User());
            } else {
                user = new User();
            }
            dUser.setUser(user);
        }
        dUserConverter.copyProperties(user, dUser);
        // 设置部门顺序
        Map<DDepartment, Long> _orders = dUser.getOrderInDepts();
        if (MapUtils.isNotEmpty(_orders)) {
            Map<Department, Long> orders = new HashMap<>();
            _orders.forEach((key, value) -> orders.put(key.getDepartment(), value));
            user.setOrders(orders);
        }

        // 设置职务信息
        Map<Department, String> post = new HashMap<Department, String>();
        Map<DDepartment, String> _post = dUser.getPosts();
        if (MapUtils.isNotEmpty(_post)) {
            Set<Entry<DDepartment, String>> postEntry = _post.entrySet();
            postEntry.forEach(e -> {
                post.put(e.getKey().getDepartment(), e.getValue());
            });
        } else if (CollectionUtils.isNotEmpty(dUser.getDepartments())) {
            String pos = dUser.getPosition();
            dUser.getDepartments().forEach(d -> {
                post.put(d.getDepartment(), pos);
            });
        }

        user.setPosts(post);
        // 设置角色
        Set<DRole> dRoles = dUser.getRoles();
        if (CollectionUtils.isNotEmpty(dRoles)) {
            List<Role> roles = dRoles.stream().map(DRole::getRole).collect(Collectors.toList());
            user.setRoles(roles);
        }
        return user;
    }

    /**
     * 将从服务上获取到的用户信息，映射到本地数据模型
     *
     * @param dUser
     * @param rsp
     * @return
     */
    private DUser copyProperties(DUser dUser, OapiUserGetResponse rsp) {
        dUserConverter.copyProperties(dUser, rsp);
        log.debug("正在合并用户信息 userid={}", dUser.getUserid());
        // 合并是否部门领导信息
        try {
            Set<DDepartment> departments = rsp.getDepartment().stream().map(dDepartmentRepository::getOne)
                    .collect(Collectors.toSet());
            dUser.setDepartments(departments);
            Map<Long, Boolean> isLeaderMap = parseLeaderMap(rsp.getIsLeaderInDepts());
            if (MapUtils.isNotEmpty(isLeaderMap)) {
                Map<DDepartment, Boolean> dLeaderMap = new HashMap<>();
                for (Entry<Long, Boolean> entry : isLeaderMap.entrySet()) {
                    dLeaderMap.put(dDepartmentRepository.getOne(entry.getKey()), entry.getValue());
                }
                dUser.setLeaderInDepts(dLeaderMap);
            }
        } catch (Exception e) {
            log.error("合并是否部门领导信息时报错", e);
        }
        // 合并部门排序信息
        try {
            Map<Long, Long> orderMap = parseOrderMap(rsp.getOrderInDepts());
            if (MapUtils.isNotEmpty(orderMap)) {
                Map<DDepartment, Long> dOrderMap = new HashMap<DDepartment, Long>();
                for (Entry<Long, Long> orderEntry : orderMap.entrySet()) {
                    dOrderMap.put(dDepartmentRepository.getOne(orderEntry.getKey()), orderEntry.getValue());
                }
                dUser.setOrderInDepts(dOrderMap);
            }

        } catch (Exception e) {
            log.error("合并部门排序信息时报错", e);
        }

        // 合并用户角色信息
        List<Roles> roles = rsp.getRoles();
        if (CollectionUtils.isNotEmpty(roles)) {
            Set<DRole> dRoles = roles.stream()
                    .map(Roles::getId)
                    .map(dRoleRepository::getOne)
                    .collect(Collectors.toSet());
            dUser.setRoles(dRoles);
        }
        return dUser;
    }

    /**
     * 解析用户是否领导信息的字符串，钉钉的接口返回的不是正常的json字符串，而是一个类似js对象的字符串，不能使用json引擎正常解析
     *
     * @param v 表示用户是否部门领导的字符串 ，格式如 "{1:false,2:true}"
     *          的形式的形式，数组是部门的id,true表示是部门领导，false表示不是部门领导
     * @return
     */
    private Map<Long, Boolean> parseLeaderMap(String v) {
        try {
            Map<Long, Boolean> result = new LinkedHashMap<>();
            if (StringUtils.isNotBlank(v)) {
                String[] kvs = v.split("[,\\{\\}]");
                for (String kv : kvs) {
                    if (StringUtils.isNotBlank(kv)) {
                        String[] kvArray = kv.split(":");
                        result.put(Long.valueOf(kvArray[0]), Boolean.valueOf(kvArray[1]));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("can not parse leader info from giving string", e);
        }
    }

    /**
     * 解析用户排序信息字符串
     *
     * @param str
     * @return
     * @see
     */
    private Map<Long, Long> parseOrderMap(String str) {
        try {
            Map<Long, Long> result = new LinkedHashMap<>();
            if (StringUtils.isNotBlank(str)) {
                String[] kvs = str.split("[,\\{\\}]");
                for (String kv : kvs) {
                    if (StringUtils.isNotBlank(kv)) {
                        String[] kvArray = kv.split(":");
                        result.put(Long.valueOf(kvArray[0]), Long.valueOf(kvArray[1]));
                    }
                }
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("can not parse leader info from giving string", e);
        }
    }

    /**
     * 新增用户信息到钉钉
     *
     * @param dUser
     * @return
     */
    private DUser createToDingTalk(DUser dUser) {
        OapiUserCreateRequest request = dUserConverter.toOapiUserCreateRequest(dUser);
        OapiUserCreateResponse rsp = dingTalkService.createUser(request);
        dUser.setUserid(rsp.getUserid());
        // 将角色信息更新到钉钉服务器
        Set<Long> roleIds = dUser.getRoles().stream().map(DRole::getId).collect(Collectors.toSet());
        dingTalkService.batchSetUserRole(Collections.singleton(dUser.getUserid()), roleIds);
        return dUser;
    }

    /**
     * 更新用信息到钉钉服务器
     *
     * @param dUser
     * @return
     */
    private DUser updateToDingTalk(DUser dUser) {
        OapiUserUpdateRequest request = dUserConverter.toOapiUserUpdateRequest(dUser);
        dingTalkService.updateUser(request);
        Set<Long> roleIds = dUser.getRoles().stream().map(DRole::getId).collect(Collectors.toSet());
        dingTalkService.batchSetUserRole(Collections.singleton(dUser.getUserid()), roleIds);
        return dUser;
    }

    @Override
    @Transactional
    public void syncToUap(String dUserid) {
        try {
            log.debug("同步id为【{}】的用户信息", dUserid);
            Thread.sleep(30);
            OapiUserGetResponse item = dingTalkService.fetchUserById(dUserid);
            DUser dUser = copyProperties(new DUser(item.getUserid()), item);
            dUser = dUserRepository.save(dUser);
            User user = toUser(dUser);
            userRepository.save(user);
        } catch (Exception e) {
            log.error("同步 id为【{}】的用户信息时出错", dUserid, e);
        }
    }

    /**
     * 同步钉钉用户的状态
     */
    @Override
    @Transactional
    public Collection<String> fetchDUserState() {
        List<DDepartment> dDepartments = dDepartmentRepository.findAll();
        // 遍历所有部门
        Set<String> userIds = dDepartments.stream()
                .map(DDepartment::getId)
                .map(departmentId -> {
                    try {
                        // 每次进程进来的时候，延迟30毫秒
                        Thread.sleep(30); // 因为钉钉对同时并发的请求数量有限制
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        return dingTalkService.fetchUsers(departmentId);
                    } catch (DDepartmentNotFoundException e) {
                        OapiUserGetDeptMemberResponse emptyResponse = new OapiUserGetDeptMemberResponse();
                        emptyResponse.setUserIds(Collections.emptyList());
                        return emptyResponse;
                    }
                })// 从钉钉服务器上拉取所有部门的用户信息
                .map(OapiUserGetDeptMemberResponse::getUserIds)
                .flatMap(List::stream)// 获取所有的用户列表
                .collect(Collectors.toSet());
        // 将不在列表中的用户标记为删除
        dUserRepository.setDeletedByUseridNotIn(userIds, true);
        // 将在列表中的用户标记为未删除
        dUserRepository.setDeletedByUseridIn(userIds, false);

        // TODO 这个应该在另外一个逻辑里面处理
        List<Long> deleteUsers = dUserRepository.findUserIdsByDUserDeleted(true);
        if (CollectionUtils.isNotEmpty(deleteUsers)) {
            userRepository.batchSetEnabled(deleteUsers, false);
        }
        return userIds;
    }
}
