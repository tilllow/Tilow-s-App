package fragments;

import static java.util.Collections.sort;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.hfad.exploreshopping.ActivityQrCodeScanner;
import com.hfad.exploreshopping.Cache;
import com.hfad.exploreshopping.ClickedItem;
import com.hfad.exploreshopping.PurchaseItem;
import com.hfad.exploreshopping.R;
import com.hfad.exploreshopping.Store;
import com.hfad.exploreshopping.StoreActivity;
import com.hfad.exploreshopping.SuggestedItem;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import adapters.ItemsAdapter;
import adapters.ViewedItemsAdapter;
import okhttp3.Headers;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";
    private List<SuggestedItem> itemList;
    private List<ClickedItem> recentlyViewed;
    private ViewedItemsAdapter adapter;
    private RecyclerView rvFragmentItems;
    private SearchView svSearchProduct;
    private ImageView ivScanQrCode;
    private ProgressBar progressBar;
    private GridView gvStores;
    private int images[] = {R.drawable.nike_photo, R.drawable.asos_logo, R.drawable.amazon_simple_logo, R.drawable.new_background};
    private String names[] = {"Nike Store", "Asos Store", "Amazon Store", "Shoes collection"};
    private String description[] = {"This is the Nike Store", "This is the Asos Store", "This is the Amazon Store", "This is the Shoe Store"};
    private List<Store> storeList = new ArrayList<>();
    private TextView tvRecentlyViewed;
    private CustomAdapter customAdapter;


    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"This is also a random log");
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        rvFragmentItems = view.findViewById(R.id.rvFragmentItems);
        svSearchProduct = view.findViewById(R.id.svSearchProduct);
        progressBar = view.findViewById(R.id.progressBar);
        gvStores = view.findViewById(R.id.gvStores);
        tvRecentlyViewed = view.findViewById(R.id.tvRecentlyViewed);
        ivScanQrCode = view.findViewById(R.id.ivScanQrCode);

        for (int i = 0; i < names.length; ++i) {
            Store store = new Store(names[i], description[i], images[i]);
            storeList.add(store);
        }

        customAdapter = new CustomAdapter(storeList, getContext());
        gvStores.setAdapter(customAdapter);

        gvStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                launchStoreActivity(position);
            }
        });

        ivScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityQrCodeScanner.class);
                startActivity(intent);
            }
        });




        svSearchProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                requestAmazonProducts(query);
                progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        itemList = new ArrayList<>();
        recentlyViewed = new ArrayList<>();
        adapter = new ViewedItemsAdapter(getContext(), recentlyViewed);
        rvFragmentItems.setAdapter(adapter);
        rvFragmentItems.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        populateViewedItems();
    }

    private void launchStoreActivity(int pos) {
        Intent i = new Intent(getContext(), StoreActivity.class);
        i.putExtra("EXTRA_POSITION", pos);
        startActivity(i);
    }

    private void requestAmazonProducts(String searchItem) {

        String apiEndpoint = "https://amazon24.p.rapidapi.com/api/product";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("categoryID", "aps");
        params.put("keyword", searchItem);
        params.put("country", "US");
        params.put("page", 1);
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key", "11823e50fcmsh8e85454fc85d650p1424d9jsn73755fa48c6a");
        headers.put("X-RapidAPI-Host", "amazon24.p.rapidapi.com");
        itemList.clear();
        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.d(TAG, "OnSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray docs = jsonObject.getJSONArray("docs");
                    //itemList.clear();
                    List<SuggestedItem> items = new ArrayList<>();

                    for (int i = 0; i < Math.min(docs.length(), 21); ++i) {

                        JSONObject productInfo = docs.getJSONObject(i);
                        String productName = productInfo.getString("product_title");
                        String productImageUrl = productInfo.getString("product_main_image_url");
                        String price = productInfo.getString("app_sale_price");
                        String currency = productInfo.getString("app_sale_price_currency");

                        if (price == null) {
                            continue;
                        }

                        String productPrice = currency + price;
                        String productDetailUrl = productInfo.getString("product_detail_url");
                        String productOriginalPrice = productInfo.getString("original_price");
                        String productRatings = productInfo.getString("evaluate_rate");
                        SuggestedItem suggestedItem = new SuggestedItem(productName, productImageUrl, productPrice, productDetailUrl, productOriginalPrice, productRatings);
                        items.add(suggestedItem);
                    }
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();

                    ;
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                //Toast.makeText(getContext(),"Your request could not be completed. Please try again",Toast.LENGTH_SHORT);
                Log.e(TAG, "OnFailure", throwable);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public class CustomAdapter extends BaseAdapter {

        private List<Store> stores;
        private List<Store> storeFiltered;
        private Context context;

        public CustomAdapter(List<Store> stores, Context context) {
            this.stores = stores;
            this.storeFiltered = stores;
            this.context = context;
        }

        @Override
        public int getCount() {
            return storeFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = getLayoutInflater().inflate(R.layout.individual_store, null);
            ImageView ivStoreImage = view.findViewById(R.id.ivStoreImage);
            TextView tvStoreName = view.findViewById(R.id.tvStoreName);
            TextView tvStoreDescription = view.findViewById(R.id.tvStoreDescription);

            ivStoreImage.setImageResource(storeFiltered.get(position).getImage());
            tvStoreName.setText(storeFiltered.get(position).getName());
            tvStoreDescription.setText(storeFiltered.get(position).getDescription());
            return view;
        }
    }

    private void populateUserSearchField(String search) {

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("searchItems", search);
        currentUser.saveInBackground();
    }

    private void populateViewedItems() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        String userId = currentUser.getObjectId();
        List<ClickedItem> temp;

        temp = new ArrayList<>();
        temp = Cache.getUserItemsViewed(userId);
        if (temp != null){
            Cache.updateUserToIdMap(userId);
            recentlyViewed.addAll(temp);
            tvRecentlyViewed.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            return;
        }

        JSONArray itemsClicked = currentUser.getJSONArray("clickedItems");

        if (itemsClicked == null) {
            return;
        }

        HashSet<String> productImageHashSet = new HashSet<String>();
        for (int i = 0; i < itemsClicked.length(); ++i) {
            try {
                JSONObject clickedItem = (JSONObject) itemsClicked.get(i);
                String itemId = (String) clickedItem.get("objectId");
                queryViewedItem(itemId);

            } catch (Exception e) {
                // TODO: Decide how to handle this exception later
            }
        }

        adapter.notifyDataSetChanged();

    }

    private void queryViewedItem(String itemId) {

        ParseQuery<ClickedItem> query = ParseQuery.getQuery(ClickedItem.class);
        query.setLimit(10);
        query.getInBackground(itemId, new GetCallback<ClickedItem>() {
            @Override
            public void done(ClickedItem object, ParseException e) {
                if (e == null) {

                    long DAY_IN_MS = 1000 * 60 * 60 * 24;
                    Date weekAgoDate = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
                    Date objectDate = object.getProductCreationDate();

                    if (objectDate.compareTo(weekAgoDate) < 0){
                        return;
                    }

                    recentlyViewed.add(object);
                    Collections.sort(recentlyViewed, new Comparator<ClickedItem>() {
                        @Override
                        public int compare(ClickedItem o1, ClickedItem o2) {

                            // Sort the viewed items by the user based on the time the user spent on the item and when last the user accessed the item.
                            Date currentTime = Calendar.getInstance().getTime();
                            Date createdAtTime1 = o1.getCreatedAt();
                            Date createdAtTime2= o2.getCreatedAt();
                            Long duration1 = currentTime.getTime() - createdAtTime1.getTime();
                            Long duration2 = currentTime.getTime() - createdAtTime2.getTime();
                            int val = (int) ((int) (o2.getTimeSpent() - o1.getTimeSpent()) + (duration1 - duration2) % 86400);
                            return (int) ((int) (o2.getTimeSpent() - o1.getTimeSpent()) + (duration1 - duration2) % 86400);
                        }
                    });
                    if (recentlyViewed.size() > 0) {
                        tvRecentlyViewed.setVisibility(View.VISIBLE);
                        Cache.updateUserToIdMap(ParseUser.getCurrentUser().getObjectId());
                        Cache.updateViewedItemsMap(ParseUser.getCurrentUser().getObjectId(), recentlyViewed);
                    }
                } else {
                    Log.i(TAG,"This is the error message obtained when trying to fetch the data from the database", e);
                }
            }
        });
    }


}