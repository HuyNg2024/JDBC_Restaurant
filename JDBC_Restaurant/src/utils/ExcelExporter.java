package utils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Exports JTable data to Excel-compatible CSV file.
 * Opens a file chooser dialog for the user to select save location.
 */
public class ExcelExporter {

    /**
     * Export a JTable to a .csv file (Excel-compatible).
     * Shows a file chooser dialog and saves the table data.
     *
     * @param table the JTable to export
     * @param parentComponent the parent component for the dialog
     */
    public static void export(JTable table, java.awt.Component parentComponent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Xuất dữ liệu ra Excel");
        fileChooser.setSelectedFile(new File("export.csv"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "CSV Files (*.csv)", "csv"));

        int result = fileChooser.showSaveDialog(parentComponent);
        if (result != JFileChooser.APPROVE_OPTION) return;

        File file = fileChooser.getSelectedFile();
        if (!file.getName().endsWith(".csv")) {
            file = new File(file.getAbsolutePath() + ".csv");
        }

        try (FileWriter writer = new FileWriter(file)) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Write BOM for Excel UTF-8 compatibility
            writer.write('\ufeff');

            // Write headers
            for (int col = 0; col < model.getColumnCount(); col++) {
                if (col > 0) writer.write(",");
                writer.write(escapeCSV(model.getColumnName(col)));
            }
            writer.write("\n");

            // Write data rows
            for (int row = 0; row < model.getRowCount(); row++) {
                for (int col = 0; col < model.getColumnCount(); col++) {
                    if (col > 0) writer.write(",");
                    Object value = model.getValueAt(row, col);
                    writer.write(escapeCSV(value != null ? value.toString() : ""));
                }
                writer.write("\n");
            }

            JOptionPane.showMessageDialog(parentComponent,
                    "Xuất dữ liệu thành công!\nFile: " + file.getAbsolutePath(),
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentComponent,
                    "Lỗi khi xuất dữ liệu: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Escape a value for CSV format.
     */
    private static String escapeCSV(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
