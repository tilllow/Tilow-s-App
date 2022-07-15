package com.hfad.exploreshopping;

import org.parceler.Parcel;

@Parcel
public class ProductStore {
    private String storeName;
    private String productCurrencySymbol;
    private String productPrice;
    private String storeUrl;
    private String lastUpdated;

    public ProductStore(){}

    ProductStore(String storeName, String productCurrencySymbol, String productPrice, String storeUrl, String lastUpdated){
        this.storeName = storeName;
        this.productCurrencySymbol = productCurrencySymbol;
        this.productPrice = productPrice;
        this.storeUrl = storeUrl;
        this.lastUpdated = lastUpdated;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getProductCurrencySymbol() {
        return productCurrencySymbol;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }
}
