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
import com.hfad.exploreshopping.PurchaseItem;
import com.hfad.exploreshopping.R;

import java.util.ArrayList;
import java.util.List;

public class PurchasedItemsAdapter extends RecyclerView.Adapter<PurchasedItemsAdapter.ViewHolder>{

    private Context context;
    private List<PurchaseItem> purchaseItemList;

    public PurchasedItemsAdapter(Context context, List<PurchaseItem> purchaseItemList){
        this.context = context;
        this.purchaseItemList = purchaseItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_purchased_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        Toast.makeText(context,"The item count is : " + purchaseItemList.size(),Toast.LENGTH_SHORT);
        return purchaseItemList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseItem item = purchaseItemList.get(position);
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

//            btnCartItemBuy.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // TODO: Add the cart item to the list of items Purchased and remove the item from the cart items
//                }
//            });
        }

        public void bind(PurchaseItem item) {
            tvPurchasedProductName.setText(item.getProductName());
            tvPurchasedItemPrice.setText("Purchase Price : " + item.getProductPrice());
            if (item.getPurchaseDate() != null){
                tvCartProductRatings.setText("Purchase Date : " + item.getPurchaseDate());
            }
            Glide.with(context).load(item.getProductImageUrl()).into(ivItemPurchasedPicture);
        }
    }

    public void clear(){
        purchaseItemList.clear();
    }

    public void AddAll(ArrayList<PurchaseItem> purchaseItems){
        purchaseItemList.addAll(purchaseItems);
    }

}
