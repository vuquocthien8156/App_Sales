package com.example.admin.app_sales.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    ImageView imageView;
    TextInputEditText edtName , edtEmail , edtPhone , edtPass , edtPassComF;
    Button btnSignUp;
    TextInputLayout inputLayoutName , inputLayoutEmail ,
            inputLayoutPhone , inputLayoutPass , inputLayoutPassComF;
    String str = MainActivity.strMain + "signupUser.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView = findViewById(R.id.imageView3);

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        edtPassComF = findViewById(R.id.edtPassRepeat);

        inputLayoutName = findViewById(R.id.textInputLayout);
        inputLayoutEmail = findViewById(R.id.textInputLayout2);
        inputLayoutPhone = findViewById(R.id.textInputLayout3);
        inputLayoutPass = findViewById(R.id.textInputLayout4);
        inputLayoutPassComF = findViewById(R.id.textInputLayout5);

        btnSignUp = findViewById(R.id.btnSignUp);


        Picasso.get().load("https://scontent.fsgn5-5.fna.fbcdn.net/v/t1.15752-9/42045137_255934391930672_722386312044216320_n.png?_nc_cat=0&oh=8675a517c5a943bbb62ffdaf34682a14&oe=5C609022")
                .error(R.drawable.imgdefault)
                .placeholder(R.drawable.imgdefault)
                .into(imageView);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidation())
                    signUp();
            }
        });

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(!edtName.getText().toString().isEmpty())
                {
                    inputLayoutName.setErrorEnabled(false);
                }
                if(!edtEmail.getText().toString().isEmpty())
                {
                    inputLayoutEmail.setErrorEnabled(false);
                }
                if(!edtPhone.getText().toString().isEmpty())
                {
                    inputLayoutPhone.setErrorEnabled(false);
                }
                if(!edtPassComF.getText().toString().isEmpty())
                {
                    inputLayoutPassComF.setErrorEnabled(false);
                }
                if(!edtPass.getText().toString().isEmpty())
                {
                    inputLayoutPass.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        edtName.addTextChangedListener(textWatcher);
        edtEmail.addTextChangedListener(textWatcher);
        edtPhone.addTextChangedListener(textWatcher);
        edtPassComF.addTextChangedListener(textWatcher);
        edtPass.addTextChangedListener(textWatcher);
    }

    void signUp() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.toString().trim()) == 1){
                    Toast.makeText(SignUpActivity.this, "Tạo thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this , LoginActivity.class);
                    intent.putExtra("Email" , edtEmail.getText().toString());
                    intent.putExtra("Pass" , edtPass.getText().toString());
                    setResult(RESULT_OK ,intent);
                    finish();
                }else {
                    if(Integer.parseInt(response.toString().trim()) == 3)
                        Toast.makeText(SignUpActivity.this, "Email này đã được sửa dụng", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(SignUpActivity.this, "Đăng kí thất bại vui lòng thử lại", Toast.LENGTH_SHORT).show();
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
                map.put("Email" , edtEmail.getText().toString().trim());
                map.put("Name" , edtName.getText().toString().trim());
                map.put("Phone" , edtPhone.getText().toString().trim());
                map.put("Password" , UltilMD5.md5(edtPass.getText().toString().trim()));
                map.put("Cate" , 2+"");
                return map;
            }
        };
        requestQueue.add(stringRequest);
    }

    boolean checkValidation(){
        boolean isValid = true;
        if(edtName.getText().toString().isEmpty())
        {
            inputLayoutName.setError(getString(R.string.ErrorName));
            isValid = false;
        }
        else
            inputLayoutName.setErrorEnabled(false);

        if(edtEmail.getText().toString().isEmpty())
        {
            inputLayoutEmail.setError(getString(R.string.ErrorUser));
            isValid = false;
        }
        else
            inputLayoutEmail.setErrorEnabled(false);

        if(edtPhone.getText().toString().isEmpty())
        {
            inputLayoutPhone.setError(getString(R.string.ErrorPhone));
            isValid = false;
        }
        else
            inputLayoutPhone.setErrorEnabled(false);

        if(edtPass.getText().toString().isEmpty())
        {
            inputLayoutPass.setError(getString(R.string.ErrorPass));
            isValid = false;
        }
        else
            inputLayoutPass.setErrorEnabled(false);


        if(edtPassComF.getText().toString().isEmpty())
        {
            inputLayoutPassComF.setError(getString(R.string.ErrorPassConfirm));
            isValid = false;
        }
        else
            inputLayoutPassComF.setErrorEnabled(false);

        return isValid;
    }
}
