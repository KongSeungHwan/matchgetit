package com.matchgetit.backend.controller;

import com.matchgetit.backend.constant.EmploymentStatus;
import com.matchgetit.backend.constant.LogInType;
import com.matchgetit.backend.constant.ManagerSupportStatus;
import com.matchgetit.backend.constant.MatchType;
import com.matchgetit.backend.dto.ManagerDTO;
import com.matchgetit.backend.dto.ManagerSupportRecordDTO;
import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.entity.ManagerEntity;
import com.matchgetit.backend.entity.ManagerSupportRecordEntity;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.repository.MemberRepository;
import com.matchgetit.backend.service.ManagerService;
import com.matchgetit.backend.service.MemberService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/matchGetIt/apply")
@Controller
@AllArgsConstructor
public class UserController {

    private final MemberRepository memberRepository;
    private final ManagerService managerService;
    private final ModelMapper modelMapper;
    private final MemberService memberService;



    // 매니저 지원한 사용자의 정보를 가져오는 엔드포인트
    @GetMapping("/getSupportedUsers")
    public String getSupportedUsers(Model model, @RequestParam(defaultValue = "1") int page) {
        int pageSize = 10; // 한 페이지에 보여줄 항목 수

        Pageable pageable = PageRequest.of(page - 1, pageSize); // 페이지 번호를 0부터 시작하도록 조정
        Page<MemberEntity> userPage = memberRepository.findByManagerSupportStatus(ManagerSupportStatus.WAITING, pageable);
        List<MemberEntity> supportedUsers = userPage.getContent();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for (MemberEntity memberEntity : supportedUsers) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUserId(memberEntity.getUserId());
            memberDTO.setName(memberEntity.getName());
            memberDTO.setPn(memberEntity.getPn());
            memberDTO.setGender(memberEntity.getGender());

            ManagerSupportRecordEntity managerSupportRecord = memberEntity.getManagerSupportRecordEntity();
            if (managerSupportRecord != null) {
                String activityZone = managerSupportRecord.getActivityZone();

                ManagerSupportRecordDTO managerSupportRecordDTO = new ManagerSupportRecordDTO();
                managerSupportRecordDTO.setActivityZone(activityZone);

                memberDTO.setManagerSupportRecordDTO(managerSupportRecordDTO);
            }
            memberDTOList.add(memberDTO);
        }

        model.addAttribute("list", memberDTOList);
        model.addAttribute("currentPage", page); // 현재 페이지 번호를 모델에 추가
        model.addAttribute("totalPages", userPage.getTotalPages()); // 전체 페이지 수를 모델에 추가


        return "admin/pages/Manage/ManagerApplicantsList";
    }


    // ManagerApplicantsList에서 등록 클릭시 유저 타입과 setManagerSupportStatus의 값을 변경해
    // 매니저 등록하는 버튼, 매니저 테이터 생성됨

    @PutMapping("/registerManager/{userId}")
    public ResponseEntity<String> registerManager(@PathVariable("userId") Long userId) {
        MemberEntity memberEntity = memberRepository.findById(userId).orElse(null);
        if (memberEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 매니저 등록 처리
        memberEntity.setManagerSupportStatus(ManagerSupportStatus.BASIC);
        memberEntity.setLoginType(LogInType.MANAGER);
        memberRepository.save(memberEntity);
        // ManagerEntity에 필요한 값들 설정
        ManagerEntity managerEntity = new ManagerEntity();
        managerEntity.setUser(memberEntity);
        managerEntity.setRegistrationDate(LocalDateTime.now()); // 매니저 등록 날짜를 설정하세요
        managerEntity.setEmploymentStatus(EmploymentStatus.active);


        // ManagerEntity를 ManagerDTO로 변환
        ManagerDTO managerDTO = modelMapper.map(managerEntity, ManagerDTO.class);

        // ManagerDTO 저장
        managerService.createManager(managerDTO);

        return ResponseEntity.ok("매니저가 성공적으로 등록되었습니다.");
    }

    // 등록 거절 처리
    @PutMapping("/rejectRegistration/{userId}")
    public ResponseEntity<String> rejectRegistration(@PathVariable("userId") Long userId) {
        MemberEntity memberEntity = memberRepository.findById(userId).orElse(null);
        if (memberEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // 거절 처리
        memberEntity.setManagerSupportStatus(ManagerSupportStatus.BASIC);
        // 매니저 지원 기록 초기화
        memberEntity.setManagerSupportRecordEntity(null);
        memberRepository.save(memberEntity);

        return ResponseEntity.ok("매니저 등록을 거절하였습니다.");
    }

    @GetMapping("/ManagerApplicantDetailsView/{userId}")
    public String viewManagerApplicantDetails(@PathVariable Long userId, Model model) {
        MemberDTO member = memberService.findMemberByIdManagerSupportRecord(userId, modelMapper);

        ManagerSupportRecordDTO managerSupportRecordDTO = new ManagerSupportRecordDTO();
        if (managerSupportRecordDTO != null) {
            managerSupportRecordDTO.setActivityZone(member.getManagerSupportRecordDTO().getActivityZone());
            managerSupportRecordDTO.setSubmissionDate(member.getManagerSupportRecordDTO().getSubmissionDate());
        }

        member.setManagerSupportRecordDTO(managerSupportRecordDTO);

        model.addAttribute("member", member);
        return "admin/pages/Manage/ManagerApplicantDetailsView";
    }





}
