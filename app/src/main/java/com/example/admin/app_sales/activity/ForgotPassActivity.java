package com.example.admin.app_sales.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.app_sales.R;

public class ForgotPassActivity extends AppCompatActivity {
    Button btnGuiMa , btnXacNhanMa;
    EditText edtSDT , edtMa;
    TextView txtNhanlai , txtThoigian , txt2;

    CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        btnGuiMa = findViewById(R.id.btnGuiMa);
        btnXacNhanMa = findViewById(R.id.btnXacnhanMa);

        edtMa = findViewById(R.id.edtXacnhanMa);
        edtSDT = findViewById(R.id.edtNhapSdt);

        txtNhanlai = findViewById(R.id.txtNhanLaiMa);
        txtThoigian = findViewById(R.id.txtThoiGian);
        txt2 = findViewById(R.id.textView2);

        //Create PopupWindow
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int heigth = displayMetrics.heightPixels;

        getWindow().setLayout((int)(width*.9) ,(int)(heigth*.3));

        WindowManager.LayoutParams par = getWindow().getAttributes();
        par.gravity = Gravity.CENTER;
        par.x = 0;
        par.y = -40;

        getWindow().setAttributes(par);
        //end

        downTimer = new CountDownTimer(31000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished/1000;
                txtThoigian.setText(time + "s");
            }
            @Override
            public void onFinish() {
                txtNhanlai.setVisibility(View.VISIBLE);
                txtThoigian.setVisibility(View.INVISIBLE);
                txt2.setVisibility(View.INVISIBLE);
            }
        };

        btnGuiMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                        && checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                        requestPermissions(new String[] {Manifest.permission.SEND_SMS} , 100);
                    else
                        sendCode();
            }
        });

        btnXacNhanMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int key = getSharedPreferences("MaXacNhan" , MODE_PRIVATE).getInt("Ma" , 1);
                if(key != 1 && !edtMa.getText().equals(""))
                {
                    if(edtMa.getText().toString().trim().equals(key+"")){
                        startActivity(new Intent(ForgotPassActivity.this , ChangePassActivity.class));
                        finish();
                    }
                    else
                        Toast.makeText(ForgotPassActivity.this, "Mã không chính xác", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtNhanlai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt2.setVisibility(View.VISIBLE);
                txtThoigian.setVisibility(View.VISIBLE);
                sendCode();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendCode();
            }
            else
                Toast.makeText(this, "Permissions deny", Toast.LENGTH_SHORT).show();
        }
    }

    void sendCode(){
        downTimer.start();

        int a = (int)(Math.random()*8999 + 1000);

        getSharedPreferences("MaXacNhan" , MODE_PRIVATE).edit().putInt("Ma" , a).commit();

        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(edtSDT.getText().toString(),
                null,
                "Mã xác nhận của bạn là: " + a,
                null,
                null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSharedPreferences("MaXacNhan" , MODE_PRIVATE)
                        .edit().remove("Ma").commit();
            }
        } ,500000);
        btnXacNhanMa.setVisibility(View.VISIBLE);
        txtThoigian.setVisibility(View.VISIBLE);
        txt2.setVisibility(View.VISIBLE);
        edtMa.setVisibility(View.VISIBLE);
        edtSDT.setVisibility(View.INVISIBLE);
        btnXacNhanMa.setVisibility(View.VISIBLE);
        btnGuiMa.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
