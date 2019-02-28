package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.app_sales.model.Product;
import com.example.admin.app_sales.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterProduct extends ArrayAdapter<Product> {
    Context context;
    int layout;
    ArrayList<Product> products;

    public AdapterProduct(@NonNull Context context, int resource, @NonNull ArrayList<Product> objects) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        products = objects;
    }

    class ViewHolder{
        TextView txtName , txtPrice , txtSoluong;
        ImageButton ibtnAdd, ibtnTru;
        ImageView img;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Product product = products.get(position);
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout , null);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imgCart);
            viewHolder.txtName = convertView.findViewById(R.id.txtNameCart);
            viewHolder.txtPrice = convertView.findViewById(R.id.txtGiaCart);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtPrice.setText(decimalFormat.format(product.getGia()) + "đ");

        Picasso.get().load(product.getUrl()).into(viewHolder.img);

        return convertView;
    }
}
