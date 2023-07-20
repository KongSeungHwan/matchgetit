package com.matchgetit.backend.controller;

import com.matchgetit.backend.dto.AdminMatchInfoDTO;
import com.matchgetit.backend.dto.AdminMatchListDTO;
import com.matchgetit.backend.dto.AdminPageUserDTO;
import com.matchgetit.backend.dto.AdminSearchMatchDTO;
import com.matchgetit.backend.service.AdminPageMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/matchGetIt/admin")
@RequiredArgsConstructor
public class AdminMatchController {
    private final AdminPageMatchService matchService;

    private final String path = "admin/pages/Match/";
//    private final String alertViewPath = "admin/components/Utils/alert";

    @GetMapping({"/matchList", "/matchList/{page}"})
    public String matchList(Model model, @PathVariable("page") Optional<Integer> page, AdminSearchMatchDTO searchMatchDTO) {
        Integer temp = searchMatchDTO.getPageSize();
        int pageSize = temp == null ? 5 : temp;

        Pageable pageable = PageRequest.of(page.orElse(0), pageSize);
        Page<AdminMatchListDTO> matchHistories = matchService.getMatchList(searchMatchDTO, pageable);

        model.addAttribute("matchHistories", matchHistories);
        model.addAttribute("currPageNum", pageable.getPageNumber());
        model.addAttribute("searchMatchDTO", searchMatchDTO);
        return path + "MatchList";
    }

    @GetMapping("matchInfo")
    public String matchInfo(Model model, @RequestParam String matchDate, @RequestParam String matchTime, @RequestParam Long stadiumId) {
        AdminMatchInfoDTO matchInfo = matchService.getMatchInfo(matchDate, matchTime, stadiumId);
        model.addAttribute("game", matchInfo);
        return path + "MatchInfo";
    }

}
