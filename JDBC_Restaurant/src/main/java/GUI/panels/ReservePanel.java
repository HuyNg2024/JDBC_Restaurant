package GUI.panels;

import BLL.ReserveBLL;
import DTO.ReserveDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.ReserveDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import utils.DateFormatter;

public class ReservePanel extends JPanel {
    private ReserveBLL reserveBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;
    private DateFormatter df = new DateFormatter();

    public ReservePanel() {
        reserveBLL = new ReserveBLL();
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
        
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐẶT BÀN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm theo KH, ghi chú...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("THÊM ĐẶT BÀN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showReserveDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"ID", "ID Khách", "ID Bàn", "Thời Gian", "Số Khách", "Ghi Chú", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable(tableModel);
        
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
        RoundedButton btnDelete = new RoundedButton("HỦY ĐẶT", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
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
            List<ReserveDTO> list = reserveBLL.getAllReservations();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<ReserveDTO> list = reserveBLL.searchReservations(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // silent
        }
    }

    private void updateTable(List<ReserveDTO> list) {
        tableModel.setRowCount(0);
        for (ReserveDTO r : list) {
            tableModel.addRow(new Object[]{
                r.getReserveId(),
                r.getCustomerId() > 0 ? "KH-" + r.getCustomerId() : "Vãng lai",
                r.getTableId() > 0 ? "Bàn " + r.getTableId() : "Đang chờ xếp",
                df.formatDateTime(r.getReserveTime()),
                r.getGuestCount() + " người",
                r.getNote(),
                r.getStatus()
            });
        }
    }

    private void showReserveDialog(ReserveDTO reserve) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        ReserveDialog dialog = new ReserveDialog((Frame) parent, reserve);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn lịch đặt cần sửa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            ReserveDTO r = reserveBLL.getReservationById(id);
            showReserveDialog(r);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn lịch đặt cần hủy!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            ReserveDTO r = reserveBLL.getReservationById(id);
            if ("completed".equals(r.getStatus()) || "cancelled".equals(r.getStatus())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không thể xóa lịch đặt đã hoàn tất hoặc đã hủy!", ToastNotification.Type.WARNING);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn hủy đặt bàn này?", "Xác nhận", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                r.setStatus("cancelled");
                reserveBLL.updateReservation(r);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Hủy đặt bàn thành công!", ToastNotification.Type.SUCCESS);
                loadData();
            }
        } catch (DatabaseException | exceptions.ValidationException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }
}
