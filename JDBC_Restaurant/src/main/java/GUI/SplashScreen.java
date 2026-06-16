package GUI;

import GUI.theme.AppTheme;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingWorker;

public class SplashScreen extends JWindow {
    private JProgressBar progressBar;
    private JLabel lblStatus;

    public SplashScreen() {
        // AppTheme will be initialized inside Main or Login
        AppTheme.applyTheme();
        
        setSize(500, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background gradient
                java.awt.GradientPaint gp = new java.awt.GradientPaint(0, 0, AppTheme.BG_PRIMARY, 0, getHeight(), AppTheme.BG_SECONDARY);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Border
                g2.setColor(AppTheme.ACCENT);
                g2.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
            }
        };
        contentPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Center: Title
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        // Create Title
        JLabel lblTitle = new JLabel("NHÀ HÀNG CAO CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitle = new JLabel("Hệ Thống Quản Lý Chuyên Nghiệp");
        lblSubtitle.setFont(AppTheme.FONT_SUBTITLE);
        lblSubtitle.setForeground(AppTheme.TEXT_SECONDARY);
        lblSubtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        centerPanel.add(Box.createVerticalGlue());
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(lblSubtitle);
        centerPanel.add(Box.createVerticalGlue());
        
        // Bottom: Progress
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);
        
        lblStatus = new JLabel("Khởi động hệ thống...");
        lblStatus.setFont(AppTheme.FONT_SMALL);
        lblStatus.setForeground(AppTheme.TEXT_PRIMARY);
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(460, 6));
        progressBar.setMaximumSize(new Dimension(460, 6));
        progressBar.setBorderPainted(false);
        progressBar.setForeground(AppTheme.ACCENT);
        progressBar.setBackground(AppTheme.BG_CARD);
        progressBar.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        bottomPanel.add(lblStatus);
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        bottomPanel.add(progressBar);

        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        
        setContentPane(contentPane);
    }

    public void showSplashAndLoad() {
        setVisible(true);
        
        new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                // Simulate loading steps
                publish(10, 0); // Progress, status step
                Thread.sleep(500);
                
                publish(30, 1); // Loading configuration
                Thread.sleep(500);
                
                publish(60, 2); // Connecting database
                // Try to test database connection here
                boolean dbOk = DAL.DBConnect.testConnection();
                if (!dbOk) {
                    System.err.println("Warning: DB Connection failed on splash.");
                }
                Thread.sleep(500);
                
                publish(90, 3); // Loading modules
                Thread.sleep(500);
                
                publish(100, 4); // Done
                Thread.sleep(200);
                return null;
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int progress = chunks.get(0);
                int step = chunks.size() > 1 ? chunks.get(1) : 0;
                
                progressBar.setValue(progress);
                switch(step) {
                    case 0: lblStatus.setText("Khởi động hệ thống..."); break;
                    case 1: lblStatus.setText("Đang nạp cấu hình..."); break;
                    case 2: lblStatus.setText("Kết nối cơ sở dữ liệu..."); break;
                    case 3: lblStatus.setText("Tải các module giao diện..."); break;
                    case 4: lblStatus.setText("Hoàn tất!"); break;
                }
            }

            @Override
            protected void done() {
                dispose();
                // Open Login frame
                java.awt.EventQueue.invokeLater(() -> {
                    new Login().setVisible(true);
                });
            }
        }.execute();
    }
}
