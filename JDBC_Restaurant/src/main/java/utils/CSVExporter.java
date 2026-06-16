package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public class CSVExporter {

    /**
     * Exports a JTable to a CSV file.
     * Uses UTF-8 with BOM so Excel opens Vietnamese characters correctly.
     */
    public static void exportTableToCSV(JTable table, String filePath) throws Exception {
        TableModel model = table.getModel();
        File file = new File(filePath);
        
        // Ensure path exists
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
            // Write UTF-8 BOM for MS Excel compatibility
            bw.write('\ufeff');

            // Write Headers
            for (int i = 0; i < model.getColumnCount(); i++) {
                bw.write(escapeCSV(model.getColumnName(i)));
                if (i < model.getColumnCount() - 1) {
                    bw.write(",");
                }
            }
            bw.newLine();

            // Write Data
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    Object cellValue = model.getValueAt(i, j);
                    String val = (cellValue == null) ? "" : cellValue.toString();
                    
                    // Simple text cleaning for badge panels (like StatusBadge)
                    if (cellValue instanceof javax.swing.JPanel) {
                        try {
                            javax.swing.JPanel p = (javax.swing.JPanel) cellValue;
                            val = ((javax.swing.JLabel)p.getComponent(0)).getText();
                        } catch(Exception e) {
                            val = "";
                        }
                    }

                    bw.write(escapeCSV(val));
                    if (j < model.getColumnCount() - 1) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
            bw.flush();
        }
    }

    /**
     * Escapes a string so it can be safely written to a CSV file.
     * Rules:
     * - If string contains comma, quote, or newline, enclose in double quotes.
     * - Any double quotes inside the string must be escaped with double quotes.
     */
    private static String escapeCSV(String data) {
        if (data == null) return "";
        data = data.replaceAll("\"", "\"\"");
        if (data.contains(",") || data.contains("\"") || data.contains("\n") || data.contains("\r")) {
            data = "\"" + data + "\"";
        }
        return data;
    }
}
