package GUI.components;

import GUI.theme.AppTheme;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Modern non-blocking toast notification.
 */
public class ToastNotification extends JDialog {
    
    public enum Type {
        SUCCESS, ERROR, WARNING, INFO
    }

    public ToastNotification(Window owner, String message, Type type) {
        super(owner, Dialog.ModalityType.MODELESS);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setBackground(new Color(0, 0, 0, 0)); // transparent background
        setFocusableWindowState(false);

        JPanel panel = new JPanel();
        panel.setBorder(new LineBorder(AppTheme.BORDER, 1, true));
        panel.setBackground(AppTheme.BG_CARD);
        panel.setLayout(new java.awt.BorderLayout());

        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(AppTheme.FONT_BODY.deriveFont(Font.BOLD));
        lblMessage.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        switch (type) {
            case SUCCESS:
                lblMessage.setForeground(AppTheme.ACCENT_GREEN);
                break;
            case ERROR:
                lblMessage.setForeground(AppTheme.ACCENT_RED);
                break;
            case WARNING:
                lblMessage.setForeground(AppTheme.ACCENT_YELLOW);
                break;
            case INFO:
                lblMessage.setForeground(AppTheme.ACCENT);
                break;
        }

        panel.add(lblMessage, java.awt.BorderLayout.CENTER);
        add(panel);
        pack();

        // Position at top center of owner
        if (owner != null) {
            int x = owner.getX() + (owner.getWidth() - getWidth()) / 2;
            int y = owner.getY() + 30; // 30px from top
            setLocation(x, y);
        } else {
            setLocationRelativeTo(null);
        }

        // Auto close after 3 seconds
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public static void showToast(Window owner, String message, Type type) {
        new ToastNotification(owner, message, type).setVisible(true);
    }
}
