package com.matchgetit.backend.dto;

import com.matchgetit.backend.constant.PayState;
import com.matchgetit.backend.entity.MemberEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

@Getter @Setter @ToString
public class AdminPaymentUserDTO {
    private Long userId;
    private String name;
    private String email;
    private String phoneNum;
    private Integer ownedCrd;
    private PayState payState;

    private static ModelMapper modelMapper = new ModelMapper();

    public static AdminPaymentUserDTO of(MemberEntity member) {
//        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        modelMapper.typeMap(MemberEntity.class, AdminPaymentUserDTO.class)
                .addMappings(mapping -> {
                    mapping.map(MemberEntity::getPn, AdminPaymentUserDTO::setPhoneNum);
                });
        return modelMapper.map(member, AdminPaymentUserDTO.class);
    }
}
