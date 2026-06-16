package GUI.panels;

import BLL.AccountBLL;
import DTO.AccountDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.AccountDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class AccountPanel extends JPanel {
    private AccountBLL accountBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;

    public AccountPanel() {
        accountBLL = new AccountBLL();
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
        
        JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm kiếm username, quyền...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("CẤP TÀI KHOẢN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showAccountDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"ID", "Tên Đăng Nhập", "Quyền", "ID Nhân Viên", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable(tableModel);
        
        table.getColumnModel().getColumn(4).setCellRenderer((t, value, isSelected, hasFocus, row, column) -> {
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
        
        RoundedButton btnEdit = new RoundedButton("ĐỔI QUYỀN/TRẠNG THÁI", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDelete = new RoundedButton("XÓA TÀI KHOẢN", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
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
            List<AccountDTO> list = accountBLL.getAllAccounts();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<AccountDTO> list = accountBLL.searchAccounts(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // silent
        }
    }

    private void updateTable(List<AccountDTO> list) {
        tableModel.setRowCount(0);
        for (AccountDTO acc : list) {
            tableModel.addRow(new Object[]{
                acc.getAccountId(),
                acc.getUsername(),
                acc.getRole().toUpperCase(),
                acc.getStaffId() > 0 ? "NV-" + acc.getStaffId() : "N/A",
                acc.getStatus()
            });
        }
    }

    private void showAccountDialog(AccountDTO acc) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        AccountDialog dialog = new AccountDialog((Frame) parent, acc);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn tài khoản cần sửa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            AccountDTO acc = accountBLL.getAccountById(id);
            if ("admin".equals(acc.getUsername())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không thể sửa tài khoản Admin gốc!", ToastNotification.Type.WARNING);
                return;
            }
            showAccountDialog(acc);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn tài khoản cần xóa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            AccountDTO acc = accountBLL.getAccountById(id);
            if ("admin".equals(acc.getUsername())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không thể xóa tài khoản Admin gốc!", ToastNotification.Type.WARNING);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn xóa tài khoản này?", "Xác nhận", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                accountBLL.deleteAccount(id);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                loadData();
            }
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }
}
