import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class StaffGUI extends JFrame {

    public StaffGUI() {
        setTitle("Staff Management");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Title
        JLabel titleLabel = new JLabel("DANH SÁCH NHÂN VIÊN", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"Staff ID", "Name", "Salary", "Work Years", "Job"};
        Object[][] data = {
            {1, "Nguyen Van A", 1000.00, 2, "Waiter"},
            {2, "Le Thi B", 1200.00, 3, "Chef"},
            {3, "Tran Van C", 900.00, 1, "Waiter"}
        };

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        add(scrollPane, BorderLayout.CENTER);

        // Optional: Bottom panel with a button
        JPanel bottomPanel = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bottomPanel.add(btnRefresh);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        // Optional: Dùng FlatLaf để giao diện đẹp hơn
        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
        } catch (Exception ex) {
            System.out.println("Không thể cài đặt giao diện FlatLaf");
        }

        SwingUtilities.invokeLater(() -> {
            new StaffGUI().setVisible(true);
        });
    }
}
