package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.MatchState;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.entity.StadiumEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter @Setter @ToString
public class AdminMatchListDTO {
    private Long matchRecId;
    private String applicationTime;
    private Date applicationDate;
    private MatchState matchState;

//    private Date matchSearchStr;
//    private Date matchSearchEnd;
//    private Long crd;

    private Long stadiumId;
    private String stadiumName;
    private Long managerId;
    private String managerName;

    private String matchTime;


    public AdminMatchListDTO() {}

    @QueryProjection
    public AdminMatchListDTO(String matchTime, Date matchDate, MatchState matchState, Long stadiumId, String stadiumName, Long managerId, String managerName) {
//        this.matchRecId = matchRecId;
        setApplicationTime(matchTime);
        this.applicationDate = matchDate;
        this.matchState = matchState;
        this.stadiumId = stadiumId;
        this.stadiumName = stadiumName;
        this.managerId = managerId;
        this.managerName = managerName;
    }

    public AdminMatchListDTO(String matchTime, Date matchDate, Long stadiumId) {
//        this.matchRecId = matchRecId;
        this.applicationTime = matchTime;
        this.applicationDate = matchDate;
        this.stadiumId = stadiumId;
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
