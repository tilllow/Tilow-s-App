package com.hfad.exploreshopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.exploreshopping.PurchaseItem;
import com.hfad.exploreshopping.PurchasedItemsAdapter;
import com.hfad.exploreshopping.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PurchasedItemsFragment extends Fragment {

    public static final String TAG = "PurchasedItemsFragment";
    ParseQuery<PurchaseItem> query;
    RecyclerView rvPurchasedItems;
    List<PurchaseItem> purchasedItemsList;
    PurchasedItemsAdapter purchasedItemsAdapter;
    List<PurchaseItem> temp = new ArrayList<>();

    public PurchasedItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_purchased_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPurchasedItems = view.findViewById(R.id.rvPurchasedItems);
        purchasedItemsList = new ArrayList<>();
        purchasedItemsAdapter = new PurchasedItemsAdapter(getContext(),purchasedItemsList);
        rvPurchasedItems.setAdapter(purchasedItemsAdapter);
        rvPurchasedItems.setLayoutManager(new LinearLayoutManager(getContext()));
        //rvPurchasedItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        populatePurchasedItemsList();
    }

    private void populatePurchasedItemsList(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<PurchaseItem> temp = null;
        JSONArray itemsPurchased = currentUser.getJSONArray("ItemsPurchased");
        temp = new ArrayList<>();
        for (int i = 0; i < itemsPurchased.length();++i){
            try{

                JSONObject purchaseItem = (JSONObject) itemsPurchased.get(i);
                String itemId = (String) purchaseItem.get("objectId");
                queryPurchaseItem(itemId);

            } catch (Exception e){
                // TODO: Decide how to handle this exception later
            }
        }
    }

    private void queryPurchaseItem(String itemId) {
        query = ParseQuery.getQuery(PurchaseItem.class);
        query.getInBackground(itemId, new GetCallback<PurchaseItem>() {
            @Override
            public void done(PurchaseItem object, ParseException e) {
                if (e == null){
                    purchasedItemsList.add(object);
                    purchasedItemsAdapter.notifyDataSetChanged();
                    Log.d(TAG,"The length of my Purchased items array is : " + purchasedItemsList.size());
                    Log.d(TAG,object + "has been added successfully");
                } else{
                    Log.d(TAG,"There was an error with fetching this data");
                }
            }
        });
    }
}