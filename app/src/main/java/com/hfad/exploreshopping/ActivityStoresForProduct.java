package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ActivityStoresForProduct extends AppCompatActivity {

    //ArrayList<ProductStore> qrStoreList;
    public static final String TAG = "ActivityStoresForProduc";
    RecyclerView rvQrStores;
    StoresAdapter storesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_for_product);
        ArrayList<ProductStore> qrStoreList = getIntent().getParcelableArrayListExtra("productStores");
        String productTitle = getIntent().getStringExtra("productTitle");
        String productDescription = getIntent().getStringExtra("productDescription");
        String productImageUrl = getIntent().getStringExtra("productImageUrl");
        qrCodeProduct qrCodeProduct = new qrCodeProduct(productTitle, productDescription, productImageUrl, qrStoreList);
        storesAdapter = new StoresAdapter(ActivityStoresForProduct.this, qrStoreList, qrCodeProduct);
        rvQrStores = findViewById(R.id.rvQrStores);
        rvQrStores.setAdapter(storesAdapter);
        rvQrStores.setLayoutManager(new LinearLayoutManager(ActivityStoresForProduct.this));
    }
}