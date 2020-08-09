package be.wouterversyck.marcusapi.mail.config;

import be.wouterversyck.marcusapi.mail.services.StubbedMailSender;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import java.util.Properties;

@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(ApplicationMailProperties.class)
public class MailConfiguration {
    private ApplicationMailProperties mailProperties;

    @Bean
    @Profile("!stubs")
    public JavaMailSender getJavaMailSender() {
        final JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getSmtpHost());
        mailSender.setPort(mailProperties.getSmtpPort());

        mailSender.setUsername(mailProperties.getSmtpUsername());
        mailSender.setPassword(mailProperties.getSmtpPassword());

        final Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", String.valueOf(mailProperties.isSmtpAuth()));
        props.put("mail.smtp.starttls.enable", String.valueOf(mailProperties.isSmtpStartTtls()));

        return mailSender;
    }

    @Bean
    @Profile("stubs")
    public JavaMailSender getStubbedJavaMailSender() {
        return new StubbedMailSender();
    }
}
