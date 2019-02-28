package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.example.admin.app_sales.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    ArrayList<String> listImg;
    LayoutInflater layoutInflater;
    Context context;

    public ViewPagerAdapter(Context context , ArrayList<String> listImg) {
        this.context = context;
        this.listImg = listImg;

        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listImg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = layoutInflater.inflate(R.layout.item_viewpager , null);
        LinearLayout layout = view.findViewById(R.id.layoutViewPager);
        ImageView imageView = view.findViewById(R.id.imageView);

        Picasso.get()
                .load(listImg.get(position))
                .into(imageView);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
