package com.ssafy.algonote.member.service;

import com.ssafy.algonote.exception.CustomException;
import com.ssafy.algonote.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
// 참고 자료 : https://green-bin.tistory.com/83
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MailService {
    private final JavaMailSender emailSender;

    public void sendEmail(String toEmail, String title, String text){
        SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);

        try{
            emailSender.send(emailForm);
        } catch (RuntimeException e){
            e.printStackTrace();
            log.info("MailService.sendEmail exception occur to Email : {}", toEmail);
            throw new CustomException(ErrorCode.UNABLE_TO_SEND_EMAIL);
        }
    }

    private SimpleMailMessage createEmailForm(String toEmail, String title, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText("Algonote 인증번호입니다: "+text);

        return message;
    }
}
