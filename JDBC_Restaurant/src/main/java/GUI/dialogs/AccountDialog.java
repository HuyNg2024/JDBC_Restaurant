package GUI.dialogs;

import BLL.AccountBLL;
import BLL.StaffBLL;
import DTO.AccountDTO;
import DTO.StaffDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AccountDialog extends JDialog {
    private AccountDTO account;
    private boolean isSaved = false;
    private AccountBLL accountBLL;
    private StaffBLL staffBLL;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cbRole;
    private JComboBox<String> cbStaff;
    private JComboBox<String> cbStatus;
    private List<StaffDTO> staffList;

    public AccountDialog(Frame owner, AccountDTO account) {
        super(owner, true);
        this.account = account;
        this.accountBLL = new AccountBLL();
        this.staffBLL = new StaffBLL();
        loadStaffList();
        initComponents();
        if (account != null) {
            loadData();
        }
    }

    private void loadStaffList() {
        try {
            staffList = staffBLL.getAllStaff();
        } catch (DatabaseException e) {
            // Ignore
        }
    }

    private void initComponents() {
        setTitle(account == null ? "Cấp Tài Khoản Mới" : "Cập Nhật Tài Khoản");
        setSize(400, 450);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(account == null ? "CẤP TÀI KHOẢN MỚI" : "CẬP NHẬT QUYỀN / TRẠNG THÁI");
        lblTitle.setFont(AppTheme.FONT_TITLE.deriveFont(18f));
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtUsername = createTextField();
        txtPassword = new JPasswordField();
        txtPassword.setFont(AppTheme.FONT_BODY);
        txtPassword.setBackground(AppTheme.BG_INPUT);
        txtPassword.setForeground(AppTheme.TEXT_PRIMARY);
        
        cbRole = new JComboBox<>(new String[]{"staff", "manager"});
        cbRole.setFont(AppTheme.FONT_BODY);
        
        cbStatus = new JComboBox<>(new String[]{"active", "inactive"});
        cbStatus.setFont(AppTheme.FONT_BODY);
        
        cbStaff = new JComboBox<>();
        cbStaff.addItem("Không liên kết");
        if (staffList != null) {
            for (StaffDTO s : staffList) {
                cbStaff.addItem("ID:" + s.getStaffId() + " - " + s.getStaffName());
            }
        }
        cbStaff.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Username (*)"));
        form.add(txtUsername);
        
        if (account == null) {
            form.add(createLabel("Mật khẩu (*)"));
            form.add(txtPassword);
        } else {
            // Cannot update password here, only role/status
            txtUsername.setEditable(false);
            form.add(createLabel("Username"));
            JLabel lblLocked = createLabel("Không thể đổi tên");
            lblLocked.setForeground(AppTheme.TEXT_SECONDARY);
            form.add(lblLocked);
        }
        
        form.add(createLabel("Nhân Viên"));
        form.add(cbStaff);
        form.add(createLabel("Quyền"));
        form.add(cbRole);
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
        txtUsername.setText(account.getUsername());
        cbRole.setSelectedItem(account.getRole());
        cbStatus.setSelectedItem(account.getStatus());
        
        if (account.getStaffId() > 0 && staffList != null) {
            for (int i = 0; i < staffList.size(); i++) {
                if (staffList.get(i).getStaffId() == account.getStaffId()) {
                    cbStaff.setSelectedIndex(i + 1);
                    break;
                }
            }
        }
    }

    private void save() {
        try {
            int staffId = 0;
            if (cbStaff.getSelectedIndex() > 0) {
                staffId = staffList.get(cbStaff.getSelectedIndex() - 1).getStaffId();
            }

            if (account == null) {
                // ADD NEW
                account = new AccountDTO();
                account.setUsername(txtUsername.getText().trim());
                account.setPassword(new String(txtPassword.getPassword()));
                account.setRole(cbRole.getSelectedItem().toString());
                account.setStatus(cbStatus.getSelectedItem().toString());
                account.setStaffId(staffId);

                accountBLL.addAccount(account);
                ToastNotification.showToast(getOwner(), "Tạo tài khoản thành công!", ToastNotification.Type.SUCCESS);
            } else {
                // UPDATE (Role, status, staff linked)
                account.setRole(cbRole.getSelectedItem().toString());
                account.setStatus(cbStatus.getSelectedItem().toString());
                account.setStaffId(staffId);

                accountBLL.updateAccount(account);
                ToastNotification.showToast(getOwner(), "Cập nhật tài khoản thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
            if (account != null && account.getAccountId() == 0) account = null; // Reset if add fails
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
