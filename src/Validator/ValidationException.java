package Validator;

public class ValidationException extends Exception {
    private String message;

    public ValidationException(String message) {
        super(message);
    }
}
