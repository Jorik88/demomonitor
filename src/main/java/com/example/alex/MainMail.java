package com.example.alex;

import com.example.alex.service.MailBean;

public class MainMail {

    public static void main(String[] args) {
        MailBean bean = new MailBean();
        bean.getNewEmails("imap", "imap.gmail.com", "993",
                "your-email", "password");
    }
}
