package GUI.panels;

import BLL.TabletopBLL;
import DTO.TabletopDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.TableDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class TablePanel extends JPanel {
    private TabletopBLL tableBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;

    public TablePanel() {
        tableBLL = new TabletopBLL();
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
        
        JLabel lblTitle = new JLabel("QUẢN LÝ BÀN ĂN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm kiếm mã bàn...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("THÊM BÀN ĂN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(150, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showTableDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"ID", "Mã Bàn", "Sức Chứa", "Tầng", "Trạng Thái"};
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
            List<TabletopDTO> list = tableBLL.getAllTables();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<TabletopDTO> list = tableBLL.searchTables(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // silent
        }
    }

    private void updateTable(List<TabletopDTO> list) {
        tableModel.setRowCount(0);
        for (TabletopDTO table : list) {
            tableModel.addRow(new Object[]{
                table.getTableId(),
                table.getTableCode(),
                table.getCapacity() + " người",
                "Tầng " + table.getFloor(),
                table.getStatus()
            });
        }
    }

    private void showTableDialog(TabletopDTO table) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        TableDialog dialog = new TableDialog((Frame) parent, table);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn bàn ăn cần sửa!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            TabletopDTO t = tableBLL.getTableById(id);
            showTableDialog(t);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn bàn ăn cần xóa!", ToastNotification.Type.WARNING);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa bàn này?", "Xác nhận", 
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = (int) table.getValueAt(row, 0);
                tableBLL.deleteTable(id);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                loadData();
            } catch (DatabaseException e) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
            }
        }
    }
}
