package com.matchgetit.backend.controller;

import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.dto.PaymentRecordDTO;
import com.matchgetit.backend.dto.SearchPaymentDTO;
import com.matchgetit.backend.service.MemberService;
import com.matchgetit.backend.service.PaymentHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/matchGetIt/payment")
@AllArgsConstructor
public class PaymentHistoryController {
    private final PaymentHistoryService paymentHistoryService;
    private final MemberService memberService;

    @GetMapping("/list-old")
    public String getPaymentUserList(Model model) {
        List<MemberDTO> userList = memberService.getAllMembers();
        List<PaymentRecordDTO> paymentRecordDTOList = new ArrayList<>();

//        for (MemberDTO user : userList) {
//            List<PaymentRecordEntity> paymentRecordEntityList = user.getPaymentRecordEntityList();
//            if (paymentRecordEntityList != null && !paymentRecordEntityList.isEmpty()) {
//                PaymentRecordEntity paymentRecordEntity = paymentRecordEntityList.get(0);
//                PaymentRecordDTO paymentRecordDTO = new PaymentRecordDTO();
//
//                paymentRecordDTO.setPaymentNumber(paymentRecordEntity.getPaymentNumber());
//                paymentRecordDTO.setGameNumber(paymentRecordEntity.getGameNumber());
//
//                paymentRecordDTO.setTransactionDateTime(paymentRecordEntity.getTransactionDate());
//                paymentRecordDTO.setCancelDateTime(paymentRecordEntity.getCancelDate());
//                paymentRecordDTO.setPaymentStatus(paymentRecordEntity.getTransactionStatus());
//
//                paymentRecordDTOList.add(paymentRecordDTO);
//            }
//        }

        model.addAttribute("userList", userList);
        model.addAttribute("paymentRecordList", paymentRecordDTOList);

        return "admin/pages/PaymentHistory/PaymentHistory";
    }

    @GetMapping({"/list", "/list/{page}"})
    public String paymentList(Model model, @PathVariable("page") Optional<Integer> page, SearchPaymentDTO searchPaymentDTO) {
//        System.out.println(searchPaymentDTO);
        Integer temp = searchPaymentDTO.getPageSize();
        int pageSize = temp == null ? 5 : temp;
        Pageable pageable = PageRequest.of(page.orElse(0), pageSize);

//        List<PaymentRecordDTO> paymentDTOList = paymentHistoryService.getPaymentRecordDTOList();
        Page<PaymentRecordDTO> paymentDTOList = paymentHistoryService.getPageablePaymentHistory(searchPaymentDTO, pageable);
        model.addAttribute("paymentList", paymentDTOList);
        model.addAttribute("currPageNum", pageable.getPageNumber());
        return "admin/pages/PaymentHistory/PaymentHistory2";
    }

}