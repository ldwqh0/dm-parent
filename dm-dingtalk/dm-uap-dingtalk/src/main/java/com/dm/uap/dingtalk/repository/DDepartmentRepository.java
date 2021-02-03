package com.dm.uap.dingtalk.repository;

import com.dm.uap.dingtalk.entity.CorpLongId;
import com.dm.uap.dingtalk.entity.DDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DDepartmentRepository extends JpaRepository<DDepartment, CorpLongId> {

    /**
     * 根据部门信息查找相关的钉钉部门信息
     *
     * @param id 部门ID
     * @return 查找到的部门
     */
    Optional<DDepartment> findByDepartmentId(@NotNull Long id);

    List<DDepartment> findByCorpId(@NotEmpty String corpId);

    @Query("update DDepartment set deleted=:deleted where corpId=:corpId and id not in :ids")
    @Modifying
    int setDeletedByCorpidAndIdNotIn(@NotNull @Param("corpId") String corpId, @NotNull @Param("ids") Collection<Long> ids, @NotNull @Param("deleted") Boolean deleted);

    default DDepartment getOne(@NotNull String corpId, @NotNull Long departmentId) {
        return getOne(new CorpLongId(corpId, departmentId));
    }

    default boolean existsById(@NotNull String corpId, @NotNull Long id) {
        return existsById(new CorpLongId(corpId, id));
    }

    List<DDepartment> findByCorpIdAndDeleted(@NotNull String corpId, @NotNull Boolean deleted);

}
