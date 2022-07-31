package com.hfad.exploreshopping;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.zxing.common.StringUtils;
import com.parse.Parse;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import adapters.ItemsAdapter;
import okhttp3.Headers;

public class StoreActivity extends AppCompatActivity {

    private List<SuggestedItem> itemList;
    private List<SuggestedItem> allItems = new ArrayList<>();
    public static final String TAG = "StoreActivity";
    private RecyclerView rvNikeShoes;
    private ItemsAdapter adapter;
    private ProgressBar pbStoreItemLoading;
    private SearchView svSearchStore;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText etPriceMin, etPriceMax;
    private Button btnSave, btnCancel;
    private TextView tvLowerPrice;
    private TextView tvHigherPrice;
    private TextView tvAmazonTodayDeals;
    private AppCompatButton btnFilter;
    private int lowerPrice = 0;
    private int upperPrice = 0;
    private int position;
    private JSONArray searchWordsArray = new JSONArray();
    private HashSet<String> searchWordSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        convertToHashSet();
        position = getIntent().getIntExtra("EXTRA_POSITION", 0);

        itemList = new ArrayList<>();
        tvLowerPrice = findViewById(R.id.tvLowerPrice);
        tvHigherPrice = findViewById(R.id.tvHigherPrice);
        tvAmazonTodayDeals = findViewById(R.id.tvAmazonTodayDeals);
        svSearchStore = findViewById(R.id.svSearchStore);
        rvNikeShoes = findViewById(R.id.rvNikeShoes);
        pbStoreItemLoading = findViewById(R.id.pbStoreItemLoading);
        btnFilter = findViewById(R.id.btnPriceFilter);

        adapter = new ItemsAdapter(this, itemList);
        rvNikeShoes.setAdapter(adapter);
        rvNikeShoes.setLayoutManager(new GridLayoutManager(this, 2));
        tvLowerPrice.setText("Low : 0");
        tvHigherPrice.setText("High : ...");

        ParseUser currentUser = ParseUser.getCurrentUser();
        searchWordsArray = currentUser.getJSONArray("searchWords");

