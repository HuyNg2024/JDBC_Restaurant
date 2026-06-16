package GUI.dialogs;

import BLL.CustomerBLL;
import BLL.ReserveBLL;
import BLL.TabletopBLL;
import DTO.CustomerDTO;
import DTO.ReserveDTO;
import DTO.TabletopDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ReserveDialog extends JDialog {
    private ReserveDTO reserve;
    private boolean isSaved = false;
    private ReserveBLL reserveBLL;
    private CustomerBLL customerBLL;
    private TabletopBLL tableBLL;

    private JComboBox<String> cbCustomer;
    private JComboBox<String> cbTable;
    private JTextField txtTime;
    private JTextField txtGuestCount;
    private JTextField txtNote;
    private JComboBox<String> cbStatus;
    
    private List<CustomerDTO> customers;
    private List<TabletopDTO> tables;

    public ReserveDialog(Frame owner, ReserveDTO reserve) {
        super(owner, true);
        this.reserve = reserve;
        this.reserveBLL = new ReserveBLL();
        this.customerBLL = new CustomerBLL();
        this.tableBLL = new TabletopBLL();
        loadDependencies();
        initComponents();
        if (reserve != null) {
            loadData();
        }
    }

    private void loadDependencies() {
        try {
            customers = customerBLL.getAllCustomers();
            tables = tableBLL.getAllTables();
        } catch (DatabaseException e) {
            // Ignore
        }
    }

    private void initComponents() {
        setTitle(reserve == null ? "Thêm Lịch Đặt Bàn" : "Cập Nhật Đặt Bàn");
        setSize(450, 500);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(reserve == null ? "THÊM LỊCH ĐẶT BÀN" : "CẬP NHẬT ĐẶT BÀN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        cbCustomer = new JComboBox<>();
        if (customers != null) {
            for (CustomerDTO c : customers) {
                cbCustomer.addItem("ID:" + c.getCustomerId() + " - " + c.getFullName());
            }
        }
        cbCustomer.setFont(AppTheme.FONT_BODY);

        cbTable = new JComboBox<>();
        cbTable.addItem("Chưa xếp bàn");
        if (tables != null) {
            for (TabletopDTO t : tables) {
                cbTable.addItem("ID:" + t.getTableId() + " - Bàn " + t.getTableCode());
            }
        }
        cbTable.setFont(AppTheme.FONT_BODY);

        txtTime = createTextField();
        if (reserve == null) {
            txtTime.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        
        txtGuestCount = createTextField();
        txtNote = createTextField();
        
        cbStatus = new JComboBox<>(new String[]{"pending", "confirmed", "completed", "cancelled"});
        cbStatus.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Khách Hàng (*)"));
        form.add(cbCustomer);
        form.add(createLabel("Bàn"));
        form.add(cbTable);
        form.add(createLabel("Thời gian (yyyy-MM-dd HH:mm:ss)"));
        form.add(txtTime);
        form.add(createLabel("Số lượng khách (*)"));
        form.add(txtGuestCount);
        form.add(createLabel("Ghi chú"));
        form.add(txtNote);
        form.add(createLabel("Trạng thái"));
        form.add(cbStatus);

        // Buttons
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        pnlBtns.setBackground(AppTheme.BG_PRIMARY);

        RoundedButton btnSave = new RoundedButton("LƯU", AppTheme.ACCENT_GREEN, AppTheme.BG_PRIMARY);
        btnSave.setPreferredSize(new Dimension(120, 35));
        btnSave.addActionListener(e -> save());

        RoundedButton btnCancel = new RoundedButton("HỦY", AppTheme.BORDER, AppTheme.TEXT_PRIMARY);
        btnCancel.setPreferredSize(new Dimension(120, 35));
        btnCancel.addActionListener(e -> dispose());

        pnlBtns.add(btnCancel);
        pnlBtns.add(btnSave);

        mainPanel.add(lblTitle);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(form);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(pnlBtns);

        setContentPane(mainPanel);
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(AppTheme.FONT_BODY);
        lbl.setForeground(AppTheme.TEXT_PRIMARY);
        return lbl;
    }

    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(AppTheme.FONT_BODY);
        txt.setBackground(AppTheme.BG_INPUT);
        txt.setForeground(AppTheme.TEXT_PRIMARY);
        txt.setCaretColor(AppTheme.TEXT_PRIMARY);
        return txt;
    }

    private void loadData() {
        if (customers != null && reserve.getCustomerId() > 0) {
            for (int i = 0; i < customers.size(); i++) {
                if (customers.get(i).getCustomerId() == reserve.getCustomerId()) {
                    cbCustomer.setSelectedIndex(i);
                    break;
                }
            }
        }
        
        if (tables != null && reserve.getTableId() > 0) {
            for (int i = 0; i < tables.size(); i++) {
                if (tables.get(i).getTableId() == reserve.getTableId()) {
                    cbTable.setSelectedIndex(i + 1); // +1 because 0 is "Chưa xếp bàn"
                    break;
                }
            }
        }
        
        txtTime.setText(reserve.getReserveTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        txtGuestCount.setText(String.valueOf(reserve.getGuestCount()));
        txtNote.setText(reserve.getNote());
        cbStatus.setSelectedItem(reserve.getStatus());
    }

    private void save() {
        try {
            int customerId = customers.get(cbCustomer.getSelectedIndex()).getCustomerId();
            
            int tableId = 0;
            if (cbTable.getSelectedIndex() > 0) {
                tableId = tables.get(cbTable.getSelectedIndex() - 1).getTableId();
            }
            
            int guests = txtGuestCount.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtGuestCount.getText().trim());
            LocalDateTime time;
            try {
                time = LocalDateTime.parse(txtTime.getText().trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } catch (Exception ex) {
                throw new ValidationException("Thời gian", "Định dạng thời gian không hợp lệ. Vui lòng nhập chuẩn yyyy-MM-dd HH:mm:ss");
            }

            if (reserve == null) {
                reserve = new ReserveDTO();
            }

            reserve.setCustomerId(customerId);
            reserve.setTableId(tableId);
            reserve.setReserveTime(time);
            reserve.setGuestCount(guests);
            reserve.setNote(txtNote.getText().trim());
            reserve.setStatus(cbStatus.getSelectedItem().toString());

            if (reserve.getReserveId() == 0) {
                reserveBLL.addReservation(reserve);
                ToastNotification.showToast(getOwner(), "Thêm đặt bàn thành công!", ToastNotification.Type.SUCCESS);
            } else {
                reserveBLL.updateReservation(reserve);
                ToastNotification.showToast(getOwner(), "Cập nhật đặt bàn thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (NumberFormatException e) {
            ToastNotification.showToast(this, "Số lượng khách phải là số nguyên!", ToastNotification.Type.ERROR);
        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        } catch (Exception e) {
            ToastNotification.showToast(this, "Lỗi: " + e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
