package com.example.admin.app_sales.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.adapter.AdapterOrder;
import com.example.admin.app_sales.model.Customer;
import com.example.admin.app_sales.model.DetailOrder;
import com.example.admin.app_sales.model.Order;
import com.example.admin.app_sales.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterOrder adapterOrder;
    ArrayList<Order> orders;
    String str = MainActivity.strMain + "getAllOrderOfUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Đơn hàng của tôi");
        setContentView(R.layout.activity_history);
        orders = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerDonHang);
        adapterOrder = new AdapterOrder(orders , this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this , DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recyclerView.getContext() ,R.drawable.diver_recy));
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this , LinearLayoutManager.VERTICAL , false));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layDonHang();

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    void layDonHang(){
        final String user = getSharedPreferences("Login" , MODE_PRIVATE).getString("Username" , null);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for(int i = 0 ; i < jsonArray.length() ; i++){

                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                int madonhang = jsonObject.getInt("ma_don_hang");
                                String ghichu = jsonObject.getString("ghi_chu");


//                                Date date = format.parse(jsonObject.getString("ngay_lap_don_hang"));
//                                Calendar ngaydat = Calendar.getInstance();
//                                ngaydat.setTime(date);

                                String ngaydat = jsonObject.getString("ngay_lap_don_hang");
                                String ngaythanhtoan;
                                if(!jsonObject.isNull("ngay_thanh_toan")) {
                                    ngaythanhtoan = jsonObject.getString("ngay_thanh_toan");
                                }
                                else
                                    ngaythanhtoan = "";


                                double phiship = jsonObject.getDouble("phi_ship");
                                double tongtien = jsonObject.getDouble("tong_tien");
                                int trangthai = jsonObject.getInt("trang_thai");

                                Customer customer = new Customer(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("ten_khach_hang")
                                        , jsonObject.getString("email")
                                        , jsonObject.getString("so_dien_thoai")
                                        , !jsonObject.isNull("avata") ? MainActivity.urlImg + jsonObject.getString("avata") : ""
                                        , jsonObject.getInt("khach_chinh")
                                        , jsonObject.getString("dia_chi")
                                        , jsonObject.getInt("mac_dinh"));

                                JSONArray Jsonchitiet = new JSONArray(jsonObject.getString("chitiet"));

                                ArrayList<DetailOrder> detailOrders = new ArrayList<>();
                                for (int j = 0 ; j < Jsonchitiet.length() ; j++){

                                    JSONObject objectchitiet = (JSONObject) Jsonchitiet.get(j);
                                    double dongia = objectchitiet.getDouble("don_gia");
                                    int so_luong = objectchitiet.getInt("so_luong");
                                    double thanhtien = objectchitiet.getDouble("thanh_tien");
                                    int trangthai1 = objectchitiet.getInt("trang_thai");

                                    JSONObject oSanPham = new JSONObject(objectchitiet.getString("san_pham"));
                                    Product product = new Product(
                                            Integer.parseInt(oSanPham.getString("ma_san_pham")),
                                            oSanPham.getString("ten_san_pham"),
                                            oSanPham.getString("mo_ta").toString(),
                                            Integer.parseInt(oSanPham.getString("gia_san_pham")),
                                            MainActivity.urlImg + oSanPham.getString("url"),
                                            Integer.parseInt(oSanPham.getString("loai_san_pham"))
                                    );
                                    detailOrders.add(new DetailOrder(product , dongia , so_luong  , thanhtien , trangthai1));
                                }

                                orders.add(new Order(madonhang , customer , ghichu , ngaydat , phiship , tongtien , trangthai , ngaythanhtoan , detailOrders));
                            }

                            if(orders.size() > 0)
                                adapterOrder.notifyDataSetChanged();
                            else
                            {
                                Toast.makeText(HistoryActivity.this, "Không có đơn hàng", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> par = new HashMap<>();
                par.put("khach" , user);
                return par;
            }
        };
        requestQueue.add(stringRequest);
    }
}
