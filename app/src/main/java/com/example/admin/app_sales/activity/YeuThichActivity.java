package com.example.admin.app_sales.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.adapter.AdapterLikeProduct;

public class YeuThichActivity extends AppCompatActivity {
    AdapterLikeProduct adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Yêu thích(" + MainActivity.lsYeuThich.size() + ")");
        setContentView(R.layout.activity_yeu_thich);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = findViewById(R.id.lvYeuThich);
        listView.setAdapter(new AdapterLikeProduct(this, MainActivity.lsYeuThich));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void set(){
        setTitle("Yêu thích(" + MainActivity.lsYeuThich.size() + ")");
    }
}
