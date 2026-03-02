package errors;

public class IRException extends CompilationException {
    public IRException(String message) {
        super(message, -1);
    }
}
