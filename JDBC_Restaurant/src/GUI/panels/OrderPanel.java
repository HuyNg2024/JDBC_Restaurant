package GUI.panels;

import BLL.OrderBLL;
import BLL.TransactionBLL;
import DTO.OrderDTO;
import DTO.TransactionDTO;
import GUI.components.RoundedButton;
import GUI.components.SearchBar;
import GUI.components.StatusBadge;
import GUI.components.StyledTable;
import GUI.components.ToastNotification;
import GUI.dialogs.OrderDialog;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import utils.CurrencyFormatter;
import utils.DateFormatter;

public class OrderPanel extends JPanel {
    private OrderBLL orderBLL;
    private TransactionBLL transBLL;
    private StyledTable table;
    private DefaultTableModel tableModel;
    private SearchBar searchBar;
    private DateFormatter df = new DateFormatter();

    public OrderPanel() {
        orderBLL = new OrderBLL();
        transBLL = new TransactionBLL();
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
        
        JLabel lblTitle = new JLabel("QUẢN LÝ ĐƠN HÀNG");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.TEXT_PRIMARY);
        
        searchBar = new SearchBar("Tìm mã đơn, SĐT KH...");
        searchBar.getTextField().getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { handleSearch(); }
            public void removeUpdate(DocumentEvent e) { handleSearch(); }
            public void changedUpdate(DocumentEvent e) { handleSearch(); }
        });

        RoundedButton btnAdd = new RoundedButton("TẠO ĐƠN MỚI (POS)", AppTheme.ACCENT, AppTheme.BG_PRIMARY);
        btnAdd.setPreferredSize(new Dimension(180, AppTheme.BUTTON_HEIGHT));
        btnAdd.addActionListener(e -> showOrderDialog(null));

        JPanel pnlHeaderRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        pnlHeaderRight.setBackground(AppTheme.BG_PRIMARY);
        pnlHeaderRight.add(searchBar);
        pnlHeaderRight.add(btnAdd);

        pnlHeader.add(lblTitle, BorderLayout.WEST);
        pnlHeader.add(pnlHeaderRight, BorderLayout.EAST);

        // --- TABLE ---
        String[] columns = {"Mã Đơn", "Bàn", "Khách Hàng", "Tổng Tiền", "Ngày Tạo", "Trạng Thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new StyledTable(tableModel);
        
        table.getColumnModel().getColumn(5).setCellRenderer((t, value, isSelected, hasFocus, row, column) -> {
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
        
        RoundedButton btnExport = new RoundedButton("XUẤT BÁO CÁO", AppTheme.ACCENT_PURPLE, AppTheme.BG_PRIMARY);
        RoundedButton btnPay = new RoundedButton("THANH TOÁN ĐƠN", AppTheme.ACCENT_GREEN, AppTheme.BG_PRIMARY);
        RoundedButton btnEdit = new RoundedButton("CHI TIẾT MÓN", AppTheme.ACCENT_YELLOW, AppTheme.BG_PRIMARY);
        RoundedButton btnDelete = new RoundedButton("HỦY ĐƠN", AppTheme.ACCENT_RED, AppTheme.BG_PRIMARY);
        RoundedButton btnRefresh = new RoundedButton("LÀM MỚI", AppTheme.BORDER, AppTheme.TEXT_PRIMARY);
        
        btnExport.addActionListener(e -> handleExport());
        btnPay.addActionListener(e -> handlePay());
        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
        btnRefresh.addActionListener(e -> loadData());
        
        pnlActions.add(btnExport);
        pnlActions.add(btnRefresh);
        pnlActions.add(btnEdit);
        pnlActions.add(btnPay);
        pnlActions.add(btnDelete);

        add(pnlHeader, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(pnlActions, BorderLayout.SOUTH);
    }

    public void loadData() {
        try {
            List<OrderDTO> list = orderBLL.getAllOrders();
            updateTable(list);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleSearch() {
        try {
            String keyword = searchBar.getText();
            List<OrderDTO> list = orderBLL.searchOrders(keyword);
            updateTable(list);
        } catch (DatabaseException e) {
            // silent
        }
    }

    private void updateTable(List<OrderDTO> list) {
        tableModel.setRowCount(0);
        for (OrderDTO order : list) {
            tableModel.addRow(new Object[]{
                order.getOrderId(),
                order.getTableId() > 0 ? "Bàn " + order.getTableId() : "Mang về",
                order.getCustomerId() > 0 ? "KH-" + order.getCustomerId() : "Vãng lai",
                CurrencyFormatter.formatVND(order.getTotalPrice()),
                order.getCreatedAt() != null ? df.formatDateTime(order.getCreatedAt()) : "",
                order.getStatus()
            });
        }
    }

    private void showOrderDialog(OrderDTO order) {
        Window parent = SwingUtilities.getWindowAncestor(this);
        OrderDialog dialog = new OrderDialog((Frame) parent, order);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadData();
        }
    }

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn đơn hàng!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            OrderDTO order = orderBLL.getOrderById(id);
            if ("completed".equals(order.getStatus()) || "cancelled".equals(order.getStatus())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Đơn hàng đã chốt, không thể thay đổi món!", ToastNotification.Type.WARNING);
                return;
            }
            showOrderDialog(order);
        } catch (DatabaseException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handlePay() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn đơn hàng cần thanh toán!", ToastNotification.Type.WARNING);
            return;
        }

        try {
            int id = (int) table.getValueAt(row, 0);
            OrderDTO order = orderBLL.getOrderById(id);
            
            if ("completed".equals(order.getStatus())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Đơn hàng này đã thanh toán rồi!", ToastNotification.Type.INFO);
                return;
            }
            if ("cancelled".equals(order.getStatus())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không thể thanh toán đơn hàng đã bị hủy!", ToastNotification.Type.WARNING);
                return;
            }
            
            String[] methods = {"Tiền mặt (Cash)", "Thẻ (Card)", "Chuyển khoản (Transfer)"};
            int choice = JOptionPane.showOptionDialog(this,
                    "Chọn phương thức thanh toán cho đơn hàng #" + id + "\nTổng tiền: " + CurrencyFormatter.formatVND(order.getTotalPrice()),
                    "Thanh toán",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, methods, methods[0]);
                    
            if (choice >= 0) {
                String method = choice == 0 ? "cash" : choice == 1 ? "card" : "transfer";
                
                // Create Transaction
                TransactionDTO trans = new TransactionDTO();
                trans.setOrderId(id);
                trans.setCustomerId(order.getCustomerId());
                trans.setAmount(order.getTotalPrice());
                trans.setPaymentMethod(method);
                trans.setStatus("completed");
                trans.setNote("Thanh toán tại quầy");
                
                transBLL.addTransaction(trans);
                
                // Update Order Status
                order.setStatus("completed");
                orderBLL.updateOrder(order);
                
                // Free up table if needed
                if (order.getTableId() > 0) {
                    new BLL.TabletopBLL().updateStatus(order.getTableId(), "empty");
                }
                
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Thanh toán thành công! Đang in hóa đơn...", ToastNotification.Type.SUCCESS);
                loadData();
                
                // Print Invoice
                try {
                    List<DTO.OrderItemDTO> items = orderBLL.getOrderItems(id);
                    String cusName = order.getCustomerId() > 0 ? "KH-" + order.getCustomerId() : "Khách vãng lai";
                    String tabName = order.getTableId() > 0 ? "Bàn " + order.getTableId() : "Mang về";
                    
                    if (order.getCustomerId() > 0) {
                        try {
                            DTO.CustomerDTO cus = new BLL.CustomerBLL().getCustomerById(order.getCustomerId());
                            if(cus != null) cusName = cus.getFullName();
                        } catch(Exception ignored){}
                    }
                    
                    utils.InvoiceGenerator.generateAndOpenInvoice(order, items, cusName, tabName);
                } catch (Exception ex) {
                    ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Lỗi in hóa đơn: " + ex.getMessage(), ToastNotification.Type.ERROR);
                }
            }

        } catch (DatabaseException | ValidationException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row < 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Vui lòng chọn đơn hàng cần hủy!", ToastNotification.Type.WARNING);
            return;
        }
        
        try {
            int id = (int) table.getValueAt(row, 0);
            OrderDTO order = orderBLL.getOrderById(id);
            if ("completed".equals(order.getStatus())) {
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không thể hủy đơn hàng đã thanh toán!", ToastNotification.Type.WARNING);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn HỦY đơn hàng này?", "Xác nhận hủy", 
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                order.setStatus("cancelled");
                orderBLL.updateOrder(order);
                ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Đã hủy đơn hàng!", ToastNotification.Type.SUCCESS);
                loadData();
            }
        } catch (DatabaseException | exceptions.ValidationException e) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    private void handleExport() {
        if (tableModel.getRowCount() == 0) {
            ToastNotification.showToast(SwingUtilities.getWindowAncestor(this), "Không có dữ liệu để xuất!", ToastNotification.Type.WARNING);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Lưu Báo Cáo Doanh Thu (CSV)");
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
