package com.example.admin.app_sales.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.activity.SearchActivity;
import com.example.admin.app_sales.adapter.Adapter;
import com.example.admin.app_sales.adapter.ViewPagerAdapter;
import com.example.admin.app_sales.model.Product;
import com.example.admin.app_sales.model.Snap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import me.angeldevil.autoscrollviewpager.AutoScrollViewPager;
import me.relex.circleindicator.CircleIndicator;


public class FragmentHome extends Fragment{
    ArrayList<String> listImg;
    AutoScrollViewPager viewPager;
    CircleIndicator indicator;
    ViewFlipper viewFlipper;

    ArrayList<Snap> snaps;
    Adapter adapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    EditText edtSearch;

    String strLinkHome = MainActivity.strMain + "LoadDataBase.php";
    public FragmentHome() {
        listImg = new ArrayList<>();
        snaps = new ArrayList<>();
    }

    void addImg() {
//        listImg.clear();
//        AssetManager assetManager = getContext().getAssets();
//        try {
//            String[] img = assetManager.list("img_quangcao");
//            for(String s : img)
//            {
//                listImg.add(convertStringtoBitmap(assetManager , s));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        listImg.add("https://previews.123rf.com/images/leezarius/leezarius1708/leezarius170800076/84358350-memorial-day-sale-promotion-advertising-horizontal-banner-template-vector-illustration--Stock-Photo.jpg  ");
        listImg.add("https://img1-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/9112/large_thumb_simple-facebook-ad-template-facebook-ad-maker-for-t-shirts.jpg");
        listImg.add("http://static1.squarespace.com/static/5a443d8eedaed8dcd6ebfd11/5b0511d86d2a73643903347f/5b161e0d575d1f3054d56359/1528177330047/vintage+style.jpg?format=1500w");
        listImg.add("https://img1-placeit-net.s3-accelerate.amazonaws.com/uploads/stage/stage_image/19736/large_thumb_stage.jpg");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        progressDialog = ProgressDialog.show(getActivity() ,  "Bạn đợi chút nhé" , "Loading...");

        final View view = inflater.inflate(R.layout.fragment_home_layout, container ,false);

        indicator = view.findViewById(R.id.indicator);
        viewPager = view.findViewById(R.id.viewpagerHome);

        addImg();

        viewPager.setAdapter(new ViewPagerAdapter(getContext() , listImg));

        viewPager.setScrollFactor(5);
        viewPager.setOffscreenPageLimit(4);
        viewPager.startAutoScroll(5000);
//        final android.os.Handler handler = new android.os.Handler();
//        final int[] i = {0};
//        //Chay Quang cao
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                switch (i[0])
//                {
//                    case 0:
//                        i[0]++;
//                        viewPager.setCurrentItem(i[0]);
//                        break;
//                    case 1:
//                        i[0]++;
//                        viewPager.setCurrentItem(i[0]);
//                        break;
//                    case 2:
//                        i[0]++;
//                        viewPager.setCurrentItem(i[0]);
//                        break;
//                    case 3:
//                        i[0]++;
//                        viewPager.setCurrentItem(i[0]);
//                        break;
//
//                        case 4:
//                        i[0] = 0;
//                        viewPager.setCurrentItem(i[0]);
//                    break;
//
//                }
//                handler.postDelayed(this , 2000);
//            }
//        } , 1000);

        indicator.setViewPager(viewPager);
//        getActivity().getWindow().setStatusBarColor(Color.parseColor("#88fdaad8"));
//        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                switch (i)
//                {
//                    case 0:
//                        getActivity().getWindow().setStatusBarColor(Color.parseColor("#88fdaad8"));
//                        break;
//                    case 1:
//                        getActivity().getWindow().setStatusBarColor(Color.parseColor("#bedbc29a"));
//                        break;
//                    case 2:
//                        getActivity().getWindow().setStatusBarColor(Color.parseColor("#64ffffff"));
//                        break;
//                    case 3:
//                        getActivity().getWindow().setStatusBarColor(Color.parseColor("#64ffffff"));
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });

//ViewFliper
//        viewFlipper = view.findViewById(R.id.viewfliper);
//        ArrayList<Integer> img = new ArrayList<>();
//        img.add(R.drawable.ic_launcher_background);
//        img.add(R.drawable.ic_launcher_foreground);
//        img.add(R.drawable.ic_home_black_24dp);
//        img.add(R.drawable.ic_dashboard_black_24dp);
//
//        for (int i : img)
//        {
//            ImageView imageView = new ImageView(getContext());
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            viewFlipper.addView(imageView);
//        }
//        viewFlipper.setAutoStart(true);
//        viewFlipper.setFlipInterval(3000);
//        viewFlipper.setInAnimation(getActivity() , R.anim.anim_viewfliper_in);
//        viewFlipper.setOutAnimation(getActivity() , R.anim.anim_viewfliper_out);
        recyclerView = view.findViewById(R.id.recyclerHome);

        adapter = new Adapter(getContext() , snaps);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

//        recyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                recyclerView.scrollToPosition(0);
//            }
//        });

        edtSearch = view.findViewById(R.id.edtSearchHome);
        edtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext() , SearchActivity.class));
            }
        });
        return view;
    }

    public static FragmentHome newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onAttach(Context context) {

        if(snaps.isEmpty()) {
            loadDataforFragmentHome();

        }
        super.onAttach(context);

    }

    private Bitmap convertStringtoBitmap(AssetManager assetManager, String s) {

            try {
                InputStream inputStream = assetManager.open("img_quangcao/" + s);
                Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                return bmp;
            } catch (IOException e) {
                e.printStackTrace();
            }

        return  null;
    }

    void loadDataforFragmentHome() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, strLinkHome, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0 ; i <response.length() ; i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                String TenLoai = jsonObject.getString("TenLoaiMain");
                                JSONArray jsonArray = jsonObject.getJSONArray("Products");
                                ArrayList<Product> products = new ArrayList<>();

                                for(int j = 0 ; j <jsonArray.length() ; j++)
                                {
                                    JSONObject object = (JSONObject) jsonArray.get(j);

                                    products.add(new Product(
                                            Integer.parseInt(object.getString("MaSanPham")),
                                            object.getString("TenSanPham"),
                                            object.getString("MoTa").toString(),
                                            Integer.parseInt(object.getString("GiaSanPham")),
                                            MainActivity.urlImg + object.getString("URL").toString(),
                                            Integer.parseInt(object.getString("LoaiSanPham"))
                                    ));
                                }
                                if(products.size()%2 == 0)
                                    snaps.add(new Snap(TenLoai , products , 2));
                                else
                                    snaps.add(new Snap(TenLoai , products , 1));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error" , error.toString());
                        progressDialog.dismiss();

                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }
}
