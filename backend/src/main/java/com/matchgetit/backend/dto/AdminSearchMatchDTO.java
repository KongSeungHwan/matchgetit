package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.MatchState;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class AdminSearchMatchDTO {
    private String matchDateStart;
    private String matchDateEnd;
    private String matchTime;
    private MatchState matchState;
    private String stadiumName;
    private Integer pageSize;
}
