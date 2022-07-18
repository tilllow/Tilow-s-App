package com.hfad.exploreshopping;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductStore implements Parcelable {
    private String storeName;
    private String productCurrencySymbol;
    private String productPrice;
    private String storeUrl;
    private String lastUpdated;

    public ProductStore() {
    }

    ProductStore(String storeName, String productCurrencySymbol, String productPrice, String storeUrl, String lastUpdated) {
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

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setProductCurrencySymbol(String productCurrencySymbol) {
        this.productCurrencySymbol = productCurrencySymbol;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public void setStoreUrl(String storeUrl) {
        this.storeUrl = storeUrl;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    private ProductStore(Parcel in) {
        this.storeName = in.readString();
        this.productCurrencySymbol = in.readString();
        this.productPrice = in.readString();
        this.storeUrl = in.readString();
        this.lastUpdated = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(storeName);
        dest.writeString(productCurrencySymbol);
        dest.writeString(productPrice);
        dest.writeString(storeUrl);
        dest.writeString(lastUpdated);
    }

    public static final Parcelable.Creator<ProductStore> CREATOR = new Parcelable.Creator<ProductStore>() {

        @Override
        public ProductStore createFromParcel(Parcel source) {
            return new ProductStore(source);
        }

        public ProductStore[] newArray(int size) {
            return new ProductStore[size];
        }
    };
}
