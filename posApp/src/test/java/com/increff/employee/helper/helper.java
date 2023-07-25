package com.increff.employee.helper;
import com.increff.employee.model.*;
public class helper {
    public static BrandForm createBrand(String brand, String category) {
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    public static ProductForm createProduct(String brandName, String category, String productBarcode, Double productMrp, String productName) {
        ProductForm productForm = new ProductForm();
        productForm.setBrand(brandName);
        productForm.setCategory(category);
        productForm.setName(productName);
        productForm.setBarcode(productBarcode);
        productForm.setMrp(productMrp);
        return productForm;
    }
    public static InventoryForm createInventory(String productBarcode, Integer quantity) {
        InventoryForm inventoryForm = new InventoryForm();
        inventoryForm.setBarcode(productBarcode);
        inventoryForm.setQuantity(quantity);
        return inventoryForm;
    }

    public static UserForm createUser(String email, String password, String role) {
        UserForm userForm = new UserForm();
        userForm.setEmail(email);
        userForm.setPassword(password);
        userForm.setRole(role);
        return userForm;
    }

    public static OrderItemForm createOrderItem(Integer orderId,String barCode, Integer quantity, Double sellingPrice) {
        OrderItemForm orderItemForm = new OrderItemForm();
        orderItemForm.setOrderId(orderId);
        orderItemForm.setQuantity(quantity);
        orderItemForm.setBarcode(barCode);
        orderItemForm.setSellingPrice(sellingPrice);
        return orderItemForm;
    }
}
