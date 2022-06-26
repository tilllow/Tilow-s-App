package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.Arrays;

public class ProductDetailActivity extends AppCompatActivity {

    TextView tvProductNamePd;
    TextView tvProductDescriptionPd;
    TextView tvProductOldPricePd;
    TextView tvProductCurrentPricePd;
    ImageView ivProductImagePd;
    RatingBar rbProductRatingPd;
    AppCompatButton btnPurchaseItem;
    AppCompatButton btnAddToCart;

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
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnPurchaseItem = findViewById(R.id.btnPurchaseItem);

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

        btnPurchaseItem.setOnClickListener(new View.OnClickListener() {

            String itemId;

            @Override
            public void onClick(View v) {

                ParseUser currentUser = ParseUser.getCurrentUser();
                //currentUser
                PurchaseItem purchaseItem = new PurchaseItem(suggestedItem);
                purchaseItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Toast.makeText(ProductDetailActivity.this,"Purchase unsuccessful",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentUser.add("ItemsPurchased",purchaseItem);
                        currentUser.saveInBackground();
                        Toast.makeText(ProductDetailActivity.this, "Purchase successful",Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {

            String itemId;

            @Override
            public void onClick(View v) {

                ParseUser currentUser = ParseUser.getCurrentUser();
                //currentUser
                CartItem cartItem = new CartItem(suggestedItem);
                cartItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Toast.makeText(ProductDetailActivity.this,"Adding to cart unsuccessful",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentUser.add("CartItems",cartItem);
                        currentUser.saveInBackground();
                        Toast.makeText(ProductDetailActivity.this, "Added to cart successfully",Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });
    }
}