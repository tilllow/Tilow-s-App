package com.hfad.exploreshopping;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class QrProductsAdapter extends RecyclerView.Adapter<QrProductsAdapter.ViewHolder> {

    public static final String TAG = "QrProductsAdapter";
    private Context context;
    private ArrayList<qrCodeProduct> qrCodeProducts;

    public QrProductsAdapter(Context context, ArrayList<qrCodeProduct> qrCodeProducts) {

        this.context = context;
        this.qrCodeProducts = qrCodeProducts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.possible_product_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        qrCodeProduct qrCodeProduct = qrCodeProducts.get(position);
        holder.bind(qrCodeProduct);
    }

    @Override
    public int getItemCount() {
        return qrCodeProducts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQrProductName;
        private TextView tvQrProductDescription;
        private ImageView ivQrProductImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQrProductDescription = itemView.findViewById(R.id.tvQrProductDetails);
            tvQrProductName = itemView.findViewById(R.id.tvQrProductTitle);
            ivQrProductImage = itemView.findViewById(R.id.ivQrProductPicture);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Log.d(TAG, "This is the length of the position returned from the getAbsoluteAdapterPosition method : " + pos);
                    Intent intent = new Intent(context, ActivityStoresForProduct.class);
                    qrCodeProduct temp = qrCodeProducts.get(pos);
                    intent.putExtra("productTitle", temp.getProductTitle());
                    intent.putExtra("productDescription", temp.getProductDescription());
                    intent.putExtra("productImageUrl", temp.getProductImageUrl());
                    intent.putParcelableArrayListExtra("productStores", temp.getProductStores());
                    Log.d(TAG, "This is the value of the productStores being passed along" + temp.getProductStores());
                    Log.d(TAG, "The length of the arraylist here is given by : " + temp.getProductStores().size());
                    Log.d(TAG, "The length of the arraylist here is given by : " + temp.getProductStores().size());
                    context.startActivity(intent);
                }
            });
        }

        public void bind(qrCodeProduct product) {

            tvQrProductName.setText(product.getProductTitle());
            tvQrProductDescription.setText(product.getProductDescription());
            Glide.with(context).load(product.getProductImageUrl()).into(ivQrProductImage);
        }
    }
}
