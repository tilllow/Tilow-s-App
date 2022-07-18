package adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hfad.exploreshopping.ProductStore;
import com.hfad.exploreshopping.R;
import com.hfad.exploreshopping.StoreWebView;
import com.hfad.exploreshopping.qrCodeProduct;

import java.util.List;

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.ViewHolder> {

    public static final String TAG = "StoresAdapter";
    private Context context;
    private List<ProductStore> productStoreList;
    private com.hfad.exploreshopping.qrCodeProduct qrCodeProduct;

    public StoresAdapter(Context context, List<ProductStore> productStoreList, qrCodeProduct qrCodeProduct) {
        this.context = context;
        this.productStoreList = productStoreList;
        this.qrCodeProduct = qrCodeProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_stores_qr_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductStore productStore = productStoreList.get(position);
        holder.bind(productStore);
    }

    @Override
    public int getItemCount() {
        return productStoreList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvQrStoreName;
        private TextView tvQrProductName;
        private TextView tvQrProductPrice;
        private ImageView ivQrStoreImage;
        private TextView tvLastUpdated;
        private TextView tvStoreDetails;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            tvQrStoreName = itemView.findViewById(R.id.tvQrStoreName);
            tvQrProductName = itemView.findViewById(R.id.tvQrProductName);
            tvQrProductPrice = itemView.findViewById(R.id.tvQrProductPrice);
            ivQrStoreImage = itemView.findViewById(R.id.ivQrStoreImage);
            tvLastUpdated = itemView.findViewById(R.id.tvLastUpdated);
            tvStoreDetails = itemView.findViewById(R.id.tvStoreDetails);

        }

        public void bind(ProductStore productStore) {

            tvQrStoreName.setText(productStore.getStoreName());
            tvQrProductName.setText(qrCodeProduct.getProductTitle());
            tvQrProductPrice.setText(productStore.getProductCurrencySymbol() + productStore.getProductPrice());
            Glide.with(context).load(qrCodeProduct.getProductImageUrl()).into(ivQrStoreImage);
            tvLastUpdated.setText(productStore.getLastUpdated());

            tvStoreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StoreWebView.class);
                    intent.putExtra("EXTRA_STORE_IMAGE_URL", productStore.getStoreUrl());
                    Log.d(TAG, "This is the url for the store : " + productStore.getStoreUrl());
                    context.startActivity(intent);
                }
            });
        }
    }

}


