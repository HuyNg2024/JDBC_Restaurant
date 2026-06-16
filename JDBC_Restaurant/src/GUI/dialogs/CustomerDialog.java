package GUI.dialogs;

import BLL.CustomerBLL;
import DTO.CustomerDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class CustomerDialog extends JDialog {
    private CustomerDTO customer;
    private boolean isSaved = false;
    private CustomerBLL customerBLL;

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtAddress;

    public CustomerDialog(Frame owner, CustomerDTO customer) {
        super(owner, true);
        this.customer = customer;
        this.customerBLL = new CustomerBLL();
        initComponents();
        if (customer != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(customer == null ? "Thêm Khách Hàng" : "Cập Nhật Khách Hàng");
        setSize(400, 450);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(customer == null ? "THÊM KHÁCH HÀNG" : "CẬP NHẬT KHÁCH HÀNG");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(5, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtFirstName = createTextField();
        txtLastName = createTextField();
        txtPhone = createTextField();
        txtEmail = createTextField();
        txtAddress = createTextField();

        form.add(createLabel("Họ đệm (*)"));
        form.add(txtFirstName);
        form.add(createLabel("Tên (*)"));
        form.add(txtLastName);
        form.add(createLabel("Số Điện Thoại (*)"));
        form.add(txtPhone);
        form.add(createLabel("Email"));
        form.add(txtEmail);
        form.add(createLabel("Địa Chỉ"));
        form.add(txtAddress);

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
        txtFirstName.setText(customer.getFirstName());
        txtLastName.setText(customer.getLastName());
        txtPhone.setText(customer.getPhone());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getAddress());
    }

    private void save() {
        try {
            if (customer == null) {
                customer = new CustomerDTO();
                customer.setTotalVisits(0);
            }

            customer.setFirstName(txtFirstName.getText().trim());
            customer.setLastName(txtLastName.getText().trim());
            customer.setPhone(txtPhone.getText().trim());
            customer.setEmail(txtEmail.getText().trim());
            customer.setAddress(txtAddress.getText().trim());

            if (customer.getCustomerId() == 0) {
                customerBLL.addCustomer(customer);
                ToastNotification.showToast(getOwner(), "Thêm khách hàng thành công!", ToastNotification.Type.SUCCESS);
            } else {
                customerBLL.updateCustomer(customer);
                ToastNotification.showToast(getOwner(), "Cập nhật khách hàng thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
