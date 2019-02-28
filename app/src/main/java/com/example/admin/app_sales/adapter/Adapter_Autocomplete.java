package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.app_sales.activity.SearchActivity;
import com.example.admin.app_sales.model.Product;
import com.example.admin.app_sales.R;

import java.util.ArrayList;

public class Adapter_Autocomplete extends ArrayAdapter<Product> {
    ArrayList<Product> products;

    private Context context;


    public Adapter_Autocomplete(@NonNull Context context, @NonNull ArrayList<Product> objects) {
        super(context , 0 , objects);
        products = new ArrayList<>(objects);
        this.context = context;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<Product> list = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                list.clear();
            } else {
                String c = charSequence.toString().toLowerCase().trim();

                for (Product p : products) {
                    if (p.getName().toLowerCase().trim().contains(c)) {
                        list.add(p);
                    }
                }
            }
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            clear();
            addAll((ArrayList<Product>)filterResults.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Product)resultValue).getName().toLowerCase().trim();
        }
    };


    class ViewHolder{
        TextView txt;
        ImageButton ibtnSend;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        final Product product = getItem(position);
        if(convertView == null)
        {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_autocomplete_search , null)
            ;
            viewHolder = new ViewHolder();
            viewHolder.txt = convertView.findViewById(R.id.txtNameSearch);
            viewHolder.ibtnSend = convertView.findViewById(R.id.ibtnSendText);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txt.setText(product.getName());
        viewHolder.ibtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchActivity)context).setTextEditText(product.getName());
            }
        });

        return convertView;
    }
}
