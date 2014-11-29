package sk.magiksoft.sodalis.core.exception;

/**
 * @author wladimiiir
 */
public class VetoException extends RuntimeException {

    public VetoException(Throwable cause) {
        super(cause);
    }

    public VetoException(String message, Throwable cause) {
        super(message, cause);
    }

    public VetoException(String message) {
        super(message);
    }

    public VetoException() {
    }


}
