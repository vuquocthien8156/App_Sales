package com.example.admin.app_sales.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.admin.app_sales.NetworkChangeReceiver;
import com.example.admin.app_sales.R;

public class Check_Internet_Activity extends Activity {
    NetworkChangeReceiver networkChangeReceiver;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__internet_);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(NetworkChangeReceiver.isConnected(this)){
            startActivity(new Intent(Check_Internet_Activity.this , MainActivity.class));
        }
    }

    public void checkInternet(View view)
    {
        progressBar.setVisibility(View.VISIBLE);

        CountDownTimer countDownTimer = new CountDownTimer(4000 , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if(NetworkChangeReceiver.isConnected(Check_Internet_Activity.this)){
                    startActivity(new Intent(Check_Internet_Activity.this , MainActivity.class));
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else{
                    Toast.makeText(Check_Internet_Activity.this, "Rất tiếc cháu không đủ tiền đăng kí mạng", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }.start();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
