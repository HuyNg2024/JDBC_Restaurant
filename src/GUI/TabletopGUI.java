package GUI;

import BLL.TabletopBLL;
import DTO.TabletopDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class TabletopGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTableCode, txtStatus, txtCapacity, txtCurrent, txtCustomerID;
    private TabletopBLL tabletopBLL = new TabletopBLL();

    public TabletopGUI() {
        setTitle("️ Quản lý bàn (TableTop)");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));
        JLabel lblTitle = new JLabel(" Quản lý bàn", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"ID", "Mã Code", "Trạng thái", "Sức chứa", "Hiện tại", "Mã KH"});
        table = new JTable(model);
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form nhập dữ liệu
        JPanel formPanel = new JPanel(new GridLayout(2, 6, 10, 10));
        txtTableCode = new JTextField();
        txtStatus = new JTextField();
        txtCapacity = new JTextField();
        txtCurrent = new JTextField();
        txtCustomerID = new JTextField();

        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        formPanel.add(new JLabel("Mã Code:"));
        formPanel.add(new JLabel("Trạng thái:"));
        formPanel.add(new JLabel("Sức chứa:"));
        formPanel.add(new JLabel("Hiện tại:"));
        formPanel.add(new JLabel("Mã KH:"));
        formPanel.add(new JLabel("")); // empty

        formPanel.add(txtTableCode);
        formPanel.add(txtStatus);
        formPanel.add(txtCapacity);
        formPanel.add(txtCurrent);
        formPanel.add(txtCustomerID);

        // Nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnAdd = new JButton(" Thêm");
        JButton btnUpdate = new JButton("️ Sửa");
        JButton btnDelete = new JButton("️ Xoá");
        JButton btnReload = new JButton(" Tải lại");
        JButton btnBack = new JButton(" Trở về");

        styleButton(btnAdd);
        styleButton(btnUpdate);
        styleButton(btnDelete);
        styleButton(btnReload);
        styleButton(btnBack);

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReload);
        buttonPanel.add(btnBack);

        // Add tất cả xuống SOUTH
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Tải dữ liệu ban đầu
        loadData();

        // Sự kiện chọn dòng → đổ lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTableCode.setText(model.getValueAt(row, 1).toString());
                txtStatus.setText(model.getValueAt(row, 2).toString());
                txtCapacity.setText(model.getValueAt(row, 3).toString());
                txtCurrent.setText(model.getValueAt(row, 4).toString());
                txtCustomerID.setText(model.getValueAt(row, 5).toString().equals("—") ? "" : model.getValueAt(row, 5).toString());
            }
        });

        // Xử lý nút
        btnAdd.addActionListener(e -> addTable());
        btnUpdate.addActionListener(e -> updateTable());
        btnDelete.addActionListener(e -> deleteTable());
        btnReload.addActionListener(e -> loadData());
        btnBack.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, " Trở về menu chính (giả lập)");
            this.dispose();
        });
    }

    private void addTable() {
        try {
            TabletopDTO table = getFormData(false);
            boolean ok = tabletopBLL.addTable(table);
            if (ok) {
                JOptionPane.showMessageDialog(this, " Thêm bàn thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Thêm bàn thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập liệu: " + ex.getMessage());
        }
    }

    private void updateTable() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "🔎 Vui lòng chọn một dòng để sửa!");
            return;
        }
        try {
            int tableID = Integer.parseInt(model.getValueAt(row, 0).toString());
            TabletopDTO table = getFormData(true);
            table.setTableID(tableID);

            boolean ok = tabletopBLL.updateTable(table);
            if (ok) {
                JOptionPane.showMessageDialog(this, " Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteTable() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, " Hãy chọn một bàn để xoá!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá bàn này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            boolean ok = tabletopBLL.deleteTable(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "️ Xoá thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, " Xoá thất bại!");
            }
        }
    }

    private TabletopDTO getFormData(boolean forUpdate) {
        String code = txtTableCode.getText().trim();
        String status = txtStatus.getText().trim();
        int capacity = Integer.parseInt(txtCapacity.getText().trim());
        int current = Integer.parseInt(txtCurrent.getText().trim());
        Integer customerID = txtCustomerID.getText().trim().isEmpty() ? null : Integer.parseInt(txtCustomerID.getText().trim());

        return new TabletopDTO(0, customerID, code, status, capacity, current);
    }

    private void clearForm() {
        txtTableCode.setText("");
        txtStatus.setText("");
        txtCapacity.setText("");
        txtCurrent.setText("");
        txtCustomerID.setText("");
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<TabletopDTO> list = tabletopBLL.getAllTables();
        for (TabletopDTO t : list) {
            model.addRow(new Object[]{
                    t.getTableID(),
                    t.getTableCode(),
                    t.getStatus(),
                    t.getCapacity(),
                    t.getCurrent(),
                    t.getCustomerID() != null ? t.getCustomerID() : "—"
            });
        }
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(110, 32));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    public static void setUIFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font.deriveFont(Font.BOLD, 14));
        UIManager.put("TextField.font", font);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TabletopGUI().setVisible(true));
    }
}
