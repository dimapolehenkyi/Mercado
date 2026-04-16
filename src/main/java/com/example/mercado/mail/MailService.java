package com.example.mercado.mail;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendConfirmationMail(String email, String code, String token) {

        String link = "http://localhost:8085/api/auth/confirm?token=" + token;

        SimpleMailMessage message = getSimpleMailMessage(email, code, link);

        try {
            mailSender.send(message);
        } catch (Exception ex) {
            throw new AppException(ErrorCode.MAIL_SEND_FAILED);
        }
    }

    @NonNull
    private SimpleMailMessage getSimpleMailMessage(String email, String code, String link) {
        String text = """
                Здравствуйте!
                
                Вы запросили подтверждение входа в аккаунт.
                
                Ваш код подтверждения:
                
                %s
                
                Или перейдите по ссылке:
                %s
                
                Код действителен 10 минут.
                
                Если вы не запрашивали подтверждение — просто проигнорируйте это письмо.
                
                С уважением,
                Команда Mercado
                """.formatted(code, link);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(System.getenv("MAIL_USERNAME"));
        message.setTo(email);
        message.setSubject("Подтверждение регистрации на TaskManager");
        message.setText(text);
        return message;
    }

}
