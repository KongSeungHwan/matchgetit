package com.matchgetit.backend.controller;

import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.entity.InquiryEntity;
import com.matchgetit.backend.repository.InquiryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@Controller
@RequestMapping(value="/matchGetIt")
public class UserInquiryBoardController {

    InquiryRepository inquiryRepository;

    @PostMapping("/userInquiry")
    public String userInquirySave(@RequestParam("category") String category, @RequestParam("QAtitle") String title, @RequestParam("QAcontent") String content, @RequestParam("QAName") String userId, HttpServletRequest request, Model model, HttpServletResponse response, HttpSession session) {

        System.out.println("1");
        System.out.println(userId);
        System.out.println(title);
        InquiryEntity inquiry = new InquiryEntity();
        inquiry.setTitle(title);
        inquiry.setContent(content);
        inquiry.setCategory(category);
        inquiry.setState("접수 대기");
//        inquiry.setRegTime(LocalDateTime.now());
//        inquiry.setCreatedBy(userId);
//        inquiry.setModifiedBy(userId);
//        System.out.println("id"+inquiry.getCreatedBy());

        if (session.getAttribute("member") != null)
            inquiry.setWriterName(((MemberDTO) session.getAttribute("member")).getName());
        else
            inquiry.setWriterName("유저");

        inquiryRepository.save(inquiry);

        return "redirect:http://localhost:3000";
    }

    @GetMapping("/userInquiryList")
    public @ResponseBody List<InquiryEntity> getInquiryList () {
        return inquiryRepository.findAll();
    }

}
