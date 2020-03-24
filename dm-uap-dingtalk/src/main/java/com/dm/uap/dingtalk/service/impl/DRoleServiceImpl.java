package com.dm.uap.dingtalk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dm.uap.dingtalk.entity.DRole;
import com.dm.uap.dingtalk.repository.DRoleRepository;
import com.dm.uap.dingtalk.service.DRoleService;
import com.dm.uap.entity.Role;
import com.dm.uap.repository.RoleRepository;

import lombok.extern.slf4j.Slf4j;
import static java.lang.Boolean.*;

@Service
@Slf4j
public class DRoleServiceImpl implements DRoleService {

    @Autowired
    private DRoleRepository dRoleRepository;

    @Autowired
    private RoleRepository roleRepository;

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

}
