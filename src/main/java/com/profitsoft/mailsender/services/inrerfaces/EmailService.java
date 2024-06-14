package com.profitsoft.mailsender.services.inrerfaces;

import com.profitsoft.mailsender.entity.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);
}

