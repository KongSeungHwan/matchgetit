package com.matchgetit.backend.service;

import com.matchgetit.backend.constant.AccountType;
import com.matchgetit.backend.entity.MemberEntity;
import com.matchgetit.backend.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@AllArgsConstructor
public class FindService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberEntity findId(String name, String pn) {
        MemberEntity member = memberRepository.findEmailByPhone(name,pn);
        if (member == null){
            throw new RuntimeException("계정이 존재하지 않습니다.");
        }
        return member;
    }

    @Transactional
    public String issuedTempPw(String email, String password){
        MemberEntity member = memberRepository.findByEmail(email);
        if (!member.getAccountType().equals(AccountType.NORMAL))
            return "실패: 소셜로그인 계정입니다";
        if (member != null){
            String pw = passwordEncoder.encode(password);
            member.setPw(pw);
            memberRepository.save(member);
            return "성공";
        }
        return "실패: 없는 계정입니다.";
    }

    public String temporaryPw() {
        char[] charSet = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] charSet2 = new char[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        char[] charSet3 = new char[] {'!', '@', '#', '$', '%', '^', '&', '*'};

        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();

        // Select a random character from each character set
        char randomChar1 = charSet[random.nextInt(charSet.length)];
        char randomChar2 = charSet2[random.nextInt(charSet2.length)];
        char randomChar3 = charSet3[random.nextInt(charSet3.length)];

        // Add the randomly selected characters to the string builder
        stringBuilder.append(randomChar1);
        stringBuilder.append(randomChar2);
        stringBuilder.append(randomChar3);

        // Generate the remaining characters randomly
        for (int i = 0; i < 7; i++) {
            int randomCharSetIndex = random.nextInt(3);
            char[] selectedCharSet;

            // Select a random character set
            switch (randomCharSetIndex) {
                case 0:
                    selectedCharSet = charSet;
                    break;
                case 1:
                    selectedCharSet = charSet2;
                    break;
                case 2:
                    selectedCharSet = charSet3;
                    break;
                default:
                    selectedCharSet = charSet;
                    break;
            }

            // Select a random character from the selected character set
            char randomChar = selectedCharSet[random.nextInt(selectedCharSet.length)];
            stringBuilder.append(randomChar);
        }

        // Shuffle the generated string randomly
        char[] randomStringChars = stringBuilder.toString().toCharArray();
        for (int i = randomStringChars.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = randomStringChars[i];
            randomStringChars[i] = randomStringChars[j];
            randomStringChars[j] = temp;
        }

        // Final generated random string
        String randomString = new String(randomStringChars);
        System.out.println("Random String: " + randomString);
        return randomString;
    }

}
