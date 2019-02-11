package com.example.alex;

import com.example.alex.service.MailBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemomonitorApplicationTests {

    @Autowired
    private MailBean mailBean;

    @Test
    public void contextLoads() {

        mailBean.getNewEmails();
    }

}

