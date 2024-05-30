package twisk.exceptions;

public class TwiskException extends Exception {
    private String message;
    public TwiskException(String errorMessage){
        super(errorMessage);
        message = errorMessage;
    }

    public String getMessage() {
        return message;
    }
}
