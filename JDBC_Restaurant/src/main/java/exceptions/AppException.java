package exceptions;

/**
 * Base exception class for the Restaurant Management System.
 * All custom exceptions extend this class.
 */
public class AppException extends Exception {

    private final String errorCode;

    public AppException(String message) {
        super(message);
        this.errorCode = "APP_ERROR";
    }

    public AppException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "APP_ERROR";
    }

    public AppException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
