package com.example.admin.app_sales.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.Customer;
import com.example.admin.app_sales.model.Product;
import com.example.admin.app_sales.ultil.UltilMD5;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends Activity {
    ImageView imageView;
    TextInputEditText edtUser;
    TextInputEditText edtPass;
    TextInputLayout textInputLayoutUser , textInputLayoutPass;
    SharedPreferences sharedPreferences;
    Button btnLogin;
    TextView txtRegis , txtForgot;
    String str = MainActivity.strMain + "getUser.php";
    String strYeuThich = MainActivity.strMain + "laySanPhamYeuThich.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        imageView = findViewById(R.id.imageView3);
        edtUser = findViewById(R.id.edtUser);
        textInputLayoutUser = findViewById(R.id.textInputLayout);
        textInputLayoutPass = findViewById(R.id.textInputLayout2);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtRegis = findViewById(R.id.txtRegis);
        txtForgot
                = findViewById(R.id.txtQuen);


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!edtUser.getText().toString().isEmpty())
                {
                    textInputLayoutUser.setErrorEnabled(false);
                }
                if(!edtPass.getText().toString().isEmpty())
                {
                    textInputLayoutPass.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        Picasso.get().load("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.15752-9/42045137_255934391930672_722386312044216320_n.png?_nc_cat=0&oh=8675a517c5a943bbb62ffdaf34682a14&oe=5C609022")
                .error(R.drawable.imgdefault)
                .placeholder(R.drawable.imgdefault)
                .into(imageView);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation())
                    checkingLogin(edtUser.getText().toString() , edtPass.getText().toString());
            }
        });

        txtRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(LoginActivity.this  ,SignUpActivity.class) , 100);
            }
        });

        edtUser.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);

        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this , ForgotPassActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_OK && data != null)
        {
            edtUser.setText(data.getExtras().getString("Email"));
            edtPass.setText(data.getExtras().getString("Pass"));
        }
    }

    void writeJSONUser(String user){
        String filename = "user.txt";
        String content = user;
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename , MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public  void checkingLogin(final String user , final String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                writeJSONUser(response.toString().trim());
                Customer customer = new Customer();

                if(response.trim() != "0"){

                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        customer = new Customer(
                                jsonObject.getInt("id"),
                                jsonObject.getString("TenKH")
                                , jsonObject.getString("Mail")
                                , jsonObject.getString("SDT")
                                , MainActivity.urlImg + jsonObject.getString("Img")
                                , jsonObject.getInt("khach_chinh")
                                , jsonObject.getString("dia_chi")
                                , jsonObject.getInt("mac_dinh"));
                    }
                     catch (JSONException e) {
                        e.printStackTrace();
                    }

                    MainActivity.khach_hang = customer;
                    laySanPhamYeuThich();
                        sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("Username" , edtUser.getText().toString());
                        editor.putString("Password" , UltilMD5.md5(edtPass.getText().toString()));
                        editor.apply();

                        setResult(RESULT_OK);
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Tài khoản này không tồn tại", Toast.LENGTH_SHORT).show();
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
                map.put("Username" , user);
                map.put("Password" , UltilMD5.md5(pass));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    boolean checkValidation(){
        boolean isValid = true;
        if(edtUser.getText().toString().isEmpty())
        {
            textInputLayoutUser.setError(getString(R.string.ErrorUser));
            isValid = false;
        }
        else
            textInputLayoutUser.setErrorEnabled(false);

        if(edtPass.getText().toString().isEmpty())
        {
            textInputLayoutPass.setError(getString(R.string.ErrorPass));
            isValid = false;
        }
        else
            textInputLayoutPass.setErrorEnabled(false);

        return isValid;
    }

    @Override
    public void onBackPressed() {
        getSharedPreferences("MaXacNhan" , MODE_PRIVATE)
                .edit().remove("Ma").apply();
        finish();
        overridePendingTransition(R.anim.anim_enter_activity , R.anim.anim_exit_activity);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        getSharedPreferences("MaXacNhan" , MODE_PRIVATE)
                .edit().remove("Ma").apply();
        finish();
        super.onDestroy();
    }

    public void laySanPhamYeuThich(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, strYeuThich, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<Product> arr = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(response.trim());
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                        arr.add(new Product(
                                Integer.parseInt(jsonObject.getString("ma_san_pham")),
                                jsonObject.getString("ten_san_pham"),
                                jsonObject.getString("mo_ta").toString(),
                                Integer.parseInt(jsonObject.getString("gia_san_pham")),
                                MainActivity.urlImg + jsonObject.getString("url").toString(),
                                Integer.parseInt(jsonObject.getString("loai_san_pham"))
                        ));
                    }

                    MainActivity.lsYeuThich = arr;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error" , error.toString());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("khach" , String.valueOf(MainActivity.khach_hang.getID()));
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

//    public void getUser(String url) {
//
////        sharedPreferences = getSharedPreferences("Login" , Context.MODE_PRIVATE);
////        final String user = sharedPreferences.getString("Username" , null);
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
////                    edtName.setText(jsonObject.getString("TenKH"));
////                    edtEmail.setText(jsonObject.getString("Mail"));
////                    edtSDT.setText(jsonObject.getString("SDT"));
////                    edtPass.setText(getActivity().getSharedPreferences("Login" , Context.MODE_PRIVATE)
////                            .getString("Password" , null));
////                    String strUrl = jsonObject.getString("Img");
////                    String urll = MainActivity.urlImg + strUrl;
////
////                    if(strUrl != null){
////                        Picasso.get().load(urll).into(imageView);
////                        if(imageView.getDrawable() == null)
////                            imageView.setImageResource(R.drawable.ic_user);
////                    }
////                    else{
////
////                        imageView.setImageResource(R.drawable.ic_user);
////                    }
//
//
//
////                    Bitmap imageBitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
////                    RoundedBitmapDrawable imageDrawable = RoundedBitmapDrawableFactory.create(getResources(), imageBitmap);
////                    imageDrawable.setCircular(true);
////                    imageDrawable.setCornerRadius(Math.max(imageBitmap.getWidth(), imageBitmap.getHeight()) / 2.0f);
////                    imageView.setImageDrawable(imageDrawable);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        })
//        {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String , String> map = new HashMap<>();
//                map.put("Email", edtUser.getText().toString());
//                return map;
//            }
//        };
//
//        requestQueue.add(jsonArrayRequest);
//    }
}
