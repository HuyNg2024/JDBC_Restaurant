package GUI.dialogs;

import BLL.ItemBLL;
import BLL.OrderBLL;
import BLL.TabletopBLL;
import DTO.ItemDTO;
import DTO.OrderDTO;
import DTO.OrderItemDTO;
import DTO.TabletopDTO;
import GUI.components.RoundedButton;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import utils.CurrencyFormatter;

public class OrderDialog extends JDialog {
    private OrderDTO order;
    private boolean isSaved = false;
    private OrderBLL orderBLL;
    private ItemBLL itemBLL;
    private TabletopBLL tableBLL;

    private JComboBox<String> cbTable;
    private StyledTable tblItems;
    private DefaultTableModel modItems;
    private JComboBox<String> cbAddItem;
    private JSpinner spinQty;
    private JSpinner spinVat;
    private JSpinner spinDiscount;
    private JLabel lblTotal;
    
    private double currentRawTotal = 0;
    
    private List<TabletopDTO> tables;
    private List<ItemDTO> menuItems;

    public OrderDialog(Frame owner, OrderDTO order) {
        super(owner, true);
        this.order = order;
        this.orderBLL = new OrderBLL();
        this.itemBLL = new ItemBLL();
        this.tableBLL = new TabletopBLL();
        
        loadDependencies();
        initComponents();
        if (order != null) {
            loadOrderItems();
        } else {
            // New order initialization
            this.order = new OrderDTO();
            this.order.setStatus("new");
            this.order.setTotalPrice(0);
        }
    }

    private void loadDependencies() {
        try {
            tables = tableBLL.getAllTables();
            menuItems = itemBLL.getAvailableItems();
        } catch (DatabaseException e) {}
    }

