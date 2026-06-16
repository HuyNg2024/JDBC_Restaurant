package GUI.dialogs;

import BLL.ItemBLL;
import BLL.ItemTypeBLL;
import DTO.ItemDTO;
import DTO.ItemTypeDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ItemDialog extends JDialog {
    private ItemDTO item;
    private boolean isSaved = false;
    private ItemBLL itemBLL;
    private ItemTypeBLL typeBLL;

    private JTextField txtName;
    private JTextField txtPrice;
    private JTextField txtDetail;
    private JTextField txtCalo;
    private JComboBox<String> cbType;
    private JComboBox<String> cbStatus;
    
    private List<ItemTypeDTO> types;

    public ItemDialog(Frame owner, ItemDTO item) {
        super(owner, true);
        this.item = item;
        this.itemBLL = new ItemBLL();
        this.typeBLL = new ItemTypeBLL();
        
        try { types = typeBLL.getAllItemTypes(); } catch(Exception e){}
        
        initComponents();
        if (item != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(item == null ? "Thêm Món Ăn" : "Cập Nhật Món Ăn");
        setSize(400, 480);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(item == null ? "THÊM MÓN ĂN" : "CẬP NHẬT MÓN ĂN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(6, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtName = createTextField();
        txtPrice = createTextField();
        txtDetail = createTextField();
        txtCalo = createTextField();
        
        cbType = new JComboBox<>();
        if (types != null) {
            for (ItemTypeDTO t : types) {
                cbType.addItem(t.getTypeName());
            }
        }
        cbType.setFont(AppTheme.FONT_BODY);
        
        cbStatus = new JComboBox<>(new String[]{"available", "unavailable"});
        cbStatus.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Tên Món (*)"));
        form.add(txtName);
        form.add(createLabel("Giá (VNĐ) (*)"));
        form.add(txtPrice);
        form.add(createLabel("Chi Tiết"));
        form.add(txtDetail);
        form.add(createLabel("Calories"));
        form.add(txtCalo);
        form.add(createLabel("Danh Mục (*)"));
        form.add(cbType);
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
        txtName.setText(item.getItemName());
        txtPrice.setText(String.format("%.0f", item.getItemPrice()));
        txtDetail.setText(item.getItemDetail());
        txtCalo.setText(String.valueOf(item.getCalories()));
        cbStatus.setSelectedItem(item.getStatus());
        
        if (types != null && item.getTypeId() > 0) {
            for (int i = 0; i < types.size(); i++) {
                if (types.get(i).getTypeId() == item.getTypeId()) {
                    cbType.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void save() {
        try {
            if (types == null || types.isEmpty()) {
                throw new ValidationException("Danh mục", "Chưa có danh mục món ăn nào! Vui lòng thêm danh mục trước.");
            }
            
            double price = txtPrice.getText().trim().isEmpty() ? 0 : Double.parseDouble(txtPrice.getText().trim());
            int calo = txtCalo.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtCalo.getText().trim());
            int typeId = types.get(cbType.getSelectedIndex()).getTypeId();

            if (item == null) {
                item = new ItemDTO();
            }

            item.setItemName(txtName.getText().trim());
            item.setItemPrice(price);
            item.setItemDetail(txtDetail.getText().trim());
            item.setCalories(calo);
            item.setTypeId(typeId);
            item.setStatus(cbStatus.getSelectedItem().toString());

            if (item.getItemId() == 0) {
                itemBLL.addItem(item);
                ToastNotification.showToast(getOwner(), "Thêm món ăn thành công!", ToastNotification.Type.SUCCESS);
            } else {
                itemBLL.updateItem(item);
                ToastNotification.showToast(getOwner(), "Cập nhật món ăn thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (NumberFormatException e) {
            ToastNotification.showToast(this, "Giá và Calories phải là số!", ToastNotification.Type.ERROR);
        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
