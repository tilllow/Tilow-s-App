package com.hfad.exploreshopping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
    private Context context;
    private List<SuggestedItem> itemsList;

    public ItemsAdapter(Context context, List<SuggestedItem> itemsList) {
        this.context = context;
        this.itemsList = itemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_individual_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        Toast.makeText(context, "The item count is : " + itemsList.size(), Toast.LENGTH_SHORT);
        return itemsList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SuggestedItem item = itemsList.get(position);
        holder.bind(item);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvProductName;
        private TextView tvProductPrice;
        private ImageView ivProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    SuggestedItem suggestedItem = itemsList.get(pos);
                    Intent i = new Intent(context, ProductDetailActivity.class);
                    i.putExtra("EXTRA_ITEM", Parcels.wrap(suggestedItem));
                    context.startActivity(i);
                }
            });
        }

        public void bind(SuggestedItem item) {
            tvProductName.setText(item.getProductName());
            tvProductPrice.setText(item.getProductPrice());

            Glide.with(context).load(item.getProductImageUrl()).into(ivProductImage);
        }
    }

    public void clear() {
        itemsList.clear();
    }

    public void AddAll(ArrayList<SuggestedItem> suggestedItems) {
        itemsList.addAll(suggestedItems);
        notifyDataSetChanged();
        //Toast.makeText(context, "Adapter has been notified successfully", Toast.LENGTH_SHORT).show();
    }

    public void setFilteredList(List<SuggestedItem> filteredList) {
        this.itemsList = filteredList;
        notifyDataSetChanged();
    }
}
