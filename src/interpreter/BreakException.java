package interpreter;

public class BreakException extends RuntimeException {
    public BreakException() {
        super(null, null, true, false);
    }
}
