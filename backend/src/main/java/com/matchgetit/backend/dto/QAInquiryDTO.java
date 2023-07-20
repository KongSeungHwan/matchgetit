package com.matchgetit.backend.dto;

import lombok.Data;

@Data
public class QAInquiryDTO {
    private Long id;
    private String title;
    private String content;
    private String category;
    private String state;
    private String writedBy; //작성자 이름
    private String regDate; //작성일

}
