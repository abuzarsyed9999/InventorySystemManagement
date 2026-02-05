package com.inventory.model;

public class Supplier {
    private int supplierId;
    private String name;
    private String contact;
    private String email;

    public Supplier() {}

    public Supplier(String name, String contact, String email) {
        this.name = name;
        this.contact = contact;
        this.email = email;
    }

    public int getSupplierId() { return supplierId; }
    public void setSupplierId(int supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() {
        return "Supplier{" +
                "id=" + supplierId +
                ", name='" + name + '\'' +
                ", contact='" + contact + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}