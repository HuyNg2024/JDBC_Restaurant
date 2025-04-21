package GUI;

import BLL.ItemBLL;
import BLL.ItemTypeBLL;
import DTO.ItemDTO;
import DTO.ItemTypeDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ItemGUI extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private JTextField txtName, txtPrice, txtCalories, txtStock, txtDetail, txtStaffID;
    private JComboBox<ItemTypeDTO> comboType;
    private ItemBLL itemBLL = new ItemBLL();
    private ItemTypeBLL itemTypeBLL = new ItemTypeBLL();
    private ArrayList<ItemTypeDTO> typeList;

    public ItemGUI() {
        setTitle(" Quản Lý Món Ăn");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setUIFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel lblTitle = new JLabel(" Danh sách món ăn", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setBorder(new EmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã", "Tên món", "Giá", "Calo", "Tồn kho", "Loại món"});
        table = new JTable(model);
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Form nhập
        JPanel formPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        formPanel.setBorder(new EmptyBorder(10, 20, 10, 20));

        txtName = new JTextField();
        txtPrice = new JTextField();
        txtCalories = new JTextField();
        txtStock = new JTextField();
        txtDetail = new JTextField();
        txtStaffID = new JTextField();
        comboType = new JComboBox<>();

        formPanel.add(new JLabel("Tên món:"));
        formPanel.add(txtName);
        formPanel.add(new JLabel("Giá:"));
        formPanel.add(txtPrice);
        formPanel.add(new JLabel("Calo:"));
        formPanel.add(txtCalories);
        formPanel.add(new JLabel("Tồn kho min:"));
        formPanel.add(txtStock);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(txtDetail);
        formPanel.add(new JLabel("Loại món:"));
        formPanel.add(comboType);

        // Nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        JButton btnAdd = new JButton(" Thêm");
        JButton btnEdit = new JButton("️ Sửa");
        JButton btnDelete = new JButton("️ Xoá");
        JButton btnReload = new JButton(" Tải lại");
        JButton btnBack = new JButton(" Trở về");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnReload);
        buttonPanel.add(btnBack);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Load dữ liệu
        loadItemTypes();
        loadData();

        // Chọn dòng -> fill form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtName.setText(model.getValueAt(row, 1).toString());
                txtPrice.setText(model.getValueAt(row, 2).toString());
                txtCalories.setText(model.getValueAt(row, 3).toString());
                txtStock.setText(model.getValueAt(row, 4).toString());
                ItemTypeDTO selectedType = findTypeByName(model.getValueAt(row, 5).toString());
                comboType.setSelectedItem(selectedType);
            }
        });

        // Nút
        btnAdd.addActionListener(e -> addItem());
        btnEdit.addActionListener(e -> editItem());
        btnDelete.addActionListener(e -> deleteItem());
        btnReload.addActionListener(e -> loadData());
        btnBack.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, " Trở về (demo)");
            this.dispose();
        });
    }

    private void addItem() {
        try {
            ItemDTO item = getFormData();
            boolean ok = itemBLL.addItem(item);
            if (ok) {
                JOptionPane.showMessageDialog(this, " Thêm thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, " Thêm thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập: " + ex.getMessage());
        }
    }

    private void editItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để sửa.");
            return;
        }
        try {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            ItemDTO item = getFormData();
            item.setItemID(id);
            boolean ok = itemBLL.updateItem(item);
            if (ok) {
                JOptionPane.showMessageDialog(this, " Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, " Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
        }
    }

    private void deleteItem() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn món để xoá.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xoá món này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(model.getValueAt(row, 0).toString());
            boolean ok = itemBLL.deleteItem(id);
            if (ok) {
                JOptionPane.showMessageDialog(this, "️ Xoá thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, " Xoá thất bại!");
            }
        }
    }

    private void loadItemTypes() {
        typeList = itemTypeBLL.getAllTypes();
        comboType.removeAllItems();
        for (ItemTypeDTO t : typeList) {
            comboType.addItem(t);
        }
    }

    private void loadData() {
        model.setRowCount(0);
        ArrayList<ItemDTO> list = itemBLL.getAllItems();

        for (ItemDTO i : list) {
            String typeName = findTypeNameById(i.getItemType());
            model.addRow(new Object[]{
                    i.getItemID(),
                    i.getItemName(),
                    i.getItemPrice(),
                    i.getCalories(),
                    i.getProductMin(),
                    typeName
            });
        }
    }

    private ItemDTO getFormData() {
        String name = txtName.getText().trim();
        double price = Double.parseDouble(txtPrice.getText().trim());
        int calories = Integer.parseInt(txtCalories.getText().trim());
        int stock = Integer.parseInt(txtStock.getText().trim());
        String detail = txtDetail.getText().trim();
        ItemTypeDTO type = (ItemTypeDTO) comboType.getSelectedItem();

        return new ItemDTO(0, name, 2, detail, stock, price, calories, type.getTypeID()); // staffID = 2 (demo)
    }

    private ItemTypeDTO findTypeByName(String name) {
        for (ItemTypeDTO t : typeList) {
            if (t.getTypeName().equals(name)) return t;
        }
        return null;
    }

    private String findTypeNameById(int id) {
        for (ItemTypeDTO t : typeList) {
            if (t.getTypeID() == id) return t.getTypeName();
        }
        return "Không rõ";
    }

    public static void setUIFont(Font font) {
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("TableHeader.font", font.deriveFont(Font.BOLD));
        UIManager.put("TextField.font", font);
        UIManager.put("ComboBox.font", font);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ItemGUI().setVisible(true));
    }
}
