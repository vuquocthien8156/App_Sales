package com.example.admin.app_sales.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.admin.app_sales.activity.PaymentActivity;
import com.example.admin.app_sales.model.Cart;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.adapter.AdapterCart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentCarts extends Fragment{
    ArrayList<Cart> carts;
    Button button , btnThanhToan;
    TextView textView;
    AdapterCart adapterCart;
    SwipeMenuListView listView;
    TextView txtTongtien;
    int sum = 0;

    String user;
    String url = MainActivity.strMain + "getCart.php";
    String urldelete = MainActivity.strMain + "deleteCart.php";
    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");

    SharedPreferences sharedPreferences = null;
    public FragmentCarts() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        user = getActivity().getSharedPreferences("Login" , MODE_PRIVATE).getString("Username" , null);


        View view= inflater.inflate(R.layout.fragment_cards_layout, container , false);
        listView = view.findViewById(R.id.lvCart);

        carts = new ArrayList<>();
        adapterCart = new AdapterCart(getContext() , R.layout.item_lv_cart , carts , this);
        listView.setAdapter(adapterCart);

        button = view.findViewById(R.id.btnTieptuc);
        btnThanhToan = view.findViewById(R.id.btnThanhToan);

        txtTongtien = view.findViewById(R.id.txtTongtien);
        textView = view.findViewById(R.id.txtfra2);

        button.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        getDataCart(user , url);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("Main" , MODE_PRIVATE);
                if(sharedPreferences.getBoolean("CheckMain" , false))
                    ((MainActivity)getContext()).setSelectedHome(0);
                else{
                    startActivity(new Intent(getContext() , MainActivity.class));
                }
            }
        });
        setSwipeListView();

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(carts.size() > 0){
                    Intent intent = new Intent(getContext() , PaymentActivity.class);
                    intent.putExtra("Carts" , carts);
                    intent.putExtra("Sum" , sum);
                    startActivityForResult(intent , 100);
                }
                else
                {
                    Toast.makeText(getContext(), "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
//            ((MainActivity)getActivity()).setSelectedHome(0);
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            MainActivity.soluongCart = 0;
            ((MainActivity)getActivity()).setCountCart();
            carts.clear();
            adapterCart.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        sharedPreferences = getActivity()
                .getSharedPreferences("CheckFirst" , MODE_PRIVATE);

        if(sharedPreferences.getBoolean("First" , true)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Vuốt sản phẩm sang trái để xóa sản phẩm");

            builder.setIcon(R.drawable.ic_main);

            builder.setPositiveButton("Hiểu Chứ?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    sharedPreferences.edit().putBoolean("First", false).apply();
                }
            });
            builder.show();
        }

    }

    public static FragmentCarts newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentCarts fragment = new FragmentCarts();
        fragment.setArguments(args);
        return fragment;
    }
    
    void setSwipeListView() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.ic_cancel_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        deleteItemCart(position);
                        break;
                }
                return false;
            }
        });
    }

    void deleteItemCart(final int position) {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, urldelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.toString().trim().equals("1")) {
                    sharedPreferences = getActivity().getSharedPreferences("Main" , MODE_PRIVATE);
                    if(sharedPreferences.getBoolean("CheckMain" , false)) {
                        MainActivity.soluongCart -= carts.get(position).getSoluong();
                        ((MainActivity)getActivity()).setCountCart();
//                        MainActivity.soluongCart -= carts.get(position).getSoluong();
                    }
                    else{
                        MainActivity.soluongCart -= carts.get(position).getSoluong();
                    }

                    carts.remove(position);
                    adapterCart.notifyDataSetChanged();
                    if(!carts.isEmpty()) {
                        button.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        button.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                    sum = 0;
                    for(Cart p : carts)
                    {
                        sum += Integer.parseInt(p.getGia())*p.getSoluong();
                    }
                    txtTongtien.setText(decimalFormat.format(sum) + "đ");
                }
                else{
                    Toast.makeText(getContext(), R.string.XoaThatBai, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("khach_hang", String.valueOf(MainActivity.khach_hang.getID()));
                map.put("MaSP", carts.get(position).getMasp().trim());
                return map;
            }
        };

        requestQueue.add(jsonStringRequest);
    }

    public void setSumPrice(int Gia , int kieu){
        if(kieu == 0)
            sum += Gia;
        else
            sum -= Gia;
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTongtien.setText(decimalFormat.format(sum) + "đ");
    }

    public void getDataCart(final String user , String url) {
        carts.clear();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    for(int i = 0 ; i < jsonArray.length() ; i++) {
                        try {
                            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                            carts.add(new Cart(
                                    jsonObject.getString("MaSP")
                                    ,jsonObject.getString("TenSP")
                                    ,jsonObject.getString("GiaSP")
                                    ,MainActivity.urlImg + jsonObject.getString("URL")
                                    ,Integer.parseInt(jsonObject.getString("SoLuong"))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    if(!carts.isEmpty()) {
                        button.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        button.setVisibility(View.VISIBLE);
                        textView.setVisibility(View.VISIBLE);
                    }
                    sum = 0;
                    for(Cart p : carts)
                    {
                        sum += Integer.parseInt(p.getGia())*p.getSoluong();
                    }
                    txtTongtien.setText(decimalFormat.format(sum) + "đ");
                    adapterCart.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("Userr", String.valueOf(MainActivity.khach_hang.getID()));
                return map;
            }
          };

        requestQueue.add(jsonArrayRequest);
    }
}
