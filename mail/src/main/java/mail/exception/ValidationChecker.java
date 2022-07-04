package mail.exception;

import mail.dto.MessageDto;
import org.springframework.stereotype.Component;

@Component
public class ValidationChecker {

    public void checkMessageDtoFields(MessageDto messageDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (messageDto.getReceiver() == null) {
            validationException
                    .add(new ValidationError("receiver", "the receiver was not given"));}
    }

}

