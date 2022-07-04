package mail.service;

import mail.dto.MessageDto;
import mail.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String username;
    private final JavaMailSenderImpl javaMailSender;
    private Logger logger = LoggerFactory.getLogger(MailService.class);

    public MailService(JavaMailSenderImpl javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(MessageDto messageDto) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setSubject("report");
        helper.setText(messageDto.getText());
        helper.setFrom(username);
        helper.setTo(messageDto.getReceiver());
        File file1= null;
        if (messageDto.getByteFile()!=null){
            file1 = messageDto.getFileName()!=null ? new File(messageDto.getFileName().toString() + ".xls") :
                    new File(new File(String.valueOf(UUID.randomUUID())) + ".xls");
            try(BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file1))) {
                bufferedOutputStream.write(messageDto.getByteFile());
                bufferedOutputStream.flush();
                FileSystemResource file
                        = new FileSystemResource(file1);
                helper.addAttachment("Report("+messageDto.getText()+").xls", file);
                javaMailSender.send(message);
            } catch (IOException e) {
                logger.error(e.getCause().toString());
                e.printStackTrace();
            }
        }
        file1.delete();

    }


}
