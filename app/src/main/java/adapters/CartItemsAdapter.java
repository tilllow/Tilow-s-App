package adapters;

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
import com.hfad.exploreshopping.CartItem;
import com.hfad.exploreshopping.R;

import java.util.ArrayList;
import java.util.List;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder>{
    private Context context;
    private List<CartItem> cartItemList;

    public CartItemsAdapter(Context context, List<CartItem> cartItemList){
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_cart_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        Toast.makeText(context,"The item count is : " + cartItemList.size(),Toast.LENGTH_SHORT);
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPurchasedProductName = itemView.findViewById(R.id.tvCartProductName);
            tvPurchasedItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartProductRatings = itemView.findViewById(R.id.tvCartProductRatings);
            ivItemPurchasedPicture = itemView.findViewById(R.id.ivCartItem);
            btnCartItemBuy = itemView.findViewById(R.id.btnCartItemBuy);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    PurchaseItem purchaseItem = purchaseItemList.get(pos);
//                    Intent i = new Intent(context,ProductDetailActivity.class);
//                    i.putExtra("EXTRA_ITEM", Parcels.wrap(purchaseItem));
//                    context.startActivity(i);
//                }
//            });
        }

        public void bind(CartItem item) {
            tvPurchasedProductName.setText(item.getProductName());
            tvPurchasedItemPrice.setText("Purchase Price : " + item.getProductPrice());
            Glide.with(context).load(item.getProductImageUrl()).into(ivItemPurchasedPicture);
        }
    }

    public void clear(){
        cartItemList.clear();
    }

    public void AddAll(ArrayList<CartItem> cartItems){
        cartItemList.addAll(cartItems);
        //notifyDataSetChanged();
        //Toast.makeText(context, "Adapter has been notified successfully", Toast.LENGTH_SHORT).show();
    }
}
