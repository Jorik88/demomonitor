package com.example.alex.service;

import com.example.alex.configuration.MailConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
@Slf4j
public class MailBean {

    private MailConfiguration config;
    private List<ReportService> services;

    @Autowired
    public MailBean(MailConfiguration mailConfiguration, List<ReportService> services) {
        this.config = mailConfiguration;
        this.services = services;
    }

    private Properties getServerProperties() {
        Properties properties = new Properties();
        properties.put(String.format("mail.%s.host", config.getProtocol()), config.getHost());
        properties.put(String.format("mail.%s.port", config.getProtocol()), config.getPort());
        properties.setProperty(String.format("mail.%s.socketFactory.class", config.getProtocol()), "javax.net.ssl.SSLSocketFactory");
        properties.setProperty(String.format("mail.%s.socketFactory.fallback", config.getProtocol()), "false");
        properties.setProperty(String.format("mail.%s.socketFactory.port", config.getProtocol()), String.valueOf(config.getPort()));

        return properties;
    }

    public void getNewEmails() {
        Properties properties = getServerProperties();
        Session session = Session.getDefaultInstance(properties);

        try {
            Store store = session.getStore(config.getProtocol());
            store.connect(config.getUsername(), config.getPassword());

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            int count = inbox.getMessageCount();
            Message[] messages = inbox.getMessages(1, count);
            for (Message message : messages) {
                if (!message.getFlags().contains(Flags.Flag.SEEN)) {
                    Address[] fromAddresses = message.getFrom();
                    System.out.println("...................");
                    System.out.println("\t From: " + fromAddresses[0].toString());
                    System.out.println("\t To: " + parseAddresses(message.getRecipients(Message.RecipientType.TO)));
                    System.out.println("\t CC: " + parseAddresses(message.getRecipients(Message.RecipientType.CC)));
                    System.out.println("\t Subject: " + message.getSubject());
                    System.out.println("\t Sent Date:" + message.getSentDate().toString());

                    try {
                        MimeMultipart content = (MimeMultipart) message.getContent();
                        BodyPart bodyPart = content.getBodyPart(1);
                        for (ReportService service : services) {
                            if (fromAddresses[0].toString().contains(service.getPaymentSystemName())) {
                                service.processReport(bodyPart, message.getSubject());
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println("Error reading content!!");
                        ex.printStackTrace();
                    }
                }

            }

            inbox.close(false);
            store.close();
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + config.getProtocol());
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
        }
    }

    private String parseAddresses(Address[] address) {

        String listOfAddress = "";
        if ((address == null) || (address.length < 1))
            return null;
        if (!(address[0] instanceof InternetAddress))
            return null;

        for (int i = 0; i < address.length; i++) {
            InternetAddress internetAddress =
                    (InternetAddress) address[0];
            listOfAddress += internetAddress.getAddress() + ",";
        }
        return listOfAddress;
    }
}
