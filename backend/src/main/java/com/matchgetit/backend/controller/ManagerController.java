package com.matchgetit.backend.controller;

import com.matchgetit.backend.constant.*;
import com.matchgetit.backend.dto.*;
import com.matchgetit.backend.entity.*;
import com.matchgetit.backend.repository.ManagerRepository;
import com.matchgetit.backend.repository.ManagerSupportRecordRepository;
import com.matchgetit.backend.repository.MemberRepository;
import com.matchgetit.backend.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@Controller
@RequestMapping("/matchGetIt/manager")
@AllArgsConstructor
public class ManagerController {
    private final ManagerService managerService;
    private final ManagerSupportRecordRepository managerSupportRecordRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;
    private final ManagerRepository managerRepository;
    private final MemberService memberService;
    private final MatchWaitService matchWaitService;
    private final StadiumService stadiumService;




    @GetMapping("/managers/test")
    public List<ManagerEntity> getAllMembers() {
        return managerRepository.findAll();
    }

    @GetMapping("/managers")
    public String getManagerList(HttpServletRequest request) {
        List<MemberEntity> managerList = memberRepository.findByLoginType(LogInType.MANAGER);
        List<MemberDTO> memberDTOList = new ArrayList<>();


        for (MemberEntity memberEntity : managerList) {
            MemberDTO memberDTO = new MemberDTO();
            memberDTO.setUserId(memberEntity.getUserId());
            memberDTO.setName(memberEntity.getName());
            memberDTO.setPn(memberEntity.getPn());

            ManagerEntity manager = memberEntity.getManagerEntity();
            if(manager != null){
                String managerImage = manager.getManagerImage();

                ManagerDTO managerDTO = new ManagerDTO();
                managerDTO.setManagerImage(managerImage);

                memberDTO.setManagerDTO(managerDTO);
            }
            memberDTOList.add(memberDTO);
        }



        request.setAttribute("managerList", memberDTOList);
        return "admin/pages/Manage/Manager";
    }

