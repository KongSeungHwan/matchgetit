package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.MatchState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter @Setter @ToString
public class AdminMatchInfoDTO {
    private Long matchRecId;

    private List<Map<String, String>> teamA_Members = new ArrayList<>();
    private List<Map<String, String>> teamB_Members = new ArrayList<>();

    private String applicationTime;
    private String applicationDate;
    private MatchState matchState;
//    private String team;
//    private String matchScore;
    private String etc; // 특이사항 또는 경기 취소 사유

    private Long stadiumId;
    private String stadiumName;
    private Long managerId;
    private String managerName;
}