        switch (position) {
            case 0:
                svSearchStore.setQueryHint("Search Nike Shoes here...");
                requestNikeProducts();
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.addUnique("searchWords",query.toLowerCase().trim());
                        currentUser.saveInBackground();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterList(newText);
                        return true;
                    }
                });
                break;
            case 1:
                requestAsosProducts();
                //tvStoreWelcomeMessage.setText("Welcome to the Asos Store");
                svSearchStore.setQueryHint("Search Asos products here...");
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.add("searchWords", query.toLowerCase().trim());
                        currentUser.saveInBackground();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterList(newText);
                        return true;
                    }
                });
                break;
            case 2:
                //tvStoreWelcomeMessage.setText("Welcome to the Amazon Store");
                svSearchStore.setQueryHint("Search Amazon products here...");
                tvAmazonTodayDeals.setVisibility(View.VISIBLE);
                requestAmazonTodayDeals();

                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.addUnique("searchWords",query.toLowerCase().trim());
                        currentUser.saveInBackground();
                        requestAmazonProducts(query);
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }

                });
                break;
            case 3:
                //tvStoreWelcomeMessage.setText("Welcome to the Shoes Collection Store");
                svSearchStore.setQueryHint("Search your shoes here...");
                requestShoesCollections();
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        currentUser.addUnique("searchWords",query.toLowerCase().trim());
                        currentUser.saveInBackground();
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filterList(newText);
                        return true;
                    }
                });
                break;
        }

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFilterOptionsDialog();
            }
        });

        int priceMin = 0, priceMax = 0;
    }

    private void filterList(String text) {
        List<SuggestedItem> filteredList = new ArrayList<>();

        for (SuggestedItem item : itemList) {
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
            if (!filteredList.isEmpty()) {
                adapter.setFilteredList(filteredList);
            }
        }
    }

    private void requestNikeProducts() {
        String nikeApiEndpoint = "https://nike-products.p.rapidapi.com/shoes";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "bef8cb8ff6mshd85f48d12008998p1f5b16jsn6a927615062e");
        headers.put("X-RapidAPI-Host", "nike-products.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(nikeApiEndpoint, headers, params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "OnSuccess");
                JSONArray jsonArray = json.jsonArray;
                Log.d(TAG,"The length of the json Array is : " + jsonArray.length());
                try {
                    allItems.clear();
                    List<SuggestedItem> items = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String shoeImageUrl = jsonObject.getString("img");
                        String productDetailUrl = jsonObject.getString("url");
                        String shoeName = jsonObject.getString("title");
                        String shoePrice = jsonObject.getString("price");
                        Log.d(TAG,"This is the shoe price");

                        //Double productPriceValue = Double.parseDouble(shoePrice.substring(1));

                        SuggestedItem shoeItem = new SuggestedItem(shoeName, shoeImageUrl, shoePrice, productDetailUrl, null, null);
                        //shoeItem.setProductPriceValue(productPriceValue);
                        allItems.add(shoeItem);
                        if (filterBasedOnPrice(shoeItem, lowerPrice, upperPrice)) {
                            items.add(shoeItem);
                        }

                    }
                    Log.d(TAG,"Outside the for loop in the Nike Store");
                    Collections.sort(items);
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                Toast.makeText(getContext(),"Your request could not be completed. Please try again",Toast.LENGTH_SHORT);
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
                Log.e(TAG, "OnFailure", throwable);
//                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void requestAsosProducts() {

        String asosApiEndpoint = "https://asos2.p.rapidapi.com/products/v2/list?store=US&offset=0&categoryId=4209&limit=48&country=US&sort=freshness&currency=USD&sizeSchema=US&lang=en-US";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host", "asos2.p.rapidapi.com");
        itemList.clear();

        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(asosApiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.d(TAG, "OnSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    List<SuggestedItem> items = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
                    allItems.clear();

                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject product = (JSONObject) jsonArray.get(i);
                        String name = product.getString("name");
                        String productImageUrl = product.getString("imageUrl");
                        String productDetailUrl = product.getString("url");
                        JSONObject priceObject = product.getJSONObject("price");
                        JSONObject currentPriceObject = priceObject.getJSONObject("current");
                        JSONObject oldPriceObject = priceObject.getJSONObject("previous");
                        String currentPrice = currentPriceObject.getString("text");
                        Double productPriceValue = currentPriceObject.getDouble("value");
                        String originalPrice = oldPriceObject.getString("text");

                        SuggestedItem productItem = new SuggestedItem(name, productImageUrl, currentPrice, productDetailUrl, originalPrice, null);
                        productItem.setCorrelation(getCorrelation(name));
                        productItem.setProductPriceValue(productPriceValue);
                        allItems.add(productItem);
                        items.add(productItem);
                    }

                    Collections.sort(items);
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(StoreActivity.this, "Your request could not be completed. Please try again", Toast.LENGTH_SHORT);
                Log.e(TAG, "OnFailure", throwable);
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void requestAmazonTodayDeals() {
        String apiEndpoint = "https://amazon24.p.rapidapi.com/api/todaydeals";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host", "amazon24.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.d(TAG, "OnSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray docs = jsonObject.getJSONArray("deal_docs");
                    //itemList.clear();
                    List<SuggestedItem> items = new ArrayList<>();
                    allItems.clear();

                    for (int i = 0; i < Math.min(docs.length(),30); ++i) {

                        JSONObject productInfo = docs.getJSONObject(i);
                        String productName = productInfo.getString("deal_title");
                        String productImageUrl = productInfo.getString("deal_main_image_url");
                        JSONObject productPriceRange = productInfo.getJSONObject("app_sale_range");

                        String currency = productPriceRange.getString("currency");
                        String productMinimumPrice = productPriceRange.getString("min");
                        String productMaximumPrice = productPriceRange.getString("max");

                        String productPrice = currency + " " + productMinimumPrice + " - " + productMaximumPrice;
                        String productDetailUrl = productInfo.getString("deal_details_url");
                        //String productOriginalPrice = productInfo.getString("original_price");
                        //String productRatings = productInfo.getString("evaluate_rate");
                        SuggestedItem suggestedItem = new SuggestedItem(productName, productImageUrl, productPrice, productDetailUrl, null, null);
                        items.add(suggestedItem);
                    }

                    Collections.sort(items);
                    itemList.addAll(items);
                    Log.d(TAG, "This is the size of the array returned from the API" + items.size());
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "Something went wrong with this request");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
                //Toast.makeText(StoreActivity.this, "Your request could not be completed. Please try again", Toast.LENGTH_SHORT);
                Log.e(TAG, "OnFailure", throwable);
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void requestShoesCollections() {

        String apiEndpoint = "https://shoes-collections.p.rapidapi.com/shoes";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host", "shoes-collections.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                JSONArray jsonArray = json.jsonArray;

                try {
                    List<SuggestedItem> items = new ArrayList<>();
                    allItems.clear();
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject productObject = (JSONObject) jsonArray.getJSONObject(i);
                        String productName = productObject.getString("name");
                        String productPrice = "$" + productObject.getString("price");
                        String productImageUrl = productObject.getString("image");
                        String productDescription = productObject.getString("description");

                        SuggestedItem item = new SuggestedItem(productName, productImageUrl, productPrice, productDescription, null, null);
                        items.add(item);
                    }

                    Collections.sort(items);
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "OnFailure", throwable);
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void requestAmazonProducts(String searchWord) {

        String amazonApiEndpoint = "https://amazon24.p.rapidapi.com/api/product?categoryID=aps&keyword=" + searchWord + "&country=US&page=1";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.put("X-RapidAPI-Key", "f8d5f4eac1msh9b16dcfdf3f9dd2p107bb4jsn348b6dddd340");
        requestHeaders.put("X-RapidAPI-Host", "amazon24.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(amazonApiEndpoint, requestHeaders, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;

                try {
                    allItems.clear();
                    List<SuggestedItem> items = new ArrayList<>();
                    JSONArray products = jsonObject.getJSONArray("docs");
                    for (int i = 0; i < Math.min(25, products.length()); ++i) {
                        JSONObject product = (JSONObject) products.get(i);

                        String productName = product.getString("product_title");
                        String productImageUrl = product.getString("product_main_image_url");
                        String productDetailUrl = product.getString("product_detail_url");
                        String productCurrency = product.getString("app_sale_price_currency");
                        String productPrice = productCurrency + product.getString("app_sale_price");
                        String temp = productPrice.replace(",","");
                        //Double productPriceValue = Double.parseDouble(temp);
                        String productRatings = null;

                        try {
                            productRatings = product.getString("evaluate_rate");
                        } catch (Exception e) {
                            Log.d(TAG, "This is the exception" + e);
                        }


                        SuggestedItem item = new SuggestedItem(productName, productImageUrl, productPrice, productDetailUrl, null, productRatings);
                        //item.setProductPriceValue(productPriceValue);
                        allItems.add(item);
                        items.add(item);
                        Log.d(TAG,"This is the name of the product item : " + productName);

//                        if (filterBasedOnPrice(item, lowerPrice, upperPrice)) {
//                            Log.d(TAG, "These are the lower and upper prices respectively " + lowerPrice + " " + upperPrice);
//                            items.add(item);
//                        } else{
//                            Log.d(TAG,"The filtered thing is returning false in this query");
//                        }

                    }

                    //Collections.sort(items);
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    tvAmazonTodayDeals.setText("Showing results for " + searchWord);

                } catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }


                Log.d(TAG, "This response passed successfully");
                // Random comment
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
                Log.i(TAG, "The request has failed", throwable);
            }
        });
    }

    private boolean filterBasedOnPrice(SuggestedItem product, int lowerBound, int upperBound) {
        int itemPrice = 0;
        Log.d(TAG,"This is the value of the productPrice : " + product.getProductPrice());
        try{
            //Log.d(TAG,"This is the value of the product price : " + product.getProductPrice());
            itemPrice = (int) Double.parseDouble(product.getProductPrice().substring(1));
        } catch(Exception e){
            // Do nothing
        }

        if (itemPrice >= lowerBound && itemPrice <= upperBound){
            return true;
        }
        return false;
//        boolean fallsWithinRange = true;
//        int itemPrice = 0;
//
//        try {
//            itemPrice = Integer.parseInt(product.getProductPrice().substring(1));
//
//        } catch (Exception e) {
//            Log.d(TAG, e.getMessage());
//        }
//
//        Log.d(TAG, "" + itemPrice);
//        if (lowerBound > itemPrice) {
//            fallsWithinRange = false;
//        } else if (upperBound > 0 && upperBound < itemPrice) {
//            fallsWithinRange = false;
//        }
//        if (upperBound < lowerBound) {
//            fallsWithinRange = false;
//        }
//
//
//        return fallsWithinRange;
    }

    private void filterEntireArray(int upperVal, int lowerVal){

        List<SuggestedItem> temp = new ArrayList<>();
        for (int i = 0; i < allItems.size(); ++i){
            SuggestedItem suggestedItem = allItems.get(i);
            try{
                String lowerPriceString = tvLowerPrice.getText().toString();
                String upperPriceString = tvHigherPrice.getText().toString();
                Log.d(TAG,"This is the value of the lowerPrice and upperPrice String : " + lowerPriceString + " " + upperPriceString);
                lowerPrice = Integer.parseInt(lowerPriceString.substring(1));
                upperPrice = Integer.parseInt(upperPriceString.substring(1));
            } catch(Exception e){
                // Do nothing
            }
            Log.d(TAG,"This is the value of the lower and upper prices respectively : " + lowerPrice + " "  + upperPrice);
            if (filterBasedOnPrice(suggestedItem,upperVal,lowerVal)){
                temp.add(suggestedItem);
            }
        }
        itemList.clear();
        itemList.addAll(temp);
        adapter.notifyDataSetChanged();
    }

    private void createFilterOptionsDialog() {
        dialogBuilder = new AlertDialog.Builder(this);
        final View filterPopupView = getLayoutInflater().inflate(R.layout.popup, null);
        etPriceMin = (EditText) filterPopupView.findViewById(R.id.etLowerPriceEntry);
        etPriceMax = (EditText) filterPopupView.findViewById(R.id.etHighPriceEntry);

        btnSave = (AppCompatButton) filterPopupView.findViewById(R.id.btnSave);
        btnCancel = (AppCompatButton) filterPopupView.findViewById(R.id.btnCancel);

        dialogBuilder.setView(filterPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lowerBound = etPriceMin.getText().toString();
                String upperBound = etPriceMax.getText().toString();

                try {
                    lowerPrice = Integer.parseInt(lowerBound);
                    upperPrice = Integer.parseInt(upperBound);
                    Log.d(TAG,"This is the value of the lowerPrice : " + lowerPrice);
                    Log.d(TAG,"This is the value of the upperPrice : " + upperPrice);

                    if (position != 2){
                        filterEntireArray(lowerPrice, upperPrice);
                    }

                    if (lowerPrice < 0 || upperPrice < 0) {
                        Toast.makeText(StoreActivity.this, "Entries must be positive", Toast.LENGTH_SHORT);
                    } else {
                        dialog.dismiss();
                        tvLowerPrice.setText("Low : $" + Integer.toString(lowerPrice));
                        tvHigherPrice.setText("High : $" + Integer.toString(upperPrice));
                        Log.d(TAG,"The value of the position here is : " + position);
//                        if (position != 2){
//                            filterEntireArray(lowerPrice, upperPrice);
//                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(StoreActivity.this, "Please enter a valid price", Toast.LENGTH_SHORT);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void convertToHashSet() {

        for (int i = 0; i < searchWordsArray.length();++i){
            try {
                String word = (String) searchWordsArray.get(i);
                searchWordSet.add(word);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private int getCorrelation(String searchWord){
        int correlation = 0;
        String lowerCaseSearchWord = searchWord.toLowerCase();
        String str[] = lowerCaseSearchWord.split(" ");
        List<String> al = new ArrayList<String>();
        al = Arrays.asList(str);
        for (int i = 0; i < al.size(); ++i){
            String word = al.get(i);
            if (searchWordSet.contains(word)){
                ++correlation;
            }
        }
        return correlation;
    }

    private int getAmazonCorrelation(SuggestedItem suggestedItem){

        int correlation = 0;
        correlation += getCorrelation(suggestedItem.getProductName());
        correlation += processAmazonRatings(suggestedItem.getProductRatings());
        return correlation;
    }

    private int getAsosCorrelation(SuggestedItem suggestedItem){
        return 0;
    }

    private int getNikeCorrelation(SuggestedItem suggestedItem){
        return 0;
    }

    private int getShoesStoreCorrelation(SuggestedItem suggestedItem){
        return 0;
    }

    private double processAmazonRatings(String amazonRating){
        double ans = 0;
        String str[] = amazonRating.split(" ");
        List<String> al = new ArrayList<String>();
        try{
            ans = Double.parseDouble(al.get(0));
        } catch(Exception e){
            e.printStackTrace();
        }
        return ans;
    }

}