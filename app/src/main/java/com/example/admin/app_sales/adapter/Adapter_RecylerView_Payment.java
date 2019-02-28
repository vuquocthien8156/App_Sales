package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter_RecylerView_Payment extends RecyclerView.Adapter<Adapter_RecylerView_Payment.ViewHolderr>{
    Context context;
    ArrayList<Cart> carts;

    public Adapter_RecylerView_Payment(Context context, ArrayList<Cart> carts) {
        this.context = context;
        this.carts = carts;
    }

    @NonNull
    @Override
    public ViewHolderr onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolderr(LayoutInflater.from(context).inflate(R.layout.item_recyler_payment , viewGroup , false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderr viewHolderr, int i) {
        Cart cart = carts.get(i);

        viewHolderr.txtName.setText(cart.getName());
        Picasso.get().load(cart.getUrl())
                .error(R.drawable.imgdefault)
                .placeholder(R.drawable.imgdefault)
                .into(viewHolderr.imageView);
        viewHolderr.txtQuantity.setText(cart.getSoluong()+ "");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolderr.txtPrice.setText(decimalFormat.format(Integer.parseInt(cart.getGia())) + "đ");
    }

    @Override
    public int getItemCount() {
        return carts.size();
    }

    class ViewHolderr extends RecyclerView.ViewHolder{
        TextView txtName , txtPrice , txtQuantity;
        ImageView imageView;

        public ViewHolderr(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCart);
            txtName = itemView.findViewById(R.id.txtNameCart);
            txtPrice = itemView.findViewById(R.id.txtGiaCart);
            txtQuantity = itemView.findViewById(R.id.txtSoluongCart);
        }
    }
}
