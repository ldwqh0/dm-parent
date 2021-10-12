package com.dm.uap.dingtalk.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.entity.Role.Status;
import com.dm.auth.repository.RoleRepository;
import com.dm.collections.CollectionUtils;
import com.dm.dingtalk.api.response.OapiRoleListResponse.OpenRoleGroup;
import com.dm.dingtalk.api.response.OpenRole;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DRoleConverter;
import com.dm.uap.dingtalk.converter.DRoleGroupConverter;
import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.entity.DRoleGroup;
import com.dm.uap.dingtalk.repository.DRoleGroupRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.service.DRoleGroupService;


import static java.lang.Boolean.*;

@Service

public class DRoleGroupServiceImpl implements DRoleGroupService {

    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private DRoleGroupConverter dRoleGroupConverter;

    @Autowired
    private DRoleGroupRepository dRoleGroupRepository;

    @Autowired
    private DRoleConverter dRoleConverter;

    @Autowired
    private DRoleRepository dRoleRepository;

//    @Autowired
//    private RoleGroupRepository uRoleGroupRepository;

    @Autowired
    private RoleRepository roleRepository;

    /**
     * 从钉钉加载角色和角色组数据，并保存在本地
     *
     * @param corpid
     * @return
     */
    private List<DRoleGroup> fetchRoleGroup(final String corpid) {
        List<OpenRoleGroup> roleGroups = dingTalkService.fetchRoleGroups(corpid);
        List<Long> roleGroupIds = roleGroups.stream().map(OpenRoleGroup::getGroupId).collect(Collectors.toList());
        List<Long> roleIds = roleGroups.stream().map(OpenRoleGroup::getRoles)
            .flatMap(List::stream).map(OpenRole::getId).collect(Collectors.toList());
        // 逻辑删除已经在钉钉中被删除的角色
        if (CollectionUtils.isNotEmpty(roleIds)) {
            dRoleRepository.setDeletedByCorpIdAndIdNotIn(corpid, roleIds, TRUE);
        }
        // 设置逻辑删除钉钉角色组
        if (CollectionUtils.isNotEmpty(roleGroupIds)) {
            dRoleGroupRepository.setDeletedByCorpidAndIdNotIn(corpid, roleGroupIds, Boolean.TRUE);
        }
        // 禁用系统角色
        List<Long> deletedIds = dRoleRepository.findRoleIdByCorpIdAndDRoleDeleted(corpid, TRUE);
        if (CollectionUtils.isNotEmpty(deletedIds)) {
            roleRepository.batchSetState(deletedIds, Status.DISABLED);
        }
        List<DRoleGroup> result = roleGroups.stream().map(originalGroup -> {
            Long groupId = originalGroup.getGroupId();
            final DRoleGroup dRoleGroup = dRoleGroupRepository.existsById(corpid, groupId)
                ? dRoleGroupRepository.getById(corpid, groupId)
                : new DRoleGroup(corpid, groupId);
            dRoleGroupConverter.copyProperties(dRoleGroup, originalGroup);
            List<OpenRole> fetchRoles = originalGroup.getRoles();
            if (CollectionUtils.isNotEmpty(fetchRoles)) {
                Set<DRole> dRoles = fetchRoles.stream()
                    .map(oRole -> {
                        Long roleid = oRole.getId();
                        DRole dRole = dRoleRepository.existsById(corpid, roleid)
                            ? dRoleRepository.getById(corpid, roleid)
                            : new DRole(corpid, roleid);
                        return dRoleConverter.copyProperties(dRole, oRole);
                    }).collect(Collectors.toSet());
                dRoleGroup.setRoles(dRoles);
            }
            return dRoleGroup;
        }).collect(Collectors.toList());
        return dRoleGroupRepository.saveAll(result);
    }

    /**
     * 同步本地角色信息到系统角色
     *
     * @param dRoleGroups
     * @return
     */
//    private List<RoleGroup> syncLocalToUap(List<DRoleGroup> dRoleGroups) {
//        List<RoleGroup> results = dRoleGroups.stream().map(dRoleGroup -> {
//            final RoleGroup uRoleGroup = Objects.isNull(dRoleGroup.getGroup()) ? new RoleGroup()
//                    : dRoleGroup.getGroup();
//            uRoleGroup.setName(dRoleGroup.getName());
//            dRoleGroup.setGroup(uRoleGroup);
//            Set<Role> roles = dRoleGroup.getRoles().stream().map(dRole -> {
//                Role uRole = dRole.getRole();
//                if (Objects.isNull(uRole)) {
//                    uRole = new Role();
//                    uRole.setState(Status.ENABLED);
//                }
//                uRole.setName(dRole.getName());
//                uRole.setGroup(uRoleGroup);
//                dRole.setRole(uRole);
//                return uRole;
//            }).collect(Collectors.toSet());
//            uRoleGroup.setRoles(roles);
//            return uRoleGroup;
//        }).collect(Collectors.toList());
//        return uRoleGroupRepository.saveAll(results);
//    }
    @Override
    @Transactional
    public void syncToUap(String corpid) {
        log.info("开始同步角色信息");
//        syncLocalToUap(fetchRoleGroup(corpid));
        log.info("同步角色信息完成");
    }

    @Override
    @Transactional
    public void deleteById(String corpid, Long id) {
        dRoleGroupRepository.deleteById(corpid, id);
    }

    @Override
    public void clear(String corpid) {
        List<DRoleGroup> drgs = dRoleGroupRepository.findByCorpIdAndDeleted(corpid, TRUE);
        drgs.forEach(drg -> {
            try {
//                RoleGroup rg = drg.getGroup();
//                uRoleGroupRepository.delete(rg);
                dRoleGroupRepository.delete(drg);
            } catch (Exception e) {
                log.info("尝试物理删除角色组信息时出现错误,[corpid={},id={}]", drg.getCorpId(), drg.getId());
            }
        });
    }
}
