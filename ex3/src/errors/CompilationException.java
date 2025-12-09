package errors;

public class CompilationException extends RuntimeException {

    private final int lineNumber;

    public CompilationException(String message, int lineNumber) {
        super("At line number " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
