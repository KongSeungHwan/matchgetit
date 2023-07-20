package com.matchgetit.backend.repository;

import com.matchgetit.backend.dto.SearchPaymentDTO;
import com.matchgetit.backend.entity.PaymentRecordEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PaymentRecordRepositoryCustom {
    List<PaymentRecordEntity> getPaymentHistoryListBy(SearchPaymentDTO searchPaymentDTO, Pageable pageable);
    Long getPaymentHistoryCountBy(SearchPaymentDTO searchPaymentDTO);
}
