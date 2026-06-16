package GUI.components;

import GUI.theme.AppTheme;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Map;
import javax.swing.JPanel;

public class BarChart extends JPanel {
    private Map<String, Double> data;
    private String title;

    public BarChart(String title, Map<String, Double> data) {
        this.title = title;
        this.data = data;
        setOpaque(false);
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 40;
        int width = getWidth();
        int height = getHeight();

        // Draw background
        g2.setColor(AppTheme.BG_CARD);
        g2.fillRoundRect(0, 0, width, height, 15, 15);

        // Draw title
        g2.setColor(AppTheme.TEXT_PRIMARY);
        g2.setFont(AppTheme.FONT_SUBTITLE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(title, padding, padding);

        // Find max value to scale bars
        double max = 0;
        for (Double value : data.values()) {
            if (value > max) max = value;
        }
        if (max == 0) max = 1; // Prevent division by zero

        int numBars = data.size();
        int availableWidth = width - 2 * padding;
        int barWidth = (availableWidth / numBars) - 20;
        if (barWidth > 60) barWidth = 60; // Max bar width

        int graphHeight = height - 2 * padding - 30; // Space for labels
        int xOffset = padding + 10;

        // Draw axes
        g2.setColor(AppTheme.BORDER);
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X axis
        g2.drawLine(padding, padding + 20, padding, height - padding); // Y axis

        // Draw bars
        g2.setFont(AppTheme.FONT_SMALL);
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            String label = entry.getKey();
            Double value = entry.getValue();

            int barHeight = (int) ((value / max) * graphHeight);
            int yOffset = height - padding - barHeight;

            // Bar shadow
            g2.setColor(AppTheme.BG_SECONDARY);
            g2.fillRect(xOffset + 2, yOffset + 2, barWidth, barHeight);

            // Bar
            g2.setColor(AppTheme.ACCENT);
            g2.fillRect(xOffset, yOffset, barWidth, barHeight);

            // Value label
            g2.setColor(AppTheme.TEXT_PRIMARY);
            String valStr = String.format("%,.0f", value);
            int strWidth = fm.stringWidth(valStr);
            g2.drawString(valStr, xOffset + (barWidth - strWidth) / 2, yOffset - 5);

            // X-axis label
            g2.setColor(AppTheme.TEXT_SECONDARY);
            strWidth = fm.stringWidth(label);
            g2.drawString(label, xOffset + (barWidth - strWidth) / 2, height - padding + 15);

            xOffset += barWidth + 20;
        }
    }
}
