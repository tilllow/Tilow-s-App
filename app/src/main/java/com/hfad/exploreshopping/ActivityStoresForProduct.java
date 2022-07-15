package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ActivityStoresForProduct extends AppCompatActivity {

    List<ProductStore> qrStoreList;
    RecyclerView rvQrStores;
    StoresAdapter storesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores_for_product);
        qrCodeProduct qrCodeProduct = getIntent().getParcelableExtra("EXTRA_QR_CODE_PRODUCT");
        qrStoreList = new ArrayList<>();
        qrStoreList = qrCodeProduct.getProductStores();
        storesAdapter = new StoresAdapter(ActivityStoresForProduct.this,qrStoreList,qrCodeProduct);
        rvQrStores = findViewById(R.id.rvQrStores);
        rvQrStores.setAdapter(storesAdapter);
        rvQrStores.setLayoutManager(new LinearLayoutManager(ActivityStoresForProduct.this));
    }
}