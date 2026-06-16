package GUI.panels;

import BLL.CustomerBLL;
import DTO.CustomerDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.CustomerDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class CustomerPanel extends JPanel {
    private CustomerBLL customerBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;

    public CustomerPanel() {
        customerBLL = new CustomerBLL();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 20));
        setBackground(AppTheme.BG_PRIMARY);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(AppTheme.BG_PRIMARY);
        
        JLabel lblTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm kiếm tên, SĐT...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("THÊM KHÁCH HÀNG", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(160, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showCustomerDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"ID", "Họ Tên", "Số Điện Thoại", "Email", "Địa Chỉ", "Tổng Số Lần Đến", "Ngày Tham Gia"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppTheme.BG_CARD);

        // --- ACTIONS ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setBackground(AppTheme.BG_PRIMARY);
        
        RoundedButton btnExport = new RoundedButton("XUẤT EXCEL", AppTheme.ACCENT_PURPLE, AppTheme.BG_PRIMARY);
        RoundedButton btnEdit = new RoundedButton("CẬP NHẬT", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDelete = new RoundedButton("XÓA", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        RoundedButton btnRefresh = new RoundedButton("LÀM MỚI", AppTheme.BORDER, AppTheme.TEXT_PRIMARY);
        
        btnExport.addActionListener(e -> handleExport());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        btnRefresh.addActionListener(e -> loadData());
        
        pnlActions.add(btnExport);
        pnlActions.add(btnRefresh);
        pnlActions.add(btnEdit);
        pnlActions.add(btnDelete);

        add(pnlHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlActions, BorderLayout.SOUTH);
    }

    public void loadData() {
        try {
            List<CustomerDTO> list = customerBLL.getAllCustomers();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<CustomerDTO> list = customerBLL.searchCustomers(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // silent
        }
    }

    private void updateTable(List<CustomerDTO> list) {
        tableModel.setRowCount(0);
        utils.DateFormatter df = new utils.DateFormatter();
        for (CustomerDTO customer : list) {
            tableModel.addRow(new Object[]{
                customer.getCustomerId(),
                customer.getFullName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress(),
                customer.getTotalVisits(),
                customer.getCreatedAt() != null ? df.formatDateTime(customer.getCreatedAt()) : ""
            });
        }
    }

    private void showCustomerDialog(CustomerDTO customer) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        CustomerDialog dialog = new CustomerDialog((Frame) parent, customer);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn khách hàng cần sửa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            CustomerDTO customer = customerBLL.getCustomerById(id);
            showCustomerDialog(customer);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn khách hàng cần xóa!", ToastNotification.Type.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa khách hàng này?", "Xác nhận", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(row, 0);
                customerBLL.deleteCustomer(id);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                loadData();
            } catch (DatabaseException e) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
            }
        }
    }

    private void handleExport() {
        if (tableModel.getRowCount() == 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không có dữ liệu để xuất!", ToastNotification.Type.WARNING);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu file Excel (CSV)");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files (*.csv)", "csv"));
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String path = fileToSave.getAbsolutePath();
            if (!path.toLowerCase().endsWith(".csv")) {
                path += ".csv";
            }
            try {
                utils.CSVExporter.exportTableToCSV(table, path);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xuất báo cáo thành công!", ToastNotification.Type.SUCCESS);
            } catch (Exception ex) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Lỗi xuất file: " + ex.getMessage(), ToastNotification.Type.ERROR);
            }
        }
    }
}
