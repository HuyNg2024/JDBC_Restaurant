package exceptions;

/**
 * Exception thrown when database operations fail.
 * Wraps SQL exceptions with user-friendly Vietnamese messages.
 */
public class DatabaseException extends AppException {

    public DatabaseException(String message) {
        super("DB_ERROR", message);
    }

    public DatabaseException(String message, Throwable cause) {
        super("DB_ERROR", message, cause);
    }
}
