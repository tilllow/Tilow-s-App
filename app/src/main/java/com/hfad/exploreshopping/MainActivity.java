package com.hfad.exploreshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationBarView;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fragments.HomeFragment;
import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    ArrayList<SuggestedItem> itemList = new ArrayList<>();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getUserPurchasedItems();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
//
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {
                Fragment fragment = new HomeFragment();
                switch (menuitem.getItemId()) {
                    case R.id.action_home:
                        // TODO: update fragment
                        Toast.makeText(MainActivity.this,"Items should load here",Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_cart:
                        Toast.makeText(MainActivity.this,"Cart!",Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        break;
                    case R.id.action_profile:
                        Toast.makeText(MainActivity.this,"Profile!",Toast.LENGTH_SHORT).show();
                        fragment = new HomeFragment();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }


//    private void getUserPurchasedItems() {
//        ParseUser user = ParseUser.getCurrentUser();
//        String userId = user.getObjectId();
//        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsPurchased");
//        query.getInBackground(userId, new GetCallback<ParseObject>() {
//            @Override
//            public void done(ParseObject object, ParseException e) {
//                if (e == null){
//                    JSONArray itemsPurchased = object.getJSONArray("ItemsPurchased");
//                    for (int i = 0; i < itemsPurchased.length(); ++i){
//                        JSONObject jsonObject = null;
//                        try {
//                            jsonObject = (JSONObject) itemsPurchased.get(i);
//                            String itemId = jsonObject.getString("objectId");
//                        } catch (JSONException ex) {
//                            ex.printStackTrace();
//                        }
//
//                    }
//                    // Do what you want to do with the user's JsonArray
//                    // Some piece of code here
//                } else {
//                    // Figure out how to deal with the errors
//                }
//            }
//        });
//    }


//    private void requestProducts(String searchItem){
//        String apiEndpoint = "https://amazon24.p.rapidapi.com/api/product?categoryID=aps&keyword=" + searchItem + "&country=US&page=1";
//        AsyncHttpClient client = new AsyncHttpClient();
//        RequestParams params = new RequestParams();
//        RequestHeaders headers = new RequestHeaders();
//        headers.put("X-RapidAPI-Key","60a5fa691emshe919d45f1178b45p1b5687jsnd340457a2d74");
//        headers.put("X-RapidAPI-Host","amazon24.p.rapidapi.com");
//
//        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Headers headers, JSON json) {
//
//                Log.d(TAG, "OnSuccess");
//                JSONObject jsonObject = json.jsonObject;
//                try {
//                    JSONArray docs = jsonObject.getJSONArray("docs");
//
//                    for (int i = 0; i < docs.length();++i){
//                        JSONObject productInfo = docs.getJSONObject(i);
//                        String productName = productInfo.getString("product_title");
//                        String productImageUrl = productInfo.getString("product_main_image_url");
//                        String productPrice = productInfo.getString("app_sale_price_currency") + productInfo.getString("app_sale_price");
//                        String productDetailUrl = productInfo.getString("product_detail_url");
//                        String productOriginalPrice = productInfo.getString("original_price");
//                        String productRatings = productInfo.getString("evaluate_rate");
//
//                        SuggestedItem suggestedItem = new SuggestedItem(productName,productImageUrl,productPrice,productDetailUrl,productOriginalPrice,productRatings);
//                        itemList.clear();
//                        itemList.add(suggestedItem);
//                    }
//                    //Log.d(TAG,productDescription);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                Log.e(TAG, "OnFailure",throwable);
//            }
//        });
//    }
}