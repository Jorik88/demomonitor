package com.example.alex.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.mail.BodyPart;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Slf4j
public class LiqPayReportService implements ReportService {

    @Override
    public void processReport(BodyPart bodyPart, String subject) {
        try {
            ZipInputStream zis = new ZipInputStream(bodyPart.getInputStream());
            ZipEntry zipEntry = zis.getNextEntry();
            byte[] buffer = new byte[1024];
            while (zipEntry != null) {
                FileOutputStream fos = new FileOutputStream(String.format("./%s.csv", subject));
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
        } catch (Exception e) {

        }
    }

    @Override
    public String getPaymentSystemName() {
        return "LiqPay";
    }
}
