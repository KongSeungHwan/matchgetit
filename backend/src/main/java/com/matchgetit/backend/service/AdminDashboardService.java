package com.matchgetit.backend.service;

import com.matchgetit.backend.constant.*;
import com.matchgetit.backend.entity.*;
import com.matchgetit.backend.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminDashboardService {
    private final DashboardDataRepository dashboardRepository;
    private final MemberRepository userRepository;
    private final ManagerRepository managerRepository;
    private final MatchWaitRepository matchWaitRepository;
    private final MatchRecRepository matchRecRepository;
    private final InquiryRepository inquiryRepository;
    private final StadiumRepository stadiumRepository;
    private final ManagerSupportRecordRepository managerApplicantRepo;

    public void createManagers() {
        for (int i=21; i<=30; i++) {
            MemberEntity member = new MemberEntity();
            ManagerEntity manager = new ManagerEntity();
            member.setName("테스터"+i);
            member.setEmail("tester"+i+"@test.com");
            member.setPn("010-9876-43"+i);
            member.setPw("1234");
            member.setGender((i%2==0) ? Gender.FEMALE : Gender.MALE);
            member.setBDay(Date.valueOf("1998-06-"+i));
            member.setRating(100L);
//            member.setRegDate(Date.valueOf("2023-05-30"));
            member.setRegDate(new java.util.Date());
            member.setLastConnectionDate(Date.valueOf(LocalDate.now()));
            member.setAccountState(AccountState.ACTIVE);
            member.setLoginType(LogInType.MANAGER);
            manager.setUser(member);
            manager.setRegistrationDate(LocalDateTime.now());
            manager.setManagerImage("https://");
            manager.setEmploymentStatus(EmploymentStatus.active);

            switch (i) {
                case 24, 27 -> manager.setEmploymentStatus(EmploymentStatus.leave);
                case 28, 30 -> manager.setTeam("A");
            }

            userRepository.save(member);
            managerRepository.save(manager);
        }
    }

    public void createManagerApplicants() {
        for (int i=31; i<=40; i++) {
            MemberEntity member = new MemberEntity();
            ManagerSupportRecordEntity managerApplicant = new ManagerSupportRecordEntity();
            member.setName("테스터"+i);
            member.setEmail("tester"+i+"@test.com");
            member.setPn("010-9876-43"+i);
            member.setPw("1234");
            member.setGender((i%2==0) ? Gender.FEMALE : Gender.MALE);
            member.setBDay(Date.valueOf("1998-10-"+(i-20)));
            member.setRating(100L);
            member.setRegDate(Date.valueOf("2023-07-01"));
            member.setLastConnectionDate(Date.valueOf(LocalDate.now()));
            member.setAccountState(AccountState.ACTIVE);
            member.setLoginType(LogInType.NORMAL);
            member.setManagerSupportStatus(ManagerSupportStatus.WAITING);
            managerApplicant.setManagerUser(member);
            managerApplicant.setActivityZone("경기");
            managerApplicant.setSubmissionDate(new java.util.Date());

            userRepository.save(member);
            managerApplicantRepo.save(managerApplicant);
        }
    }

    public void createMatches() {
        for (int i=0; i<10; i++) {
            MatchRecEntity game = new MatchRecEntity();
            game.setApplicationDate(new java.util.Date());
            game.setApplicationTime("A");
            game.setMatchState(MatchState.COMPLETE);

            MemberEntity manager = userRepository.findById(22L).orElseThrow(EntityNotFoundException::new);
            game.setManager(manager);
            StadiumEntity stadium = stadiumRepository.findById(1).orElseThrow(EntityNotFoundException::new);
            game.setStadium(stadium);

            matchRecRepository.save(game);
        }
    }

    public void createDashboradDataEntity() {
        DashboardDataEntity dashboardData = new DashboardDataEntity();
        dashboardData.setCanceledMembership(10);
        dashboardRepository.save(dashboardData);
    }


    @Transactional(readOnly = true)
    public Map<String, Long> getUserCounts() {
        Map<String, Long> userCounts = new HashMap<>();
        userCounts.put("allUsers", userRepository.count());

        java.util.Date from = Date.valueOf(LocalDate.now());
        java.util.Date to = Date.valueOf(LocalDate.now().plusDays(1));
        userCounts.put("signUpToday", userRepository.countByRegDateBetween(from, to));

//        long withdrawal = userRepository.countByRegDateBefore(Date.valueOf(LocalDate.now().minusDays(1)));
//        System.out.println(withdrawal);

        userCounts.put("canceledMembership", dashboardRepository.findCanceledMembership());
        return userCounts;
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getManagerCounts() {
        Map<String, Long> managerCounts = new HashMap<>();
        managerCounts.put("allManagers", managerRepository.count());
        managerCounts.put("assigned", managerRepository.countByTeamIsNotNull());
        managerCounts.put("notAssigned", managerRepository.countByTeamIsNull());
        managerCounts.put("leftManagers", managerRepository.countByEmploymentStatusLike(EmploymentStatus.leave));
        managerCounts.put("bannedManagers", managerRepository.countByEmploymentStatusLike(EmploymentStatus.leave));
        return managerCounts;
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getMatchCounts() {
        Map<String, Long> matchCounts = new HashMap<>();
        matchCounts.put("finished", matchRecRepository.countByMatchStateIs(MatchState.COMPLETE));
        matchCounts.put("canceled", matchRecRepository.countByMatchStateIs(MatchState.CANCEL));
        matchCounts.put("proceeding", matchWaitRepository.countProceedingMatch());
        matchCounts.put("allMatchWait", matchWaitRepository.count());

//        matchCounts.put("canceled", Long.valueOf(getDashboardEntity().getCanceledMatch()));
//        long sum = 0;
//        for (long l: matchCounts.values()) sum += l;
//        matchCounts.put("reservedToday", sum);

        return matchCounts;
    }

    public Map<String, Long> getInquiryCounts() {
        Map<String, Long> inquiryCounts = new HashMap<>();

        LocalDateTime from = LocalDate.now().atStartOfDay();
        LocalDateTime to = LocalDate.now().plusDays(1).atStartOfDay();
        inquiryCounts.put("registeredToday", inquiryRepository.countByRegTimeBetween(from, to));

        inquiryCounts.put("waiting", inquiryRepository.countByStateContains("접수 대기"));
        inquiryCounts.put("inProgress", inquiryRepository.countByStateContains("처리 중"));
        return inquiryCounts;
    }

}
