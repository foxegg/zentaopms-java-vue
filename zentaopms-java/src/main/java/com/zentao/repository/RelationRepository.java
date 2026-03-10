package com.zentao.repository;

import com.zentao.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Integer> {

    /** 与 PHP story unlinkStory 一致：删除两需求之间的 linkedto/linkedfrom 关系 */
    @Modifying
    @Query(value = "DELETE FROM zt_relation WHERE relation IN ('linkedto','linkedfrom') " +
            "AND AType IN ('story','requirement','epic') AND BType IN ('story','requirement','epic') " +
            "AND ((AID = :id1 AND BID = :id2) OR (AID = :id2 AND BID = :id1))", nativeQuery = true)
    void deleteStoryLink(@Param("id1") int id1, @Param("id2") int id2);

    List<Relation> findByAIDAndRelationAndAType(int aId, String relation, String aType);

    List<Relation> findByBIDAndRelationAndBType(int bId, String relation, String bType);

    /** 是否存在需求关联（linkedto/linkedfrom 任一方存在即可） */
    boolean existsByAIDAndBIDAndRelation(int aID, int bID, String relation);
}
