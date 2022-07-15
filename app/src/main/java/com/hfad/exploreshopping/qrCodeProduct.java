package com.hfad.exploreshopping;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.ArrayList;

public class qrCodeProduct implements Parcelable {

    private String productTitle;
    private String productDescription;
    private String productImageUrl;
    private ArrayList<ProductStore> productStores;

    public qrCodeProduct() {
    }

    public qrCodeProduct(Parcel in){
        String [] data = new String[3];
    }

    public qrCodeProduct(String productTitle, String productDescription, String productImageUrl, ArrayList<ProductStore> productStores) {
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productImageUrl = productImageUrl;
        this.productStores = productStores;
    }

    public qrCodeProduct(android.os.Parcel in) {
        productTitle = in.readString();
        productDescription = in.readString();
        productImageUrl = in.readString();
        productStores = in.readArrayList(ProductStore.class.getClassLoader());
    }


    public static final Parcelable.Creator<qrCodeProduct> CREATOR = new Parcelable.Creator<qrCodeProduct>(){
            @Override
            public qrCodeProduct createFromParcel(android.os.Parcel in) {
                return new qrCodeProduct(in);
            }

            @Override
            public qrCodeProduct[] newArray(int size) {
                return new qrCodeProduct[size];
            }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(productTitle);
        dest.writeString(productDescription);
        dest.writeString(productImageUrl);
        dest.writeList(productStores);
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
