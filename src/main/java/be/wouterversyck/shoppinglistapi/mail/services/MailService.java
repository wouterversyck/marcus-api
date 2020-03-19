package be.wouterversyck.shoppinglistapi.mail.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;


@Service
public class MailService {
    private final JavaMailSender mailSender;
    private final ITemplateEngine templateEngine;

    public MailService(final JavaMailSender mailSender, final ITemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendPasswordSetMail(final String username, final String emailAddress, final String token) throws MessagingException {
        final var mimeMessage = mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        message.setTo(emailAddress);
        message.setSubject("Woopsel password set");

        final var context = new Context();
        context.setVariable("token", token);
        context.setVariable("username", username);

        message.setText(templateEngine.process("password-mail.html", context), true);

        mailSender.send(mimeMessage);
    }
}
