package com.inventory.ui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JFrame {
    private ProductDAO productDAO = new ProductDAO();

    public ReportPanel() {
        setTitle(" Inventory Reports");
        setSize(700, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Title
        JLabel title = new JLabel("Inventory Reports", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        // Report content
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        reportArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String report = generateReport();
        reportArea.setText(report);

        add(new JScrollPane(reportArea), BorderLayout.CENTER);

        // Refresh button
        JButton btnRefresh = new JButton(" Refresh Report");
        btnRefresh.addActionListener(e -> {
            reportArea.setText(generateReport());
        });
        add(btnRefresh, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append(" INVENTORY MANAGEMENT SYSTEM - REPORT\n");
        sb.append("========================================\n\n");

        // Total inventory value
        double totalValue = 0.0;
        int totalItems = 0;
        var products = productDAO.getAllProducts();
        for (Product p : products) {
            totalValue += p.getPrice() * p.getQuantity();
            totalItems += p.getQuantity();
        }
        sb.append(String.format(" Total Inventory Value: ₹%.2f\n", totalValue));
        sb.append(String.format(" Total Items in Stock: %d\n\n", totalItems));

        // Low stock items
        sb.append(" LOW STOCK ITEMS ( < 10 units ):\n");
        sb.append("----------------------------------------\n");
        var lowStock = productDAO.getLowStockProducts(10);
        if (lowStock.isEmpty()) {
            sb.append(" No low-stock items.\n");
        } else {
            for (Product p : lowStock) {
                sb.append(String.format("- %s (ID: %d): %d units @ ₹%.2f\n",
                    p.getName(), p.getProductId(), p.getQuantity(), p.getPrice()));
            }
        }
        sb.append("\n");

        // Top 5 products by quantity
        sb.append(" TOP 5 PRODUCTS BY QUANTITY:\n");
        sb.append("----------------------------------------\n");
        // Sort products by quantity (descending)
        products.sort((p1, p2) -> Integer.compare(p2.getQuantity(), p1.getQuantity()));
        int count = 0;
        for (Product p : products) {
            if (count >= 5) break;
            sb.append(String.format("- %s: %d units\n", p.getName(), p.getQuantity()));
            count++;
        }
        if (count == 0) {
            sb.append("No products found.\n");
        }

        sb.append("\nGenerated on: ").append(java.time.LocalDateTime.now().toString().substring(0, 19));
        return sb.toString();
    }
}