package be.wouterversyck.shoppinglistapi.mail.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    private static final String EMAIL = "EMAIL";
    private static final String TOKEN = "TOKEN";

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private MailService mailService;

    @Test
    void shouldSendMail_WhenPasswordSetMailIsRequested() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(
                new MimeMessage((Session)null));
        mailService.sendPasswordSetMail(EMAIL, TOKEN);

        verify(mailSender).send(any(MimeMessage.class));
    }
}