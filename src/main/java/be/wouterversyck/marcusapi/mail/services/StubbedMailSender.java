package be.wouterversyck.marcusapi.mail.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;

@Slf4j
public class StubbedMailSender implements JavaMailSender {
    @Override
    public MimeMessage createMimeMessage() {
        return new MimeMessage((Session) null);
    }

    @Override
    public MimeMessage createMimeMessage(final InputStream inputStream) throws MailException {
        return new MimeMessage((Session) null);
    }

    @Override
    public void send(final MimeMessage mimeMessage) throws MailException {
        send();
    }

    @Override
    public void send(final MimeMessage... mimeMessages) throws MailException {
        send();
    }

    @Override
    public void send(final MimeMessagePreparator mimeMessagePreparator) throws MailException {
        send();
    }

    @Override
    public void send(final MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        send();
    }

    @Override
    public void send(final SimpleMailMessage simpleMailMessage) throws MailException {
        send();
    }

    @Override
    public void send(final SimpleMailMessage... simpleMailMessages) throws MailException {
        send();
    }

    private void send() {
        log.info("[STUBS ON] Mail send called in stubs mode [STUBS ON]");
    }
}
