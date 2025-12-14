package uzum.spring.billsplitter.exception;

public class NegativePriceException extends RuntimeException {

    public NegativePriceException() {
        super("Item price cannot be negative");
    }

    public NegativePriceException(String message) {
        super(message);
    }
}