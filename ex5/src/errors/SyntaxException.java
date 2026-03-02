package errors;

public class SyntaxException extends CompilationException {

    public SyntaxException(String message, int lineNumber) {
        super(message, lineNumber);
    }

}
