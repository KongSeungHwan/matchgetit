package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.PaymentStatus;
import com.matchgetit.backend.entity.MemberEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter @Setter
public class PaymentRecordDTO {
    private Long paymentNumber;
    private MemberEntity member;
    private int price;
    private int canceledPrice;
    private Date transactionDate; // 거래 일시
    private Date cancelDate; // 취소 일시
    private String gameNumber; // 경기 번호
    private PaymentStatus transactionStatus; // 거래 상태
}