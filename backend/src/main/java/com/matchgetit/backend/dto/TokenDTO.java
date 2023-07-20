package com.matchgetit.backend.dto;

import com.matchgetit.backend.entity.MemberEntity;
import lombok.Data;

@Data
public class TokenDTO {
    private Long tokenId;
    private MemberEntity member;
    private String token;
}
