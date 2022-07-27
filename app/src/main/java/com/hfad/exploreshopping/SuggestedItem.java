package com.hfad.exploreshopping;


import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

import java.util.ArrayList;


@Parcel
public class SuggestedItem implements Comparable<SuggestedItem>{

    private String productName;
    private String productImageUrl;
    private String productPrice;
    private String productDetailUrl;
    private String productOriginalPrice = "null";
    private String productRatings = "No ratings available";
    private double productPriceValue = 0;
    private int correlation = 0;

    public SuggestedItem() {
    }

    public SuggestedItem(ClickedItem clickedItem) {
        this.productName = clickedItem.getProductName();
        this.productDetailUrl = clickedItem.getProductDetailUrl();
        this.productImageUrl = clickedItem.getProductImageUrl();
        this.productOriginalPrice = clickedItem.getProductOriginalPrice();
        this.productRatings = clickedItem.getProductRatings();
        this.productPrice = clickedItem.getProductPrice();
    }

    public SuggestedItem(String productName, String productImageUrl, String productPrice, String productDetailUrl, String productOriginalPrice, String productRatings) {
        if (productOriginalPrice != null) {
            this.productOriginalPrice = productOriginalPrice;
        }
        if (productRatings != null) {
            this.productRatings = productRatings;
        }
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

    String processRatings(String ratings) {
        return null;
    }

    public void setProductPriceValue(double productPriceValue){
        this.productPriceValue = productPriceValue;
    }

    public int getCorrelation() {
        return correlation;
    }

    @Override
    public int compareTo(SuggestedItem o) {
        return this.getCorrelation() - o.getCorrelation();
    }

    public void setCorrelation(int correlation) {
        this.correlation = correlation;
    }
}
