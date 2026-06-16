package utils;

import exceptions.ValidationException;

/**
 * Utility class for input validation.
 * Provides common validation methods with Vietnamese error messages.
 */
public class Validator {

    /**
     * Check if string is null or empty.
     */
    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Validate that a field is not empty.
     * @throws ValidationException if the field is empty
     */
    public static void validateRequired(String value, String fieldName) throws ValidationException {
        if (isNullOrEmpty(value)) {
            throw new ValidationException(fieldName, fieldName + " không được để trống!");
        }
    }

    /**
     * Validate Vietnamese phone number (10 digits, starts with 0).
     */
    public static boolean isValidPhone(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return phone.matches("^0\\d{9}$");
    }

    /**
     * Validate phone and throw if invalid.
     * @throws ValidationException if phone format is invalid
     */
    public static void validatePhone(String phone, String fieldName) throws ValidationException {
        validateRequired(phone, fieldName);
        if (!isValidPhone(phone)) {
            throw new ValidationException(fieldName, "Số điện thoại phải gồm 10 chữ số và bắt đầu bằng 0!");
        }
    }

    /**
     * Check if string is a valid positive integer.
     */
    public static boolean isPositiveInteger(String s) {
        if (isNullOrEmpty(s)) return false;
        try {
            return Integer.parseInt(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if string is a valid non-negative integer.
     */
    public static boolean isNonNegativeInteger(String s) {
        if (isNullOrEmpty(s)) return false;
        try {
            return Integer.parseInt(s.trim()) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if string is a valid positive double.
     */
    public static boolean isPositiveDouble(String s) {
        if (isNullOrEmpty(s)) return false;
        try {
            return Double.parseDouble(s.trim()) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Validate that a value is a positive number.
     * @throws ValidationException if not a valid positive number
     */
    public static void validatePositiveDouble(String value, String fieldName) throws ValidationException {
        validateRequired(value, fieldName);
        if (!isPositiveDouble(value)) {
            throw new ValidationException(fieldName, fieldName + " phải là số dương hợp lệ!");
        }
    }

    /**
     * Validate that a value is a non-negative integer.
     * @throws ValidationException if not a valid non-negative integer
     */
    public static void validateNonNegativeInt(String value, String fieldName) throws ValidationException {
        validateRequired(value, fieldName);
        if (!isNonNegativeInteger(value)) {
            throw new ValidationException(fieldName, fieldName + " phải là số nguyên không âm!");
        }
    }

    /**
     * Validate name contains only letters and spaces.
     */
    public static boolean isValidName(String name) {
        if (isNullOrEmpty(name)) return false;
        return name.matches("^[\\p{L}\\s]+$");
    }

    /**
     * Validate email format.
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) return true; // email is optional
        return email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");
    }

    /**
     * Validate email and throw if invalid.
     * @throws ValidationException if email format is invalid
     */
    public static void validateEmail(String email, String fieldName) throws ValidationException {
        if (!isNullOrEmpty(email) && !isValidEmail(email)) {
            throw new ValidationException(fieldName, "Định dạng email không hợp lệ!");
        }
    }
}
