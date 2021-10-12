package com.dm.uap.dingtalk.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.entity.Role;
import com.dm.collections.CollectionUtils;
import com.dm.collections.Maps;
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
import com.dm.uap.dingtalk.entity.DUserId;
import com.dm.uap.dingtalk.entity.DdSyncLog;
import com.dm.uap.dingtalk.repository.DDepartmentRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.repository.DUserRepository;
import com.dm.uap.dingtalk.service.DDepartmentService;
import com.dm.uap.dingtalk.service.DRoleGroupService;
import com.dm.uap.dingtalk.service.DRoleService;
import com.dm.uap.dingtalk.service.DUserService;
import com.dm.uap.entity.Department;
import com.dm.uap.entity.User;
import com.dm.uap.repository.UserRepository;


import static java.lang.Boolean.*;

@Service

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
    private DRoleService dRoleService;

    private volatile Boolean syncing = Boolean.FALSE;

    private final Object lock = new Object();

    /**
     * 采用批处理同步的模式
     */
    @Transactional
    @Override
    public void syncToUap(String corpid) {
        if (Boolean.TRUE.equals(this.syncing)) {
            log.info("一个同步进程在进行中，返回");
        } else {
            // TODO　这个代码玩笑大了
            synchronized (lock) {
                try {
                    syncing = Boolean.TRUE;
                    dDepartmentService.syncToUap(corpid);
                    dRoleGroupService.syncToUap(corpid);
                    syncToUap(fetch(corpid));
                    // 尝试物理删除已经在钉钉中删除的不存在的用户,如果不存在引用关系，直接删除，如果存在引用关系，逻辑删除
                    this.clear(corpid);
                    // 尝试物理删除已经在钉钉中删除的角色信息
                    dRoleService.clear(corpid);
                    dRoleGroupService.clear(corpid);
                    // 尝试物理删除已经在钉钉中删除的部门信息
                    dDepartmentService.clear(corpid);
                } catch (Exception e) {
                    log.error("同步时发生错误", e);
                    throw new RuntimeException(e);
                } finally {
                    syncing = Boolean.FALSE;
                }
            }
        }
    }

    @Transactional
    @Async
    @Override
    public CompletableFuture<DdSyncLog> asyncToUap(String corpid) {
        this.syncToUap(corpid);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public DUser save(DUser dUser) {
        try {
            // 如果用户被标记为删除，从钉钉通讯录中删除用户
            if (Boolean.TRUE.equals(dUser.getDeleted())) {
                dingTalkService.deleteUser(dUser.getCorpId(), dUser.getUserid());
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

    @Override
    public Optional<DUser> findByUserid(String corpid, String userid) {
        return dUserRepository.findById(corpid, userid);
    }

    @Override
    public Optional<DUser> findById(DUserId id) {
        return dUserRepository.findById(id);
    }

    /**
     * 同步指定用户的用户信息到系统
     */
    @Override
    @Transactional
    public DUser syncToUap(String corpid, String userid) {
        try {
            OapiUserGetResponse org = dingTalkService.fetchUserById(corpid, userid);
            DUser dUser = copyProperties(new DUser(corpid, userid), org);
            syncToUap(dUser);
            return dUserRepository.save(dUser);
        } catch (Exception e) {
            log.error("获取指定用户信息时出现错误", e);
            throw e;
        }
    }

    @Transactional
    @Async
    @Override
    public CompletableFuture<DUser> asyncToUap(String corpid, String userid) {
        return CompletableFuture.completedFuture(this.syncToUap(corpid, userid));
    }

    @Override
    @Transactional
    public void delete(String corpid, String unionid) {
        if (dUserRepository.existsById(corpid, unionid)) {
            DUser du = dUserRepository.getById(corpid, unionid);
            try {
                deletePhysical(du);
            } catch (Exception e) {
                log.info("物理删除失败，标记逻辑删除");
                User user = du.getUser();
                user.setEnabled(FALSE);
                du.setDeleted(TRUE);
            }
        }

        dUserRepository.deleteById(DUserId.of(corpid, unionid));
    }

    /**
     * 保存用户信息到钉钉, <br >
     * 保存前根据userid判断是否在钉钉中已经存在相关用户, <br>
     * 以便区别是新增还是修改
     *
     * @param dUser 钉钉用户信息
     * @return 创建成功之后的DUser对象，主要是更新创建的userid
     */
    private DUser saveToDingTalk(DUser dUser) {
        String userId = dUser.getUserid();
        String corpid = dUser.getCorpId();
        if (StringUtils.isNotBlank(userId)) {
            OapiUserGetResponse response = dingTalkService.fetchUserById(corpid, userId);
            // 这个状态码代表找到了相关的用户信息
            if (Objects.equals(0L, response.getErrcode()) && StringUtils.isNotBlank(response.getUserid())) {
                // 更新钉钉用户到服务器
                return updateToDingTalk(corpid, dUser);
            }
        }
        // 创建用户到钉钉服务器
        return createToDingTalk(corpid, dUser);
    }

    /**
     * 将钉钉用户信息同步到系统uap
     *
     * @param dUsers 钉钉用户信息
     * @return 用户列表
     */
    private List<User> syncToUap(List<DUser> dUsers) {
        log.info("开始同步用户信息到UAP");
        List<User> users = dUsers.stream().map(this::toUser).collect(Collectors.toList());
        try {
            return userRepository.saveAll(users);
        } finally {
            log.info("同步用户信息到UAP完成");
        }

    }

    /**
     * 同步指定钉钉用户到系统用户
     *
     * @param dUser 钉钉用户信息
     * @return 系统用户信息
     */
    private User syncToUap(DUser dUser) {
        return userRepository.save(this.toUser(dUser));
    }

    private User toUser(DUser dUser) {
        User user = dUser.getUser();
        if (Objects.isNull(user)) {
            if (StringUtils.isNotBlank(dUser.getUserid())) {
                Optional<User> dUserOptional = userRepository.findOneByUsernameIgnoreCase(dUser.getUserid());
                user = dUserOptional.orElseGet(User::new);
            } else {
                user = new User();
            }
            dUser.setUser(user);
        }
        dUserConverter.copyProperties(user, dUser);
        // 设置部门顺序
        Map<DDepartment, Long> _orders = dUser.getOrderInDepts();
        if (Maps.isNotEmpty(_orders)) {
            Map<Department, Long> orders = new HashMap<>();
            _orders.forEach((key, value) -> orders.put(key.getDepartment(), value));
            user.setOrders(orders);
        }

        // 设置职务信息
        Map<Department, String> post = new HashMap<>();
        Map<DDepartment, String> _post = dUser.getPosts();
        if (Maps.isNotEmpty(_post)) {
            Set<Entry<DDepartment, String>> postEntry = _post.entrySet();
            postEntry.forEach(e -> post.put(e.getKey().getDepartment(), e.getValue()));
        } else if (CollectionUtils.isNotEmpty(dUser.getDepartments())) {
            String pos = dUser.getPosition();
            dUser.getDepartments().forEach(d -> post.put(d.getDepartment(), pos));
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
     * 从服务器拉取钉钉用户信息，并将信息保存到本地
     *
     * @param corpid 企业号
     * @return 用户信息的列表
     */
    private List<DUser> fetch(final String corpid) {
        List<DDepartment> dDepartments = dDepartmentRepository.findByCorpId(corpid);
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
                return dingTalkService.fetchUsers(corpid, departmentId);
            })// 从钉钉服务器上拉取所有部门的用户信息
            .map(OapiUserGetDeptMemberResponse::getUserIds)
            .flatMap(List::stream)// 获取所有的用户列表
            .collect(Collectors.toSet());

        List<DUser> users = userIds.stream()
            // 将从服务器上抓取的数据，复制到本地数据库中
            .map(userid -> {
                try {
                    return dingTalkService.fetchUserById(corpid, userid);
                } catch (Exception e) {
                    log.error("从服务器抓取用户[{}]的信息出错,错误信息是 [{}]", userid, e.getMessage());
                    return null;
                }
            })
            .filter(item -> Objects.nonNull(item))
            .map(item -> {
                String unionid = item.getUnionid();
                DUser du = dUserRepository.findByCorpIdAndUnionid(corpid, unionid)
                    .orElseGet(() -> new DUser(corpid, unionid));
                return copyProperties(du, item);
            })
            .collect(Collectors.toList());

        List<String> unionids = users.stream().map(DUser::getUnionid).collect(Collectors.toList());
        // 将存在于本地用户，但不存在于钉钉的用户，将本地状态设置为删除，
        dUserRepository.setDeletedByCorpIdAndUnionidNotIn(corpid, unionids, TRUE);
        List<Long> deleteUsers = dUserRepository.findUserIdsByCorpIdAndDUserDeleted(corpid, TRUE);
        if (CollectionUtils.isNotEmpty(deleteUsers)) {
            userRepository.batchSetEnabled(deleteUsers, FALSE);
        }

        // 保存所有用户信息
        return dUserRepository.saveAll(users);
    }

    /**
     * 将从服务上获取到的用户信息，映射到本地数据模型
     *
     * @param dUser 钉钉用户信息
     * @param rsp   远程用户信息
     * @return 保存合并后的钉钉用户
     */
    private DUser copyProperties(DUser dUser, OapiUserGetResponse rsp) {
        dUserConverter.copyProperties(dUser, rsp);
        log.info("正在合并用户信息 corpid={}, unionid={}, userid={}", dUser.getCorpId(), dUser.getUnionid(), dUser.getUserid());
        String corpid = dUser.getCorpId();
        // 合并是否部门领导信息
        try {
            Set<DDepartment> departments = rsp.getDepartment().stream()
                .map(departmentid -> dDepartmentRepository.getById(corpid, departmentid))
                .collect(Collectors.toSet());
            dUser.setDepartments(departments);
            Map<Long, Boolean> isLeaderMap = parseLeaderMap(rsp.getIsLeaderInDepts());
            if (Maps.isNotEmpty(isLeaderMap)) {
                Map<DDepartment, Boolean> dLeaderMap = new HashMap<>();
                for (Entry<Long, Boolean> entry : isLeaderMap.entrySet()) {
                    dLeaderMap.put(dDepartmentRepository.getById(corpid, entry.getKey()), entry.getValue());
                }
                dUser.setLeaderInDepts(dLeaderMap);
            }
        } catch (Exception e) {
            log.error("合并是否部门领导信息时报错", e);
        }
        // 合并部门排序信息
        try {
            Map<Long, Long> orderMap = parseOrderMap(rsp.getOrderInDepts());
            if (Maps.isNotEmpty(orderMap)) {
                Map<DDepartment, Long> dOrderMap = new HashMap<>();
                for (Entry<Long, Long> orderEntry : orderMap.entrySet()) {
                    dOrderMap.put(dDepartmentRepository.getById(corpid, orderEntry.getKey()), orderEntry.getValue());
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
                .map(roleid -> dRoleRepository.getById(corpid, roleid))
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
     * @return 解析结果
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
     * 解析用户排序信息字符串 {@link parseLeaderMap}
     *
     * @param str 要解析的字符串
     * @return 解析后的排序信息
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
     * @param dUser 钉钉用户信息
     * @return 保存后的钉钉用户信息
     */
    private DUser createToDingTalk(String corpid, DUser dUser) {
        OapiUserCreateRequest request = dUserConverter.toOapiUserCreateRequest(dUser);
        OapiUserCreateResponse rsp = dingTalkService.createUser(corpid, request);
        String userid = rsp.getUserid();
        Set<DRole> roles = dUser.getRoles();
        // 将角色信息更新到钉钉服务器
        Set<Long> roleIds = roles.stream().map(DRole::getId).collect(Collectors.toSet());
        dingTalkService.batchSetUserRole(corpid, Collections.singleton(userid), roleIds);
        DUser result = new DUser(corpid, userid);
        // 重新从服务器获取用户的最新信息
        OapiUserGetResponse ougr = dingTalkService.fetchUserById(corpid, userid);
        result.setRoles(roles);
        dUserConverter.copyProperties(result, ougr);
        return result;
    }

    /**
     * 更新用信息到钉钉服务器
     *
     * @param dUser 钉钉用户信息
     * @return 钉钉用户信息
     */
    private DUser updateToDingTalk(String corpid, DUser dUser) {
        OapiUserUpdateRequest request = dUserConverter.toOapiUserUpdateRequest(dUser);
        dingTalkService.updateUser(corpid, request);
        Set<Long> roleIds = dUser.getRoles().stream().map(DRole::getId).collect(Collectors.toSet());
        dingTalkService.batchSetUserRole(corpid, Collections.singleton(dUser.getUserid()), roleIds);
        return dUser;
    }

    /**
     * 尝试物理删除标记为删除的用户
     */
    @Override
    @Transactional
    public void clear(String corpid) {
        List<DUser> deleteUsers = dUserRepository.findByCorpIdAndDeleted(corpid, TRUE);
        deleteUsers.forEach(du -> {
            try {
                deletePhysical(du);
            } catch (Exception e) {
                log.info("尝试删除用户失败,[corpid={},userid={}]", du.getCorpId(), du.getUserid());
            }
        });
    }

    /**
     * 尝试物理删除
     *
     * @param dUser 要删除的钉钉用户
     */
    private void deletePhysical(DUser dUser) {
        User user = dUser.getUser();
        userRepository.delete(user);
        dUserRepository.delete(dUser);
    }
}
