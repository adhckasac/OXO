package code.OXOExceptions;
public class OutsideCellRangeException extends CellDoesNotExistException {
    private final String failMessage;
    public OutsideCellRangeException(String message) {
        failMessage = message;
    }

    public String toString(){
        return this.getClass().getName()+": "+failMessage;
    }
}