package interpreter;

public class ReturnException extends RuntimeException {
    public final Object value;
    public ReturnException(Object value) {
        super(null, null, true, false);
        this.value = value;
    }
}
