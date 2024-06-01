package twisk.exceptions;

/**
 * The TwiskException class is the base class for all custom exceptions in the Twisk application.
 * It extends the Exception class and provides a constructor to set the error message.
 */
public class TwiskException extends Exception {

    /**
     * Constructs a new TwiskException with the specified detail message.
     *
     * @param errorMessage the detail message to be displayed with the exception.
     */
    public TwiskException(String errorMessage) {
        super(errorMessage);
    }
}
