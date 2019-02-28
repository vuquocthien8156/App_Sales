package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.app_sales.ImageLoader;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.DetailActivity;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.model.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ItemSnapAdapter extends RecyclerView.Adapter<ItemSnapAdapter.ViewHolderItem> {
    private Context context;
    private ArrayList<Product> products;

    public ItemSnapAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_on_snap , null);
        return new ViewHolderItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItem holder, int position) {
        Product product = products.get(position);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textView.setText(decimalFormat.format(product.getGia()) + "đ");
        holder.textView2.setText(product.getName());

        ImageLoader imageLoader = new ImageLoader(context);
        imageLoader.DisplayImage(product.getUrl() , R.drawable.imgdefault , holder.imageView);
//        Picasso.get().load(product.getUrl())
//                .placeholder(R.drawable.imgdefault)
//                .error(R.drawable.imgdefault)
//                .centerCrop()
//                .fit()
//                .into(holder.imageView);
//        GlideApp.with(context).load(product.getUrl())
//                .centerCrop()
//                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ViewHolderItem extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView , textView2;
        LinearLayout linearLayout;
        public ViewHolderItem(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItemOnSnap);
            textView = itemView.findViewById(R.id.txtGiaItemOnSnap);
            textView2 = itemView.findViewById(R.id.txtTenSanPham);
            linearLayout = itemView.findViewById(R.id.layoutItemOnSnap);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(((MainActivity)context) , DetailActivity.class);
                    intent.putExtra("product" , products.get(getAdapterPosition()));

                    ((MainActivity)context).startActivity(intent);
                }
            });
        }
    }
}