    //매니저 삭제
    @PutMapping("/deactivate/{userId}")
    public ResponseEntity<String> deactivateManager(@PathVariable Long userId) {
        MemberEntity memberEntity = memberRepository.findById(userId).orElse(null);
        if (memberEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        System.out.println("2323");
        // 매니저 지원 레코드 초기화
        ManagerSupportRecordEntity managerSupportRecordEntity = memberEntity.getManagerSupportRecordEntity();
        if (managerSupportRecordEntity != null) {
            memberEntity.setManagerSupportRecordEntity(null);
            managerSupportRecordEntity.setManagerUser(null);
            memberRepository.save(memberEntity);
            managerSupportRecordRepository.delete(managerSupportRecordEntity);
        }

        // 매니저 삭제
        ManagerEntity managerEntity = memberEntity.getManagerEntity();
        if (managerEntity != null) {
            memberEntity.setManagerEntity(null);
            managerEntity.setUser(null);
            memberRepository.save(memberEntity);
            managerRepository.delete(managerEntity);
        }

        // 거절 처리
        memberEntity.setLoginType(LogInType.NORMAL);
        memberRepository.save(memberEntity);

        return ResponseEntity.ok("매니저 삭제하였습니다.");
    }



    //매니저 정보수정 (휴직신청때 필요)
    @PutMapping("/EditManager/updateManager/{managerId}")
    public ResponseEntity<String> updateManager(@PathVariable("managerId") Long managerId, @RequestBody ManagerDTO updatedManager) {
        ManagerDTO managerDTO = managerService.getManagerById(managerId);

        if (managerDTO != null) {
            // 수정된 매니저 정보 업데이트
            managerDTO.getUser().setName(updatedManager.getUser().getName());
            managerDTO.getUser().setPn(updatedManager.getUser().getPn());
            managerDTO.getUser().setGender(updatedManager.getUser().getGender());
            managerDTO.setEmploymentStatus(updatedManager.getEmploymentStatus());

            if (EmploymentStatus.leave.equals(updatedManager.getEmploymentStatus())) {
                managerDTO.setLeaveStartDate(updatedManager.getLeaveStartDate());
                managerDTO.setLeaveEndDate(updatedManager.getLeaveEndDate());
                managerDTO.setLeaveReason(updatedManager.getLeaveReason());
            } else {
                managerDTO.setLeaveStartDate(null);
                managerDTO.setLeaveEndDate(null);
                managerDTO.setLeaveReason(null);
            }

            // 매니저 정보를 업데이트하는 메서드 호출
            managerService.updateManager(managerId, managerDTO);

            return ResponseEntity.ok().body("success");
        } else {
            return ResponseEntity.notFound().build();
        }
    }







    //일반유저가 매니저 신청때 사용되는 메서드
    @PostMapping("/submitForm/{userId}")
    public ResponseEntity<String> submitForm(@PathVariable("userId") Long userId, @RequestBody ManagerSupportRecordDTO formDTO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        MemberDTO memberDto = (MemberDTO) session.getAttribute("member");
        MemberEntity member = modelMapper.map(memberDto, MemberEntity.class);

        if (member != null) {
            member.setManagerSupportStatus(ManagerSupportStatus.WAITING);
            memberRepository.save(member);
        }

        // 중복 체크 로직
        boolean isDuplicate = managerSupportRecordRepository.existsByManagerUserAndIdNot(member, formDTO.getId());

        if (isDuplicate) {
            return ResponseEntity.badRequest().body("이미 지원된 지원자입니다.");
        }

        // 매니저 지원 양식 제출 처리
        ManagerSupportRecordEntity supportRecord = new ManagerSupportRecordEntity();
        supportRecord.setManagerUser(member);
        supportRecord.setActivityZone(formDTO.getActivityZone());
        supportRecord.setSubmissionDate(new Date()); // 현재 날짜와 시간으로 설정

        // supportRecord를 DB에 저장
        managerSupportRecordRepository.save(supportRecord);

        return ResponseEntity.ok("매니저 지원이 완료되었습니다.");
    }




    @GetMapping("/managerInfo/{userId}")
    public String viewManagerDetails(@PathVariable Long userId, Model model) {
        MemberDTO member = memberService.findMemberByIdManager(userId);

        ManagerDTO managerDTO = new ManagerDTO();
        managerDTO.setManagerId(member.getManagerDTO().getManagerId());
        managerDTO.setEmploymentStatus(member.getManagerDTO().getEmploymentStatus());
        managerDTO.setRegistrationDate(member.getManagerDTO().getRegistrationDate());

        member.setManagerDTO(managerDTO);

        model.addAttribute("manager", member);
        return "admin/pages/Manage/ManagerInfo";
    }

    @GetMapping("/EditManager/{userId}")
    public String editManager (@PathVariable Long userId, Model model){
        MemberDTO member = memberService.findMemberByIdManager(userId);

        ManagerDTO managerDTO = new ManagerDTO();
        if (managerDTO != null){
            managerDTO.setManagerId(member.getManagerDTO().getManagerId());
            managerDTO.setEmploymentStatus(member.getManagerDTO().getEmploymentStatus());
            managerDTO.setRegistrationDate(member.getManagerDTO().getRegistrationDate());
            managerDTO.setLeaveStartDate(member.getManagerDTO().getLeaveStartDate());
            managerDTO.setLeaveEndDate(member.getManagerDTO().getLeaveEndDate());
            managerDTO.setLeaveReason(member.getManagerDTO().getLeaveReason());
            System.out.println(member.getManagerDTO().getEmploymentStatus());
        }
        member.setManagerDTO(managerDTO);
        managerDTO.getEmploymentStatus().name();
        model.addAttribute("manager", member);

        return "admin/pages/Manage/EditManager";
    }
    
    
    @PostMapping("/getMatchList")
    public ResponseEntity<List<String>> getMatchList(@RequestParam String userId, @RequestParam String date){
        try{
            List<String> match = matchWaitService.getMatchWaitByManagerIdAndDate(userId,date);
            return new ResponseEntity<>((match!=null)?match:new ArrayList<>(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//날짜 입력 받을 시에 해당 잡힌 시간 출력
    @PostMapping("/getMatchDetails")
    public ResponseEntity<List<MatchWaitDTO>> getMatchWaitByMngId(@RequestParam String userId,@RequestParam String date,@RequestParam String time){
        try{
            List<MatchWaitDTO> match = matchWaitService.getMatchWaitByManagerIdAndDateAndTime(userId,date,time);
            return new ResponseEntity<>((match!=null)?match:new ArrayList<>(),HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//날짜 시간까지 입력 받을 시에 매치 잡힌 회원들 list 리턴
    @PostMapping("/matchEnd")
    public ResponseEntity<String> endMatchWait(@RequestParam String userId,@RequestParam String date,@RequestParam String time,@RequestParam String score,@RequestParam String etc){
        try{
            List<MatchWaitDTO> match = matchWaitService.getMatchWaitByManagerIdAndDateAndTime(userId,date,time);
            matchWaitService.matchEnd(match,score,etc);
                System.out.println(etc);
            return new ResponseEntity<>("성공",HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("실패",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/getStadium")
    public ResponseEntity<StadiumDTO> getStadium(@RequestParam String mngId){
        try{
            StadiumDTO stadium= stadiumService.findStadiumByMngId(mngId);
            return new ResponseEntity<>(stadium,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
