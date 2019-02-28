package com.example.admin.app_sales.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.admin.app_sales.R;
import com.r0adkll.slidr.Slidr;
import com.r0adkll.slidr.model.SlidrInterface;
import com.squareup.picasso.Picasso;

public class ImageFullScreenActivity extends Activity {
    ImageView imageView;
    ImageButton imageButton;

    SlidrInterface slidr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_full_screen);
        imageView = findViewById(R.id.imgFull);
        imageButton = findViewById(R.id.ibtnCancle);

        slidr = Slidr.attach(this);

        Picasso.get().load(getIntent().getExtras().getString("img"))
                .into(imageView);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
