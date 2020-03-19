package be.wouterversyck.shoppinglistapi.mail.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MailServiceTest {

    private static final String EMAIL = "EMAIL";
    private static final String USERNAME = "USERNAME";
    private static final String TEMPLATE = "password-mail.html";
    private static final String CONTENT = "CONTENT";
    private static final String TOKEN = "TOKEN";

    @Mock
    private JavaMailSender mailSender;
    @Mock
    private ITemplateEngine templateEngine;

    private MailService mailService;

    @BeforeEach
    public void setup() {
        this.mailService = new MailService(mailSender, templateEngine);
    }

    @Test
    void shouldSendMail_WhenPasswordSetMailIsRequested() throws MessagingException {
        when(mailSender.createMimeMessage()).thenReturn(
                new MimeMessage((Session)null));
        Context context = new Context();
        context.setVariable("token", TOKEN);
        context.setVariable("user", USERNAME);
        when(templateEngine.process(eq(TEMPLATE), any(Context.class))).thenReturn(CONTENT);

        mailService.sendPasswordSetMail(USERNAME, EMAIL, TOKEN);
        verify(mailSender).send(any(MimeMessage.class));
    }
}