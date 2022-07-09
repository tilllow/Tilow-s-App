package com.hfad.exploreshopping;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hfad.exploreshopping.CartItem;
import com.hfad.exploreshopping.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.hfad.exploreshopping.CartItemsAdapter;

public class CartItemsFragment extends Fragment {

    ParseQuery<CartItem> query;
    RecyclerView rvCartItems;
    List<CartItem> cartItemsList;
    CartItemsAdapter cartItemsAdapter;
    List<CartItem> temp = new ArrayList<>();

    public CartItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart_items, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCartItems = view.findViewById(R.id.rvCartItems);
        cartItemsList = new ArrayList<>();
        cartItemsAdapter = new CartItemsAdapter(getContext(),cartItemsList);
        rvCartItems.setAdapter(cartItemsAdapter);
        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCartItems.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        populateCartItemsList();
    }

    private void populateCartItemsList() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        JSONArray itemsInCart = currentUser.getJSONArray("CartItems");
        if (itemsInCart == null){
            return;
        }
        for (int i = 0; i < itemsInCart.length();++i){
            try{
                JSONObject cartItem = (JSONObject) itemsInCart.get(i);
                String itemId = (String) cartItem.get("objectId");
                queryPurchaseItem(itemId);
            } catch (Exception e){
                // TODO: Decide how to handle this exception later
            }
        }
    }

    private void queryPurchaseItem(String itemId) {
        query = ParseQuery.getQuery(CartItem.class);
        query.addDescendingOrder("createdAt");
        query.getInBackground(itemId, new GetCallback<CartItem>() {
            @Override
            public void done(CartItem object, ParseException e) {
                if (e == null){
                    cartItemsList.add(object);
                    cartItemsAdapter.notifyDataSetChanged();
                } else{
                    // TODO: Decide how to handle any error that may result later
                }
            }
        });
    }
}