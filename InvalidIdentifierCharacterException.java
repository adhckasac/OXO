package code.OXOExceptions;

public class InvalidIdentifierCharacterException extends InvalidIdentifierException {
    private final String failMessage;
    public InvalidIdentifierCharacterException(String message) {
        failMessage = message;
    }

    public String toString(){
        return this.getClass().getName()+": "+failMessage;
    }
}
