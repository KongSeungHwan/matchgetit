package com.matchgetit.backend.repository;

import com.matchgetit.backend.constant.MatchState;
import com.matchgetit.backend.entity.MatchRecEntity;
import com.matchgetit.backend.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MatchRecRepository extends JpaRepository<MatchRecEntity,Long>, MatchRecRepositoryCustom {
    List<MatchRecEntity> findByMemberAndApplicationDate(MemberEntity member, Date applicationDate);

    // Dashboard 사용
    long countByMatchStateIs(MatchState matchState);
}
