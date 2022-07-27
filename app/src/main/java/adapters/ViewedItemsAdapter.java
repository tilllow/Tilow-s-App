package adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.exploreshopping.ClickedItem;
import com.hfad.exploreshopping.ProductDetailActivity;
import com.hfad.exploreshopping.R;
import com.hfad.exploreshopping.SuggestedItem;

import org.parceler.Parcels;

import java.util.List;

public class ViewedItemsAdapter extends RecyclerView.Adapter<ViewedItemsAdapter.ViewHolder>{

    private Context context;
    private List<ClickedItem> clickedItemList;

    public ViewedItemsAdapter(Context context, List<ClickedItem> viewedItemList) {
        this.context = context;
        this.clickedItemList = viewedItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewed_items_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ClickedItem clickedItem = clickedItemList.get(position);
        holder.bind(clickedItem);
    }



    @Override
    public int getItemCount() {
        return clickedItemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvViewedItemProductName;
        private TextView tvProductPrice;
        private ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvViewedItemProductName = itemView.findViewById(R.id.tvClickedItemProductName);
            tvProductPrice = itemView.findViewById(R.id.tvClickedItemProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    ClickedItem clickedItem = clickedItemList.get(pos);
                    SuggestedItem suggestedItem = new SuggestedItem(clickedItem);
                    Intent i = new Intent(context, ProductDetailActivity.class);
                    i.putExtra("EXTRA_ITEM", Parcels.wrap(suggestedItem));
                    context.startActivity(i);
                }
            });

        }

        public void bind(ClickedItem clickedItem) {

            tvViewedItemProductName.setText(clickedItem.getProductName());
            tvProductPrice.setText(clickedItem.getProductPrice());
            Glide.with(context).load(clickedItem.getProductImageUrl()).into(ivProductImage);
        }
    }
}
