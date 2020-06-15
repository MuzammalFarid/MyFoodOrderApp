package com.example.myfoodorderapp.Model;

public class Movie {
    private String title;
    private int imageProduct;

    public Movie(String title, int imageProduct) {
        this.title = title;
        this.imageProduct = imageProduct;
    }

    public Movie(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public int getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(int imageProduct) {
        this.imageProduct = imageProduct;
    }
}
