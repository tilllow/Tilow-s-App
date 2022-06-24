package fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.hfad.exploreshopping.ItemsAdapter;
import com.hfad.exploreshopping.R;
import com.hfad.exploreshopping.SuggestedItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class HomeFragment extends Fragment {
    public static final String TAG = "HomeFragment";
    private List<SuggestedItem> itemList;
    private EditText etFragmentSearch;
    private ImageButton ibFragmentSearch;
    private ItemsAdapter adapter;
    private RecyclerView rvFragmentItems;

    public HomeFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvFragmentItems = view.findViewById(R.id.rvFragmentItems);
        etFragmentSearch = view.findViewById(R.id.etFragmentSearch);
        ibFragmentSearch = view.findViewById(R.id.ibFragmentSearch);
        itemList = new ArrayList<>();
        adapter = new ItemsAdapter(getContext(),itemList);
        rvFragmentItems.setAdapter(adapter);
        rvFragmentItems.setLayoutManager(new GridLayoutManager(getContext(),3));
        rvFragmentItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        rvFragmentItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));


        ibFragmentSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchItem = etFragmentSearch.getText().toString();
                //Toast.makeText(getContext(),"This is the search item " + searchItem,Toast.LENGTH_SHORT).show();
                if (searchItem != null){
                    requestProducts(searchItem);
                }

            }
        });
    }

    private void requestProducts(String searchItem){
        String apiEndpoint = "https://amazon24.p.rapidapi.com/api/product?categoryID=aps&keyword=" + searchItem + "&country=US&page=1";
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        headers.put("X-RapidAPI-Key","60a5fa691emshe919d45f1178b45p1b5687jsnd340457a2d74");
        headers.put("X-RapidAPI-Host","amazon24.p.rapidapi.com");

        client.get(apiEndpoint, headers, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {

                Log.d(TAG, "OnSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray docs = jsonObject.getJSONArray("docs");
                    //itemList.clear();
                    List<SuggestedItem> items = new ArrayList<>();

                    for (int i = 0; i < Math.min(docs.length(),21);++i){
                        JSONObject productInfo = docs.getJSONObject(i);
                        String productName = productInfo.getString("product_title");
                        String productImageUrl = productInfo.getString("product_main_image_url");
                        String productPrice = productInfo.getString("app_sale_price_currency") + productInfo.getString("app_sale_price");
                        String productDetailUrl = productInfo.getString("product_detail_url");
                        String productOriginalPrice = productInfo.getString("original_price");
                        String productRatings = productInfo.getString("evaluate_rate");
                        SuggestedItem suggestedItem = new SuggestedItem(productName,productImageUrl,productPrice,productDetailUrl,productOriginalPrice,productRatings);
                        items.add(suggestedItem);
                    }
                    Log.d(TAG,"This is the item's length" + items.size());
                    itemList.addAll(items);
                    adapter.notifyDataSetChanged();
                    //Log.d(TAG,productDescription);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "OnFailure",throwable);
            }
        });
    }
}