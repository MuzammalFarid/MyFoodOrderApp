package com.example.myfoodorderapp.Model;

public class BestDealModel {
    private String title;
    private int productImage;

    public BestDealModel(String title, int productImage) {
        this.title = title;
        this.productImage = productImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProductImage() {
        return productImage;
    }

    public void setProductImage(int productImage) {
        this.productImage = productImage;
    }
}
