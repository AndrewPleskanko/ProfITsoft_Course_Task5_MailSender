package com.profitsoft.mailsender.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import com.profitsoft.mailsender.MailSenderApplication;
import com.profitsoft.mailsender.entity.EmailMessage;
import com.profitsoft.mailsender.enums.MessageStatus;
import com.profitsoft.mailsender.repository.EmailMessageRepository;

@SpringBootTest
@ContextConfiguration(classes = {MailSenderApplication.class, BaseServiceTest.class})
class EmailServiceIntegrationTest {

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private EmailServiceImpl emailService;

    @BeforeEach
    public void beforeEach() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(EmailMessage.class);
        if (indexOps.exists()) {
            indexOps.delete();
        }
        indexOps.create();
        indexOps.createMapping();
    }

    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).delete();
    }

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testSendEmailSuccess() {
        // given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setRecipientEmail("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        emailService.sendEmail(emailMessage);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
        List<EmailMessage> savedMessages = emailMessageRepository.findByRecipientEmail("test@example.com");
        assertEquals(MessageStatus.SENT, savedMessages.get(0).getStatus());
        Assertions.assertFalse(savedMessages.isEmpty(), "No EmailMessage found with the given recipient email");
    }

    @Test
    public void testSendEmailFailure() {
        // given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setRecipientEmail("invalid_email");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        doThrow(new MailSendException("error")).when(mailSender).send(any(SimpleMailMessage.class));

        // when
        emailService.sendEmail(emailMessage);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
        assertEquals(MessageStatus.ERROR, emailMessage.getStatus());
    }
}