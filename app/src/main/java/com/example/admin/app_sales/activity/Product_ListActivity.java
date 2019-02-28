package com.example.admin.app_sales.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.model.Product;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.adapter.AdapterProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Product_ListActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<Product> products;
    AdapterProduct adapterProduct;
    String url = MainActivity.strMain + "getProductByIdCateMaterial.php";
    String urlSearch = MainActivity.strMain + "getProductbySearch.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__list);
        listView = findViewById(R.id.lv_product);
        products = new ArrayList<>();
        adapterProduct = new AdapterProduct(this , R.layout.item_lv_product , products);
        listView.setAdapter(adapterProduct);

        int i = getIntent().getIntExtra("MaLoaiCuThe" , -1);
        String j = getIntent().getStringExtra("KeyWord");

        if(i > 0)
            loadData(i);
        else if(j != null){
            getPProductBySearch(j);
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Product_ListActivity.this , DetailActivity.class);
                intent.putExtra("product" , products.get(i));

                startActivity(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadData(final int ma) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);

                            for(int j = 0 ; j <jsonArray.length() ; j++)
                            {
                                JSONObject object = (JSONObject) jsonArray.get(j);

                                products.add(new Product(
                                        Integer.parseInt(object.getString("MaSanPham")),
                                        object.getString("TenSanPham"),
                                        object.getString("MoTa"),
                                        Integer.parseInt(object.getString("GiaSanPham")),
                                        MainActivity.urlImg + object.getString("URL"),
                                        Integer.parseInt(object.getString("LoaiSanPham"))
                                ));
                            }
                            adapterProduct.notifyDataSetChanged();
                            setTitle(getIntent().getStringExtra("TenLoaiCuThe") + "(" + products.size() + ")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error" , error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("MaCuThe" , String.valueOf(ma));
                return map;
            }
        };

        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
//        onBackPressed();
        return super.onSupportNavigateUp();
    }

    void getPProductBySearch(final String keyWord){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, urlSearch, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONArray jsonArray = null;

                try {
                    //products.clear();
                    jsonArray = new JSONArray(response);
                    for(int j = 0 ; j <jsonArray.length() ; j++) {
                        JSONObject object = (JSONObject) jsonArray.get(j);
                        Product product = new Product(
                                Integer.parseInt(object.getString("MaSanPham")),
                                object.getString("TenSanPham"),
                                object.getString("MoTa"),
                                Integer.parseInt(object.getString("GiaSanPham")),
                                MainActivity.urlImg + object.getString("URL"),
                                Integer.parseInt(object.getString("LoaiSanPham")));
                            products.add(product);
                        }
                        adapterProduct.notifyDataSetChanged();
                        setTitle("Kết quả tìm kiếm cho '" + keyWord + "' (" + products.size() + ")");

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
                map.put("KeyWord", keyWord.trim());
                return map;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }
}
