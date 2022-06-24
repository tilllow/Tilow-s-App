package com.hfad.exploreshopping;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;

@ParseClassName("PurchaseItem")
public class PurchaseItem extends ParseObject {
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_IMAGE_URL = "productImageUrl";
    public static final String KEY_PRODUCT_PRICE = "productPrice";
    public static final String KEY_PRODUCT_DETAIL_URL = "productDetailUrl";
    public static final String KEY_PRODUCT_ORIGINAL_PRICE = "productOriginalPrice";
    public static final String KEY_PRODUCT_RATINGS = "productRatings";
    public static final String KEY_PRODUCT_CREATED_AT = "createdAt";

    //PurchaseItem(){};
//    PurchaseItem(String productName,String productImageUrl,String productPrice, String productDetailUrl,String productOriginalPrice, String productRatings){
//        put(KEY_PRODUCT_NAME,productName);
//        put(KEY_IMAGE_URL,productImageUrl);
//        put(KEY_PRODUCT_PRICE,productPrice);
//        put(KEY_PRODUCT_DETAIL_URL,productDetailUrl);
//        put(KEY_PRODUCT_ORIGINAL_PRICE,productOriginalPrice);
//        put()
//    }


//    public void setImage(ParseFile parseFile){
//        put(KEY_IMAGE,parseFile);
//    }
//
//    public void setProductName(String productName){
//        put(KEY_PRODUCT_NAME,productName);
//    }
//
//    public void setProductPrice(String productPrice){
//        put(KEY_PRODUCT_PRICE,productPrice);
//    }
//
//    public String getProductName(){
//        return getString(KEY_PRODUCT_NAME);
//    }
//
//    public String getProductPrice(){
//        return getString(KEY_PRODUCT_PRICE);
//    }
//
//    public ParseFile getImage(){
//        return getParseFile(KEY_IMAGE);
//    }

}
