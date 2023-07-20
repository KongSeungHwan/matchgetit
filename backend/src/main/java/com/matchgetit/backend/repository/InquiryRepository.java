package com.matchgetit.backend.repository;

import com.matchgetit.backend.entity.InquiryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>, InquiryRepositoryCustom {
    long countByRegTimeBetween(LocalDateTime from, LocalDateTime to);
    long countByStateContains(String state);
}
