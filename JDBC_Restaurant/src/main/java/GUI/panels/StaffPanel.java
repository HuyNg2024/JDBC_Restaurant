package GUI.panels;

import BLL.StaffBLL;
import DTO.StaffDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.StaffDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class StaffPanel extends JPanel {
    private StaffBLL staffBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;
    
    public StaffPanel() {
        staffBLL = new StaffBLL();
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
        
        JLabel lblTitle = new JLabel("QUẢN LÝ NHÂN VIÊN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm kiếm tên, SĐT...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("THÊM NHÂN VIÊN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showStaffDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"ID", "Tên Nhân Viên", "Số Điện Thoại", "Vị Trí", "Lương", "Kinh Nghiệm", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable(tableModel);
        
        // Custom render for Status column
        table.getColumnModel().getColumn(6).setCellRenderer((t, value, isSelected, hasFocus, row, column) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p.setBackground(isSelected ? AppTheme.BG_HOVER : AppTheme.BG_CARD);
            if (value != null) {
                p.add(new StatusBadge(value.toString()));
            }
            return p;
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(AppTheme.BG_CARD);

        // --- ACTIONS ---
        JPanel pnlActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlActions.setBackground(AppTheme.BG_PRIMARY);
        
        RoundedButton btnEdit = new RoundedButton("CẬP NHẬT", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDelete = new RoundedButton("XÓA", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        RoundedButton btnRefresh = new RoundedButton("LÀM MỚI", AppTheme.BORDER, AppTheme.TEXT_PRIMARY);
        
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        btnRefresh.addActionListener(e -> loadData());
        
        pnlActions.add(btnRefresh);
        pnlActions.add(btnEdit);
        pnlActions.add(btnDelete);

        add(pnlHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlActions, BorderLayout.SOUTH);
    }

    public void loadData() {
        try {
            List<StaffDTO> list = staffBLL.getAllStaff();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<StaffDTO> list = staffBLL.searchStaff(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // Log silent error for search
        }
    }

    private void updateTable(List<StaffDTO> list) {
        tableModel.setRowCount(0);
        for (StaffDTO staff : list) {
            tableModel.addRow(new Object[]{
                staff.getStaffId(),
                staff.getStaffName(),
                staff.getPhone(),
                staff.getJob(),
                utils.CurrencyFormatter.formatVND(staff.getSalary()),
                staff.getWorkYears() + " năm",
                staff.getStatus()
            });
        }
    }

    private void showStaffDialog(StaffDTO staff) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        StaffDialog dialog = new StaffDialog((Frame) parent, staff);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn nhân viên cần sửa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            StaffDTO staff = staffBLL.getStaffById(id);
            showStaffDialog(staff);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn nhân viên cần xóa!", ToastNotification.Type.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(row, 0);
                staffBLL.deleteStaff(id);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                loadData();
            } catch (DatabaseException e) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
            }
        }
    }
}
