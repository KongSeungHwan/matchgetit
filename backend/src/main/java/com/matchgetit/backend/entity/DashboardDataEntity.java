package com.matchgetit.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "DASHBOARD_DATA")
@Getter @Setter @ToString
public class DashboardDataEntity {
    @Id
    @Column(name = "dashboard_data_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // (오늘) 탈퇴한 회원 수
    private Integer canceledMembership;
    // (오늘) 취소된 경기 수
    private Integer canceledMatch;
}
// 병합에 아직 반영되지 않은 파일.