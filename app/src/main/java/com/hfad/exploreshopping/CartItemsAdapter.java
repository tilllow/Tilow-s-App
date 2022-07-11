package com.hfad.exploreshopping;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.parse.DeleteCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {
    public static final String TAG = "CartItemsAdapter";
    private Context context;
    private List<CartItem> cartItemList;

    public CartItemsAdapter(Context context, List<CartItem> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        Toast.makeText(context, "The item count is : " + cartItemList.size(), Toast.LENGTH_SHORT);
        return cartItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItemList.get(position);
        holder.bind(item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPurchasedProductName;
        private TextView tvPurchasedItemPrice;
        private TextView tvCartProductRatings;
        private ImageView ivItemPurchasedPicture;
        private Button btnCartItemBuy;
        private Button btnCartRemove;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPurchasedProductName = itemView.findViewById(R.id.tvCartProductName);
            tvPurchasedItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartProductRatings = itemView.findViewById(R.id.tvCartProductRatings);
            ivItemPurchasedPicture = itemView.findViewById(R.id.ivCartItem);
            btnCartItemBuy = itemView.findViewById(R.id.btnCartItemBuy);
            btnCartRemove = itemView.findViewById(R.id.btnCartRemove);

        }

        public void bind(CartItem item) {
            tvPurchasedProductName.setText(item.getProductName());
            tvPurchasedItemPrice.setText("Purchase Price : " + item.getProductPrice());
            if (item.getProductRatings() != null) {
                tvCartProductRatings.setText("Product Ratings : " + item.getProductRatings());
            }
            Glide.with(context).load(item.getProductImageUrl()).into(ivItemPurchasedPicture);
            btnCartItemBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PurchaseItem newPurchaseItem = new PurchaseItem(item);
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    newPurchaseItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(context, "Purchase unsuccessful", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            currentUser.add("ItemsPurchased", newPurchaseItem);
                            currentUser.saveInBackground();
                            Toast.makeText(context, "Purchase successful", Toast.LENGTH_SHORT).show();

                            item.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        int pos = getAdapterPosition();
                                        cartItemList.remove(pos);
                                        notifyDataSetChanged();
                                    }
                                }
                            });
                        }
                    });
                }
            });

            btnCartRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PurchaseItem newPurchaseItem = new PurchaseItem(item);
                    newPurchaseItem.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Toast.makeText(context, "Unable to remove item from cart", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            item.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e == null) {
                                        int pos = getAdapterPosition();
                                        cartItemList.remove(pos);
                                        notifyDataSetChanged();
                                        Toast.makeText(context, "Item Removed Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    public void clear() {
        cartItemList.clear();
    }

    public void AddAll(ArrayList<CartItem> cartItems) {
        cartItemList.addAll(cartItems);
    }
}
