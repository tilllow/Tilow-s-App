package com.hfad.exploreshopping;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvPurchasedItems);
    }

    PurchaseItem deletedPurchaseItem = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedPurchaseItem = purchasedItemsList.get(position);
                    String itemId = deletedPurchaseItem.getObjectId();
                    purchasedItemsList.remove(position);
                    purchasedItemsAdapter.notifyItemRemoved(position);
                    final boolean[] isUndone = {false};
                    Snackbar.make(rvPurchasedItems,deletedPurchaseItem.getProductName() + " removed",Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    purchasedItemsList.add(position,deletedPurchaseItem);
                                    purchasedItemsAdapter.notifyItemInserted(position);
                                    isUndone[0] = true;
                                }
                            })
                            .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    if (isUndone[0] == false){
                                        deletedPurchaseItem.deleteInBackground();
                                    }
                                    super.onDismissed(transientBottomBar, event);
                                }

                                @Override
                                public void onShown(Snackbar transientBottomBar) {
                                    super.onShown(transientBottomBar);
                                }
                            })
                            .show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.background_color_light))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.red))
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    private void populatePurchasedItemsList(){
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<PurchaseItem> temp ;
        JSONArray itemsPurchased = currentUser.getJSONArray("ItemsPurchased");
        temp = new ArrayList<>();

        if (itemsPurchased == null){
            return;
        }
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
        query.addDescendingOrder("createdAt");

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