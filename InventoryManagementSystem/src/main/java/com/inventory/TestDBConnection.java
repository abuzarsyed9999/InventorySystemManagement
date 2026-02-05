package com.inventory;

import com.inventory.util.DBConnection;
import java.sql.Connection;

public class TestDBConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();
        if (conn != null) {
            System.out.println(" SUCCESS: Connected to inventory_db!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(" FAILED: Could not connect to inventory_db.");
        }
    }
}