package be.wouterversyck.shoppinglistapi.mail.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;

import static java.lang.String.format;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordSetMail(final String emailAddress, final String token) throws MessagingException {
        final var mime = mailSender.createMimeMessage();
        mime.addRecipients(Message.RecipientType.TO, emailAddress);
        mime.addHeader("Subject", createMessage(token));
        mime.setText("content");

        mailSender.send(mime);
    }

    private String createMessage(final String token) {
        return format("Account activation click link https://woopsel.be/#/activate?token=%s", token);
    }
}
