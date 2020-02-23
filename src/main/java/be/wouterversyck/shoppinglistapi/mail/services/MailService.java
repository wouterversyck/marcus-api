package be.wouterversyck.shoppinglistapi.mail.services;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(final JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordSetMail(final String emailAddress) throws MessagingException {
        final var mime = mailSender.createMimeMessage();
        mime.addRecipients(Message.RecipientType.TO, emailAddress);
        mime.addHeader("Subject", "Account activation");
        mime.setText("content");

        mailSender.send(mime);
    }
}
