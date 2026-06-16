package GUI.theme;

import com.formdev.flatlaf.FlatDarkLaf;
import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;

public class AppTheme {
    // --- Colors (Inspired by Catppuccin Mocha) ---
    public static final Color BG_PRIMARY = new Color(30, 30, 46);     // Main background
    public static final Color BG_SECONDARY = new Color(36, 36, 54);   // Sidebar
    public static final Color BG_CARD = new Color(45, 45, 65);        // Cards/panels
    public static final Color BG_HOVER = new Color(55, 55, 75);       // Hover state
    public static final Color BG_INPUT = new Color(40, 40, 58);       // Input fields
    
    public static final Color ACCENT = new Color(137, 180, 250);      // Blue accent
    public static final Color ACCENT_GREEN = new Color(166, 227, 161); // Success
    public static final Color ACCENT_RED = new Color(243, 139, 168);   // Danger
    public static final Color ACCENT_YELLOW = new Color(249, 226, 175); // Warning
    
    public static final Color TEXT_PRIMARY = new Color(205, 214, 244); // Main text
    public static final Color TEXT_SECONDARY = new Color(147, 153, 178); // Subtle text
    public static final Color BORDER = new Color(69, 71, 90);          // Borders
    public static final Color SIDEBAR_ACTIVE = new Color(49, 50, 68);  // Active sidebar item

    // --- Fonts ---
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 13);
    
    // --- Dimensions ---
    public static final int BORDER_RADIUS = 10;
    public static final int BUTTON_HEIGHT = 36;
    public static final int INPUT_HEIGHT = 36;
    public static final int TABLE_ROW_HEIGHT = 35;

    public static void applyTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            
            // Global overrides for FlatLaf
            UIManager.put("Panel.background", BG_PRIMARY);
            UIManager.put("Label.foreground", TEXT_PRIMARY);
            UIManager.put("Label.font", FONT_BODY);
            
            // TextField
            UIManager.put("TextField.background", BG_INPUT);
            UIManager.put("TextField.foreground", TEXT_PRIMARY);
            UIManager.put("TextField.caretForeground", TEXT_PRIMARY);
            UIManager.put("TextField.arc", BORDER_RADIUS);
            UIManager.put("TextField.borderWidth", 1);
            UIManager.put("TextField.borderColor", BORDER);
            UIManager.put("TextField.focusedBorderColor", ACCENT);
            UIManager.put("TextField.font", FONT_BODY);
            
            // ComboBox
            UIManager.put("ComboBox.background", BG_INPUT);
            UIManager.put("ComboBox.foreground", TEXT_PRIMARY);
            UIManager.put("ComboBox.selectionBackground", ACCENT);
            UIManager.put("ComboBox.selectionForeground", BG_PRIMARY);
            
            // Table
            UIManager.put("Table.background", BG_CARD);
            UIManager.put("Table.foreground", TEXT_PRIMARY);
            UIManager.put("Table.selectionBackground", BG_HOVER);
            UIManager.put("Table.selectionForeground", TEXT_PRIMARY);
            UIManager.put("Table.gridColor", BORDER);
            UIManager.put("Table.font", FONT_BODY);
            UIManager.put("TableHeader.background", BG_SECONDARY);
            UIManager.put("TableHeader.foreground", TEXT_PRIMARY);
            UIManager.put("TableHeader.font", FONT_SUBTITLE);
            UIManager.put("Table.rowHeight", TABLE_ROW_HEIGHT);
            
            // ScrollPane
            UIManager.put("ScrollPane.background", BG_PRIMARY);
            UIManager.put("ScrollPane.border", null);
            UIManager.put("ScrollBar.thumb", BORDER);
            UIManager.put("ScrollBar.thumbHover", TEXT_SECONDARY);
            
        } catch (Exception ex) {
            System.err.println("Failed to initialize AppTheme");
        }
    }
}
