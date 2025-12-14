package uzum.spring.billsplitter.exception;

public class InvalidCommissionException extends RuntimeException {

    public InvalidCommissionException() {
        super("Commission percent must be between 0 and 100");
    }

    public InvalidCommissionException(String message) {
        super(message);
    }
}