package twisk.exceptions;

/**
 * The TempsException class is a custom exception used to handle errors related to time (temps) in the Twisk application.
 * It extends the TwiskException class.
 */
public class TempsException extends TwiskException {

    /**
     * Constructs a new TempsException with the specified detail message.
     *
     * @param errorMessage the detail message to be displayed with the exception.
     */
    public TempsException(String errorMessage) {
        super(errorMessage);
    }
}
