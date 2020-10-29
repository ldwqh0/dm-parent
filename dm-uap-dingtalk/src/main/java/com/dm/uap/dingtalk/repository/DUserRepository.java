package com.dm.uap.dingtalk.repository;

import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.entity.DUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DUserRepository extends JpaRepository<DUser, DUserId>, QuerydslPredicateExecutor<DUser> {

    /**
     * 根据用户的唯一识别符和查找用户
     *
     * @param unionid 用户唯一识别符号
     * @return 查找到的用户
     */
    List<DUser> findByUnionid(String unionid);

    /**
     * 批量设置指定钉钉用户的删除状态
     *
     * @param corpId  要设置状态的机构
     * @param ids     要设置状态的钉钉用户
     * @param deleted 重新设置的用户状态
     * @return 受影响的行数
     */
    @Query("update DUser set deleted=:deleted where corpId=:corpId and unionid not in(:ids)")
    @Modifying
    int setDeletedByCorpIdAndUnionidNotIn(@Param("corpId") String corpId, @Param("ids") Collection<String> ids, @Param("deleted") boolean deleted);

    /**
     * 根据删除标记查询钉钉用户对应的用户ID
     *
     * @param deleted 需要过滤条件的状态
     * @return 查询到的用户ID的列表
     */
    @Query("select du.user.id from DUser du where du.corpId=:corpId and du.deleted=:deleted")
    List<Long> findUserIdsByCorpIdAndDUserDeleted(@Param("corpId") String corpId, @Param("deleted") boolean deleted);

    default DUser getOne(String corpid, String userid) {
        return getOne(DUserId.of(corpid, userid));
    }

    default Optional<DUser> findById(String corpid, String userid) {
        return findById(DUserId.of(corpid, userid));
    }

    /**
     * 根据企业号和用户唯一识别符查找用户，主要用于确认是用户是否存在
     *
     * @param corpId  企业和
     * @param unionid 用于唯一识别符号
     * @return 返回查找到的用户
     */
    Optional<DUser> findByCorpIdAndUnionid(String corpId, String unionid);

    /**
     * @param corpid  企业ID
     * @param deleted 删除状态
     */
    List<DUser> findByCorpIdAndDeleted(String corpid, Boolean deleted);

    default boolean existsById(String corpid, String userid) {
        return existsById(DUserId.of(corpid, userid));
    }

}
