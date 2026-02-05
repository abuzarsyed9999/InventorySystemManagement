package com.inventory.ui;

import javax.swing.*;
import java.awt.*;

public class InventoryApp extends JFrame {
    public InventoryApp() {
        setTitle(" Inventory Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel();
        header.setBackground(new Color(25, 118, 210));
        header.setPreferredSize(new Dimension(900, 80));
        JLabel title = new JLabel("Inventory Management System", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);
        add(header, BorderLayout.NORTH);

        // Buttons
        JPanel buttons = new JPanel(new GridLayout(2, 2, 30, 30));
        buttons.setBorder(BorderFactory.createEmptyBorder(50, 70, 50, 70));

        JButton btnProducts = createButton(" Products", new Color(41, 128, 185));
        btnProducts.addActionListener(e -> new ProductPanel().setVisible(true));
        buttons.add(btnProducts);

        JButton btnSuppliers = createButton(" Suppliers", new Color(39, 174, 96));
        btnSuppliers.addActionListener(e -> new SupplierPanel().setVisible(true));
        buttons.add(btnSuppliers);

        JButton btnOrders = createButton(" Orders", new Color(192, 57, 43));
        btnOrders.addActionListener(e -> new OrderPanel().setVisible(true));
        buttons.add(btnOrders);

        JButton btnReports = createButton(" Reports", new Color(155, 89, 182));
        btnReports.addActionListener(e -> new ReportPanel().setVisible(true));
        buttons.add(btnReports);

        add(buttons, BorderLayout.CENTER);

        JLabel footer = new JLabel(" 2026 | Inventory Management System | SYED ABUZAR", JLabel.CENTER);
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(footer, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new InventoryApp();
        });
    }
}