package GUI.dialogs;

import BLL.TabletopBLL;
import DTO.TabletopDTO;
import GUI.components.RoundedButton;
import GUI.components.ToastNotification;
import GUI.theme.AppTheme;
import exceptions.DatabaseException;
import exceptions.ValidationException;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class TableDialog extends JDialog {
    private TabletopDTO table;
    private boolean isSaved = false;
    private TabletopBLL tableBLL;

    private JTextField txtTableCode;
    private JTextField txtCapacity;
    private JTextField txtFloor;
    private JComboBox<String> cbStatus;

    public TableDialog(Frame owner, TabletopDTO table) {
        super(owner, true);
        this.table = table;
        this.tableBLL = new TabletopBLL();
        initComponents();
        if (table != null) {
            loadData();
        }
    }

    private void initComponents() {
        setTitle(table == null ? "Thêm Bàn Ăn" : "Cập Nhật Bàn Ăn");
        setSize(400, 380);
        setLocationRelativeTo(getOwner());
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(AppTheme.BG_PRIMARY);
        mainPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitle = new JLabel(table == null ? "THÊM BÀN ĂN" : "CẬP NHẬT BÀN ĂN");
        lblTitle.setFont(AppTheme.FONT_TITLE);
        lblTitle.setForeground(AppTheme.ACCENT);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Form Fields
        JPanel form = new JPanel(new GridLayout(4, 2, 10, 15));
        form.setBackground(AppTheme.BG_PRIMARY);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtTableCode = createTextField();
        txtCapacity = createTextField();
        txtFloor = createTextField();
        
        cbStatus = new JComboBox<>(new String[]{"empty", "reserved", "occupied"});
        cbStatus.setFont(AppTheme.FONT_BODY);

        form.add(createLabel("Mã Bàn (*)"));
        form.add(txtTableCode);
        form.add(createLabel("Sức Chứa (*)"));
        form.add(txtCapacity);
        form.add(createLabel("Tầng (*)"));
        form.add(txtFloor);
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
        txtTableCode.setText(table.getTableCode());
        txtCapacity.setText(String.valueOf(table.getCapacity()));
        txtFloor.setText(String.valueOf(table.getFloor()));
        cbStatus.setSelectedItem(table.getStatus());
    }

    private void save() {
        try {
            int capacity = txtCapacity.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtCapacity.getText().trim());
            int floor = txtFloor.getText().trim().isEmpty() ? 0 : Integer.parseInt(txtFloor.getText().trim());

            if (table == null) {
                table = new TabletopDTO();
            }

            table.setTableCode(txtTableCode.getText().trim());
            table.setCapacity(capacity);
            table.setFloor(floor);
            table.setStatus(cbStatus.getSelectedItem().toString());

            if (table.getTableId() == 0) {
                tableBLL.addTable(table);
                ToastNotification.showToast(getOwner(), "Thêm bàn ăn thành công!", ToastNotification.Type.SUCCESS);
            } else {
                tableBLL.updateTable(table);
                ToastNotification.showToast(getOwner(), "Cập nhật bàn ăn thành công!", ToastNotification.Type.SUCCESS);
            }
            
            isSaved = true;
            dispose();

        } catch (NumberFormatException e) {
            ToastNotification.showToast(this, "Sức chứa và Tầng phải là số!", ToastNotification.Type.ERROR);
        } catch (ValidationException | DatabaseException e) {
            ToastNotification.showToast(this, e.getMessage(), ToastNotification.Type.ERROR);
        }
    }

    public boolean isSaved() {
        return isSaved;
    }
}
