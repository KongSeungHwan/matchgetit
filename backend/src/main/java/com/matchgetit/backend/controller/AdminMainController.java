package com.matchgetit.backend.controller;

import com.matchgetit.backend.constant.LogInType;
import com.matchgetit.backend.dto.MemberDTO;
import com.matchgetit.backend.service.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.WebContext;

import java.net.URI;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AdminMainController {
    private final AdminDashboardService dashboardService;
    private final AdminPageUserService userService;
    private final PaymentHistoryService paymentService;
    private final StadiumService stadiumService;

    @PostConstruct
    public void createUsers() {
//        dashboardService.createDashboradDataEntity();
//        userService.createUsers();
//        dashboardService.createManagers();
//        paymentService.createPayments();
//        stadiumService.insertStadium();
//        dashboardService.createManagerApplicants();
//        dashboardService.createMatches();
    }

    @GetMapping("/matchGetIt/admin/gate")
    public String gate(HttpSession session, Model model, HttpServletRequest request) {
        System.out.println(session.getAttribute("member"));
        System.out.println(session.getAttribute("jwtToken"));
        MemberDTO member = (MemberDTO) session.getAttribute("member");

        if (member == null || member.getLoginType() != LogInType.ADMIN) {
            model.addAttribute("msg", "잘못된 접근입니다.");
            model.addAttribute("url", "http://localhost:3000/");
            return "admin/components/Utils/alert";
        }

//        return "forword:/matchGetIt/admin/login";
        return "redirect:/matchGetIt/admin";
    }


    @GetMapping(value = {"/matchGetIt/admin"})
    public String mainPage(Model model) {
        Map<String, Long> userCounts = dashboardService.getUserCounts();
        model.addAttribute("userCounts", userCounts);

        Map<String, Long> managerCounts = dashboardService.getManagerCounts();
        model.addAttribute("managerCounts", managerCounts);

        Map<String, Long> matchCounts = dashboardService.getMatchCounts();
        model.addAttribute("matchCounts", matchCounts);

        Map<String, Long> inquiryCounts = dashboardService.getInquiryCounts();
        model.addAttribute("inquiryCounts", inquiryCounts);


        List<Long> userChartData = dashboardService.getUserChartData();
        model.addAttribute("userChartData", userChartData);

        List<LocalDate> dateList = dashboardService.getDateList();
//        List<String> dateListStr = new ArrayList<>();
        List<String> monthList = new ArrayList<>();
        List<String> dateOfMonthList = new ArrayList<>();

        for (LocalDate date: dateList) {
//            dateListStr.add(date.minusDays(1).format(DateTimeFormatter.ofPattern("MM/dd")));
            monthList.add(date.minusDays(1).format(DateTimeFormatter.ofPattern("MM")));
            dateOfMonthList.add(date.minusDays(1).format(DateTimeFormatter.ofPattern("dd")));
        }

        model.addAttribute("monthList", monthList);
        model.addAttribute("dateOfMonthList", dateOfMonthList);

        return "admin/pages/Dashboard/Dashboard";
    }


    @GetMapping("/matchGetIt/admin/logout")
    public String adminLogout() {
        RequestEntity<String> request = RequestEntity.post("http://localhost:8081/matchGetIt/auth/logout").body("");
        ResponseEntity<String> response = new RestTemplate().exchange(request, String.class);
        System.out.println(">>>>>>>>>"+response.getBody());
        return "redirect:http://localhost:3000";
    }


    @PostMapping("/matchGetIt/admin/refund/{userId}")
    @ResponseBody
    public ResponseEntity<String> refund(@PathVariable Long userId, @RequestParam int refundPrice) {
        paymentService.refund(userId, refundPrice);
        return ResponseEntity.ok(null);
    }

}
