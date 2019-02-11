package com.example.alex.service;

import javax.mail.BodyPart;

public interface ReportService {

    void processReport(BodyPart bodyPart, String subject);

    String getPaymentSystemName();
}
