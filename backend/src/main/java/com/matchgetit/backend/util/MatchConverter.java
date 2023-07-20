//package com.matchgetit.backend.util;
//
//import com.matchgetit.backend.entity.MatchEntity;
//import com.matchgetit.backend.entity.MatchWaitEntity;
//import com.matchgetit.backend.entity.PartyEntity;
//import com.matchgetit.backend.entity.StadiumEntity;
//import com.matchgetit.backend.repository.MatchRepository;
//import com.matchgetit.backend.repository.MatchWaitRepository;
//import com.matchgetit.backend.repository.StadiumRepository;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//public class MatchConverter {
//    private final MatchRepository matchRepository;
//    private final MatchWaitRepository matchWaitRepository;
//    private final StadiumRepository stadiumRepository;
//    public MatchWaitEntity convert(MatchEntity matchEntity,String team) {
//        MatchWaitEntity matchWaitEntity = new MatchWaitEntity();
//        matchWaitEntity.setMember(matchEntity.getMember());
//        matchWaitEntity.setStadium(matchEntity.getStadium());
//        matchWaitEntity.setParty(matchEntity.getParty());
//        matchWaitEntity.setSearchStart(matchEntity.getSearchStart());
//        matchWaitEntity.setSearchEnd(matchEntity.getSearchEnd());
//        matchWaitEntity.setCrd(matchEntity.getCrd());
//        matchWaitEntity.setPoint(matchEntity.getPoint());
//        matchWaitEntity.setCycle(matchEntity.getCycle());
//        matchWaitEntity.setTeam(team);
//        matchRepository.delete(matchEntity);
//        return matchWaitEntity;
//    }
//    public MatchEntity convertRevers(MatchWaitEntity matchWaitEntity) {
//        MatchEntity matchEntity = new MatchEntity();
//        int cycle = matchWaitEntity.getCycle();
//        PartyEntity party = matchEntity.getParty();
//        matchEntity.setMember(matchWaitEntity.getMember());
//        List<StadiumEntity> stdList = stadiumRepository.findAll();
//        StadiumEntity stadium = NearStadium.findNearestStadiums(stdList,party.getLocationX(),party.getLocationY(),cycle+1);
//        matchEntity.setStadium(stadium);
//        matchEntity.setParty(matchWaitEntity.getParty());
//        matchEntity.setSearchStart(matchWaitEntity.getSearchStart());
//        matchEntity.setSearchEnd(matchWaitEntity.getSearchEnd());
//        matchEntity.setCrd(matchWaitEntity.getCrd());
//        matchEntity.setPoint(matchWaitEntity.getPoint());
//        matchEntity.setCycle(cycle+1);
//        matchRepository.save(matchEntity);
//        matchWaitRepository.delete(matchWaitEntity);
//        return matchEntity;
//    }//매치웨이트 테이블에서 다시 매치테이블로 튕겨 나오는 메소드
//}