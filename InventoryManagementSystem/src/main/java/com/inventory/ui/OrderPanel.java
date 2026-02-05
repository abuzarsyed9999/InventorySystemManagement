package com.inventory.ui;

import com.inventory.dao.OrderDAO;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Order;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class OrderPanel extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private OrderDAO orderDAO = new OrderDAO();
    private ProductDAO productDAO = new ProductDAO();

    public OrderPanel() {
        setTitle(" Orders Management");
        setSize(900, 550);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"Order ID", "Product ID", "Quantity", "Type", "Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadAllOrders();

        // Top panel: Type filter and Refresh
        JPanel topPanel = new JPanel(new FlowLayout());
        JButton btnPurchases = new JButton(" Purchases");
        JButton btnSales = new JButton(" Sales");
        JButton btnAll = new JButton(" All Orders");

        btnPurchases.addActionListener(e -> loadOrdersByType("PURCHASE"));
        btnSales.addActionListener(e -> loadOrdersByType("SALE"));
        btnAll.addActionListener(e -> loadAllOrders());

        topPanel.add(btnPurchases);
        topPanel.add(btnSales);
        topPanel.add(btnAll);

        // Bottom panel: Action buttons
        JPanel bottomPanel = new JPanel();
        JButton btnNewPurchase = new JButton(" New Purchase");
        JButton btnNewSale = new JButton(" New Sale");

        btnNewPurchase.addActionListener(e -> createOrder("PURCHASE"));
        btnNewSale.addActionListener(e -> createOrder("SALE"));

        bottomPanel.add(btnNewPurchase);
        bottomPanel.add(btnNewSale);

        // Assemble UI
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadAllOrders() {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getAllOrders();
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getOrderId(),
                o.getProductId(),
                o.getQuantity(),
                o.getOrderType(),
                o.getOrderDate() != null ? o.getOrderDate().toString().substring(0, 19) : "N/A",
                o.getStatus()
            });
        }
    }

    private void loadOrdersByType(String type) {
        tableModel.setRowCount(0);
        List<Order> orders = orderDAO.getOrdersByType(type);
        for (Order o : orders) {
            tableModel.addRow(new Object[]{
                o.getOrderId(),
                o.getProductId(),
                o.getQuantity(),
                o.getOrderType(),
                o.getOrderDate() != null ? o.getOrderDate().toString().substring(0, 19) : "N/A",
                o.getStatus()
            });
        }
    }

    private void createOrder(String orderType) {
        JTextField productIdField = new JTextField();
        JTextField quantityField = new JTextField();

        String action = "PURCHASE".equals(orderType) ? "Purchase" : "Sale";
        Object[] message = {
            "Product ID:", productIdField,
            "Quantity:", quantityField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "New " + action, JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String pidText = productIdField.getText().trim();
                String qtyText = quantityField.getText().trim();

                if (pidText.isEmpty() || qtyText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Product ID and Quantity are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int productId = Integer.parseInt(pidText);
                int quantity = Integer.parseInt(qtyText);

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(this, " Quantity must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //  CORRECT METHOD: getProductById
                Product product = productDAO.getProductById(productId);
                if (product == null) {
                    JOptionPane.showMessageDialog(this, " Product ID '" + productId + "' not found!\nCheck Products list for valid IDs.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // For SALES: check sufficient stock
                if ("SALE".equals(orderType) && quantity > product.getQuantity()) {
                    JOptionPane.showMessageDialog(this, " Insufficient stock!\nAvailable: " + product.getQuantity(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Order order = new Order(productId, quantity, orderType);
                if (orderDAO.addOrder(order)) {
                    JOptionPane.showMessageDialog(this, " " + action + " order recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    
                    // Update product stock
                    int newQuantity = "PURCHASE".equals(orderType) 
                        ? product.getQuantity() + quantity 
                        : product.getQuantity() - quantity;
                    
                    product.setQuantity(newQuantity);
                    productDAO.updateProduct(product);
                    
                    loadAllOrders();
                } else {
                    JOptionPane.showMessageDialog(this, " Failed to record order.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, " Please enter valid numbers for Product ID and Quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
