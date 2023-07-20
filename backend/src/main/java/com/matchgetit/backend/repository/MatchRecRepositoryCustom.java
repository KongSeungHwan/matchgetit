package com.matchgetit.backend.repository;

import com.matchgetit.backend.dto.AdminMatchRecDTO;
import com.matchgetit.backend.dto.AdminMatchListDTO;
import com.matchgetit.backend.dto.AdminSearchMatchDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MatchRecRepositoryCustom {
    List<AdminMatchListDTO> getMatchListBy(AdminSearchMatchDTO searchMatchDTO, Pageable pageable);
//    List<AdminMatchListDTO> getPagedMatchListBy(AdminSearchMatchDTO searchMatchDTO);
    Long getMatchCountBy(AdminSearchMatchDTO searchMatchDTO);
    List<AdminMatchRecDTO> getMatchInfoBy(String matchDate, String matchTime, Long stadiumId);
}
