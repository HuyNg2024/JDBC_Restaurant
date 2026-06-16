package GUI.components;

import GUI.theme.AppTheme;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

public class StatusBadge extends JLabel {
    private Color bgColor;

    public StatusBadge(String text) {
        super(text);
        setFont(AppTheme.FONT_SMALL);
        setForeground(AppTheme.BG_PRIMARY); // Text is dark for contrast against badge
        setBorder(new EmptyBorder(3, 10, 3, 10));
        setOpaque(false);
        updateStatus(text);
    }

    public void updateStatus(String status) {
        setText(status.toUpperCase());
        String lowerStatus = status.toLowerCase();
        
        if (lowerStatus.equals("active") || lowerStatus.equals("available") || lowerStatus.equals("completed") || lowerStatus.equals("empty")) {
            bgColor = AppTheme.ACCENT_GREEN;
        } else if (lowerStatus.equals("inactive") || lowerStatus.equals("unavailable") || lowerStatus.equals("cancelled")) {
            bgColor = AppTheme.ACCENT_RED;
        } else if (lowerStatus.equals("pending") || lowerStatus.equals("processing") || lowerStatus.equals("occupied")) {
            bgColor = AppTheme.ACCENT_YELLOW;
        } else if (lowerStatus.equals("reserved")) {
            bgColor = AppTheme.ACCENT_PURPLE;
        } else {
            bgColor = AppTheme.TEXT_SECONDARY;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(bgColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight()); // fully rounded
        g2.dispose();
        super.paintComponent(g);
    }
}
