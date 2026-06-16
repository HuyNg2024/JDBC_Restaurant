package GUI.panels;

import BLL.CustomerBLL;
import BLL.OrderBLL;
import BLL.StaffBLL;
import BLL.TransactionBLL;
import DTO.TransactionDTO;
import GUI.theme.AppTheme;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import utils.CurrencyFormatter;

public class DashboardPanel extends JPanel {
    
    private TransactionBLL transBLL = new TransactionBLL();
    private OrderBLL orderBLL = new OrderBLL();
    private CustomerBLL customerBLL = new CustomerBLL();
    private StaffBLL staffBLL = new StaffBLL();

    public DashboardPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(20, 20));
        setBackground(AppTheme.BG_PRIMARY);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // --- TITLE ---
        JLabel lblTitle = new JLabel("TỔNG QUAN HỆ THỐNG");
        lblTitle.setFont(AppTheme.FONT_TITLE.deriveFont(Font.BOLD, 32f));
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        add(lblTitle, BorderLayout.NORTH);

        // --- STATS CARDS ---
        JPanel pnlCards = new JPanel(new GridLayout(1, 4, 20, 0));
        pnlCards.setBackground(AppTheme.BG_PRIMARY);
        pnlCards.setPreferredSize(new Dimension(getWidth(), 150));

        double totalRevenue = 0;
        int totalTrans = 0;
        try {
            List<TransactionDTO> trans = transBLL.getAllTransactions();
            for (TransactionDTO t : trans) {
                if ("completed".equals(t.getStatus())) {
                    totalRevenue += t.getAmount();
                    totalTrans++;
                }
            }
        } catch (Exception e) {}

        int totalCustomers = 0;
        try { totalCustomers = customerBLL.getAllCustomers().size(); } catch (Exception e) {}

        int activeStaff = 0;
        try { activeStaff = staffBLL.getAllStaff().size(); } catch (Exception e) {}

        pnlCards.add(createCard("DOANH THU", CurrencyFormatter.formatVND(totalRevenue), AppTheme.ACCENT_GREEN));
        pnlCards.add(createCard("GIAO DỊCH", String.valueOf(totalTrans), AppTheme.ACCENT));
        pnlCards.add(createCard("KHÁCH HÀNG", String.valueOf(totalCustomers), AppTheme.ACCENT_PURPLE));
        pnlCards.add(createCard("NHÂN SỰ", String.valueOf(activeStaff), AppTheme.ACCENT_YELLOW));

        // --- CENTER / BOTTOM CONTENT ---
        JPanel pnlCenter = new JPanel(new BorderLayout(20, 20));
        pnlCenter.setBackground(AppTheme.BG_PRIMARY);
        
        // Prepare dummy data for 7-day revenue chart
        // In a real app, you would query this grouping by Date from transBLL
        java.util.LinkedHashMap<String, Double> chartData = new java.util.LinkedHashMap<>();
        chartData.put("T2", 1500000.0);
        chartData.put("T3", 2100000.0);
        chartData.put("T4", 1800000.0);
        chartData.put("T5", 3200000.0);
        chartData.put("T6", 4500000.0);
        chartData.put("T7", 6800000.0);
        chartData.put("CN", 5900000.0);
        
        // Add the real total revenue to today (CN for demo)
        chartData.put("CN", chartData.get("CN") + totalRevenue);
        
        GUI.components.BarChart revenueChart = new GUI.components.BarChart("BIỂU ĐỒ DOANH THU 7 NGÀY GẦN NHẤT (VNĐ)", chartData);
        
        pnlCenter.add(revenueChart, BorderLayout.CENTER);

        add(pnlCards, BorderLayout.CENTER);
        add(pnlCenter, BorderLayout.SOUTH);
    }

    private JPanel createCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(AppTheme.BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, accentColor),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(AppTheme.FONT_SUBTITLE);
        lblTitle.setForeground(AppTheme.TEXT_SECONDARY);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(AppTheme.FONT_TITLE.deriveFont(Font.BOLD, 28f));
        lblValue.setForeground(AppTheme.TEXT_PRIMARY);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblTitle);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(lblValue);

        return card;
    }
}
