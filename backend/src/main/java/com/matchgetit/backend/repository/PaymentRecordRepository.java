package com.matchgetit.backend.repository;
import com.matchgetit.backend.constant.PaymentStatus;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.entity.PaymentRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecordEntity, Long>, PaymentRecordRepositoryCustom {
    List<PaymentRecordEntity> findByMember(MemberEntity member);

    List<PaymentRecordEntity> findByMemberContaining(MemberEntity member);

    // 결제 상태로 레코드를 조회하는 메서드
    List<PaymentRecordEntity> findByTransactionStatusIn(List<PaymentStatus> statuses);

    // 거래 일자 범위로 레코드를 조회하는 메서드
    List<PaymentRecordEntity> findByTransactionDateBetween(Date startDate, Date endDate);

    // 게임 번호로 레코드를 조회하는 메서드
    List<PaymentRecordEntity> findByGameNumberContaining(String number);
}