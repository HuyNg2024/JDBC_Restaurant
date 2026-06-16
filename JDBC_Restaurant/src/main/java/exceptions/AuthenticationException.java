package exceptions;

/**
 * Exception thrown when authentication fails.
 * Used for login failures, invalid credentials, etc.
 */
public class AuthenticationException extends AppException {

    public AuthenticationException(String message) {
        super("AUTH_ERROR", message);
    }
}
