package utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {
    
    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

    /**
     * Formats a double value into Vietnamese Dong string (e.g., 100.000 đ)
     * @param amount The double value to format
     * @return Formatted string
     */
    public static String formatVND(double amount) {
        return formatter.format(amount);
    }
}
