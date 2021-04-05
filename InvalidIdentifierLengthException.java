package code.OXOExceptions;

public class InvalidIdentifierLengthException extends InvalidIdentifierException {
    private final String failMessage;
    public InvalidIdentifierLengthException(String message) {
        failMessage = message;
    }

    public String toString(){
        return this.getClass().getName()+": "+failMessage;
    }
}
