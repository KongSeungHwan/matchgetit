package com.matchgetit.backend.service;

import com.matchgetit.backend.constant.PaymentStatus;
import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.dto.PaymentRecordDTO;
import com.matchgetit.backend.dto.SearchPaymentDTO;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.entity.PaymentRecordEntity;
import com.matchgetit.backend.repository.MemberRepository;
import com.matchgetit.backend.repository.PaymentRecordRepository;
import com.matchgetit.backend.util.FormatDate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class PaymentHistoryService {

    private final PaymentRecordRepository paymentRecordRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    public List<PaymentRecordEntity> getPaymentRecords() {
        return paymentRecordRepository.findAll();
    }

    public List<PaymentRecordEntity> getPaymentRecordsByDateRange(Date startDate, Date endDate) {
        return paymentRecordRepository.findByTransactionDateBetween(startDate, endDate);
    }

    public List<PaymentRecordEntity> getPaymentRecordsBySearchCondition(String condition, String keyword) {
        if (condition.equals("name")) {
            // 회원 이름으로 결제 기록 검색
            List<MemberEntity> members = memberRepository.findByNameContaining(keyword);
            List<PaymentRecordEntity> paymentRecords = new ArrayList<>();
            for (MemberEntity member : members) {
                List<PaymentRecordEntity> records = paymentRecordRepository.findByMemberContaining(member);
                paymentRecords.addAll(records);
            }
            return paymentRecords;
        } else if (condition.equals("userId")) {
            // 회원 ID로 결제 기록 검색
            Long userId = Long.valueOf(keyword);
            MemberEntity member = memberRepository.findByUserId(userId);
            if (member != null) {
                return paymentRecordRepository.findByMemberContaining(member);
            }
        } else if (condition.equals("gameNumber")) {
            // 게임 번호로 결제 기록 검색
            return paymentRecordRepository.findByGameNumberContaining(keyword);
        }
        return null;
    }

    public List<PaymentRecordEntity> getPaymentRecordsByStatus(List<PaymentStatus> statuses) {
        return paymentRecordRepository.findByTransactionStatusIn(statuses);
    }


    public List<PaymentRecordDTO> getPaymentRecordDTOList() {
        List<PaymentRecordEntity> paymentList = paymentRecordRepository.findAll();
        List<PaymentRecordDTO> paymentDTOList = new ArrayList<>();

//        modelMapper.typeMap(PaymentRecordEntity.class, PaymentRecordDTO.class)
//                .addMappings(mapping -> {
//                    mapping.map(PaymentRecordEntity::getTransactionDate, PaymentRecordDTO::setTransactionDateTime);
//                    mapping.map(PaymentRecordEntity::getCancelDate, PaymentRecordDTO::setCancelDateTime);
//                    mapping.map(PaymentRecordEntity::getTransactionStatus, PaymentRecordDTO::setPaymentStatus);
//                });
//
//        modelMapper.typeMap(MemberEntity.class, MemberDTO.class)
//                .addMappings(mapping -> {
//                    mapping.skip(MemberDTO::setPaymentRecordDTO);
//                    mapping.skip(MemberDTO::setPaymentRecordEntityList);
//                });

        for (PaymentRecordEntity payment: paymentList) {
            PaymentRecordDTO paymentDTO = modelMapper.map(payment, PaymentRecordDTO.class);
//            paymentDTO.setMember(modelMapper.map(payment.getMember(), MemberDTO.class));
            paymentDTOList.add(paymentDTO);
        }

        return paymentDTOList;
    }


    public List<PaymentRecordDTO> findByMemberAndDate(Long userId, Date selectDate){
        MemberEntity member = memberRepository.findByUserId(userId);
        System.out.println(member.getName());
        List<PaymentRecordEntity> paymentEnList = paymentRecordRepository.findByMember(member);
        return paymentEnList.stream().filter(pm->FormatDate.formatDateToString(pm.getTransactionDate()).equals(FormatDate.formatDateToString(selectDate)))
                .map(p->modelMapper.map(p, PaymentRecordDTO.class)).toList();
    }

    public void insertData(MemberDTO member, int price) {
        PaymentRecordEntity paymentRecordEntity = new PaymentRecordEntity();
        paymentRecordEntity.setMember(modelMapper.map(member,MemberEntity.class));
        paymentRecordEntity.setTransactionDate(new Date());
        System.out.println(paymentRecordEntity.getTransactionDate());
        paymentRecordEntity.setTransactionStatus(PaymentStatus.COMPLETED);
        paymentRecordEntity.setPrice(price);
        paymentRecordRepository.save(paymentRecordEntity);
    }






    public void createPayments() {
        for (int i=1; i<=10; i++) {
            PaymentRecordEntity payment = new PaymentRecordEntity();
            payment.setPrice(10000);
            payment.setTransactionDate(new Date());
            payment.setGameNumber("200");
            payment.setTransactionStatus(PaymentStatus.COMPLETED);


            if (i>5) {
                payment.setTransactionDate(java.sql.Date.valueOf("2023-07-01"));
                payment.setCancelDate(new Date());
                payment.setCanceledPrice(5000);
                payment.setPrice(-5000);
                payment.setTransactionStatus(PaymentStatus.REFUNDED);
            }
            /*
            if (i==5) {
                payment.setTransactionDate(java.sql.Date.valueOf("2023-06-23"));
                payment.setCancelDate(new Date());
                payment.setCanceledPrice(10000);
                payment.setTransactionStatus(PaymentStatus.CANCELED);
            }
            */

            MemberEntity member = memberRepository.findByUserId((long) i);
            payment.setMember(member);

            paymentRecordRepository.save(payment);
        }
    }


    public Page<PaymentRecordDTO> getPageablePaymentHistory(SearchPaymentDTO searchPaymentDTO, Pageable pageable) {
        List<String> paymentStatusList = searchPaymentDTO.getPaymentStatus();
        List<PaymentStatus> enumPaymentStatusList = new ArrayList<>();

        if (paymentStatusList != null) {
            for (String paymentStatus: paymentStatusList) {
                enumPaymentStatusList.add(PaymentStatus.valueOf(paymentStatus.toUpperCase()));
            }
        }
        searchPaymentDTO.setEnumPaymentStatus(enumPaymentStatusList);

        List<PaymentRecordEntity> paymentList = paymentRecordRepository.getPaymentHistoryListBy(searchPaymentDTO, pageable);
        List<PaymentRecordDTO> paymentDTOList = new ArrayList<>();

        for (PaymentRecordEntity payment: paymentList) {
            PaymentRecordDTO paymentDTO = modelMapper.map(payment, PaymentRecordDTO.class);
//            paymentDTO.setMember(modelMapper.map(payment.getMember(), MemberDTO.class));
            paymentDTOList.add(paymentDTO);
        }

        Long total = paymentRecordRepository.getPaymentHistoryCountBy(searchPaymentDTO);
        return new PageImpl<>(paymentDTOList, pageable, total);
    }

}