package com.matchgetit.backend.entity;

import com.matchgetit.backend.constant.MatchState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="MATCH_REC")
@Getter
@Setter
public class MatchRecEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="MATCH_REC_ID")
    private Long matchRecId;
    @ManyToOne
    @JoinColumn(name="MEMBER_ID")
    private MemberEntity member;
    @ManyToOne
    @JoinColumn(name="PARTY_LEADER_ID")
    private MemberEntity partyLeader;
    @ManyToOne
    @JoinColumn(name="STD_ID",nullable=true)
    private StadiumEntity stadium;
    //matchWait 칼럼 외래키
    @ManyToOne
    @JoinColumn(name="MANAGER_ID")
    private MemberEntity manager;//매니저 id ( = 유저id )
    @Column(name="MATCH_SCORE")
    private String matchScore;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MATCH_SEARCH_STR")
    private Date matchSearchStr;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MATCH_SEARCH_END")
    private Date matchSearchEnd;
    @Column(name="TEAM")
    private String team;
    @Column(name="APPLICATION_TIME")
    private String applicationTime;
    @Temporal(TemporalType.DATE)
    @Column(name="APPLICATION_DATE")
    private Date applicationDate;
    @Temporal(TemporalType.DATE)
    @Column(name="CREDIT")
    private Long crd;
    @Enumerated(EnumType.STRING)
    @Column(name="MATCH_STATE")
    private MatchState matchState;
    @Column(name="ETC")
    private String etc;
}
