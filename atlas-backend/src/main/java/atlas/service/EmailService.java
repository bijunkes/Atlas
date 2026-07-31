package atlas.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String remetente;

    @Async
    public void enviarEmail(
            String destinatario,
            String assunto,
            String mensagem) {

        try {

            MimeMessage email = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(email, true);

            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject(assunto);
            helper.setText(mensagem);

            mailSender.send(email);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao enviar email", e);
        }

    }

}