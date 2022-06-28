package com.hfad.exploreshopping;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ParseClassName("PurchaseItem")
public class PurchaseItem extends ParseObject {
    public static final String TAG = "PurchaseItem";
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_IMAGE_URL = "productImageUrl";
    public static final String KEY_PRODUCT_PRICE = "productPrice";
    public static final String KEY_PRODUCT_DETAIL_URL = "productDetailUrl";
    public static final String KEY_PRODUCT_ORIGINAL_PRICE = "productOriginalPrice";
    public static final String KEY_PRODUCT_RATINGS = "productRatings";
    public static final String KEY_PRODUCT_CREATED_AT = "createdAt";



    public PurchaseItem(){}

    public PurchaseItem(SuggestedItem suggestedItem){
        // Add to the purchaseItems
        setProductName(suggestedItem.getProductName());
        setProductDetailUrl(suggestedItem.getProductDetailUrl());
        setProductImageUrl(suggestedItem.getProductImageUrl());
        setProductOriginalPrice(suggestedItem.getProductOriginalPrice());
        setProductRatings(suggestedItem.getProductRatings());
        setProductPrice(suggestedItem.getProductPrice());
    }

    public String getProductName(){
        return getString(KEY_PRODUCT_NAME);
    }

    public String getProductPrice(){return getString(KEY_PRODUCT_PRICE);}

    public String getPurchaseDate(){
        Date purchaseDate = getCreatedAt();
        SimpleDateFormat fmtOut = new SimpleDateFormat("MM/dd/yyyy");
        String date = fmtOut.format(purchaseDate);
        return date;
    }

    public  String getProductOriginalPrice(){
        return getString(KEY_PRODUCT_ORIGINAL_PRICE);
    }

    public String getProductRatings(){return getString(KEY_PRODUCT_RATINGS);};

    public String getProductDetailUrl(){return getString(KEY_PRODUCT_DETAIL_URL);};

    public String getProductImageUrl(){return getString(KEY_IMAGE_URL);};

    public void setProductName(String name){put(KEY_PRODUCT_NAME,name);}

    public void setProductPrice(String price){put(KEY_PRODUCT_PRICE,price);}

    public void setProductOriginalPrice(String price){put(KEY_PRODUCT_ORIGINAL_PRICE,price);}

    public void setProductRatings(String ratings){put(KEY_PRODUCT_RATINGS,ratings);}

    public void setProductDetailUrl(String detailUrl){put(KEY_PRODUCT_DETAIL_URL,detailUrl);}

    public void setProductImageUrl(String imageUrl){put(KEY_IMAGE_URL,imageUrl);}

}
