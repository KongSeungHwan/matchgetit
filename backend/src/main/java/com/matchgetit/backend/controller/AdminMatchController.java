package com.matchgetit.backend.controller;

import com.matchgetit.backend.dto.MatchRecDTO;
import com.matchgetit.backend.entity.MatchRecEntity;
import com.matchgetit.backend.service.AdminPageMatchService;
import com.matchgetit.backend.service.MatchRecService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/matchGetIt/admin")
@RequiredArgsConstructor
public class AdminMatchController {
    private final AdminPageMatchService matchService;

    private final String path = "admin/pages/Match/";
//    private final String alertViewPath = "admin/components/Utils/alert";

    @GetMapping({"/matchList", "/matchList/{page}"})
    public String matchList(Model model) {
        List<MatchRecEntity> matchHistories = matchService.getMatchList();
        model.addAttribute("matchHistories", matchHistories);
        return path + "MatchList";
    }

    @GetMapping("matchInfo")
    public String matchInfo() {
        return path + "MatchInfo";
    }

}
