package GUI.components;

import GUI.theme.AppTheme;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class SearchBar extends JPanel {
    private final JTextField txtSearch;

    public SearchBar(String placeholder) {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_INPUT);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(AppTheme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        setPreferredSize(new Dimension(250, AppTheme.INPUT_HEIGHT));

        txtSearch = new JTextField();
        txtSearch.setBackground(AppTheme.BG_INPUT);
        txtSearch.setForeground(AppTheme.TEXT_PRIMARY);
        txtSearch.setCaretColor(AppTheme.TEXT_PRIMARY);
        txtSearch.setFont(AppTheme.FONT_BODY);
        txtSearch.setBorder(null);
        
        // Use FlatLaf placeholder feature if available, else standard text
        txtSearch.putClientProperty("JTextField.placeholderText", placeholder);
        
        add(txtSearch, BorderLayout.CENTER);
    }

    public String getText() {
        return txtSearch.getText();
    }

    public void setText(String text) {
        txtSearch.setText(text);
    }

    public JTextField getTextField() {
        return txtSearch;
    }
}
