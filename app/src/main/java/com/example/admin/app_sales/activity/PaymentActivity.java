package com.example.admin.app_sales.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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
import com.example.admin.app_sales.adapter.Adapter_RecylerView_Payment;
import com.example.admin.app_sales.model.Cart;
import com.example.admin.app_sales.model.Customer;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {
    TextView txtChinhSua, txtTongGia , txtTamtinh , txtVanChuyen , txtTongCong , txtKhach , txtSDT , txtDiaChi;
    Button btnThanhToan , btnOk;
    EditText edtGhiChu , edtDiaChi;
    FrameLayout frm;
    RecyclerView recyclerView;
    ArrayList<Cart> carts;
    String cartstoString;
    DecimalFormat decimalFormat;
    int tongTienSanPham = 0;
    int sumSoLuong = 0;
    int phiVanChuyen = 0;
    int tongCong = 0;

    String str = MainActivity.strMain + "getAddresss.php";
    String urlInsertOrder = MainActivity.strMain + "addOrder.php";

    public static Customer cusOrder= null;
    private String urldelete =MainActivity.strMain + "deleteAllCart.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.kiemtra);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        decimalFormat = new DecimalFormat("###,###,###");

        anhxa();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        carts = (ArrayList<Cart>) getIntent().getExtras().get("Carts");

        getUserDefalt();

        for(Cart c  : carts){
            sumSoLuong += c.getSoluong();
        }

        txtTamtinh.setText("Tạm tính ( " + sumSoLuong + " sản phẩm)");
        tongTienSanPham = getIntent().getIntExtra("Sum" , 0);
        txtTongGia.setText(decimalFormat.format(tongTienSanPham) + "đ");
        txtVanChuyen.setText(decimalFormat.format(phiVanChuyen)+"đ");
        tongCong = tongTienSanPham + phiVanChuyen;
        txtTongCong.setText(decimalFormat.format(tongCong) + "đ");

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this , LinearLayout.VERTICAL , false);
        Adapter_RecylerView_Payment adapter = new Adapter_RecylerView_Payment(this , carts);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        Gson gson = new Gson();
        cartstoString = gson.toJson(carts);

        btnThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!cusOrder.getDiaChi().equals("null"))
                    insertOrder();
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setMessage(R.string.chuanhapdiachi);
                    builder.setPositiveButton("Hiểu rồi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
            }
        });



//        btnOk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!edtDiaChi.getText().equals("")) {
//                    cusOrder.setDiaChi(edtDiaChi.getText().toString());
//                    txtDiaChi.setText(edtDiaChi.getText().toString());
//                    frm.setVisibility(View.GONE);
//                    txtDiaChi.setVisibility(View.VISIBLE);
//                    updateUser();
//                }
//                else
//                    Toast.makeText(PaymentActivity.this, "Nhập địa chỉ", Toast.LENGTH_SHORT).show();
//            }
//        });

        txtChinhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PaymentActivity.this , SelectAddressActivity.class) , 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK){
            setThongTinKhach();
        }
    }

    void setThongTinKhach(){
        txtKhach.setText(cusOrder.getTenKH());
        txtSDT.setText(cusOrder.getSDT());
        txtDiaChi.setText(cusOrder.getDiaChi().equals("null") ? "Nhập địa chỉ giao hàng" : cusOrder.getDiaChi());
        Toast.makeText(this, cusOrder.getDiaChi() , Toast.LENGTH_SHORT).show();
    }

    public void getUserDefalt(){
        final String user = getSharedPreferences("Login" , MODE_PRIVATE).getString("Username" , null);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);

                            cusOrder = new Customer(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("ten_khach_hang")
                                    , jsonObject.getString("email")
                                    , jsonObject.getString("so_dien_thoai")
                                    , MainActivity.urlImg + jsonObject.getString("avata")
                                    , jsonObject.getInt("khach_chinh")
                                    , jsonObject.getString("dia_chi")
                                    , jsonObject.getInt("mac_dinh")) ;


                            txtKhach.setText(cusOrder.getTenKH());
                            txtSDT.setText(cusOrder.getSDT());
                            txtDiaChi.setText(cusOrder.getDiaChi().equals("null") ? "Nhập địa chỉ giao hàng" : cusOrder.getDiaChi());

                            if(cusOrder.getDiaChi().equals("null")){
                                txtDiaChi.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(PaymentActivity.this , Create_Edit_CustomerActivity.class));
                                    }
                                });
                                txtDiaChi.setTextColor(Color.RED);
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
                Map<String , String> map = new HashMap<>();
                map.put("khach" , user);
                map.put("chinh" , "1");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }


    void anhxa(){
        recyclerView = findViewById(R.id.recylerviewPayment);

        txtTongGia = findViewById(R.id.txtTongGiasanpham);
        txtTamtinh = findViewById(R.id.txtTamtinh);
        txtTongCong = findViewById(R.id.txtTongCong);
        txtVanChuyen = findViewById(R.id.txtPhiVanChuyen);
        txtKhach = findViewById(R.id.txtTenKhach);
        txtSDT = findViewById(R.id.SDTKhach);
        txtDiaChi = findViewById(R.id.txtDiaChi);
        txtChinhSua =findViewById(R.id.txtChinhSua);

        btnThanhToan = findViewById(R.id.btnThanhToann);
//        btnOk = findViewById(R.id.btnDiachi);

        edtGhiChu = findViewById(R.id.edtGhiChu);
//        edtDiaChi = findViewById(R.id.edtDiachi);
//        frm = findViewById(R.id.frm);
    }

    void insertOrder(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlInsertOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.toString().trim().equals("1")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                            builder.setMessage(R.string.dathangthanhcong);
                            builder.setPositiveButton(R.string.Xacnhan, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteItemCart();
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            });
                            builder.show();
                        }
                        else
                            Toast.makeText(PaymentActivity.this, R.string.themthatbaivuilongthulai, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("KhachHang" , String.valueOf(MainActivity.khach_hang.getID()));
                params.put("GhiChu" , edtGhiChu.getText().toString());
                Date date = new Date();
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                params.put("NgayLap" , format.format(date).toString());
                params.put("PhiShip" , phiVanChuyen + "");
                params.put("TongTien" , tongCong + "");

                params.put("Carts" , cartstoString);

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    void deleteItemCart() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonStringRequest = new StringRequest(Request.Method.POST, urldelete, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
                return map;
            }
        };

        requestQueue.add(jsonStringRequest);
    }

    private void updateUser() {
        String str = MainActivity.strMain + "updateUser.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.toString().trim()) == 1){
                    Toast.makeText(PaymentActivity.this, "thành công", Toast.LENGTH_SHORT).show();
//                    getUser(url);
                    MainActivity.khach_hang.setDiaChi(cusOrder.getDiaChi());
                }else {
                    Toast.makeText(PaymentActivity.this , "thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("khach_hang" , String.valueOf(MainActivity.khach_hang.getID()));
                map.put("DiaChi", cusOrder.getDiaChi());

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
