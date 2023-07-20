package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.PaymentStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @ToString
public class SearchPaymentDTO {
    private String checkStartDate;
    private String checkEndDate;
    private String dateType;
    private String searchCondition;
    private String searchKeyword;
    private Integer pageSize;
    private List<String> paymentStatus;
    private List<PaymentStatus> enumPaymentStatus;
}
