package com.matchgetit.backend.service;

import com.matchgetit.backend.constant.*;
import com.matchgetit.backend.dto.*;
import com.matchgetit.backend.entity.ManagerEntity;
import com.matchgetit.backend.entity.ManagerSupportRecordEntity;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.entity.PartyEntity;
import com.matchgetit.backend.repository.MemberRepository;
import com.matchgetit.backend.util.FormatDate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public void signUp(String email, String password, String name, String pn, String birthDay, Gender gender, Proficiency proficiency, AccountType accountType) {
        // 이미 존재하는 사용자인지 확인
        if (memberRepository.findByEmail(email) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        MemberEntity member = new MemberEntity();
        member.setEmail(email);
        member.setPw(passwordEncoder.encode(password)); // 비밀번호 암호화
        member.setName(name);
        member.setPn(pn);
        member.setBDay(FormatDate.parseDate(birthDay));
        member.setGender(gender);
        member.setPrfcn(proficiency);
        member.setAccountType(accountType);
        member.setLoginType(LogInType.ADMIN);
        member.setPayState(PayState.POINT);
        member.setManagerSupportStatus(ManagerSupportStatus.BASIC);
        if(proficiency == Proficiency.ADVANCED)member.setRating(800L);
        else if(proficiency == Proficiency.MIDDLE)member.setRating(500L);
        else member.setRating(300L);
        member.setOwnedCrd(0);

        member.setRegDate(new Date());
        member.setAccountState(AccountState.ACTIVE);
        member.setLastConnectionDate(new Date());
        memberRepository.save(member);
    }
    public void socialSignUp(String email, String name, String pn, String birthDay, Gender gender, Proficiency proficiency,AccountType accountType,LogInType logInType) {
        // 이미 존재하는 사용자인지 확인
        if (memberRepository.findByEmail(email) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        MemberEntity member = new MemberEntity();
        member.setEmail(email);
        member.setName(name);
        member.setPn(pn);
        member.setBDay(FormatDate.parseDate(birthDay));
        member.setGender(gender);
        member.setPrfcn(proficiency);
        member.setAccountType(accountType);
        member.setLoginType(logInType);
        member.setPayState(PayState.POINT);
        member.setManagerSupportStatus(ManagerSupportStatus.BASIC);
        if(proficiency == Proficiency.ADVANCED)member.setRating(800L);
        else if(proficiency == Proficiency.MIDDLE)member.setRating(500L);
        else member.setRating(300L);
        member.setOwnedCrd(0);

        member.setRegDate(new Date());
        member.setAccountState(AccountState.ACTIVE);
        member.setLastConnectionDate(new Date());
        memberRepository.save(member);
    }
    public void googleSignUp(String email, String name,String pn, String birthday, Gender gender, Proficiency proficiency, AccountType accountType, LogInType logInType) {
        // 이미 존재하는 사용자인지 확인
        if (memberRepository.findByEmail(email) != null) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        MemberEntity member = new MemberEntity();
        member.setEmail(email);
        member.setName(name);
        member.setGender(gender);
        member.setBDay(FormatDate.parseDate(birthday));
        member.setPn(pn);
        member.setPrfcn(proficiency);
        member.setAccountType(accountType);
        member.setLoginType(logInType);
        member.setPayState(PayState.POINT);
        if (proficiency == Proficiency.ADVANCED) member.setRating(800L);
        else if (proficiency == Proficiency.MIDDLE) member.setRating(500L);
        else member.setRating(300L);
        member.setOwnedCrd(0);

        member.setRegDate(new Date());
        member.setAccountState(AccountState.ACTIVE);
        member.setLastConnectionDate(new Date());
        memberRepository.save(member);
    }

    public MemberDTO login(String email, String password) {
        MemberEntity member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new RuntimeException("계정 정보가 일치하지 않습니다");
        }

        if (!passwordEncoder.matches(password, member.getPw())) {
            throw new RuntimeException("계정 정보가 일치하지 않습니다");
        }
        return new ModelMapper().map(member, MemberDTO.class);
    }
    public MemberDTO findMemberById(Long userId){
        MemberEntity memberEntity= memberRepository.findByUserId(userId);
        if(memberEntity ==null) return null;
        else return modelMapper.map(memberEntity,MemberDTO.class);
    }
    public MemberDTO findMemberByEmail(String email){
        MemberEntity memberEntity= memberRepository.findByEmail(email);
        if(memberEntity ==null) return null;
        else return modelMapper.map(memberEntity,MemberDTO.class);
    }
    public MemberDTO findMemberByPhoneNumber(String pn){
        MemberEntity memberEntity= memberRepository.findByPn(pn);
        if(memberEntity ==null) return null;
        else return modelMapper.map(memberEntity,MemberDTO.class);
    }
    public void updateParty(Long userId, PartyDTO partyDTO) {
        MemberEntity member = memberRepository.findByUserId(userId);
        if (member == null) {
            throw new RuntimeException("해당 회원이 존재하지 않습니다.");
        }
        if (partyDTO == null) {
            throw new RuntimeException("해당 파티가 존재하지 않습니다.");
        }
        PartyEntity partyEntity= modelMapper.map(partyDTO,PartyEntity.class);

        member.setParty(partyEntity);
        memberRepository.save(member);
    }
    public void deleteParty(Long userId) {
        MemberEntity member = memberRepository.findByUserId(userId);
        if (member == null) {
            throw new RuntimeException("해당 회원이 존재하지 않습니다.");
        }
        member.setParty(null);
        memberRepository.save(member);
    }
    @Transactional
    public int updateCredit(Long userId, int value) {
        MemberEntity member = memberRepository.findByUserId(userId);
        int renewCrd =member.getOwnedCrd()+value;
        System.out.println(renewCrd);
        member.setOwnedCrd(renewCrd);
        memberRepository.save(member);
        return renewCrd;
    }//결제 시 사용하는 메소드

    public List<MemberDTO> getAllMembers() {
        List<MemberEntity> memberEntities = memberRepository.findAll();
        return memberEntities.stream()
                .map(memberEntity -> modelMapper.map(memberEntity, MemberDTO.class))
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateProfile(Long id, String name, String email, String pn, HttpSession session) {
        MemberEntity member = memberRepository.findByUserId(id);

        if (member != null) {
            member.setEmail(email);
            member.setName(name);
            member.setPn(pn);
            memberRepository.save(member);

        } else {
            throw new RuntimeException("프로필 업데이트에 실패했습니다.");
        }
    }
    @Transactional
    public void deleteProfile(Long id, HttpSession session){
        memberRepository.deleteById(id);
        session.removeAttribute("member");
    }

    @Transactional
    public void updatePw(Long id, String password){
        MemberEntity member = memberRepository.findByUserId(id);
        if (member != null) {
            member.setPw(passwordEncoder.encode(password));
            memberRepository.save(member);

        }else{
            throw new RuntimeException("프로필 업데이트에 실패했습니다.");
        }
    }

    public Page<MemberDTO> getMembersByPage(Pageable pageable) {
        Page<MemberEntity> memberPage = memberRepository.findAll(pageable);
        List<MemberEntity> memberEntities = memberPage.getContent();

        List<MemberDTO> memberDTOs = new ArrayList<>();
        for (MemberEntity memberEntity : memberEntities) {
            MemberDTO memberDTO = convertToDTO(memberEntity);
            memberDTOs.add(memberDTO);
        }

        return new PageImpl<>(memberDTOs, pageable, memberPage.getTotalElements());
    }

    private MemberDTO convertToDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setUserId(memberEntity.getUserId());
        memberDTO.setName(memberEntity.getName());
        return memberDTO;
    }


    public MemberDTO findMemberByIdManager(Long userId) {
        MemberEntity memberEntity = memberRepository.findByUserId(userId);

        if (memberEntity == null) {
            return null;
        } else {
            MemberDTO memberDTO = modelMapper.map(memberEntity, MemberDTO.class);

            ManagerEntity managerEntity = memberEntity.getManagerEntity();
            if (managerEntity != null) {
                ManagerDTO managerDTO = modelMapper.map(managerEntity, ManagerDTO.class);
                memberDTO.setManagerDTO(managerDTO);
            }

            return memberDTO;
        }
    }

    public MemberDTO findMemberByIdManagerSupportRecord(Long userId, ModelMapper modelMapper) {
        MemberEntity memberEntity = memberRepository.findByUserId(userId);

        if (memberEntity == null) {
            return null;
        } else {
            MemberDTO memberDTO = modelMapper.map(memberEntity, MemberDTO.class);

            ManagerSupportRecordEntity managerSupportRecordEntity = memberEntity.getManagerSupportRecordEntity();
            if (managerSupportRecordEntity != null) {
                ManagerSupportRecordDTO managerSupportRecordDTO = modelMapper.map(managerSupportRecordEntity, ManagerSupportRecordDTO.class);
                memberDTO.setManagerSupportRecordDTO(managerSupportRecordDTO);
            }

            return memberDTO;
        }
    }






}
