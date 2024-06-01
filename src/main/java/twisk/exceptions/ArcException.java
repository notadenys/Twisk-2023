package twisk.exceptions;

/**
 * The ArcException class is a custom exception used to handle errors related to arcs in the Twisk application.
 * It extends the TwiskException class.
 */
public class ArcException extends TwiskException {

    /**
     * Constructs a new ArcException with the specified detail message.
     *
     * @param errorMessage the detail message to be displayed with the exception.
     */
    public ArcException(String errorMessage) {
        super(errorMessage);
    }
}
