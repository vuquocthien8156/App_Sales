package com.example.admin.app_sales.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.Customer;

import java.util.HashMap;
import java.util.Map;

public class Create_Edit_CustomerActivity extends AppCompatActivity {

    Customer customerEdit = null;

    EditText edtTen , edtSDT , edtThanhPho , edtQuan , edtPhuong , edtDiaChi;
    Button btnLuu;
    Switch aSwitch;
    String strThem = MainActivity.strMain + "addAddress.php";
    String str = MainActivity.strMain + "updateUser.php";
    Boolean isCheckedd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__edit__customer);
        anhXa();
        if(getIntent().hasExtra("Edit"))
            customerEdit = (Customer) getIntent().getExtras().getSerializable("Edit");

        if(customerEdit != null){
            edtTen.setText(customerEdit.getTenKH());
            edtSDT.setText(customerEdit.getSDT());
            String[] str = customerEdit.getDiaChi().split(",");
            edtDiaChi.setText(str[0]);
            edtThanhPho.setText(str[3]);
            edtPhuong.setText(str[1]);
            edtQuan.setText(str[2]);

            if(customerEdit.getMacDinh() == 1){
                aSwitch.setChecked(true);
                aSwitch.setEnabled(false);
            }
        }

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customerEdit != null){
                    updateUser(customerEdit.getID() + "");
                }
                else
                    AddUser();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckedd = isChecked;
            }
        });
    }

    public void updateUser(final String user){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Create_Edit_CustomerActivity.this, response, Toast.LENGTH_SHORT).show();
                if(Integer.parseInt(response.trim()) == 1){
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Toast.makeText(Create_Edit_CustomerActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("khach_hang" , user);
                map.put("MacDinh", isCheckedd ? "1" : "0");
                map.put("Name", edtTen.getText().toString());
                map.put("Phone", edtSDT.getText().toString());
                String diachi = edtDiaChi.getText().toString().replace("," , "");
                diachi +=  "," + edtPhuong.getText().toString();
                diachi += "," + edtQuan.getText().toString();
                diachi += "," + edtThanhPho.getText().toString();
                map.put("DiaChi", diachi);
                map.put("Email" , MainActivity.khach_hang.getEmail());
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    void AddUser(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strThem, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.trim()) == 1){
//                    Intent i = new Intent(Create_Edit_CustomerActivity.this , SelectAddressActivity.class);
//                    i.putExtra("add" , );
                    setResult(RESULT_OK);
                    finish();
                }else {
                    Toast.makeText(Create_Edit_CustomerActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
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
                map.put("MacDinh", isCheckedd ? "1" : "0");
                map.put("Name", edtTen.getText().toString());
                map.put("Phone", edtSDT.getText().toString());
                String diachi = edtDiaChi.getText().toString().replace("," , "");
                diachi +=  "," + edtPhuong.getText().toString();
                diachi += "," + edtQuan.getText().toString();
                diachi += "," + edtThanhPho.getText().toString();
                map.put("Address", diachi);
                map.put("Email" , MainActivity.khach_hang.getEmail());

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    void anhXa(){
        edtTen = findViewById(R.id.edtHoTen);
        edtSDT = findViewById(R.id.edtSoDienThoai);
        edtThanhPho = findViewById(R.id.edtThanhPho);
        edtPhuong = findViewById(R.id.edtPhuong);
        edtDiaChi = findViewById(R.id.edtDiaChiChinhSua);
        edtQuan = findViewById(R.id.edtQuan);

        btnLuu = findViewById(R.id.btnLuu);

        aSwitch = findViewById(R.id.switchDefault);
    }
}
