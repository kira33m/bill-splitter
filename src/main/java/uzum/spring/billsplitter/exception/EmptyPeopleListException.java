package uzum.spring.billsplitter.exception;

public class EmptyPeopleListException extends RuntimeException {

    public EmptyPeopleListException() {
        super("List of people must not be empty");
    }

    public EmptyPeopleListException(String message) {
        super(message);
    }
}