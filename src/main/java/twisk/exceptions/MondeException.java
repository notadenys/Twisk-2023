package twisk.exceptions;

/**
 * The MondeException class is a custom exception used to handle errors related to the Monde (world) in the Twisk application.
 * It extends the TwiskException class.
 */
public class MondeException extends TwiskException {

    /**
     * Constructs a new MondeException with the specified detail message.
     *
     * @param errorMessage the detail message to be displayed with the exception.
     */
    public MondeException(String errorMessage) {
        super(errorMessage);
    }
}
