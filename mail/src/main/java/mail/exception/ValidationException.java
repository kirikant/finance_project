package mail.exception;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends Exception {

    private List<ValidationError> validationExceptions = new ArrayList<>();

    public ValidationException(List<ValidationError> validationExceptions) {
        this.validationExceptions = validationExceptions;
    }

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public  void add(ValidationError validationException){
        validationExceptions.add(validationException);
    }

    public List<ValidationError> getValidationExceptions() {
        return validationExceptions;
    }
}