    private void initComponents() {
        setTitle(order == null ? "Tạo Đơn Hàng Mới (POS)" : "Chi Tiết Đơn Hàng #" + order.getOrderId());
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- TOP: Info & Table Selection ---
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pnlTop.setBackground(AppTheme.BG_PRIMARY);
        
        JLabel lblTable = new JLabel("Chọn Bàn:");
        lblTable.setFont(AppTheme.FONT_BODY);
        lblTable.setForeground(AppTheme.TEXT_PRIMARY);
        
        cbTable = new JComboBox<>();
        cbTable.addItem("0 - Mang về (Take away)");
        if (tables != null) {
            for (TabletopDTO t : tables) {
                cbTable.addItem(t.getTableId() + " - " + t.getTableCode() + " (" + t.getStatus() + ")");
            }
        }
        cbTable.setFont(AppTheme.FONT_BODY);
        if (order != null && order.getTableId() > 0) {
            // Select correct table in combo box
            for (int i=0; i<cbTable.getItemCount(); i++) {
                if(cbTable.getItemAt(i).startsWith(order.getTableId() + " -")) {
                    cbTable.setSelectedIndex(i);
                    break;
                }
            }
        }

        RoundedButton btnSaveOrder = new RoundedButton(order == null ? "TẠO ĐƠN" : "CẬP NHẬT BÀN", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnSaveOrder.addActionListener(e -> saveOrderInfo());
        
        pnlTop.add(lblTable);
        pnlTop.add(cbTable);
        pnlTop.add(btnSaveOrder);

        // --- CENTER: Order Items Table ---
        JPanel pnlCenter = new JPanel(new BorderLayout(0, 10));
        pnlCenter.setBackground(AppTheme.BG_PRIMARY);
        
        String[] cols = {"Mã Món", "Tên Món", "Đơn Giá", "Số Lượng", "Thành Tiền"};
        modItems = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblItems = new StyledTable(modItems);
        JScrollPane scroll = new JScrollPane(tblItems);
        scroll.getViewport().setBackground(AppTheme.BG_CARD);
        
        pnlCenter.add(new JLabel("Chi tiết món ăn:"){
            {setFont(AppTheme.FONT_SUBTITLE); setForeground(AppTheme.TEXT_PRIMARY);}
        }, BorderLayout.NORTH);
        pnlCenter.add(scroll, BorderLayout.CENTER);

        // --- BOTTOM: Add Item & Total ---
        JPanel pnlBot = new JPanel(new BorderLayout(10, 0));
        pnlBot.setBackground(AppTheme.BG_PRIMARY);
        
        // Add item controls (Left side)
        JPanel pnlAddItem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlAddItem.setBackground(AppTheme.BG_PRIMARY);
        
        cbAddItem = new JComboBox<>();
        if (menuItems != null) {
            for (ItemDTO i : menuItems) {
                cbAddItem.addItem(i.getItemId() + " - " + i.getItemName() + " (" + CurrencyFormatter.formatVND(i.getItemPrice()) + ")");
            }
        }
        cbAddItem.setFont(AppTheme.FONT_BODY);
        
        spinQty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spinQty.setFont(AppTheme.FONT_BODY);
        
        RoundedButton btnAddItem = new RoundedButton("THÊM MÓN", AppTheme.ACCENT_GREEN, AppTheme.BG_PRIMARY);
        btnAddItem.addActionListener(e -> addOrderItem());
        
        RoundedButton btnRemoveItem = new RoundedButton("BỚT MÓN", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        btnRemoveItem.addActionListener(e -> removeOrderItem());
        
        pnlAddItem.add(cbAddItem);
        pnlAddItem.add(new JLabel("SL:"){{setForeground(AppTheme.TEXT_PRIMARY);}});
        pnlAddItem.add(spinQty);
        pnlAddItem.add(btnAddItem);
        pnlAddItem.add(btnRemoveItem);
        
        // Total and Taxes (Right side)
        JPanel pnlTotal = new JPanel(new GridLayout(3, 2, 5, 5));
        pnlTotal.setBackground(AppTheme.BG_PRIMARY);
        
        JLabel lblVatTxt = new JLabel("Thuế VAT (%):");
        lblVatTxt.setForeground(AppTheme.TEXT_SECONDARY);
        spinVat = new JSpinner(new SpinnerNumberModel(8, 0, 100, 1)); // Default 8% VAT
        spinVat.addChangeListener(e -> recalculateTotal());
        
        JLabel lblDiscountTxt = new JLabel("Giảm giá (%):");
        lblDiscountTxt.setForeground(AppTheme.TEXT_SECONDARY);
        spinDiscount = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        spinDiscount.addChangeListener(e -> recalculateTotal());
        
        pnlTotal.add(lblVatTxt);
        pnlTotal.add(spinVat);
        pnlTotal.add(lblDiscountTxt);
        pnlTotal.add(spinDiscount);
        
        lblTotal = new JLabel("Tổng: 0 ₫", SwingConstants.RIGHT);
        lblTotal.setFont(AppTheme.FONT_TITLE.deriveFont(18f));
        lblTotal.setForeground(AppTheme.ACCENT_YELLOW);
        
        pnlTotal.add(new JLabel("")); // Spacer
        pnlTotal.add(lblTotal);
        
        pnlBot.add(pnlAddItem, BorderLayout.WEST);
        pnlBot.add(pnlTotal, BorderLayout.EAST);
        
        // --- ASSEMBLE ---
        mainPanel.add(pnlTop, BorderLayout.NORTH);
        mainPanel.add(pnlCenter, BorderLayout.CENTER);
        mainPanel.add(pnlBot, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    private void saveOrderInfo() {
        try {
            int tableId = 0;
            if (cbTable.getSelectedIndex() > 0) {
                String selected = cbTable.getSelectedItem().toString();
                tableId = Integer.parseInt(selected.split(" ")[0]);
            }

            order.setTableId(tableId);

            if (order.getOrderId() == 0) {
                orderBLL.addOrder(order);
                ToastNotification.showToast(this, "Tạo đơn hàng thành công! Bạn có thể thêm món.", ToastNotification.Type.SUCCESS);
                setTitle("Chi Tiết Đơn Hàng #" + order.getOrderId());
                isSaved = true;
            } else {
                orderBLL.updateOrder(order);
                ToastNotification.showToast(this, "Cập nhật bàn thành công!", ToastNotification.Type.SUCCESS);
                isSaved = true;
            }
            
            // Mark table as occupied
            if (tableId > 0) {
                tableBLL.updateStatus(tableId, "occupied");
            }
            
        } catch (ValidationException | DatabaseException ex) {
            ToastNotification.showToast(this, ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void loadOrderItems() {
        try {
            List<OrderItemDTO> items = orderBLL.getOrderItems(order.getOrderId());
            modItems.setRowCount(0);
            currentRawTotal = 0;
            for (OrderItemDTO item : items) {
                double amount = item.getQuantity() * item.getUnitPrice();
                currentRawTotal += amount;
                modItems.addRow(new Object[]{
                    item.getItemId(),
                    item.getItemName(),
                    CurrencyFormatter.formatVND(item.getUnitPrice()),
                    item.getQuantity(),
                    CurrencyFormatter.formatVND(amount)
                });
            }
            recalculateTotal();
        } catch (DatabaseException e) {
            // silent
        }
    }
    
    private void recalculateTotal() {
        try {
            int vat = (int) spinVat.getValue();
            int discount = (int) spinDiscount.getValue();
            
            double totalAfterDiscount = currentRawTotal * (1 - discount / 100.0);
            double finalTotal = totalAfterDiscount * (1 + vat / 100.0);
            
            // Save to order memory
            order.setTotalPrice(finalTotal);
            lblTotal.setText("Tổng: " + CurrencyFormatter.formatVND(finalTotal));
        } catch(Exception e){}
    }

    private void addOrderItem() {
        if (order.getOrderId() == 0) {
            ToastNotification.showToast(this, "Vui lòng bấm TẠO ĐƠN trước khi thêm món!", ToastNotification.Type.WARNING);
            return;
        }
        if (menuItems == null || menuItems.isEmpty()) return;
        
        try {
            int idx = cbAddItem.getSelectedIndex();
            ItemDTO selectedItem = menuItems.get(idx);
            int qty = (Integer) spinQty.getValue();
            
            OrderItemDTO item = new OrderItemDTO();
            item.setOrderId(order.getOrderId());
            item.setItemId(selectedItem.getItemId());
            item.setQuantity(qty);
            item.setUnitPrice(selectedItem.getItemPrice());
            
            orderBLL.addOrderItem(item);
            loadOrderItems();
            isSaved = true;
            
        } catch (ValidationException | DatabaseException ex) {
            ToastNotification.showToast(this, ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void removeOrderItem() {
        int row = tblItems.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(this, "Chọn món trong bảng để bớt!", ToastNotification.Type.WARNING);
            return;
        }
        
        int itemId = (int) tblItems.getValueAt(row, 0);
        try {
            orderBLL.removeOrderItem(order.getOrderId(), itemId);
            loadOrderItems();
            isSaved = true;
        } catch (DatabaseException ex) {
            ToastNotification.showToast(this, ex.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
