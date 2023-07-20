package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.MatchState;
import com.matchgetit.backend.entity.MemberEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class AdminMatchRecDTO {
    private Long matchRecId;
    private MemberEntity member;

    private String applicationTime;
    private Date applicationDate;
    private MatchState matchState;
    private String team;
    private String matchScore;
    private String etc; // 특이사항 또는 경기 취소 사유

    private Long stadiumId;
    private String stadiumName;
    private Long managerId;
    private String managerName;

    private String matchTime;

//    private Date matchSearchStr;
//    private Date matchSearchEnd;
//    private Long crd;

    public AdminMatchRecDTO() {}

    @QueryProjection
    public AdminMatchRecDTO(Long matchRecId, MemberEntity member, String applicationTime, Date applicationDate,
                            MatchState matchState, String team, String matchScore, String etc,
                            Long stadiumId, String stadiumName, Long managerId, String managerName) {
        this.matchRecId = matchRecId;
        this.member = member;
        setApplicationTime(applicationTime);
        this.applicationDate = applicationDate;
        this.matchState = matchState;
        this.team = team;
        this.matchScore = matchScore;
        this.etc = etc;
        this.stadiumId = stadiumId;
        this.stadiumName = stadiumName;
        this.managerId = managerId;
        this.managerName = managerName;
    }


    public void setApplicationTime(String applicationTime) {
        this.applicationTime = applicationTime;

        switch (applicationTime) {
            case "A" -> this.matchTime = "10:00 ~ 12:00";
            case "B" -> this.matchTime = "12:00 ~ 14:00";
            case "C" -> this.matchTime = "14:00 ~ 16:00";
            case "D" -> this.matchTime = "16:00 ~ 18:00";
            case "E" -> this.matchTime = "18:00 ~ 20:00";
            case "F" -> this.matchTime = "20:00 ~ 22:00";
        }
    }
}
