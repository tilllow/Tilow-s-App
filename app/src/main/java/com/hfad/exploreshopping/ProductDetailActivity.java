package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.Arrays;
import java.util.HashSet;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String TAG = "ProductDetailActivity";
    TextView tvProductNamePd;
    TextView tvProductDescriptionPd;
    TextView tvProductOldPricePd;
    TextView tvProductCurrentPricePd;
    ImageView ivProductImagePd;
    TextView tvProductRatings;
    TextView tvProductOldPriceText;
    TextView tvProductRatingsText;
    Boolean productInCartOrPurchased = false;

    AppCompatButton btnPurchaseItem;
    AppCompatButton btnAddToCart;
    SuggestedItem suggestedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        tvProductNamePd = findViewById(R.id.tvProductNamePd);
        tvProductDescriptionPd = findViewById(R.id.tvProductDescriptionPd);
        tvProductOldPricePd = findViewById(R.id.tvProductOldPricePd);
        tvProductCurrentPricePd = findViewById(R.id.tvProductCurrentPricePd);
        ivProductImagePd = findViewById(R.id.ivProductImagePd);
        tvProductRatings = findViewById(R.id.tvProductRatings);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnPurchaseItem = findViewById(R.id.btnPurchaseItem);
        tvProductOldPriceText = findViewById(R.id.tvProductOldPriceText);
        tvProductRatingsText = findViewById(R.id.tvProductRatingsText);

        suggestedItem = Parcels.unwrap(getIntent().getParcelableExtra("EXTRA_ITEM"));

        String productName = suggestedItem.getProductName();
        String productImageUrl = suggestedItem.getProductImageUrl();
        String productPrice = suggestedItem.getProductPrice();
        String productDetailUrl = suggestedItem.getProductDetailUrl();
        String productOriginalPrice = suggestedItem.getProductOriginalPrice();
        String productRatings = suggestedItem.getProductRatings();

        tvProductNamePd.setText(productName);
        tvProductDescriptionPd.setText(productDetailUrl);

        if (productRatings == null || productRatings.length() == 0 || productRatings == "null") {
            tvProductRatings.setVisibility(View.GONE);
            tvProductRatingsText.setVisibility(View.GONE);
        } else {
            tvProductRatings.setText(productRatings);
        }

        if (productOriginalPrice == null || productOriginalPrice.equals("null") || productOriginalPrice == "") {
            Log.d(TAG, "The value of product ratings is : " + productRatings);
            tvProductOldPricePd.setVisibility(View.GONE);
            tvProductOldPriceText.setVisibility(View.GONE);
        } else {
            tvProductOldPricePd.setText(productOriginalPrice);
        }
        tvProductCurrentPricePd.setText(productPrice);
        if (productImageUrl != null) {
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
                        if (e != null) {
                            Toast.makeText(ProductDetailActivity.this, "Purchase unsuccessful", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentUser.add("ItemsPurchased", purchaseItem);
                        currentUser.saveInBackground();
                        Toast.makeText(ProductDetailActivity.this, "Purchase successful", Toast.LENGTH_SHORT).show();
                        productInCartOrPurchased = true;
                        finish();
                    }
                });


            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                CartItem cartItem = new CartItem(suggestedItem);
                cartItem.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Toast.makeText(ProductDetailActivity.this, "Adding to cart unsuccessful", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        currentUser.addUnique("CartItems", cartItem);
                        currentUser.saveInBackground();
                        Toast.makeText(ProductDetailActivity.this, "Added to cart successfully", Toast.LENGTH_SHORT).show();
                        productInCartOrPurchased = true;
                        finish();
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {

        if (productInCartOrPurchased.equals(false)) {
            queryViewedItem();
        }
        super.onDestroy();
    }

    private void queryViewedItem() {
        ClickedItem clickedItem = new ClickedItem(suggestedItem);
        ParseUser currentUser = ParseUser.getCurrentUser();
        JSONArray itemsClicked = currentUser.getJSONArray("clickedItems");
        HashSet<String> imageUrlSet = new HashSet<>();

        if (itemsClicked == null) {
            clickedItem.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.d(TAG, "Item has been successfully saved");
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.addUnique("clickedItems", clickedItem);
                        currentUser.saveInBackground();
                    } else {
                        Log.d(TAG, "An error occurred when saving item in parse");
                    }
                }
            });
            return;
        }

        for (int i = 0; i < itemsClicked.length(); ++i) {
            try {
                JSONObject itemAlreadyClicked = (JSONObject) itemsClicked.get(i);
                String itemId = (String) itemAlreadyClicked.get("objectId");

                ParseQuery<ClickedItem> query = ParseQuery.getQuery(ClickedItem.class);
                int finalI = i;
                query.getInBackground(itemId, new GetCallback<ClickedItem>() {
                    @Override
                    public void done(ClickedItem object, ParseException e) {
                        if (e == null) {
                            String imageUrl = object.getProductImageUrl();
                            imageUrlSet.add(imageUrl);

                            final int val = finalI;

                            if (val == itemsClicked.length() - 1) {
                                if (!imageUrlSet.contains(clickedItem.getProductImageUrl())) {
                                    Log.d(TAG, "This is a case of an brand new product");
                                    clickedItem.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e == null) {
                                                ParseUser currentUser = ParseUser.getCurrentUser();
                                                currentUser.addUnique("clickedItems", clickedItem);
                                                currentUser.saveInBackground();
                                                Log.d(TAG, clickedItem.getProductImageUrl() + "was added on Parse");
                                            } else {
                                                Log.d(TAG, "Unable to add item to Parse");
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "This is the case of an already viewed item from the user");

                                }
                            }

                        } else {
                            Log.d(TAG, "There was an error with fetching this data");
                        }
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}