package com.example.admin.app_sales.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.NetworkChangeReceiver;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.fragment.FragmentCarts;
import com.example.admin.app_sales.fragment.FragmentHome;
import com.example.admin.app_sales.fragment.FragmentList;
import com.example.admin.app_sales.fragment.FragmentUser;
import com.example.admin.app_sales.model.Customer;
import com.example.admin.app_sales.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
//implements NetworkChangeReceiver.ConnectivityReceiverListener

                boolean doubleBackToExitPressedOnce = false;

                android.support.v4.app.FragmentManager fragmentManager;
                android.support.v4.app.FragmentTransaction fragmentTransaction;

                SharedPreferences sharedPreferences;
                BottomNavigationView navigation;
                public static ArrayList<Product> lsYeuThich;
                public static Customer khach_hang = new Customer();

//    public static String strMain = "https://muadovoithien.000webhostapp.com/Thien/";
                public static String strMain = "http://192.168.43.236:8000/Thien/";
                String urlCart = strMain + "getCart.php";
                String url = strMain + "getCountCart.php";
                public static String urlImg = strMain;
                String str = strMain + "getUser.php";
                String strYeuThich = strMain + "laySanPhamYeuThich.php";

                final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
                NetworkChangeReceiver networkChangeReceiver;

                FragmentHome fragmentHome;
                FragmentCarts fragmentCards;
                FragmentList fragmentList;FragmentUser fragmentUser;
                TextView txtSoluongCart;
                public static int soluongCart = 0;
                private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                        = new BottomNavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:

                                addFragmentHome();

                                break;
                            case R.id.navigation_list:
                                addFragmentList();
                                break;
                case R.id.navigation_cart:
                    sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);
                    if(sharedPreferences.getString("Username" , null) == null) {
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 100);
                        overridePendingTransition(R.anim.anim_enter_activity , R.anim.anim_exit_activity);
                    }else {

                            addFragmentCard();
                    }
                    break;

                case R.id.navigation_account:
                    sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);
                    if(sharedPreferences.getString("Username" , null) == null) {
                        startActivityForResult(new Intent(MainActivity.this, LoginActivity.class), 100);
                        overridePendingTransition(R.anim.anim_enter_activity , R.anim.anim_exit_activity);

                    }else {
                                addFragmentUser();
                    }
                    break;
            }

            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode == RESULT_CANCELED )
        {
            View view = navigation.findViewById(R.id.navigation_home);
            view.performClick();
        }
        if(requestCode == 100 && resultCode == RESULT_OK )
        {
            fragmentUser.checkFirst = true;
            View view = navigation.findViewById(R.id.navigation_account);
            view.performClick();
        }

        if(requestCode == 102 && resultCode == RESULT_OK )
        {
            View view = navigation.findViewById(R.id.navigation_cart);
            view.performClick();
        }
    }

    void addFragmentHome() {

         android.support.v4.app.FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        if(fragmentHome.isAdded()){
            fr.show(fragmentHome);
        }
        else
            fr.add(R.id.frameMain, fragmentHome , "Home");

        if (fragmentList.isAdded())
            fr.hide(fragmentList);
        if (fragmentCards.isAdded())
            fr.hide(fragmentCards);
        if  (fragmentUser.isAdded())
            fr.hide(fragmentUser);

        fr.commit();

    }

    void addFragmentList() {

        android.support.v4.app.FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        if(fragmentList.isAdded()){
            fr.show(fragmentList);
        }
        else
            fr.add(R.id.frameMain, fragmentList , "List");

        if (fragmentHome.isAdded())
            fr.hide(fragmentHome);
        if (fragmentCards.isAdded())
            fr.hide(fragmentCards);
        if  (fragmentUser.isAdded())
            fr.hide(fragmentUser);
        fr.commit();
    }

    void addFragmentCard() {

        android.support.v4.app.FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        if(fragmentCards.isAdded()){
            fr.show(fragmentCards);
            fragmentCards.getDataCart(sharedPreferences.getString("Username" , null) , urlCart);
        }
        else
            fr.add(R.id.frameMain, fragmentCards , "Carts");

        if (fragmentHome.isAdded())
            fr.hide(fragmentHome);
        if (fragmentList.isAdded())
            fr.hide(fragmentList);
        if  (fragmentUser.isAdded())
            fr.hide(fragmentUser);

        fr.commit();

    }

    void addFragmentUser() {

        android.support.v4.app.FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        if(fragmentUser.isAdded()){
            fr.show(fragmentUser);
        }
        else
            fr.add(R.id.frameMain, fragmentUser , "User");

        if (fragmentHome.isAdded())
            fr.hide(fragmentHome);
        if (fragmentList.isAdded())
            fr.hide(fragmentList);
        if  (fragmentCards.isAdded())
            fr.hide(fragmentCards);

        fr.commit();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        final LinearLayout linearLayoutStart = findViewById(R.id.linearStart);
//        final LinearLayout linearLayoutMain = findViewById(R.id.linearMain);
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                linearLayoutMain.setVisibility(View.VISIBLE);
//                linearLayoutStart.setVisibility(View.INVISIBLE);
//            }
//
//        }, 5000);

        //Check Exception Check Main Running
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {

                // fix our issues for sharedpreferences
                sharedPreferences = getSharedPreferences("Main" , MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("CheckMain" , false).commit();

                // Handle everthing else
                defaultHandler.uncaughtException(thread, throwable);
            }
        });


        sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);
        String user = sharedPreferences.getString("Username" , null);
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        fragmentHome = FragmentHome.newInstance();
        fragmentCards = FragmentCarts.newInstance();
        fragmentList = FragmentList.newInstance();
        fragmentUser = FragmentUser.newInstance();

        addFragmentHome();

        loadNotifiCountCart();
        if(user != null) {
            getCountCart(user, url);
//            getUser();
            checkingLogin();
        }
    }

    public  void checkingLogin() {
        sharedPreferences = getSharedPreferences("Login" , MODE_PRIVATE);
        final String user = sharedPreferences.getString("Username" , null).trim();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Customer customer = new Customer();

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
                return map;
            }
        };
        requestQueue.add(stringRequest);
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

    void getUser(){
        try {
            FileInputStream inputStream = openFileInput("user.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null){
                stringBuffer.append(line);
            }

            JSONObject jsonObject = new JSONObject(stringBuffer.toString());

            khach_hang = new Customer(
                    jsonObject.getInt("id"),
                    jsonObject.getString("TenKH")
                    , jsonObject.getString("Mail")
                    , jsonObject.getString("SDT")
                    , MainActivity.urlImg + jsonObject.getString("Img")
                    , jsonObject.getInt("khach_chinh")
                    , jsonObject.getString("dia_chi")
                    , jsonObject.getInt("mac_dinh"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        String user = sharedPreferences.getString("Username" , null);
        if(soluongCart == 0 && user != null)
            getCountCart(user , url);

        txtSoluongCart.setText(soluongCart + "+");
        super.onRestart();
    }


    void loadNotifiCountCart() {
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(2);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, bottomNavigationMenuView, false);

        txtSoluongCart = badge.findViewById(R.id.notifications_badge);

        itemView.addView(badge);
    }

    public void setSelectedHome(int i) {
        if(i == 0) {
            View view = navigation.findViewById(R.id.navigation_home);
            view.performClick();
//            loadNotifiCountCart();
        }

    }

    void getCountCart(final String user , String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest jsonArrayRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.trim().isEmpty()) {
                    txtSoluongCart.setText(response.trim() + "+");
                    soluongCart = Integer.parseInt(response.trim());
                }
                else{
                    txtSoluongCart.setText(0 + "+");
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                map.put("Userr", String.valueOf(MainActivity.khach_hang.getID()));
                return map;
            }
        };

        requestQueue.add(jsonArrayRequest);
    }

    public void setCountCart()
    {
        txtSoluongCart.setText(soluongCart + "+");
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Nhấp 1 lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        }
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        sharedPreferences = getSharedPreferences("Main" , MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("CheckMain" , true).commit();
    }

    @Override
    protected void onStop() {
        sharedPreferences = getSharedPreferences("Main" , MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("CheckMain" , false).apply();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!NetworkChangeReceiver.isConnected(this))
            startActivity(new Intent(MainActivity.this , Check_Internet_Activity.class));
    }

}
