package GUI.components;

import GUI.theme.AppTheme;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

public class RoundedButton extends JButton {
    private Color backgroundColor;
    private Color hoverColor;

    public RoundedButton(String text, Color bgColor, Color fgColor) {
        super(text);
        this.backgroundColor = bgColor;
        this.hoverColor = calculateHoverColor(bgColor);
        
        setForeground(fgColor);
        setFont(AppTheme.FONT_BUTTON);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(hoverColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(backgroundColor);
            }
        });
    }

    private Color calculateHoverColor(Color bg) {
        int r = Math.min(255, bg.getRed() + 20);
        int g = Math.min(255, bg.getGreen() + 20);
        int b = Math.min(255, bg.getBlue() + 20);
        return new Color(r, g, b);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        if (getModel().isPressed()) {
            g2.setColor(hoverColor.darker());
        } else if (getModel().isRollover()) {
            g2.setColor(hoverColor);
        } else {
            g2.setColor(backgroundColor);
        }
        
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), AppTheme.BORDER_RADIUS, AppTheme.BORDER_RADIUS);
        g2.dispose();
        super.paintComponent(g);
    }
}
