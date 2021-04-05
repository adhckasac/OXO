package code.OXOExceptions;
public class CellAlreadyTakenException extends OXOMoveException{
    private final String failMessage;
    public CellAlreadyTakenException(String message) {
        failMessage = message;
    }
    public String toString(){
        return this.getClass().getName()+": "+failMessage;
    }
}
