package com.example.admin.app_sales.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.admin.app_sales.Converter;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.fragment.Sheet_Bottom_DetailProduct;
import com.example.admin.app_sales.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity implements Sheet_Bottom_DetailProduct.setOnClickThemVaoGioHang {
    ImageView imageView;
    Toolbar toolbar;
    FloatingActionButton fapYeuThich;
    TextView txtName , txtPrice , txtDerip;
    Button btnThem;
    int soluong;
    CoordinatorLayout coordinatorLayout;

    Product product;

    SharedPreferences sharedPreferences;
    int check = 0;
    String url =MainActivity.strMain + "addCart.php";
    String urlYeuThich =MainActivity.strMain + "yeuThich.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        ColorStateList themeColorStateList = new ColorStateList(
//                new int[][]{
//                        new int[]{android.R.attr.state_pressed},
//                        new int[]{android.R.attr.state_enabled},
//                        new int[]{android.R.attr.state_focused, android.R.attr.state_pressed},
//                        new int[]{-android.R.attr.state_enabled},
//                        new int[]{} // this should be empty to make default color as we want
//                },
//                new int[]{
//                        pressedFontColor,
//                        defaultFontColor,
//                        pressedFontColor,
//                        disabledFontColor,
//                        defaultFontColor
//                }
//        );

        sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);

        fapYeuThich = findViewById(R.id.fabYeuThich);

        imageView = findViewById(R.id.app_bar_image);

        toolbar = findViewById(R.id.toolbar);

        txtName = findViewById(R.id.txtTenDetail);
        txtPrice = findViewById(R.id.txtGiaDetail);
        txtDerip = findViewById(R.id.txtMotaDetail);

        btnThem = findViewById(R.id.btnThemVaoGH);

        coordinatorLayout = findViewById(R.id.coondiLayout);

        Intent intent = getIntent();
        product = (Product) intent.getExtras().get("product");
        Picasso.get().load(product.getUrl())
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this , ImageFullScreenActivity
                        .class).putExtra("img" , product.getUrl()));
            }
        });

        txtName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtPrice.setText(decimalFormat.format(product.getGia()) + "đ");
        txtDerip.setText(product.getMota());

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnThem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(sharedPreferences.getString("Username" , null) == null) {
                    startActivityForResult(new Intent(DetailActivity.this, LoginActivity.class), 100);
                }else {
                    Sheet_Bottom_DetailProduct sheet_bottom_detailProduct =
                            new Sheet_Bottom_DetailProduct();
                    sheet_bottom_detailProduct.show(getSupportFragmentManager(), "");
                }
            }
        });

        if(MainActivity.lsYeuThich != null)
            for(Product i : MainActivity.lsYeuThich){
                if(i.getId() == product.getId()){
                    check = 1;
                    fapYeuThich.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.yeuthich));
                }
            }

        fapYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getString("Username" , null) != null){
                    if(check == 0) {
                        fapYeuThich.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.yeuthich));
                        check = 1;
                        MainActivity.lsYeuThich.add(product);
                        Log.d("thich" , "1");
                        yeuthich();
                    }
                    else {
                        fapYeuThich.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this, R.drawable.koyeuthich));
                        check = 0;
                        MainActivity.lsYeuThich.remove(product);
                        Log.d("thich" , "0");
                        yeuthich();
                    }
                }
                else
                    Toast.makeText(DetailActivity.this, "Chức năng này cần đăng nhập", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void yeuthich(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlYeuThich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("1")){
                    Toast.makeText(DetailActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(DetailActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();

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
                par.put("yeuthich" , String.valueOf(check));
                par.put("masp" , String.valueOf(product.getId()));
                return par;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucart , menu);
        MenuItem menuItem = menu.findItem(R.id.menuCart);
        menuItem.setIcon(Converter.convertLayoutToImage(this , MainActivity.soluongCart , R.drawable.ic_cart));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menuCart)
            if(sharedPreferences.getString("Username" , null) == null && sharedPreferences.getString("Password" , null) == null)
                startActivityForResult(new Intent(DetailActivity.this, LoginActivity.class), 100);
            else {
                startActivity(new Intent(DetailActivity.this , CartActivity.class));
            }
            return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void truyenSoLuong(int soLuong) {
        this.soluong = soLuong;

        addCart();
    }

    @Override
    protected void onResume() {

        super.onResume();
        invalidateOptionsMenu();

    }

    void addCart() {
        RequestQueue requestQueue = Volley.newRequestQueue(DetailActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.toString().trim().equals("1")) {
                            MainActivity.soluongCart += soluong;
                            invalidateOptionsMenu();
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.themthanhcong
                                    , Snackbar.LENGTH_SHORT)
                                    .setAction(R.string.dengiohang, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(DetailActivity.this , CartActivity.class));
                                        }
                                    });
                            snackbar.show();
                        }
                        else{
                            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.themthatbai
                                    , Snackbar.LENGTH_INDEFINITE)
                                    .setAction(R.string.thulai, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            addCart();
                                        }
                                    });
                            snackbar.show();
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
                map.put("MaSP" , product.getId()+"");
                map.put("SoLuong" , soluong+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
