package com.matchgetit.backend.controller;

import com.matchgetit.backend.dto.PaymentRecordDTO;
import com.matchgetit.backend.service.PaymentHistoryService;
import com.matchgetit.backend.util.FormatDate;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/matchGetIt/payment")
@AllArgsConstructor
public class UserPaymentController {
    private final PaymentHistoryService paymentHistoryService;

    @PostMapping("/userPayHistory")
    public ResponseEntity<List<PaymentRecordDTO>> findUserPayHistory(@RequestParam String userId, @RequestParam String date) {
            System.out.println(date);
            System.out.println(userId);
            List<PaymentRecordDTO> paymentList = paymentHistoryService.findByMemberAndDate(Long.parseLong(userId), FormatDate.parseDate(date));
            if (paymentList != null) {
                return new ResponseEntity<>(paymentList, HttpStatus.OK);
            } else return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
}