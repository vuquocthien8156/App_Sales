package com.example.admin.app_sales.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.adapter.Adapter_Autocomplete;
import com.example.admin.app_sales.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    ListView listView;
    ImageButton imageButton , ibtnXoa;
    ArrayList<Product> products;
    EditText editText;
    private String url = MainActivity.strMain + "getAllProduct.php";
    Adapter_Autocomplete arrayAdapter;
    ArrayList<Product> resuft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        imageButton = findViewById(R.id.btnBack);
        ibtnXoa = findViewById(R.id.ibtnXoa);
        listView = findViewById(R.id.lvSearch);
        //listView.setVisibility(View.INVISIBLE);

        products = new ArrayList<>();
        resuft = new ArrayList<>();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                onBackPressed();
            }
        });

        ibtnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });
        loadAllProduct();

    }

    public void setTextEdt(String text){
        editText.setText(text);
    }

    void loadAllProduct() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray jsonArray = null;

                        try {
                            ArrayList<Product> products1 = new ArrayList<>();

                            jsonArray = new JSONArray(response);
                            for(int j = 0 ; j <jsonArray.length() ; j++) {
                                JSONObject object = (JSONObject) jsonArray.get(j);
                                Product product = new Product(
                                        Integer.parseInt(object.getString("MaSanPham")),
                                        object.getString("TenSanPham"),
                                        object.getString("MoTa").toString(),
                                        Integer.parseInt(object.getString("GiaSanPham")),
                                        object.getString("URL").toString(),
                                        Integer.parseInt(object.getString("LoaiSanPham")));
                                products.add(product);
                            }
                            //resuft.addAll(products);
                            editText = findViewById(R.id.edtSearch);
                            arrayAdapter = new Adapter_Autocomplete(SearchActivity.this
                                    ,  products);

                            listView.setAdapter(arrayAdapter);
                            arrayAdapter.clear();
                            arrayAdapter.notifyDataSetChanged();

                            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                                @Override
                                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                                    if(i == EditorInfo.IME_ACTION_SEARCH)
                                    {
                                        if(!editText.getText().toString().isEmpty() || !products.isEmpty()) {
                                            Intent intent = new Intent(SearchActivity.this, Product_ListActivity.class);
                                            intent.putExtra("KeyWord", editText.getText().toString().trim());
                                            startActivity(intent);
                                        }
                                    }
                                    return false;
                                }
                            });

                            editText.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                                    if(charSequence == null || charSequence.length() == 0) {
//                                        ibtnXoa.setVisibility(View.INVISIBLE);
//                                        listView.setVisibility(View.INVISIBLE);
//                                        arrayAdapter.clear();
//                                        products.addAll(resuft);
//                                        arrayAdapter.notifyDataSetChanged();
//                                    }else {
//                                        ibtnXoa.setVisibility(View.VISIBLE);
//                                        ArrayList<Product> pro = new ArrayList<>();
//                                        pro.clear();
//                                        for(Product p : resuft){
//                                            if(p.getName().toString()
//                                                    .toLowerCase()
//                                                    .trim().contains(charSequence.toString())){
//                                                pro.add(p);
//                                            }
//                                        }
//                                        if(pro.size() > 0) {
//                                            listView.setVisibility(View.VISIBLE);
//                                            arrayAdapter.clear();
//                                            products.addAll(pro);
//                                            arrayAdapter.notifyDataSetChanged();
//                                        }
//                                        else
//                                            listView.setVisibility(View.INVISIBLE);
//
//                                    }
                                    if(!charSequence.toString().isEmpty()) {
                                        arrayAdapter.getFilter().filter(charSequence.toString());
                                        ibtnXoa.setVisibility(View.VISIBLE);
                                    }
                                    else {
                                        ibtnXoa.setVisibility(View.INVISIBLE);
                                        arrayAdapter.getFilter().filter(charSequence.toString());
                                    }
                                }

                                @Override
                                public void afterTextChanged(Editable editable) {

                                }
                            });
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
        });

        requestQueue.add(stringRequest);
    }

    public void setTextEditText(String text){
        editText.setText(text);
    }
}
