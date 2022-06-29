package com.hfad.exploreshopping;


import android.os.Parcelable;

import org.parceler.Parcel;

@Parcel
public class SuggestedItem {

    private String productName;
    private String productImageUrl;
    private String productPrice;
    private String productDetailUrl;
    private String productOriginalPrice;
    private String productRatings;

    public SuggestedItem(){
    }

    public SuggestedItem(String productName, String productImageUrl, String productPrice, String productDetailUrl, String productOriginalPrice, String productRatings) {
        this.productName = productName;
        this.productImageUrl = productImageUrl;
        this.productPrice = productPrice;
        this.productDetailUrl = productDetailUrl;
        this.productOriginalPrice = productOriginalPrice;
        this.productRatings = productRatings;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getProductDetailUrl() {
        return productDetailUrl;
    }

    public String getProductOriginalPrice() {
        return productOriginalPrice;
    }

    public String getProductRatings() {
        return productRatings;
    }

    String processRatings(String ratings){
        return null;
    }
}
