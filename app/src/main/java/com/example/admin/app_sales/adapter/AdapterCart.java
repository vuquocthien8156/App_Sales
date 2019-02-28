package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.example.admin.app_sales.model.Cart;
import com.example.admin.app_sales.fragment.FragmentCarts;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class AdapterCart extends ArrayAdapter<Cart> {
    Context context;
    int layout;
    ArrayList<Cart> carts;
    FragmentCarts fragmentCarts;

    public AdapterCart(@NonNull Context context, int resource, @NonNull ArrayList<Cart> objects , FragmentCarts fragmentCarts) {
        super(context, resource, objects);
        this.context = context;
        layout = resource;
        carts = objects;
        this.fragmentCarts = fragmentCarts;
    }

    class ViewHolder{
        TextView txtName , txtPrice , txtSoluong;
        ImageButton ibtnAdd, ibtnTru;
        ImageView img;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Cart cart = carts.get(position);
        final ViewHolder viewHolder;
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layout , null);
            viewHolder = new ViewHolder();
            viewHolder.img = convertView.findViewById(R.id.imgCart);
            viewHolder.txtName = convertView.findViewById(R.id.txtNameCart);
            viewHolder.txtPrice = convertView.findViewById(R.id.txtGiaCart);
            viewHolder.txtSoluong = convertView.findViewById(R.id.txtSoluongCart);
            viewHolder.ibtnAdd = convertView.findViewById(R.id.btnAdd);
            viewHolder.ibtnTru = convertView.findViewById(R.id.btnTru);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtName.setText(cart.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtPrice.setText(decimalFormat.format(Integer.parseInt(cart.getGia())) + "đ");
        viewHolder.txtSoluong.setText(cart.getSoluong() + "");


        viewHolder.ibtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SL = Integer.parseInt(viewHolder.txtSoluong.getText().toString());
                SL++;
                int sl = cart.getSoluong();

                if(SL < 11) {
                    if(context.getSharedPreferences("Main" , MODE_PRIVATE)
                            .getBoolean("CheckMain" , false)) {
                        MainActivity.soluongCart += 1;
                        ((MainActivity) context).setCountCart();
                    }
                    else
                        MainActivity.soluongCart += 1;

                    addCart(++sl , cart.getMasp() , "0");
                    cart.setSoluong(sl);
                    fragmentCarts.setSumPrice(Integer.parseInt(cart.getGia()) , 0);
                    viewHolder.txtSoluong.setText(SL + "");
                }
                if(SL >= 10) {
                    viewHolder.ibtnAdd.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Mua nhiều dậy mua vừa thôi nè <3", Toast.LENGTH_SHORT).show();
                }
                if(SL > 0)
                    viewHolder.ibtnTru.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.ibtnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int SL = Integer.parseInt(viewHolder.txtSoluong.getText().toString());
                SL--;

                int sl = cart.getSoluong();


                if(SL > 0) {
                    fragmentCarts.setSumPrice(Integer.parseInt(cart.getGia()) , 1);
                    if(context.getSharedPreferences("Main" , MODE_PRIVATE)
                            .getBoolean("CheckMain" , false)) {

                        MainActivity.soluongCart -= 1;
                        ((MainActivity) context).setCountCart();
                    }
                    else
                        MainActivity.soluongCart -= 1;

                    addCart(--sl , cart.getMasp() , "1");

                    cart.setSoluong(sl);

                    viewHolder.txtSoluong.setText(SL + "");
                }
                if(SL <= 1){
                    viewHolder.ibtnTru.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "Hong mua thì xóa đi nè <3", Toast.LENGTH_SHORT).show();
                }
                if(SL <= 10)
                    viewHolder.ibtnAdd.setVisibility(View.VISIBLE);
            }
        });

        Picasso.get().load(cart.getUrl()).into(viewHolder.img);
        return convertView;
    }

    // Su dung lai
    void addCart(final int sl , final String masp , final String kieu) {
        String url = MainActivity.strMain + "addCart.php";
        final String user = getContext().getSharedPreferences("Login" , Context.MODE_PRIVATE).getString("Username" , null);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.toString().equals("1")) {

                        }
                        else{

                        }
                        //end onResponse
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("khach_hang" , String.valueOf(MainActivity.khach_hang.getID()));
                map.put("MaSP" , masp);
                map.put("SoLuong" , sl + "");
                map.put("Kieu" , kieu);
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
