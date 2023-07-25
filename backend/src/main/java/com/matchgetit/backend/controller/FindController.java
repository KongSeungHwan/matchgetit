package com.matchgetit.backend.controller;
import com.matchgetit.backend.config.SocialEnv;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.request.FindRequest;
import com.matchgetit.backend.service.FindService;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Properties;

@RestController
@RequestMapping("/matchGetIt/find")
@AllArgsConstructor
public class FindController {
    private FindService findService;

    private final SocialEnv socialEnv;

    @PostMapping("/Id")
    public ResponseEntity<String> FindId(@RequestBody FindRequest findRequest, HttpServletRequest request) {
        try {
            MemberEntity memberEntity = findService.findId(
                    findRequest.getName(),
                    findRequest.getPn()
            );
            return new ResponseEntity<>(memberEntity.getEmail(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody FindRequest findRequest) throws IOException {
        String content;
        String tempPassword = findService.temporaryPw();
        content=findService.issuedTempPw(findRequest.getEmail(), tempPassword);
        if(content.equals("성공")){
            String recipientEmail = findRequest.getTemporaryPw();
            String subject = "Match Get It 임시비밀번호 입니다.";
            String mailContent = "고객님의 임시비밀번호는" + tempPassword + "입니다. 프로필 설정란에서 비밀번호를 재설정 해주세요!" ;
            content = "임시비밀번호를 메일로 전송했습니다.";
            sendEmail(recipientEmail, subject, mailContent);
        }
        return content;
    }

    public void sendEmail(String recipientEmail, String subject, String content) {
        // 이메일 세션 생성
        Session session = null;
        try {
            // SMTP 서버 설정
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.naver.com");
            properties.put("mail.smtp.port", "465");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.ssl.enable", "true"); // SSL 보안 연결 설정

            session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(socialEnv.getSmtpId(), socialEnv.getSmtpPw());
                }
            });

            // 이메일 메시지 생성
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(socialEnv.getSmtpId()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(content);

            // 이메일 발송
            Transport.send(message);

            System.out.println("이메일이 성공적으로 발송되었습니다.");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("이메일 발송 중 오류가 발생했습니다.");
        } finally {
            if (session != null) {
                try {
                    session.getTransport().close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}