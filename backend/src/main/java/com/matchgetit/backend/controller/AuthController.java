package com.matchgetit.backend.controller;

import com.matchgetit.backend.config.JwtTokenProvider;
import com.matchgetit.backend.constant.AccountState;
import com.matchgetit.backend.constant.AccountType;
import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.request.SignUpRequest;
import com.matchgetit.backend.request.UpdateRequest;
import com.matchgetit.backend.service.MemberService;
import com.matchgetit.backend.service.PaymentHistoryService;
import com.matchgetit.backend.service.StadiumService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.matchgetit.backend.request.LoginRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matchGetIt/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private MemberService memberService;
    private final StadiumService stadiumService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/insert")
    @ResponseBody
    public String insertDummyData() {
        stadiumService.insertStadium();
        return "더미 데이터가 성공적으로 삽입되었습니다.";
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                // 유효성 검사 오류가 있는 경우 처리
                StringBuilder errorReason = new StringBuilder();
                for (FieldError error : bindingResult.getFieldErrors()) {
                    errorReason.append(error.getDefaultMessage()).append("\n");
                }
                return new ResponseEntity<>(errorReason.toString(), HttpStatus.BAD_REQUEST);
            }
            MemberDTO member = memberService.login(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );
            if(member!=null){
                if (member.getAccountState() == AccountState.BANNED) {
                    return new ResponseEntity<>("정지된 계정입니다.", HttpStatus.UNAUTHORIZED);
                }
                String token = jwtTokenProvider.generateToken(member.getEmail());
                System.out.println("토큰>>>>>>>"+token);
                session.setAttribute("token",token);
                session.setAttribute("member",member);
                return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
            }else{
                return new ResponseEntity<>("로그인 실패", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.removeAttribute("member");
        return ResponseEntity.ok("로그아웃되었습니다.");
    }
    @PostMapping("/signUp")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequest signUpRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있는 경우 처리
            StringBuilder errorReason = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorReason.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errorReason.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            memberService.signUp(
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    signUpRequest.getName(),
                    signUpRequest.getPn(),
                    signUpRequest.getBirthDay(),
                    signUpRequest.getGender(),
                    signUpRequest.getProficiency(),
                    AccountType.NORMAL
            );
            return new ResponseEntity<>("회원가입이 완료되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/session")
    public ResponseEntity<MemberDTO> getUserProfile(HttpSession session) {
        MemberDTO member = (MemberDTO) session.getAttribute("member");
        
        if (member != null) {
            return ResponseEntity.ok(member);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @PostMapping("/token")
    public ResponseEntity<String> getUserToken(HttpSession session) {
        try{
            String token = (String)session.getAttribute("token");
            if(token==null) {
                MemberDTO member = (MemberDTO) session.getAttribute("member");
                MemberDTO auth = memberService.findMemberByEmail(member.getEmail());
                if (auth != null) {
                    token = jwtTokenProvider.generateToken(member.getEmail());
                    session.setAttribute("token", token);
                    System.out.println("인증 성공! 토큰 발급>>>>" + token);
                    return ResponseEntity.ok(token);
                } else {
                    return new ResponseEntity<>("비로그인 상태", HttpStatus.OK);
                }
            }else return ResponseEntity.ok(token);

        }catch(Exception e){
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/vaildateEmail")
    public ResponseEntity<String> validateEmail(@RequestParam String email){
        MemberDTO dto=  memberService.findMemberByEmail(email);
        if(dto!=null){
            return new ResponseEntity<>("불가능",HttpStatus.UNAUTHORIZED);
        }else{
            return new ResponseEntity<>("가능", HttpStatus.OK);
        }
    }
    @PostMapping("/validatePhoneNumber")
    public ResponseEntity<String> validatePhoneNumber(@RequestParam String phoneNumber){
        MemberDTO dto=  memberService.findMemberByPhoneNumber(phoneNumber);
        if((dto==null)&&(phoneNumber.matches("^\\d{3}-\\d{4}-\\d{4}$"))){
            return new ResponseEntity<>("가능", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("불가능",HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/update")
    public ResponseEntity<String> updateProfile(@RequestParam Long id , @Valid @RequestBody UpdateRequest updateRequest,  BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있는 경우 처리
            StringBuilder errorReason = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorReason.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errorReason.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            HttpSession session = request.getSession(false);
            MemberDTO member = (MemberDTO) session.getAttribute("member");
            System.out.println(member.getAccountType());
            if (member.getAccountType() == AccountType.NORMAL) {
                memberService.updateProfile(
                        id,
                        updateRequest.getName(),
                        updateRequest.getEmail(),
                        updateRequest.getPn(),
                        session
                );

                return new ResponseEntity<>("프로필이 수정되었습니다.", HttpStatus.OK);
            }else {
                return new ResponseEntity<>("권한이 없습니다.", HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfile(@RequestParam Long id, HttpSession session){
        try {
            memberService.deleteProfile(id, session);
            return new ResponseEntity<>("회원탈퇴 되었습니다.", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/updatePw")
    public ResponseEntity<String> updatePw(@RequestParam Long id , @RequestBody UpdateRequest updateRequest,  BindingResult bindingResult, HttpServletRequest request){
        if (bindingResult.hasErrors()) {
            // 유효성 검사 오류가 있는 경우 처리
            StringBuilder errorReason = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorReason.append(error.getDefaultMessage()).append("\n");
            }
            return new ResponseEntity<>(errorReason.toString(), HttpStatus.BAD_REQUEST);
        }
        try {
            HttpSession session = request.getSession(false);
            MemberDTO member = (MemberDTO) session.getAttribute("member");
            if (member.getAccountType() == AccountType.NORMAL) {
                memberService.updatePw(
                        id,
                        updateRequest.getPassword()
                );
            }
            return new ResponseEntity<>("프로필이 수정되었습니다.",HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
