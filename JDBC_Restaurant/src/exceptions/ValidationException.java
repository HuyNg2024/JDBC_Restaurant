package exceptions;

/**
 * Exception thrown when input validation fails.
 * Contains the field name that failed validation.
 */
public class ValidationException extends AppException {

    private final String fieldName;

    public ValidationException(String fieldName, String message) {
        super("VALIDATION_ERROR", message);
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
