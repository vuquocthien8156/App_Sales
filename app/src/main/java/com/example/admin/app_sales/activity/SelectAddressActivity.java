package com.example.admin.app_sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.adapter.AdapterSelectAddress;
import com.example.admin.app_sales.model.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SelectAddressActivity extends AppCompatActivity {
    String str = MainActivity.strMain + "getAddresss.php";
    AdapterSelectAddress adapterSelectAddress;
    ArrayList<Customer> customers;
    ListView lv;

    Button btnThem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Địa chỉ giao hàng");
        setContentView(R.layout.activity_select_address);
        lv = findViewById(R.id.lvSelectAddress);
        btnThem = findViewById(R.id.btnThemDiaChi);
        customers = new ArrayList<>();
        adapterSelectAddress = new AdapterSelectAddress(this , customers);
        lv.setAdapter(adapterSelectAddress);
        getData();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SelectAddressActivity.this , Create_Edit_CustomerActivity.class) , 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100){
            getData();
        }
        if(resultCode == RESULT_OK && requestCode == 101){
//            Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
            getData();
        }
    }

    @Override
    protected void onRestart() {
//        getData();
        super.onRestart();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void getData(){
        final String user = getSharedPreferences("Login" , MODE_PRIVATE).getString("Username" , null);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                customers.clear();

                                JSONArray jsonArray = new JSONArray(response);
                                int macdinh = 0;
                                Customer cMacDinh;
                                for(int i = 0 ; i < jsonArray.length() ; i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    Customer c = new Customer(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("ten_khach_hang")
                                            , jsonObject.getString("email")
                                            , jsonObject.getString("so_dien_thoai")
                                            , MainActivity.urlImg + jsonObject.getString("avata")
                                            , jsonObject.getInt("khach_chinh")
                                            , jsonObject.getString("dia_chi")
                                            , jsonObject.getInt("mac_dinh")) ;

                                    if(jsonObject.getInt("mac_dinh") == 1){
                                        customers.add(0 , c);
                                    }
                                    else
                                        customers.add( c);

                                }

                                Collections.reverse(customers);

                                cMacDinh = customers.get(jsonArray.length() - 1);
                                customers.remove(jsonArray.length() - 1);
                                customers.add(0 , cMacDinh);

                                adapterSelectAddress.notifyDataSetChanged();
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
                Map<String , String> map = new HashMap<>();
                map.put("khach" , user);
                map.put("chinh" , "0");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
