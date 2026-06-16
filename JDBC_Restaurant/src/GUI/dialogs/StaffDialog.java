package GUI.dialogs;

import BLL.StaffBLL;
import DTO.StaffDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class StaffDialog extends JDialog {
    private StaffDTO staff;
    private boolean isSaved = false;
    private StaffBLL staffBLL;

    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtSalary;
    private JTextField txtExp;
    private JComboBox<String> cbJob;
    private JComboBox<String> cbStatus;

    public StaffDialog(Frame owner, StaffDTO staff) {
        super(owner, true);
        this.staff = staff;
        this.staffBLL = new StaffBLL();
        initComponents();
        if (staff != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(staff == null ? "Thêm Nhân Viên Mới" : "Cập Nhật Nhân Viên");
        setSize(400, 500);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(staff == null ? "THÊM NHÂN VIÊN" : "CẬP NHẬT NHÂN VIÊN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtName = createTextField();
        txtPhone = createTextField();
        txtSalary = createTextField();
        txtExp = createTextField();
        
        cbJob = new JComboBox<>(new String[]{"Manager", "Chef", "Waiter", "Cashier"});
        cbJob.setFont(AppTheme.FONT_BODY);
        
        cbStatus = new JComboBox<>(new String[]{"active", "inactive"});
        cbStatus.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Họ và Tên (*)"));
        form.add(txtName);
        form.add(createLabel("Số Điện Thoại (*)"));
        form.add(txtPhone);
        form.add(createLabel("Lương cơ bản (*)"));
        form.add(txtSalary);
        form.add(createLabel("Kinh nghiệm (Năm)"));
        form.add(txtExp);
        form.add(createLabel("Vị trí"));
        form.add(cbJob);
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
        txtName.setText(staff.getStaffName());
        txtPhone.setText(staff.getPhone());
        txtSalary.setText(String.format("%.0f", staff.getSalary()));
        txtExp.setText(String.valueOf(staff.getWorkYears()));
        cbJob.setSelectedItem(staff.getJob());
        cbStatus.setSelectedItem(staff.getStatus());
    }

    private void save() {
        try {
            double salary = txtSalary.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtSalary.getText().trim());
            int exp = txtExp.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtExp.getText().trim());

            if (staff == null) {
                staff = new StaffDTO();
            }

            staff.setStaffName(txtName.getText().trim());
            staff.setPhone(txtPhone.getText().trim());
            staff.setSalary(salary);
            staff.setWorkYears(exp);
            staff.setJob(cbJob.getSelectedItem().toString());
            staff.setStatus(cbStatus.getSelectedItem().toString());

            if (staff.getStaffId() == 0) {
                staffBLL.addStaff(staff);
                ToastNotification.showToast(getOwner(), "Thêm nhân viên thành công!", ToastNotification.Type.SUCCESS);
            } else {
                staffBLL.updateStaff(staff);
                ToastNotification.showToast(getOwner(), "Cập nhật nhân viên thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (NumberFormatException e) {
            ToastNotification.showToast(this, "Lương và Kinh nghiệm phải là số!", ToastNotification.Type.ERROR);
        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
