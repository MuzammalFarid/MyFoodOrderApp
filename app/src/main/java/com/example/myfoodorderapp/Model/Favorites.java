package com.example.myfoodorderapp.Model;

public class Favorites {

    private String ProductId, ProductName, Price, ID, UserPhone, FoodImage;

    public Favorites(){

    }

    public Favorites(String foodId, String foodName, String foodPrice, String foodMenuId, String userPhone, String FoodImages) {
        ProductId = foodId;
        ProductName = foodName;
        Price = foodPrice;
        ID = foodMenuId;
        UserPhone = userPhone;
        FoodImage = FoodImages;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getFoodImage() {
        return FoodImage;
    }

    public void setFoodImage(String foodImage) {
        FoodImage = foodImage;
    }
}

