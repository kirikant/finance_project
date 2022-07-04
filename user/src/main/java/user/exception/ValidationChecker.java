package user.exception;

import org.springframework.stereotype.Component;
import user.dto.UserDto;

@Component
public class ValidationChecker {

    public void checkUserDtoFields(UserDto userDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (userDto.getLogin() == null) {
            validationException
                    .add(new ValidationError("login", "the login was not given"));
        }
        if (userDto.getPassword() == null) {
            validationException
                    .add(new ValidationError("password", "the password was not given"));
        }
        if (userDto.getEmail() == null) {
            validationException
                    .add(new ValidationError("email", "the email was not given"));
        }
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }

    public void checkPageParams(Integer page, Integer size) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (page == null) validationException
                .add(new ValidationError("page", "the page parameter was not given"));
        if (page < 0) validationException
                .add(new ValidationError("page", "the page parameter is incorrect"));
        if (size == null) validationException
                .add(new ValidationError("size", "the size parameter was not given"));
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }

    public void checkLoginFields (UserDto userDto) throws ValidationException {
        ValidationException validationException = new ValidationException();
        if (userDto.getLogin() == null) {
            validationException
                    .add(new ValidationError("login", "the login was not given"));
        }
        if (userDto.getPassword() == null) {
            validationException
                    .add(new ValidationError("password", "the password was not given"));
        }
        if (validationException.getValidationExceptions().size() > 0) throw validationException;
    }
}

