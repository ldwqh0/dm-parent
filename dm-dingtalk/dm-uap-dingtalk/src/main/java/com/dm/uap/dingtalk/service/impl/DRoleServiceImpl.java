package com.dm.uap.dingtalk.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.auth.entity.Role;
import com.dm.auth.entity.Role.Status;
import com.dm.auth.repository.RoleRepository;
import com.dm.dingtalk.api.response.OpenRole;
import com.dm.dingtalk.api.service.DingTalkService;
import com.dm.uap.dingtalk.converter.DRoleConverter;
import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.repository.DRoleGroupRepository;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.service.DRoleService;
//import com.dm.uap.repository.RoleGroupRepository;

import lombok.extern.slf4j.Slf4j;
import static java.lang.Boolean.*;

@Service
@Slf4j
public class DRoleServiceImpl implements DRoleService {

    @Autowired
    private DRoleRepository dRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

//    @Autowired
//    private RoleGroupRepository roleGroupRepository;

    @Autowired
    private DRoleGroupRepository dRoleGroupRepository;

    @Autowired
    private DingTalkService dingTalkService;

    @Autowired
    private DRoleConverter dRoleConverter;

    @Override
    @Transactional
    public void clear(String corpid) {
        List<DRole> dRoles = dRoleRepository.findByCorpIdAndDeleted(corpid, TRUE);
        dRoles.forEach(dRole -> {
            try {
                Role role = dRole.getRole();
                roleRepository.delete(role);
                dRoleRepository.delete(dRole);
            } catch (Exception e) {
                log.info("尝试物理删除角色时失败,[corpid={},id={}]", dRole.getCorpId(), dRole.getId());
            }
        });
    }

    @Override
    @Transactional
    public void delete(String corpid, Long id) {
        if (dRoleRepository.existsById(corpid, id)) {
            DRole drole = dRoleRepository.getOne(corpid, id);
            try {
                deletePhysical(drole);
            } catch (Exception e) {
                log.info("尝试物理删除失败，使用逻辑删除");
                deleteLogic(drole);
            }
        }
    }

    private void deletePhysical(DRole drole) {
        roleRepository.delete(drole.getRole());
        dRoleRepository.delete(drole);
    }

    private void deleteLogic(DRole drole) {
        Role role = drole.getRole();
        role.setState(Status.DISABLED);
        drole.setDeleted(TRUE);
        roleRepository.save(role);
        dRoleRepository.save(drole);
    }

    @Override
    @Async
    public CompletableFuture<DRole> asyncToUap(String corpid, Long roleid) {
        return CompletableFuture.completedFuture(syncToUap(corpid, roleid));
    }

    public DRole syncToUap(String corpid, Long roleid) {
        OpenRole role = dingTalkService.fetchRoleById(corpid, roleid);
        DRole dRole = saveToLocale(role);
        return saveToUap(dRole);
    }

    private DRole saveToUap(DRole dRole) {
        Role role = dRole.getRole();
        if (Objects.isNull(role)) {
            role = new Role();
        }
        role = dRoleConverter.copyProperties(role, dRole);
//        role.setGroup(dRole.getGroup().getGroup());
        dRole.setRole(roleRepository.save(role));
        return dRoleRepository.save(dRole);
    }

    private DRole saveToLocale(OpenRole source) {
        DRole dRole = new DRole(source.getCorpId(), source.getId());
        // 复制角色属性
        dRole = dRoleConverter.copyProperties(dRole, source);
        // 设置角色组
        dRole.setGroup(dRoleGroupRepository.getOne(source.getCorpId(), source.getGroupId()));
        return dRoleRepository.save(dRole);
    }

}
