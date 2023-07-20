package com.matchgetit.backend.service;

import com.matchgetit.backend.entity.MatchRecEntity;
import com.matchgetit.backend.repository.MatchRecRepository;
import com.matchgetit.backend.repository.MatchRepository;
import com.matchgetit.backend.repository.MatchWaitRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminPageMatchService {
    private final MatchRepository matchRepository;
    private final MatchWaitRepository matchWaitRepository;
    private final MatchRecRepository matchRecRepository;
    private final ModelMapper modelMapper;


    public List<MatchRecEntity> getMatchList() {
        return matchRecRepository.findAll();
    }

}
