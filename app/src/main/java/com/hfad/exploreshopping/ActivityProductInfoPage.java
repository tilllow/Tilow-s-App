package com.hfad.exploreshopping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ActivityProductInfoPage extends AppCompatActivity {

    public static final String TAG = "ActivityProductInfoPage";
    List<qrCodeProduct> allProducts;
    QrProductsAdapter qrProductsAdapter;
    RecyclerView rvQrProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Inside the ProductInfoPage's onCreate method");
        setContentView(R.layout.activity_product_info_page);
        allProducts = new ArrayList<>();
        qrProductsAdapter = new QrProductsAdapter(ActivityProductInfoPage.this,allProducts);

        rvQrProducts = findViewById(R.id.rvQrProducts);
        rvQrProducts.setAdapter(qrProductsAdapter);
        rvQrProducts.setLayoutManager(new LinearLayoutManager(ActivityProductInfoPage.this));

        String qrCode = getIntent().getStringExtra("QR_CODE");
        queryProducts(qrCode);
    }

    private void queryProducts(String qrCode) {

        Log.d(TAG,"The queryProducts method has been called");
        String apiEndpoint = "https://barcode-lookup.p.rapidapi.com/v3/products";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("barcode",qrCode);
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host", "barcode-lookup.p.rapidapi.com");

        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG,"Item has been queried successfully");
                JSONObject jsonObject = json.jsonObject;

                try{
                    JSONArray products = jsonObject.getJSONArray("products");

                    for (int i = 0; i < products.length(); ++i){

                        JSONObject possibleProduct = (JSONObject) products.get(i);
                        String productTitle = possibleProduct.getString("title");
                        String productDescription = possibleProduct.getString("description");
                        JSONArray imageArray = possibleProduct.getJSONArray("images");
                        String productImage = imageArray.getString(0);
                        JSONArray stores = possibleProduct.getJSONArray("stores");
                        ArrayList<ProductStore> productStores = new ArrayList<>();

                        for (int j = 0; j < stores.length(); ++j){
                            JSONObject jsonObject1 = (JSONObject) stores.get(i);
                            String storeName = jsonObject1.getString("name");
                            String currencySymbol = jsonObject1.getString("currency_symbol");
                            String itemPrice = jsonObject1.getString("price");
                            String storeUrl = jsonObject1.getString("link");
                            String lastUpdated = jsonObject1.getString("last_update");

                            ProductStore eachStore = new ProductStore(storeName,currencySymbol,itemPrice,storeUrl,lastUpdated);
                            productStores.add(eachStore);
                        }

                        qrCodeProduct foundProduct = new qrCodeProduct(productTitle,productDescription,productImage,productStores);
                        allProducts.add(foundProduct);
                        qrProductsAdapter.notifyItemInserted(allProducts.size());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {

            }
        });
    }
}