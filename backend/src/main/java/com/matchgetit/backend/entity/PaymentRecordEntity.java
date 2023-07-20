package com.matchgetit.backend.entity;

import com.matchgetit.backend.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "payments")
@Getter @Setter
public class PaymentRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "paymentNumber")
    private Long paymentNumber;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private MemberEntity member;

    @Column
    private int price;

    @Column
    private int canceledPrice;

    @Column
    @Temporal(TemporalType.DATE)
    private Date transactionDate; // 거래 일시

    @Column
    @Temporal(TemporalType.DATE)
    private Date cancelDate; // 취소 일시

    @Column
    private String gameNumber; // 경기 번호

    @Column
    @Enumerated(EnumType.STRING)
    private PaymentStatus transactionStatus; // 거래 상태
}