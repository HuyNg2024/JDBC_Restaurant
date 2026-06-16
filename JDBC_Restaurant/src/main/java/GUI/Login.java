package GUI;

import BLL.AccountBLL;
import DTO.AccountDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.AuthenticationException;
import exceptions.DatabaseException;
import utils.SessionManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private RoundedButton btnLogin;
    private AccountBLL accountBLL;

    public Login() {
        accountBLL = new AccountBLL();
        initComponents();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Nhà Hàng - Đăng nhập");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        // Logo / Title
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÀ HÀNG");
        lblTitle.setFont(AppTheme.FONT_TITLE.deriveFont(28f));
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Đăng nhập để tiếp tục");
        lblSub.setFont(AppTheme.FONT_BODY);
        lblSub.setForeground(AppTheme.TEXT_SECONDARY);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inputs
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(AppTheme.BG_PRIMARY);
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblUser = new JLabel("Tên đăng nhập:");
        lblUser.setFont(AppTheme.FONT_BODY);
        lblUser.setForeground(AppTheme.TEXT_PRIMARY);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtUsername = new JTextField();
        txtUsername.setMaximumSize(new Dimension(Short.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(AppTheme.FONT_BODY);
        lblPass.setForeground(AppTheme.TEXT_PRIMARY);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);

        txtPassword = new JPasswordField();
        txtPassword.setMaximumSize(new Dimension(Short.MAX_VALUE, AppTheme.INPUT_HEIGHT));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        formPanel.add(lblUser);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtUsername);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtPassword);

        // Button
        btnLogin = new RoundedButton("ĐĂNG NHẬP", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnLogin.setMaximumSize(new Dimension(Short.MAX_VALUE, 45));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnLogin.addActionListener(this::handleLogin);
        
        // Enter key to login
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin(null);
                }
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);

        // Add to main
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(lblSub);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(btnLogin);
        mainPanel.add(Box.createVerticalGlue());

        setContentPane(mainPanel);
    }

    private void handleLogin(ActionEvent e) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        try {
            // Check login via BLL
            AccountDTO account = accountBLL.checkLogin(username, password);
            
            // Set session
            SessionManager.getInstance().login(account);
            
            // Show success and open main frame
            ToastNotification.showToast(this, "Đăng nhập thành công!", ToastNotification.Type.SUCCESS);
            
            // Launch Main Frame
            java.awt.EventQueue.invokeLater(() -> {
                new ManagerMainForm().setVisible(true);
            });
            
            this.dispose();

        } catch (AuthenticationException ex) {
            ToastNotification.showToast(this, ex.getMessage(), ToastNotification.Type.ERROR);
            txtPassword.setText("");
            txtPassword.requestFocus();
        } catch (DatabaseException ex) {
            ToastNotification.showToast(this, "Lỗi kết nối CSDL!", ToastNotification.Type.ERROR);
        } catch (Exception ex) {
            ToastNotification.showToast(this, "Có lỗi xảy ra: " + ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public static void main(String[] args) {
        // Apply FlatLaf Look and Feel
        try {
            javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.themes.FlatMacDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        
        AppTheme.applyTheme();
        java.awt.EventQueue.invokeLater(() -> {
            new SplashScreen().showSplashAndLoad();
        });
    }
}
