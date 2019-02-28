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

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.activity.Product_ListActivity;
import com.example.admin.app_sales.model.Catelory;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemDetailAdapter extends RecyclerView.Adapter<ItemDetailAdapter.ViewHolder>{
    Context context;
    ArrayList<Catelory> catelories;


    public ItemDetailAdapter(Context context , ArrayList<Catelory> catelories ) {
        this.context = context;
        this.catelories = catelories;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recylerview , parent , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.textView.setText(catelories.get(position).getTenLoaiCuThe());
        Picasso.get().load(catelories.get(position).getUrl())
                .centerCrop()
                .placeholder(R.drawable.imgdefault)
                .error(R.drawable.imgdefault)
                .fit()
                .into(holder.imageView);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context , Product_ListActivity.class);
                intent.putExtra("TenLoaiCuThe" , catelories.get(position).getTenLoaiCuThe());
                intent.putExtra("MaLoaiCuThe" , catelories.get(position).getMaLoaiCuThe());

                ((MainActivity)context).startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return catelories.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgItem);
            textView = itemView.findViewById(R.id.txtItem);
            linearLayout = itemView.findViewById(R.id.layoutItemListDetai);
        }
    }
}
