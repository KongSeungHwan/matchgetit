package com.matchgetit.backend.controller;

import com.matchgetit.backend.entity.InquiryCommentEntity;
import com.matchgetit.backend.entity.InquiryEntity;
import com.matchgetit.backend.repository.InquiryCommentRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value="/matchGetIt")
public class QACommentController {
    InquiryCommentRepository inquiryCommentRepository;

    @GetMapping("/userInquiryCommentList")
    public @ResponseBody List<InquiryCommentEntity> getInquiryCommentList () {
        return inquiryCommentRepository.findAll();
    }

//    @PostMapping("/userInquiryAddComment")
//    public String userInquiryCommentSave(@RequestParam("category") String category, @RequestParam("QAtitle") String title, @RequestParam("QAcontent") String content, @RequestParam("QAName") String userId, HttpServletRequest request, Model model, HttpServletResponse response) {
//
//        InquiryCommentEntity inquiryComment = new InquiryCommentEntity();
//
//        inquiryCommentRepository.save(inquiryComment);
//
//        return "redirect:http://localhost:3000";
//    }

}
