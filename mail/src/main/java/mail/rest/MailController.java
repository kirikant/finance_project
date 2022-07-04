package mail.rest;

import mail.dto.MessageDto;
import mail.service.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    public MailController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/message")
    public ResponseEntity<?> sendMail(@RequestBody MessageDto messageDto) throws MessagingException {
        mailService.sendMessage(messageDto);
        return ResponseEntity.ok().build();
    }
}
