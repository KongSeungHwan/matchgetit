package com.matchgetit.backend.service;

import com.matchgetit.backend.dto.MatchRecDTO;
import com.matchgetit.backend.entity.MatchRecEntity;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.repository.MatchRecRepository;
import com.matchgetit.backend.repository.MemberRepository;
import com.matchgetit.backend.util.FormatDate;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MatchRecService {
    private final MemberRepository memberRepository;
    private final MatchRecRepository matchRecRepository;
    private final ModelMapper modelMapper;

    public List<MatchRecDTO> getMatchHistory(String userId,String date){
        MemberEntity member = memberRepository.findByUserId(Long.parseLong(userId));
        List<MatchRecEntity> matchRecList = matchRecRepository.findByMemberAndApplicationDate(member, FormatDate.parseDate(date));
        matchRecList.forEach(m-> System.out.println(m.getMatchRecId()));
        return matchRecList.stream().map(m->modelMapper.map(m,MatchRecDTO.class)).toList();
    }

}
