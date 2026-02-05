package com.inventory.ui;

import com.inventory.dao.SupplierDAO;
import com.inventory.model.Supplier;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SupplierPanel extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private SupplierDAO supplierDAO = new SupplierDAO();

    public SupplierPanel() {
        setTitle("🚚 Suppliers Management");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Table setup
        String[] columns = {"ID", "Name", "Contact", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        loadSuppliers();

        // Top panel: Search and Refresh
        JPanel topPanel = new JPanel(new FlowLayout());
        JTextField searchField = new JTextField(20);
        JButton btnSearch = new JButton("🔍 Search");
        JButton btnRefresh = new JButton("🔄 Refresh");

        btnSearch.addActionListener(e -> searchSuppliers(searchField.getText()));
        btnRefresh.addActionListener(e -> loadSuppliers());

        topPanel.add(new JLabel("Search:"));
        topPanel.add(searchField);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);

        // Bottom panel: CRUD buttons
        JPanel bottomPanel = new JPanel();
        JButton btnAdd = new JButton("➕ Add Supplier");
        JButton btnEdit = new JButton("✏️ Edit Supplier");
        JButton btnDelete = new JButton("🗑️ Delete Supplier");

        btnAdd.addActionListener(e -> addSupplier());
        btnEdit.addActionListener(e -> editSupplier());
        btnDelete.addActionListener(e -> deleteSupplier());

        bottomPanel.add(btnAdd);
        bottomPanel.add(btnEdit);
        bottomPanel.add(btnDelete);

        // Assemble UI
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadSuppliers() {
        tableModel.setRowCount(0);
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getName(),
                s.getContact(),
                s.getEmail()
            });
        }
    }

    private void searchSuppliers(String keyword) {
        if (keyword.trim().isEmpty()) {
            loadSuppliers();
            return;
        }
        tableModel.setRowCount(0);
        List<Supplier> suppliers = supplierDAO.searchSuppliers(keyword.trim());
        for (Supplier s : suppliers) {
            tableModel.addRow(new Object[]{
                s.getSupplierId(),
                s.getName(),
                s.getContact(),
                s.getEmail()
            });
        }
    }

    private void addSupplier() {
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] message = {
            "Supplier Name:", nameField,
            "Contact Number:", contactField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Supplier name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Supplier supplier = new Supplier(name, contact, email);
            if (supplierDAO.addSupplier(supplier)) {
                JOptionPane.showMessageDialog(this, "✅ Supplier added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadSuppliers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to add supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editSupplier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (Integer) table.getValueAt(selectedRow, 0);
        String currentName = (String) table.getValueAt(selectedRow, 1);
        String currentContact = (String) table.getValueAt(selectedRow, 2);
        String currentEmail = (String) table.getValueAt(selectedRow, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField contactField = new JTextField(currentContact);
        JTextField emailField = new JTextField(currentEmail);

        Object[] message = {
            "Supplier Name:", nameField,
            "Contact Number:", contactField,
            "Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Edit Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "❌ Supplier name is required!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Supplier supplier = new Supplier(name, contact, email);
            supplier.setSupplierId(supplierId);

            if (supplierDAO.updateSupplier(supplier)) {
                JOptionPane.showMessageDialog(this, "✅ Supplier updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadSuppliers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to update supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteSupplier() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "⚠️ Please select a supplier to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int supplierId = (Integer) table.getValueAt(selectedRow, 0);
        String supplierName = (String) table.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete supplier:\n" + supplierName + " (ID: " + supplierId + ") ?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            if (supplierDAO.deleteSupplier(supplierId)) {
                JOptionPane.showMessageDialog(this, "✅ Supplier deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadSuppliers();
            } else {
                JOptionPane.showMessageDialog(this, "❌ Failed to delete supplier.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}