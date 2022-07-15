package com.hfad.exploreshopping;

import android.os.Parcelable;

import org.parceler.Parcel;


public class ProductStore implements Parcelable {
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

    public ProductStore(android.os.Parcel in){
        String [] data = new String[5];
        this.storeName = data[0];
        this.productCurrencySymbol = data[1];
        this.productPrice = data[2];
        this.storeUrl = data[3];
        this.lastUpdated = data[4];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.storeName,
                this.productCurrencySymbol,
                this.productPrice,
                this.storeUrl,
                this.lastUpdated
        });
    }

    public static final Parcelable.Creator<ProductStore> CREATOR = new Parcelable.Creator<ProductStore>(){

        @Override
        public ProductStore createFromParcel(android.os.Parcel source) {
            return new ProductStore(source);
        }

        public ProductStore[] newArray(int size){
            return new ProductStore[size];
        }
    };
}
