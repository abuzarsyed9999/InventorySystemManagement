package com.inventory.ui;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ProductPanel extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private ProductDAO productDAO = new ProductDAO();

    public ProductPanel() {
        setTitle(" Products Management");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Category", "Price (₹)", "Quantity", "Supplier ID"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadProducts();

        // Top panel: Search and Low Stock
        JPanel topPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton btnSearch = new JButton("Search");
        JButton btnLowStock = new JButton(" Low Stock Alert");
        JButton btnRefresh = new JButton(" Refresh");

        btnSearch.addActionListener(e -> searchProducts(searchField.getText()));
        btnLowStock.addActionListener(e -> showLowStock());
        btnRefresh.addActionListener(e -> loadProducts());

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(btnLowStock);
        topPanel.add(btnRefresh);

        // Bottom panel: CRUD buttons
        JPanel bottomPanel = new JPanel();
        JButton btnAdd = new JButton(" Add Product");
        JButton btnEdit = new JButton(" Edit Product");
        JButton btnDelete = new JButton(" Delete Product");

        btnAdd.addActionListener(e -> addProduct());
        btnEdit.addActionListener(e -> editProduct());
        btnDelete.addActionListener(e -> deleteProduct());

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        // Assemble UI
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                p.getCategory(),
                String.format("%.2f", p.getPrice()),
                p.getQuantity(),
                p.getSupplierId() == 0 ? "None" : p.getSupplierId()
            });
        }
    }

    private void searchProducts(String keyword) {
        if (keyword.trim().isEmpty()) {
            loadProducts();
            return;
        }
        tableModel.setRowCount(0);
        List<Product> products = productDAO.searchProducts(keyword.trim());
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getProductId(),
                p.getName(),
                p.getCategory(),
                String.format("%.2f", p.getPrice()),
                p.getQuantity(),
                p.getSupplierId() == 0 ? "None" : p.getSupplierId()
            });
        }
    }

    private void showLowStock() {
        tableModel.setRowCount(0);
        List<Product> products = productDAO.getLowStockProducts(10); // threshold = 10
        if (products.isEmpty()) {
            JOptionPane.showMessageDialog(this, " No low-stock items!", "Low Stock Alert", JOptionPane.INFORMATION_MESSAGE);
        } else {
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                    p.getProductId(),
                    p.getName(),
                    p.getCategory(),
                    String.format("%.2f", p.getPrice()),
                    p.getQuantity(),
                    p.getSupplierId() == 0 ? "None" : p.getSupplierId()
                });
            }
            JOptionPane.showMessageDialog(this, " Showing items with stock < 10", "Low Stock Alert", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addProduct() {
        JTextField nameField = new JTextField();
        JTextField categoryField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        JTextField supplierField = new JTextField();

        Object[] message = {
            "Product Name:", nameField,
            "Category:", categoryField,
            "Price (₹):", priceField,
            "Quantity:", quantityField,
            "Supplier ID (0 if none):", supplierField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int supplierId = Integer.parseInt(supplierField.getText().trim());

                if (name.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Name and Category are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product product = new Product(name, category, price, quantity);
                product.setSupplierId(supplierId);

                if (productDAO.addProduct(product)) {
                    JOptionPane.showMessageDialog(this, " Product added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(this, " Failed to add product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, " Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, " Please select a product to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (Integer) table.getValueAt(selectedRow, 0);
        String currentName = (String) table.getValueAt(selectedRow, 1);
        String currentCategory = (String) table.getValueAt(selectedRow, 2);
        String currentPrice = (String) table.getValueAt(selectedRow, 3);
        int currentQuantity = (Integer) table.getValueAt(selectedRow, 4);
        String currentSupplier = (String) table.getValueAt(selectedRow, 5);

        JTextField nameField = new JTextField(currentName);
        JTextField categoryField = new JTextField(currentCategory);
        JTextField priceField = new JTextField(currentPrice);
        JTextField quantityField = new JTextField(String.valueOf(currentQuantity));
        JTextField supplierField = new JTextField(currentSupplier.equals("None") ? "0" : currentSupplier);

        Object[] message = {
            "Product Name:", nameField,
            "Category:", categoryField,
            "Price (₹):", priceField,
            "Quantity:", quantityField,
            "Supplier ID (0 if none):", supplierField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Product", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int quantity = Integer.parseInt(quantityField.getText().trim());
                int supplierId = Integer.parseInt(supplierField.getText().trim());

                if (name.isEmpty() || category.isEmpty()) {
                    JOptionPane.showMessageDialog(this, " Name and Category are required!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Product product = new Product(name, category, price, quantity);
                product.setProductId(productId);
                product.setSupplierId(supplierId);

                if (productDAO.updateProduct(product)) {
                    JOptionPane.showMessageDialog(this, " Product updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadProducts();
                } else {
                    JOptionPane.showMessageDialog(this, " Failed to update product.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, " Invalid number format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, " Please select a product to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int productId = (Integer) table.getValueAt(selectedRow, 0);
        String productName = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete:\n" + productName + " (ID: " + productId + ") ?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (productDAO.deleteProduct(productId)) {
                JOptionPane.showMessageDialog(this, "Product deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadProducts();
            } else {
                JOptionPane.showMessageDialog(this, " Failed to delete product.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
