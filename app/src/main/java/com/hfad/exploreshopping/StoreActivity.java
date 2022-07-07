package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.model.LazyHeaders;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.facebook.stetho.json.annotation.JsonValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class StoreActivity extends AppCompatActivity {
    private List<SuggestedItem> itemList;
    public static final String TAG = "StoreActivity";
    private RecyclerView rvNikeShoes;
    private ItemsAdapter adapter;
    private ProgressBar pbStoreItemLoading;
    private SearchView svSearchStore;
    TextView tvStoreWelcomeMessage;
    TextView tvAmazonTodayDeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        int position = getIntent().getIntExtra("EXTRA_POSITION",0);

        itemList = new ArrayList<>();
        tvStoreWelcomeMessage = findViewById(R.id.tvStoreWelcomeMessage);
        tvAmazonTodayDeals = findViewById(R.id.tvAmazonTodayDeals);
        svSearchStore = findViewById(R.id.svSearchStore);
        rvNikeShoes = findViewById(R.id.rvNikeShoes);
        pbStoreItemLoading = findViewById(R.id.pbStoreItemLoading);

        adapter = new ItemsAdapter(this,itemList);
        rvNikeShoes.setAdapter(adapter);
        rvNikeShoes.setLayoutManager(new GridLayoutManager(this,2));


        switch (position){
            case 0: tvStoreWelcomeMessage.setText("Welcome to the Nike Store");
                    svSearchStore.setQueryHint("Search Nike Shoes here...");
                    requestNikeProducts();
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
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
                tvStoreWelcomeMessage.setText("Welcome to the Asos Store");
                svSearchStore.setQueryHint("Search Asos products here...");
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
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
                tvStoreWelcomeMessage.setText("Welcome to the Amazon Store");
                svSearchStore.setQueryHint("Search Amazon products here...");
                tvAmazonTodayDeals.setVisibility(View.VISIBLE);
                requestAmazonTodayDeals();

                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
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
                tvStoreWelcomeMessage.setText("Welcome to the Shoes Collection Store");
                svSearchStore.setQueryHint("Search your shoes here...");
                requestShoesCollections();
                svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
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

//        svSearchStore.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterList(newText);
//                return true;
//            }
//        });
    }

    private void filterList(String text) {
        List<SuggestedItem> filteredList = new ArrayList<>();

        for (SuggestedItem item : itemList){
            if (item.getProductName().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(item);
            }
            if (!filteredList.isEmpty()){
                adapter.setFilteredList(filteredList);
            }
        }
    }

    private void requestNikeProducts(){
        String nikeApiEndpoint = "https://nike-products.p.rapidapi.com/shoes";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key","11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host","nike-products.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(nikeApiEndpoint, headers, params, new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "OnSuccess");
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<SuggestedItem> items = new ArrayList<>();
                     for (int i = 0; i < jsonArray.length(); ++i){
                         JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                         String shoeImageUrl = jsonObject.getString("img");
                         String productDetailUrl = jsonObject.getString("url");
                         String shoeName = jsonObject.getString("title");
                         String shoePrice = jsonObject.getString("price");

                         SuggestedItem shoeItem = new SuggestedItem(shoeName,shoeImageUrl,shoePrice,productDetailUrl,null,null);
                         items.add(shoeItem);
                     }

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
                Log.e(TAG, "OnFailure",throwable);
//                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void requestAsosProducts(){

        String asosApiEndpoint = "https://asos2.p.rapidapi.com/products/v2/list?store=US&offset=0&categoryId=4209&limit=48&country=US&sort=freshness&currency=USD&sizeSchema=US&lang=en-US";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key","11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host","asos2.p.rapidapi.com");
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

                    for (int i = 0; i < jsonArray.length();++i){
                        JSONObject product = (JSONObject) jsonArray.get(i);
                        String name = product.getString("name");
                        String productImageUrl = product.getString("imageUrl");
                        String productDetailUrl = product.getString("url");
                        JSONObject priceObject = product.getJSONObject("price");
                        JSONObject currentPriceObject = priceObject.getJSONObject("current");
                        JSONObject oldPriceObject = priceObject.getJSONObject("previous");
                        String currentPrice = currentPriceObject.getString("text");
                        String originalPrice = oldPriceObject.getString("text");

                        SuggestedItem productItem = new SuggestedItem(name,productImageUrl,currentPrice,productDetailUrl,originalPrice,null);
                        items.add(productItem);
                    }

                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                }
                catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Toast.makeText(StoreActivity.this,"Your request could not be completed. Please try again",Toast.LENGTH_SHORT);
                Log.e(TAG, "OnFailure",throwable);
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void requestAmazonTodayDeals(){
        String apiEndpoint = "https://amazon24.p.rapidapi.com/api/todaydeals";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key","11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host","amazon24.p.rapidapi.com");
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

                    for (int i = 0; i < docs.length();++i){

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
                        SuggestedItem suggestedItem = new SuggestedItem(productName,productImageUrl,productPrice,productDetailUrl,null,null);
                        items.add(suggestedItem);
                    }
                    itemList.addAll(items);
                    Log.d(TAG,"This is the size of the array returned from the API" + items.size());
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                } catch (JSONException e) {
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    Log.d(TAG,"Something went wrong with this request");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(StoreActivity.this,"Your request could not be completed. Please try again",Toast.LENGTH_SHORT);
                Log.e(TAG, "OnFailure",throwable);
                //progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void requestShoesCollections(){

        String apiEndpoint = "https://shoes-collections.p.rapidapi.com/shoes";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key","11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host","shoes-collections.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);

        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                JSONArray jsonArray = json.jsonArray;

                try{
                    List<SuggestedItem> items = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length();++i){
                        JSONObject productObject = (JSONObject) jsonArray.getJSONObject(i);
                        String productName = productObject.getString("name");
                        String productPrice = "$" + productObject.getString("price");
                        String productImageUrl = productObject.getString("image");
                        String productDescription = productObject.getString("description");

                        SuggestedItem item = new SuggestedItem(productName,productImageUrl,productPrice,productDescription,null,null);
                        items.add(item);
                    }

                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                }catch(JSONException e){
                    e.printStackTrace();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG,"OnFailure",throwable);
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void requestAmazonProducts(String searchWord){

        String amazonApiEndpoint = "https://amazon60.p.rapidapi.com/search/" + searchWord + "?api_key=e4037415b3d58b93e689d4ed83405ffb";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        RequestHeaders requestHeaders = new RequestHeaders();
        requestHeaders.put("X-RapidAPI-Key","11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        requestHeaders.put("X-RapidAPI-Host","amazon60.p.rapidapi.com");
        itemList.clear();
        pbStoreItemLoading.setVisibility(View.VISIBLE);
        // new changes

        client.get(amazonApiEndpoint, requestHeaders, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;


                try{
                    List<SuggestedItem> items = new ArrayList<>();
                    JSONArray products = jsonObject.getJSONArray("results");
                    for (int i = 0; i < products.length();++i){
                        JSONObject product = (JSONObject) products.get(i);

                        String productName = product.getString("name");
                        String productImageUrl = product.getString("image");
                        String productDetailUrl = product.getString("url");
                        String productPrice = product.getString("price_string");
                        String productRatings = String.valueOf(product.getInt("stars")) ;

                        SuggestedItem item = new SuggestedItem(productName,productImageUrl,productPrice,productDetailUrl,null,productRatings);
                        items.add(item);
                        // Made some new changes to the app
                    }
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    tvAmazonTodayDeals.setText("Showing results for " + searchWord);

                } catch(JSONException e){
                    pbStoreItemLoading.setVisibility(View.INVISIBLE);
                    e.printStackTrace();
                }


                Log.d(TAG,"This response passed successfully");
                // Random comment
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                pbStoreItemLoading.setVisibility(View.INVISIBLE);
                Log.i(TAG,"The request has failed",throwable);
            }
        });
    }
}