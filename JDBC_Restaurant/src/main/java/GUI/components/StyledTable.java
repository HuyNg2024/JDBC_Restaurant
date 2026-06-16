package GUI.components;

import GUI.theme.AppTheme;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StyledTable extends JTable {
    
    public StyledTable() {
        super();
        setupTable();
    }
    
    public StyledTable(DefaultTableModel model) {
        super(model);
        setupTable();
    }
    
    private void setupTable() {
        setRowHeight(AppTheme.TABLE_ROW_HEIGHT);
        setShowGrid(true);
        setGridColor(AppTheme.BORDER);
        setIntercellSpacing(new java.awt.Dimension(0, 0));
        setSelectionBackground(AppTheme.BG_HOVER);
        setSelectionForeground(AppTheme.TEXT_PRIMARY);
        setFont(AppTheme.FONT_BODY);
        
        getTableHeader().setReorderingAllowed(false);
        getTableHeader().setFont(AppTheme.FONT_SUBTITLE);
        getTableHeader().setBackground(AppTheme.BG_SECONDARY);
        getTableHeader().setForeground(AppTheme.TEXT_PRIMARY);
        
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Padding
                return c;
            }
        });
    }

    // Optional: make table cells non-editable by default
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
