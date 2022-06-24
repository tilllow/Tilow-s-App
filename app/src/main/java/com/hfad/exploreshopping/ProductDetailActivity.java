package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

public class ProductDetailActivity extends AppCompatActivity {

    TextView tvProductNamePd;
    TextView tvProductDescriptionPd;
    TextView tvProductOldPricePd;
    TextView tvProductCurrentPricePd;
    ImageView ivProductImagePd;
    RatingBar rbProductRatingPd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvProductNamePd = findViewById(R.id.tvProductNamePd);
        tvProductDescriptionPd = findViewById(R.id.tvProductDescriptionPd);
        tvProductOldPricePd = findViewById(R.id.tvProductOldPricePd);
        tvProductCurrentPricePd = findViewById(R.id.tvProductCurrentPricePd);
        ivProductImagePd = findViewById(R.id.ivProductImagePd);
        rbProductRatingPd = findViewById(R.id.rbProductRatingPd);

        SuggestedItem suggestedItem = Parcels.unwrap(getIntent().getParcelableExtra("EXTRA_ITEM"));

        String productName = suggestedItem.getProductName();
        String productImageUrl = suggestedItem.getProductImageUrl();
        String productPrice = suggestedItem.getProductPrice();
        String productDetailUrl = suggestedItem.getProductDetailUrl();
        String productOriginalPrice = suggestedItem.getProductOriginalPrice();
        String productRatings = suggestedItem.getProductRatings();

        tvProductNamePd.setText(productName);
        tvProductDescriptionPd.setText(productDetailUrl);

        if (productOriginalPrice != null){
            tvProductOldPricePd.setText(productOriginalPrice);
        }else{
            tvProductOldPricePd.setVisibility(View.GONE);
        }
        tvProductCurrentPricePd.setText(productPrice);
        if (productImageUrl != null){
            Glide.with(this).load(productImageUrl).into(ivProductImagePd);
        }
    }
}