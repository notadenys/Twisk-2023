package twisk.exceptions;

/**
 * The JetonsException class is a custom exception used to handle errors related to jetons (tokens) in the Twisk application.
 * It extends the TwiskException class.
 */
public class JetonsException extends TwiskException {

    /**
     * Constructs a new JetonsException with the specified detail message.
     *
     * @param errorMessage the detail message to be displayed with the exception.
     */
    public JetonsException(String errorMessage) {
        super(errorMessage);
    }
}
