package GUI.dialogs;

import BLL.DrinkBLL;
import DTO.DrinkDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class DrinkDialog extends JDialog {
    private DrinkDTO drink;
    private boolean isSaved = false;
    private DrinkBLL drinkBLL;

    private JTextField txtName;
    private JTextField txtPrice;
    private JComboBox<String> cbCategory;
    private JComboBox<String> cbStatus;

    public DrinkDialog(Frame owner, DrinkDTO drink) {
        super(owner, true);
        this.drink = drink;
        this.drinkBLL = new DrinkBLL();
        initComponents();
        if (drink != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(drink == null ? "Thêm Thức Uống" : "Cập Nhật Thức Uống");
        setSize(400, 380);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(drink == null ? "THÊM THỨC UỐNG" : "CẬP NHẬT THỨC UỐNG");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtName = createTextField();
        txtPrice = createTextField();
        
        cbCategory = new JComboBox<>(new String[]{"Coffee", "Tea", "Juice", "Soft drink", "Other"});
        cbCategory.setFont(AppTheme.FONT_BODY);
        
        cbStatus = new JComboBox<>(new String[]{"available", "unavailable"});
        cbStatus.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Tên (*)"));
        form.add(txtName);
        form.add(createLabel("Giá (VNĐ) (*)"));
        form.add(txtPrice);
        form.add(createLabel("Phân Loại"));
        form.add(cbCategory);
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
        txtName.setText(drink.getDrinkName());
        txtPrice.setText(String.format("%.0f", drink.getDrinkPrice()));
        cbCategory.setSelectedItem(drink.getCategory());
        cbStatus.setSelectedItem(drink.getStatus());
    }

    private void save() {
        try {
            double price = txtPrice.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtPrice.getText().trim());

            if (drink == null) {
                drink = new DrinkDTO();
            }

            drink.setDrinkName(txtName.getText().trim());
            drink.setDrinkPrice(price);
            drink.setCategory(cbCategory.getSelectedItem().toString());
            drink.setStatus(cbStatus.getSelectedItem().toString());

            if (drink.getDrinkId() == 0) {
                drinkBLL.addDrink(drink);
                ToastNotification.showToast(getOwner(), "Thêm thức uống thành công!", ToastNotification.Type.SUCCESS);
            } else {
                drinkBLL.updateDrink(drink);
                ToastNotification.showToast(getOwner(), "Cập nhật thức uống thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (NumberFormatException e) {
            ToastNotification.showToast(this, "Giá phải là số!", ToastNotification.Type.ERROR);
        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
