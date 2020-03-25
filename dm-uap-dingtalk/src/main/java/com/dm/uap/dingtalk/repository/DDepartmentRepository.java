package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DDepartment;

public interface DDepartmentRepository extends JpaRepository<DDepartment, CorpLongId> {

    /**
     * 根据部门信息查找相关的钉钉部门信息
     * 
     * @param id
     * @return
     */
    public Optional<DDepartment> findByDepartmentId(Long id);

    public List<DDepartment> findByCorpId(String corpid);

    @Query("update DDepartment set deleted=?3 where corpId=?1 and (deleted !=?3 or deleted is null) and id not in (?2)")
    @Modifying
    public int setDeletedByCorpidAndIdNotIn(String corpid, Collection<Long> ids, Boolean deleted);

    public default DDepartment getOne(String corpid, Long departmentid) {
        return getOne(new CorpLongId(corpid, departmentid));
    }

    public default boolean existsById(String corpid, Long id) {
        return existsById(new CorpLongId(corpid, id));
    }

    public List<DDepartment> findByCorpIdAndDeleted(String corpid, Boolean deleted);

}
