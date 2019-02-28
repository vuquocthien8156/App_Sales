package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.activity.YeuThichActivity;
import com.example.admin.app_sales.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterLikeProduct extends ArrayAdapter<Product> {
    Context context;
    ArrayList<Product> products;
    public AdapterLikeProduct(Context context, ArrayList<Product> objects) {
        super(context, 0, objects);
        this.context = context;
        products = objects;
    }

    class ViewHolder{
        ImageView img;
        TextView txtName , txtPrice;
        ImageButton btn;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Product p = products.get(position);
        ViewHolder v;

        if(convertView == null){
            v = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_yeuthich , null);
            v.txtName = convertView.findViewById(R.id.txtNameYeuThich);
            v.txtPrice = convertView.findViewById(R.id.txtGiaYeuThich);
            v.img = convertView.findViewById(R.id.imgYeuThich);
            v.btn = convertView.findViewById(R.id.btnBoThich);
            convertView.setTag(v);
        }
        else
            v = (ViewHolder) convertView.getTag();

        v.txtName.setText(p.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        v.txtPrice.setText(decimalFormat.format(p.getGia()) +"đ");
        Picasso.get().load(p.getUrl()).into(v.img);

        v.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yeuthich(p.getId());
                MainActivity.lsYeuThich.remove(p);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    public void yeuthich(final int p){
        String urlYeuThich = MainActivity.strMain + "yeuThich.php";

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlYeuThich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("1")){
                    ((YeuThichActivity)context).set();
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<>();
                par.put("khach" , String.valueOf(MainActivity.khach_hang.getID()));
                par.put("yeuthich" , String.valueOf(0));
                par.put("masp" , String.valueOf(p));
                return par;
            }
        };
        requestQueue.add(stringRequest);
    }


}
