package utils;

import DTO.AccountDTO;

/**
 * Manages the current logged-in user session.
 * Singleton pattern - stores user info after successful login.
 */
public class SessionManager {

    private static AccountDTO currentUser;

    private SessionManager() {}

    /**
     * Set the current logged-in user.
     */
    public static void login(AccountDTO user) {
        currentUser = user;
    }

    /**
     * Clear the current session.
     */
    public static void logout() {
        currentUser = null;
    }

    /**
     * Get the current logged-in user.
     * @return AccountDTO or null if not logged in
     */
    public static AccountDTO getCurrentUser() {
        return currentUser;
    }

    /**
     * Get the current user's role.
     * @return "manager", "staff", or null
     */
    public static String getRole() {
        return currentUser != null ? currentUser.getRole() : null;
    }

    /**
     * Check if the current user is a manager.
     */
    public static boolean isManager() {
        return currentUser != null && "manager".equals(currentUser.getRole());
    }

    /**
     * Check if a user is logged in.
     */
    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    /**
     * Get display name for the current user.
     */
    public static String getDisplayName() {
        return currentUser != null ? currentUser.getUsername() : "Guest";
    }
}
