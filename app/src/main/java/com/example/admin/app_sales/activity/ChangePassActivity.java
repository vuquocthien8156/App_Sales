package com.example.admin.app_sales.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.ultil.UltilMD5;

import java.util.HashMap;
import java.util.Map;

public class ChangePassActivity extends AppCompatActivity {
    EditText edtOld, edtNew , edtConfirm;
    Button btnXacnhan;

    String str = MainActivity.strMain + "updateUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        edtOld = findViewById(R.id.edtPassOld);
        edtNew = findViewById(R.id.edtPassNew);
        edtConfirm = findViewById(R.id.edtPassComfirm);

        btnXacnhan = findViewById(R.id.btnXacnhan);

        createPopupWindow();

        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!UltilMD5.md5(edtOld.getText().toString().trim()).equals(
                        getSharedPreferences("Login" , MODE_PRIVATE)
                                .getString("Password" , null))){
                    Toast.makeText(
                            ChangePassActivity.this, "Mật khẩu không chính xác", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!edtNew.getText().toString().trim().equals(edtConfirm.getText().toString().trim())){
                        Toast.makeText(ChangePassActivity.this, "Mật khẩu xác nhận không chính xác", Toast.LENGTH_SHORT).show();
                    }
                    else
                        updatePassUser();
                }
            }
        });

    }

    private void createPopupWindow() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int heigth = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.9) ,(int)(heigth*.5));

        WindowManager.LayoutParams par = getWindow().getAttributes();
        par.gravity = Gravity.TOP;
        par.x = 0;
        par.y = 100;


        getWindow().setAttributes(par);
    }

    private void updatePassUser() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.trim()) == 1){
                    Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    getSharedPreferences("Login", MODE_PRIVATE)
                            .edit().putString("Password" , UltilMD5.md5(edtNew.getText().toString().trim())).apply();
                    finish();
                }else {
                    Toast.makeText(ChangePassActivity.this, "Đổi mật khẩu không thành công thử lại sau", Toast.LENGTH_LONG).show();
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

                map.put("Email" , getSharedPreferences("Login" , MODE_PRIVATE).getString("Username" , null));
                map.put("Password", edtNew.getText().toString().trim());

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }
}
