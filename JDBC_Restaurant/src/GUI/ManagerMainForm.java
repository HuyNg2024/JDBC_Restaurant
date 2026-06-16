package GUI;

import GUI.theme.AppTheme;
import utils.SessionManager;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ManagerMainForm extends JFrame {
    
    private JPanel sidebarPanel;
    private JPanel mainContentPanel;
    private CardLayout cardLayout;
    
    // Panels
    private JPanel pnlStaff;
    private JPanel pnlCustomer;
    private JPanel pnlMenu;
    private JPanel pnlTable;
    private JPanel pnlReserve;
    private JPanel pnlOrder;
    
    // Sidebar items tracking
    private JPanel activeSidebarItem = null;

    public ManagerMainForm() {
        initComponents();
        setupPanels();
    }

    private void initComponents() {
        setTitle("Hệ Thống Quản Lý Nhà Hàng - Trình Quản Lý");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- SIDEBAR ---
        sidebarPanel = new JPanel();
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(AppTheme.SIDEBAR_WIDTH, getHeight()));
        sidebarPanel.setBackground(AppTheme.BG_SECONDARY);
        sidebarPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        // --- LOGO ---
        JLabel lblLogo = new JLabel("POS");
        lblLogo.setFont(AppTheme.FONT_TITLE.deriveFont(Font.BOLD, 32f));
        lblLogo.setForeground(AppTheme.ACCENT);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblRole = new JLabel();
        if (SessionManager.getInstance().isLoggedIn()) {
            lblRole.setText("Xin chào, " + SessionManager.getInstance().getCurrentUser().getUsername());
        }
        lblRole.setFont(AppTheme.FONT_SMALL);
        lblRole.setForeground(AppTheme.TEXT_SECONDARY);
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebarPanel.add(lblLogo);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        sidebarPanel.add(lblRole);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        // Menu Items
        addSidebarItem("Tổng quan", "DASHBOARD");
        addSidebarItem("Quản lý Bàn", "TABLE");
        addSidebarItem("Quản lý Thực Đơn", "MENU");
        addSidebarItem("Quản lý Khách Hàng", "CUSTOMER");
        addSidebarItem("Đặt Bàn", "RESERVE");
        addSidebarItem("Đơn Hàng", "ORDER");
        
        // Admin only
        if (SessionManager.getInstance().isLoggedIn() && 
            "manager".equals(SessionManager.getInstance().getCurrentUser().getRole())) {
            addSidebarItem("Quản lý Nhân Viên", "STAFF");
            addSidebarItem("Tài Khoản", "ACCOUNT");
        }

        sidebarPanel.add(Box.createVerticalGlue());
        
        // Logout button
        JPanel pnlLogout = createSidebarItem("Đăng xuất", "LOGOUT");
        sidebarPanel.add(pnlLogout);

        // --- MAIN CONTENT ---
        cardLayout = new CardLayout();
        mainContentPanel = new JPanel(cardLayout);
        mainContentPanel.setBackground(AppTheme.BG_PRIMARY);

        add(sidebarPanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void addSidebarItem(String title, String cardName) {
        JPanel item = createSidebarItem(title, cardName);
        sidebarPanel.add(item);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private JPanel createSidebarItem(String title, String action) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setMaximumSize(new Dimension(AppTheme.SIDEBAR_WIDTH - 20, 45));
        panel.setBackground(AppTheme.BG_SECONDARY);
        panel.setBorder(new EmptyBorder(10, 15, 10, 15));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel label = new JLabel(title);
        label.setFont(AppTheme.FONT_SIDEBAR);
        label.setForeground(AppTheme.TEXT_SECONDARY);
        panel.add(label, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (panel != activeSidebarItem) {
                    panel.setBackground(AppTheme.BG_HOVER);
                    label.setForeground(AppTheme.TEXT_PRIMARY);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (panel != activeSidebarItem) {
                    panel.setBackground(AppTheme.BG_SECONDARY);
                    label.setForeground(AppTheme.TEXT_SECONDARY);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if ("LOGOUT".equals(action)) {
                    logout();
                    return;
                }
                
                // Update active state
                if (activeSidebarItem != null) {
                    activeSidebarItem.setBackground(AppTheme.BG_SECONDARY);
                    ((JLabel)activeSidebarItem.getComponent(0)).setForeground(AppTheme.TEXT_SECONDARY);
                    ((JLabel)activeSidebarItem.getComponent(0)).setFont(AppTheme.FONT_SIDEBAR);
                }
                
                activeSidebarItem = panel;
                activeSidebarItem.setBackground(AppTheme.SIDEBAR_ACTIVE);
                label.setForeground(AppTheme.ACCENT);
                label.setFont(AppTheme.FONT_SIDEBAR_ACTIVE);
                
                // Switch Card
                cardLayout.show(mainContentPanel, action);
            }
        });

        // Set default active
        if ("DASHBOARD".equals(action) && activeSidebarItem == null) {
            activeSidebarItem = panel;
            panel.setBackground(AppTheme.SIDEBAR_ACTIVE);
            label.setForeground(AppTheme.ACCENT);
            label.setFont(AppTheme.FONT_SIDEBAR_ACTIVE);
        }

        return panel;
    }

    private void setupPanels() {
        // Link the actual Panels
        JPanel pnlDashboard = new GUI.panels.DashboardPanel();
        pnlCustomer = new GUI.panels.CustomerPanel();
        pnlTable = new GUI.panels.TablePanel();
        pnlStaff = new GUI.panels.StaffPanel();
        JPanel pnlAccount = new GUI.panels.AccountPanel();
        pnlReserve = new GUI.panels.ReservePanel();
        pnlMenu = new GUI.panels.MenuPanel();
        pnlOrder = new GUI.panels.OrderPanel();

        mainContentPanel.add(pnlDashboard, "DASHBOARD");
        mainContentPanel.add(pnlTable, "TABLE");
        mainContentPanel.add(pnlMenu, "MENU");
        mainContentPanel.add(pnlCustomer, "CUSTOMER");
        mainContentPanel.add(pnlReserve, "RESERVE");
        mainContentPanel.add(pnlOrder, "ORDER");
        mainContentPanel.add(pnlStaff, "STAFF");
        mainContentPanel.add(pnlAccount, "ACCOUNT");
        
        cardLayout.show(mainContentPanel, "DASHBOARD");
    }

    private JPanel createPlaceholderPanel(String title) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(AppTheme.BG_PRIMARY);
        JLabel l = new JLabel(title, SwingConstants.CENTER);
        l.setFont(AppTheme.FONT_TITLE.deriveFont(40f));
        l.setForeground(AppTheme.TEXT_SECONDARY);
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.getInstance().logout();
            this.dispose();
            java.awt.EventQueue.invokeLater(() -> {
                new Login().setVisible(true);
            });
        }
    }
}
