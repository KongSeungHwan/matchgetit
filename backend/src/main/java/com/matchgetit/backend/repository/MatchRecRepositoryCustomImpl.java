package com.matchgetit.backend.repository;

import com.matchgetit.backend.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

//import java.util.Date;
import java.sql.Date;
import java.util.List;

import static com.matchgetit.backend.entity.QMatchRecEntity.matchRecEntity;
import static com.matchgetit.backend.entity.QPaymentRecordEntity.paymentRecordEntity;

public class MatchRecRepositoryCustomImpl implements MatchRecRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MatchRecRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<AdminMatchListDTO> getMatchListBy(AdminSearchMatchDTO searchMatchDTO, Pageable pageable) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!StringUtils.isEmpty(searchMatchDTO.getMatchTime())) {
            booleanBuilder.and(matchRecEntity.applicationTime.eq(searchMatchDTO.getMatchTime()));
        }

        if (!StringUtils.isEmpty(searchMatchDTO.getStadiumName())) {
            booleanBuilder.and(matchRecEntity.stadium.stdName.containsIgnoreCase(searchMatchDTO.getStadiumName()));
        }

        if (searchMatchDTO.getMatchState() != null) {
            booleanBuilder.and(matchRecEntity.matchState.eq(searchMatchDTO.getMatchState()));
        }

        List<AdminMatchListDTO> content = queryFactory
                .selectDistinct(new QAdminMatchListDTO(
//                        matchRecEntity.matchRecId,
                        matchRecEntity.applicationTime,
                        matchRecEntity.applicationDate,
                        matchRecEntity.matchState,
                        matchRecEntity.stadium.stdId,
                        matchRecEntity.stadium.stdName,
                        matchRecEntity.manager.userId,
                        matchRecEntity.manager.name
                ))
                .from(matchRecEntity)
                .where(
                        searchDateBetween(searchMatchDTO),
                        booleanBuilder
                )
                .orderBy(matchRecEntity.applicationDate.desc())
                .offset(pageable.getOffset()).limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    public List<AdminMatchListDTO> getPagedMatchListBy(AdminSearchMatchDTO searchMatchDTO) {
        return null;
    }

    public Long getMatchCountBy(AdminSearchMatchDTO searchMatchDTO) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!StringUtils.isEmpty(searchMatchDTO.getMatchTime())) {
            booleanBuilder.and(matchRecEntity.applicationTime.eq(searchMatchDTO.getMatchTime()));
        }

        if (!StringUtils.isEmpty(searchMatchDTO.getStadiumName())) {
            booleanBuilder.and(matchRecEntity.stadium.stdName.containsIgnoreCase(searchMatchDTO.getStadiumName()));
        }

        if (searchMatchDTO.getMatchState() != null) {
            booleanBuilder.and(matchRecEntity.matchState.eq(searchMatchDTO.getMatchState()));
        }

        List<AdminMatchListDTO> content = queryFactory
                .selectDistinct(Projections.constructor(AdminMatchListDTO.class,
                        matchRecEntity.applicationTime,
                        matchRecEntity.applicationDate,
                        matchRecEntity.stadium.stdId))
                .from(matchRecEntity)
                .where(
                        searchDateBetween(searchMatchDTO),
                        booleanBuilder
                )
                .orderBy(matchRecEntity.applicationDate.desc())
                .fetch();
        return (long) content.size();
    }


    @Override
    public List<AdminMatchRecDTO> getMatchInfoBy(String matchDate, String matchTime, Long stadiumId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!StringUtils.isEmpty(matchDate)) {
            Date from = Date.valueOf(matchDate);
            Date to = Date.valueOf(matchDate);
            to.setDate(to.getDate()+1);
            booleanBuilder.and(matchRecEntity.applicationDate.between(from, to));
        }

        if (!StringUtils.isEmpty(matchTime)) {
            booleanBuilder.and(matchRecEntity.applicationTime.eq(matchTime));
        }

        if (stadiumId != null) {
            booleanBuilder.and(matchRecEntity.stadium.stdId.eq(stadiumId));
        }

        List<AdminMatchRecDTO> matchInfoDTO = queryFactory
                .selectDistinct(new QAdminMatchRecDTO(
                        matchRecEntity.matchRecId,
                        matchRecEntity.member,
                        matchRecEntity.applicationTime,
                        matchRecEntity.applicationDate,
                        matchRecEntity.matchState,
                        matchRecEntity.team,
                        matchRecEntity.matchScore,
                        matchRecEntity.etc,
                        matchRecEntity.stadium.stdId,
                        matchRecEntity.stadium.stdName,
                        matchRecEntity.manager.userId,
                        matchRecEntity.manager.name
                ))
                .from(matchRecEntity)
                .where(booleanBuilder)
                .orderBy(matchRecEntity.matchRecId.desc())
                .fetch();

        return matchInfoDTO;
    }


    private BooleanExpression searchDateBetween(AdminSearchMatchDTO searchMatchDTO) {
        String searchDateStart = searchMatchDTO.getMatchDateStart();
        String searchDateEnd = searchMatchDTO.getMatchDateEnd();

        if (searchDateStart == null || searchDateEnd == null || searchDateStart.isEmpty() || searchDateEnd.isEmpty())
            return null;

        Date from = Date.valueOf(searchDateStart);
        Date to = Date.valueOf(searchDateEnd);
        to.setDate(to.getDate()+1);

        return matchRecEntity.applicationDate.between(from, to);
    }

}
