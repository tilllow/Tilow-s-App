package com.hfad.exploreshopping;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("CartItem")
public class CartItem extends ParseObject {
    public static final String KEY_PRODUCT_NAME = "productName";
    public static final String KEY_PRODUCT_PRICE = "productPrice";
    public static final String KEY_PRODUCT_ORIGINAL_PRICE = "productOriginalPrice";
    public static final String KEY_PRODUCT_RATINGS = "productRatings";
    public static final String KEY_PRODUCT_DETAIL_URL = "productDetailUrl";
    public static final String KEY_PRODUCT_IMAGE_URL = "productImageUrl";


    public CartItem(){}

    public CartItem(SuggestedItem suggestedItem){
        //Add to the cartItems
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

    public  String getProductOriginalPrice(){
        return getString(KEY_PRODUCT_ORIGINAL_PRICE);
    }

    public String getProductRatings(){return getString(KEY_PRODUCT_RATINGS);};

    public String getProductDetailUrl(){return getString(KEY_PRODUCT_DETAIL_URL);};

    public String getProductImageUrl(){return getString(KEY_PRODUCT_IMAGE_URL);};

    public void setProductName(String name){put(KEY_PRODUCT_NAME,name);}

    public void setProductPrice(String price){put(KEY_PRODUCT_PRICE,price);}

    public void setProductOriginalPrice(String price){put(KEY_PRODUCT_ORIGINAL_PRICE,price);}

    public void setProductRatings(String ratings){put(KEY_PRODUCT_RATINGS,ratings);}

    public void setProductDetailUrl(String detailUrl){put(KEY_PRODUCT_DETAIL_URL,detailUrl);}

    public void setProductImageUrl(String imageUrl){put(KEY_PRODUCT_IMAGE_URL,imageUrl);}

}
