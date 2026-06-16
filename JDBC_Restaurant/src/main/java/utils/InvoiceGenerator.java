package utils;

import DTO.OrderDTO;
import DTO.OrderItemDTO;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class InvoiceGenerator {

    public static void generateAndOpenInvoice(OrderDTO order, List<OrderItemDTO> items, String customerName, String tableName) throws Exception {
        // Create temporary html file
        File tempFile = File.createTempFile("invoice_order_" + order.getOrderId() + "_", ".html");
        
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html><head><meta charset='UTF-8'>");
        html.append("<title>Hóa Đơn #").append(order.getOrderId()).append("</title>");
        html.append("<style>");
        html.append("body { font-family: 'Courier New', Courier, monospace; width: 350px; margin: 0 auto; color: #000; }");
        html.append("h2, h3, p { text-align: center; margin: 5px 0; }");
        html.append("table { width: 100%; border-collapse: collapse; margin-top: 15px; }");
        html.append("th, td { text-align: left; padding: 5px 0; border-bottom: 1px dashed #000; }");
        html.append("th { font-weight: bold; }");
        html.append(".right { text-align: right; }");
        html.append(".center { text-align: center; }");
        html.append(".total-row { font-size: 1.2em; font-weight: bold; border-top: 2px solid #000; }");
        html.append(".footer { margin-top: 20px; font-size: 0.9em; border-top: 1px dashed #000; padding-top: 10px;}");
        html.append("</style></head><body>");

        // Header
        html.append("<h2>NHÀ HÀNG CAO CẤP</h2>");
        html.append("<p>123 Đường Tôn Đức Thắng, TP.HCM</p>");
        html.append("<p>SĐT: 0123.456.789</p>");
        html.append("<h3>PHIẾU THANH TOÁN</h3>");
        
        // Info
        DateFormatter df = new DateFormatter();
        html.append("<p style='text-align:left; margin-top:15px;'>Mã Đơn: #").append(order.getOrderId()).append("</p>");
        html.append("<p style='text-align:left;'>Ngày: ").append(df.formatDateTime(java.time.LocalDateTime.now())).append("</p>");
        if(tableName != null && !tableName.isEmpty()) {
            html.append("<p style='text-align:left;'>Bàn: ").append(tableName).append("</p>");
        }
        if(customerName != null && !customerName.isEmpty()) {
            html.append("<p style='text-align:left;'>Khách hàng: ").append(customerName).append("</p>");
        }

        // Items Table
        html.append("<table>");
        html.append("<tr><th>Món</th><th class='center'>SL</th><th class='right'>TTiền</th></tr>");
        
        for (OrderItemDTO item : items) {
            html.append("<tr>");
            html.append("<td>").append(item.getItemName()).append("<br><small>")
                .append(CurrencyFormatter.formatVND(item.getUnitPrice())).append("</small></td>");
            html.append("<td class='center'>").append(item.getQuantity()).append("</td>");
            html.append("<td class='right'>").append(CurrencyFormatter.formatVND(item.getUnitPrice() * item.getQuantity())).append("</td>");
            html.append("</tr>");
        }

        // Totals
        // Discount/VAT could be added here in the future
        html.append("<tr><td colspan='2' style='padding-top:10px;'>Cộng tiền hàng:</td><td class='right' style='padding-top:10px;'>")
            .append(CurrencyFormatter.formatVND(order.getTotalPrice())).append("</td></tr>");
            
        double vat = order.getTotalPrice() * 0.08; // Example 8% VAT
        double finalTotal = order.getTotalPrice() + vat;

        html.append("<tr><td colspan='2'>Thuế VAT (8%):</td><td class='right'>")
            .append(CurrencyFormatter.formatVND(vat)).append("</td></tr>");

        html.append("<tr class='total-row'><td colspan='2' style='padding-top:10px;'>TỔNG CỘNG:</td><td class='right' style='padding-top:10px;'>")
            .append(CurrencyFormatter.formatVND(finalTotal)).append("</td></tr>");
            
        html.append("</table>");

        // Footer
        html.append("<div class='footer'>");
        html.append("<p>Cảm ơn quý khách và hẹn gặp lại!</p>");
        html.append("<p>Password Wifi: nhahang123</p>");
        html.append("</div>");
        
        // Auto Print Script
        html.append("<script>window.onload = function() { window.print(); }</script>");

        html.append("</body></html>");

        // Write to file
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile), "UTF-8"))) {
            writer.write(html.toString());
        }

        // Open in browser
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(tempFile.toURI());
        } else {
            System.out.println("Desktop browsing is not supported. File saved at: " + tempFile.getAbsolutePath());
        }
    }
}
