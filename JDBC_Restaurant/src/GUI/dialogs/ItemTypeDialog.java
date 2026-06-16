package GUI.dialogs;

import BLL.ItemTypeBLL;
import DTO.ItemTypeDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ItemTypeDialog extends JDialog {
    private ItemTypeDTO type;
    private boolean isSaved = false;
    private ItemTypeBLL typeBLL;

    private JTextField txtName;
    private JTextField txtDesc;

    public ItemTypeDialog(Frame owner, ItemTypeDTO type) {
        super(owner, true);
        this.type = type;
        this.typeBLL = new ItemTypeBLL();
        initComponents();
        if (type != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(type == null ? "Thêm Danh Mục" : "Cập Nhật Danh Mục");
        setSize(400, 300);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(type == null ? "THÊM DANH MỤC" : "CẬP NHẬT DANH MỤC");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(2, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtName = createTextField();
        txtDesc = createTextField();

        form.add(createLabel("Tên Danh Mục (*)"));
        form.add(txtName);
        form.add(createLabel("Mô tả"));
        form.add(txtDesc);

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
        txtName.setText(type.getTypeName());
        txtDesc.setText(type.getDescription());
    }

    private void save() {
        try {
            if (type == null) {
                type = new ItemTypeDTO();
            }

            type.setTypeName(txtName.getText().trim());
            type.setDescription(txtDesc.getText().trim());

            if (type.getTypeId() == 0) {
                typeBLL.addItemType(type);
                ToastNotification.showToast(getOwner(), "Thêm danh mục thành công!", ToastNotification.Type.SUCCESS);
            } else {
                typeBLL.updateItemType(type);
                ToastNotification.showToast(getOwner(), "Cập nhật danh mục thành công!", ToastNotification.Type.SUCCESS);
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
