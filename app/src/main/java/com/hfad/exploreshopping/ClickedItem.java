package com.hfad.exploreshopping;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("clickedItem")
public class ClickedItem extends ParseObject {

    public static final String TAG = "ClickedItem";
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_IMAGE_URL = "productImageUrl";
    public static final String KEY_PRODUCT_PRICE = "productPrice";
    public static final String KEY_PRODUCT_DETAIL_URL = "productDetailUrl";
    public static final String KEY_PRODUCT_ORIGINAL_PRICE = "productOriginalPrice";
    public static final String KEY_PRODUCT_RATINGS = "productRatings";
    public static final String KEY_PRODUCT_CREATED_AT = "createdAt";


    public ClickedItem() {
    }

    public ClickedItem(SuggestedItem suggestedItem) {

        setProductName(suggestedItem.getProductName());
        setProductDetailUrl(suggestedItem.getProductDetailUrl());
        setProductImageUrl(suggestedItem.getProductImageUrl());
        setProductOriginalPrice(suggestedItem.getProductOriginalPrice());
        setProductRatings(suggestedItem.getProductRatings());
        setProductPrice(suggestedItem.getProductPrice());
    }

    public ClickedItem(CartItem cartItem) {

        setProductName(cartItem.getProductName());
        setProductDetailUrl(cartItem.getProductDetailUrl());
        setProductImageUrl(cartItem.getProductImageUrl());
        setProductOriginalPrice(cartItem.getProductOriginalPrice());
        setProductRatings(cartItem.getProductRatings());
        setProductPrice(cartItem.getProductPrice());
    }

    public String getProductName() {
        return getString(KEY_PRODUCT_NAME);
    }

    public String getProductPrice() {
        return getString(KEY_PRODUCT_PRICE);
    }

    public String getProductOriginalPrice() {
        return getString(KEY_PRODUCT_ORIGINAL_PRICE);
    }

    public String getProductRatings() {
        return getString(KEY_PRODUCT_RATINGS);
    }

    ;

    public String getProductDetailUrl() {
        return getString(KEY_PRODUCT_DETAIL_URL);
    }

    ;

    public String getProductImageUrl() {
        return getString(KEY_IMAGE_URL);
    }

    ;

    public void setProductName(String name) {
        put(KEY_PRODUCT_NAME, name);
    }

    public void setProductPrice(String price) {
        put(KEY_PRODUCT_PRICE, price);
    }

    public void setProductOriginalPrice(String price) {
        if (price == null || price.equals("null")) {
            return;
        }
        put(KEY_PRODUCT_ORIGINAL_PRICE, price);
    }

    public void setProductRatings(String ratings) {
        if (ratings == null || ratings.equals("null")) {
            return;
        }
        put(KEY_PRODUCT_RATINGS, ratings);
    }


    public boolean equals(ClickedItem clickedItem) {

        boolean condition1 = (getProductName() == clickedItem.getProductName()) || (getProductName().equals(clickedItem.getProductName()));
        boolean condition2 = (getProductPrice() == clickedItem.getProductPrice()) || (getProductPrice().equals(clickedItem.getProductPrice()));
        boolean condition3 = (getProductImageUrl() == clickedItem.getProductImageUrl()) || (getProductImageUrl().equals(clickedItem.getProductImageUrl()));

        return condition1 && condition2 && condition3;
    }

    public void setProductDetailUrl(String detailUrl) {
        put(KEY_PRODUCT_DETAIL_URL, detailUrl);
    }

    public void setProductImageUrl(String imageUrl) {
        put(KEY_IMAGE_URL, imageUrl);
    }
}
