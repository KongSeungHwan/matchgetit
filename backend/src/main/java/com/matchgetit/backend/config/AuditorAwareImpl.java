package com.matchgetit.backend.config;

import com.matchgetit.backend.dto.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.WebUtils;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    //이 메소드로 @CreatedBy(작성자) 및 @LastModifiedBy(수정자)를 설정함.
    @Override @NotNull
    public Optional<String> getCurrentAuditor() {
        String userId = "";
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            MemberDTO memberDTO = (MemberDTO) request.getSession().getAttribute("member");
            if (memberDTO != null) {
                userId = memberDTO.getEmail();
            }
        }

//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (auth != null) {
//            userId = auth.getName();
//        }

        if (userId == null || userId.isEmpty()) {
//            userId = "Anonymous";
            userId = "관리자";
        }
        return Optional.of(userId);
//        return Optional.of("테스터");
    }

}
