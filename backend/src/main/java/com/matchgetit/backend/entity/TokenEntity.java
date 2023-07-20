package com.matchgetit.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="TOKEN_ID")
    private Long tokenId;
    @JoinColumn(name="USER_ID")
    private MemberEntity member;
    @Column(name="token")
    private String token;
}
