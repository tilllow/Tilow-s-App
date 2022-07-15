package com.hfad.exploreshopping;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class qrCodeProduct {

    private String productTitle;
    private String productDescription;
    private String productImageUrl;
    private ArrayList<ProductStore> productStores;

    public qrCodeProduct() {
    }

    public qrCodeProduct(String productTitle, String productDescription, String productImageUrl, ArrayList<ProductStore> productStores) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productStores = productStores;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public ArrayList<ProductStore> getProductStores() {
        return productStores;
    }
}
