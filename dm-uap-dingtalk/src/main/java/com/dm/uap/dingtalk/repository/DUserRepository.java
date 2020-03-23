package com.dm.uap.dingtalk.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import com.dm.uap.dingtalk.entity.DUser;
import com.dm.uap.dingtalk.entity.DUserId;

public interface DUserRepository extends JpaRepository<DUser, DUserId>, QuerydslPredicateExecutor<DUser> {

    /**
     * 批量设置指定钉钉用户的删除状态
     * 
     * @param userIds 要设置状态的钉钉用户
     * @param b       重新设置的用户状态
     * @return
     */
    @Query("update DUser set deleted=?3 where corpId=?1 and deleted != ?3 and userid not in(?2)")
    @Modifying
    public int setDeletedByCorpidAndUseridNotIn(String corpid, Collection<String> userIds, boolean b);

    /**
     * 根据用户的唯一识别符和查找用户
     * 
     * @param unionid
     * @return
     */
    public List<DUser> findByUnionid(String unionid);

    /**
     * 根据删除标记查询钉钉用户对应的用户ID
     * 
     * @param b 需要过滤条件的状态
     * @return
     */
    @Query("select du.user.id from DUser du where du.corpId=?1 and du.deleted=?2")
    public List<Long> findUserIdsByCorpidAndDUserDeleted(String corpid, boolean b);

    /**
     * 根据钉钉用户ID查询用户ID
     * 
     * @param userid 要查询的钉钉用户ID的集合
     * @return
     */
    @Query("select du.user.id from DUser du where du.corpId=?1 and du.userid in (?2)")
    public List<Long> findUserIdsByCorpidAndIdIn(String corpid, Collection<String> userid);

    @Query("update DUser set deleted = ?3 where corpId=?1 and deleted != ?3 and userid in (?2)")
    @Modifying
    public void setDeletedByCorpidAndIdIn(String corpid, Collection<String> list, boolean b);

    public default DUser getOne(String corpid, String userid) {
        return getOne(new DUserId(corpid, userid));
    }

    public default Optional<DUser> findById(String corpid, String userid) {
        return findById(new DUserId(corpid, userid));
    }

    /**
     * 根据企业号和用户唯一识别符查找用户，主要用于确认是用户是否存在
     * 
     * @param corpId
     * @param unionid
     * @return
     */
    public Optional<DUser> findByCorpIdAndUnionid(String corpId, String unionid);

}
