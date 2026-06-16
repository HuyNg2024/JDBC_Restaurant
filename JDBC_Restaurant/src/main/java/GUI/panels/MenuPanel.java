package GUI.panels;

import BLL.DrinkBLL;
import BLL.ItemBLL;
import BLL.ItemTypeBLL;
import DTO.DrinkDTO;
import DTO.ItemDTO;
import DTO.ItemTypeDTO;
import GUI.components.RoundedButton;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.DrinkDialog;
import GUI.dialogs.ItemDialog;
import GUI.dialogs.ItemTypeDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class MenuPanel extends JPanel {
    private JTabbedPane tabbedPane;
    
    // BLLs
    private ItemBLL itemBLL = new ItemBLL();
    private DrinkBLL drinkBLL = new DrinkBLL();
    private ItemTypeBLL typeBLL = new ItemTypeBLL();
    
    // Tables & Models
    private StyledTable tblFood;
    private DefaultTableModel modFood;
    private StyledTable tblDrink;
    private DefaultTableModel modDrink;
    private StyledTable tblType;
    private DefaultTableModel modType;

    public MenuPanel() {
        initComponents();
        loadAllData();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(AppTheme.BG_PRIMARY);
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- HEADER ---
        JLabel lblTitle = new JLabel("QUẢN LÝ THỰC ĐƠN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        lblTitle.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(lblTitle, BorderLayout.NORTH);

        // --- TABS ---
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(AppTheme.FONT_SUBTITLE);
        tabbedPane.setBackground(AppTheme.BG_SECONDARY);
        tabbedPane.setForeground(AppTheme.TEXT_PRIMARY);

        tabbedPane.addTab("Món Ăn", createFoodTab());
        tabbedPane.addTab("Thức Uống", createDrinkTab());
        tabbedPane.addTab("Danh Mục Món Ăn", createTypeTab());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==========================================
    // 1. FOOD TAB
    // ==========================================
    private JPanel createFoodTab() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(AppTheme.BG_PRIMARY);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Top actions
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTop.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnAdd = new RoundedButton("THÊM MÓN ĂN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.addActionListener(e -> {
            ItemDialog d = new ItemDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            d.setVisible(true);
            if(d.isSaved()) loadFood();
        });
        pnlTop.add(btnAdd);

        // Table
        String[] cols = {"ID", "Tên Món", "Giá (VNĐ)", "Chi Tiết", "Calories", "ID Danh Mục", "Trạng Thái"};
        modFood = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblFood = new StyledTable(modFood);
        tblFood.getColumnModel().getColumn(6).setCellRenderer((t, v, sel, f, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p.setBackground(sel ? AppTheme.BG_HOVER : AppTheme.BG_CARD);
            if (v != null) p.add(new StatusBadge(v.toString()));
            return p;
        });

        JScrollPane scroll = new JScrollPane(tblFood);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);

        // Bottom actions
        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnEdit = new RoundedButton("CẬP NHẬT", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDel = new RoundedButton("XÓA", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        
        btnEdit.addActionListener(e -> {
            int row = tblFood.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn món cần sửa!", ToastNotification.Type.WARNING); return; }
            try {
                ItemDTO dto = itemBLL.getItemById((int) tblFood.getValueAt(row, 0));
                ItemDialog d = new ItemDialog((Frame) SwingUtilities.getWindowAncestor(this), dto);
                d.setVisible(true);
                if(d.isSaved()) loadFood();
            } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
        });
        
        btnDel.addActionListener(e -> {
            int row = tblFood.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn món cần xóa!", ToastNotification.Type.WARNING); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?", "Xóa", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    itemBLL.deleteItem((int) tblFood.getValueAt(row, 0));
                    ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                    loadFood();
                } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
            }
        });

        pnlBot.add(btnEdit);
        pnlBot.add(btnDel);

        pnl.add(pnlTop, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);
        pnl.add(pnlBot, BorderLayout.SOUTH);
        return pnl;
    }

    // ==========================================
    // 2. DRINK TAB
    // ==========================================
    private JPanel createDrinkTab() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(AppTheme.BG_PRIMARY);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTop.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnAdd = new RoundedButton("THÊM THỨC UỐNG", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.addActionListener(e -> {
            DrinkDialog d = new DrinkDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            d.setVisible(true);
            if(d.isSaved()) loadDrink();
        });
        pnlTop.add(btnAdd);

        String[] cols = {"ID", "Tên Thức Uống", "Giá (VNĐ)", "Phân Loại", "Trạng Thái"};
        modDrink = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDrink = new StyledTable(modDrink);
        tblDrink.getColumnModel().getColumn(4).setCellRenderer((t, v, sel, f, r, c) -> {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            p.setBackground(sel ? AppTheme.BG_HOVER : AppTheme.BG_CARD);
            if (v != null) p.add(new StatusBadge(v.toString()));
            return p;
        });

        JScrollPane scroll = new JScrollPane(tblDrink);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnEdit = new RoundedButton("CẬP NHẬT", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDel = new RoundedButton("XÓA", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        
        btnEdit.addActionListener(e -> {
            int row = tblDrink.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn thức uống cần sửa!", ToastNotification.Type.WARNING); return; }
            try {
                DrinkDTO dto = drinkBLL.getDrinkById((int) tblDrink.getValueAt(row, 0));
                DrinkDialog d = new DrinkDialog((Frame) SwingUtilities.getWindowAncestor(this), dto);
                d.setVisible(true);
                if(d.isSaved()) loadDrink();
            } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
        });
        
        btnDel.addActionListener(e -> {
            int row = tblDrink.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn thức uống cần xóa!", ToastNotification.Type.WARNING); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Chắc chắn xóa?", "Xóa", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    drinkBLL.deleteDrink((int) tblDrink.getValueAt(row, 0));
                    ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                    loadDrink();
                } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
            }
        });

        pnlBot.add(btnEdit);
        pnlBot.add(btnDel);

        pnl.add(pnlTop, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);
        pnl.add(pnlBot, BorderLayout.SOUTH);
        return pnl;
    }

    // ==========================================
    // 3. ITEM TYPE TAB
    // ==========================================
    private JPanel createTypeTab() {
        JPanel pnl = new JPanel(new BorderLayout(0, 10));
        pnl.setBackground(AppTheme.BG_PRIMARY);
        pnl.setBorder(new EmptyBorder(10, 0, 0, 0));

        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlTop.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnAdd = new RoundedButton("THÊM DANH MỤC", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.addActionListener(e -> {
            ItemTypeDialog d = new ItemTypeDialog((Frame) SwingUtilities.getWindowAncestor(this), null);
            d.setVisible(true);
            if(d.isSaved()) loadType();
        });
        pnlTop.add(btnAdd);

        String[] cols = {"ID", "Tên Danh Mục", "Mô Tả"};
        modType = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblType = new StyledTable(modType);

        JScrollPane scroll = new JScrollPane(tblType);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);

        JPanel pnlBot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        pnlBot.setBackground(AppTheme.BG_PRIMARY);
        RoundedButton btnEdit = new RoundedButton("CẬP NHẬT", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDel = new RoundedButton("XÓA", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        
        btnEdit.addActionListener(e -> {
            int row = tblType.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn danh mục cần sửa!", ToastNotification.Type.WARNING); return; }
            try {
                ItemTypeDTO dto = typeBLL.getItemTypeById((int) tblType.getValueAt(row, 0));
                ItemTypeDialog d = new ItemTypeDialog((Frame) SwingUtilities.getWindowAncestor(this), dto);
                d.setVisible(true);
                if(d.isSaved()) loadType();
            } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
        });
        
        btnDel.addActionListener(e -> {
            int row = tblType.getSelectedRow();
            if(row < 0) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Chọn danh mục cần xóa!", ToastNotification.Type.WARNING); return; }
            int confirm = JOptionPane.showConfirmDialog(this, "Chắc chắn xóa? (Có thể lỗi nếu danh mục đang có món ăn)", "Xóa", JOptionPane.YES_NO_OPTION);
            if(confirm == JOptionPane.YES_OPTION) {
                try {
                    typeBLL.deleteItemType((int) tblType.getValueAt(row, 0));
                    ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Xóa thành công!", ToastNotification.Type.SUCCESS);
                    loadType();
                } catch (DatabaseException ex) { ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), ex.getMessage(), ToastNotification.Type.ERROR); }
            }
        });

        pnlBot.add(btnEdit);
        pnlBot.add(btnDel);

        pnl.add(pnlTop, BorderLayout.NORTH);
        pnl.add(scroll, BorderLayout.CENTER);
        pnl.add(pnlBot, BorderLayout.SOUTH);
        return pnl;
    }

    // ==========================================
    // DATA LOADERS
    // ==========================================
    public void loadAllData() {
        loadFood();
        loadDrink();
        loadType();
    }

    private void loadFood() {
        try {
            List<ItemDTO> list = itemBLL.getAllItems();
            modFood.setRowCount(0);
            for (ItemDTO i : list) {
                modFood.addRow(new Object[]{ i.getItemId(), i.getItemName(), utils.CurrencyFormatter.formatVND(i.getItemPrice()), i.getItemDetail(), i.getCalories(), i.getTypeId(), i.getStatus() });
            }
        } catch (DatabaseException e) { /* silent */ }
    }

    private void loadDrink() {
        try {
            List<DrinkDTO> list = drinkBLL.getAllDrinks();
            modDrink.setRowCount(0);
            for (DrinkDTO d : list) {
                modDrink.addRow(new Object[]{ d.getDrinkId(), d.getDrinkName(), utils.CurrencyFormatter.formatVND(d.getDrinkPrice()), d.getCategory(), d.getStatus() });
            }
        } catch (DatabaseException e) { /* silent */ }
    }

    private void loadType() {
        try {
            List<ItemTypeDTO> list = typeBLL.getAllItemTypes();
            modType.setRowCount(0);
            for (ItemTypeDTO t : list) {
                modType.addRow(new Object[]{ t.getTypeId(), t.getTypeName(), t.getDescription() });
            }
        } catch (DatabaseException e) { /* silent */ }
    }
}
