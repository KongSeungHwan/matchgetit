package com.matchgetit.backend.repository;

import com.matchgetit.backend.dto.SearchPaymentDTO;
import com.matchgetit.backend.entity.PaymentRecordEntity;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.sql.Date;
import java.util.List;

import static com.matchgetit.backend.entity.QPaymentRecordEntity.paymentRecordEntity;

public class PaymentRecordRepositoryCustomImpl implements PaymentRecordRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PaymentRecordRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<PaymentRecordEntity> getPaymentHistoryListBy(SearchPaymentDTO searchPaymentDTO, Pageable pageable) {
        List<PaymentRecordEntity> content = queryFactory
                .selectFrom(paymentRecordEntity)
                .where(
                        searchWithCondition(searchPaymentDTO.getSearchCondition(), searchPaymentDTO.getSearchKeyword()),
                        searchDateBetween(searchPaymentDTO),
                        searchPaymentStatus(searchPaymentDTO)
                )
                .orderBy(paymentRecordEntity.paymentNumber.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    @Override
    public Long getPaymentHistoryCountBy(SearchPaymentDTO searchPaymentDTO) {
        return queryFactory
                .select(Wildcard.count)
                .from(paymentRecordEntity)
                .where(
                        searchWithCondition(searchPaymentDTO.getSearchCondition(), searchPaymentDTO.getSearchKeyword()),
                        searchDateBetween(searchPaymentDTO),
                        searchPaymentStatus(searchPaymentDTO)
                )
                .fetchFirst();
    }


    private BooleanExpression searchWithCondition(String searchType, String searchValue) {
        if (searchValue == null || searchValue.isEmpty()) return null;
        else if (StringUtils.equals("paymentId", searchType)) {
            return paymentRecordEntity.paymentNumber.like("%"+searchValue+"%");
        }
        else if (StringUtils.equals("userNumber", searchType)) {
            return paymentRecordEntity.member.userId.like("%"+searchValue+"%");
        }
        else if (StringUtils.equals("userName", searchType)) {
            return paymentRecordEntity.member.name.containsIgnoreCase(searchValue);
        }
        else if (StringUtils.equals("gameNumber", searchType)) {
            return paymentRecordEntity.gameNumber.like("%"+searchValue+"%");
        }
        return null;
    }

    private BooleanExpression searchDateBetween(SearchPaymentDTO searchPaymentDTO) {
        String searchDateStart = searchPaymentDTO.getCheckStartDate();
        String searchDateEnd = searchPaymentDTO.getCheckEndDate();
        String dateType = searchPaymentDTO.getDateType();

        if (searchDateStart == null || searchDateEnd == null || searchDateStart.isEmpty() || searchDateEnd.isEmpty())
            return null;

        Date from = Date.valueOf(searchDateStart);
        Date to = Date.valueOf(searchDateEnd);
        to.setDate(to.getDate()+1);

//        if (StringUtils.equals("paidDate", dateType)) {
//            return paymentRecordEntity.transactionDate.between(from, to);
//        }
//        if (StringUtils.equals("canceledDate", dateType)) {
//            return paymentRecordEntity.cancelDate.between(from, to);
//        }
//        else return null;
        return paymentRecordEntity.transactionDate.between(from, to);
    }

    private BooleanExpression searchPaymentStatus(SearchPaymentDTO searchPaymentDTO) {
        List<String> paymentStatusList = searchPaymentDTO.getPaymentStatus();
        if (paymentStatusList == null || paymentStatusList.isEmpty())
            return null;
        return paymentRecordEntity.transactionStatus.in(searchPaymentDTO.getEnumPaymentStatus());
    }
}
