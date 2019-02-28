package com.example.admin.app_sales.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.adapter.AdapterList;
import com.example.admin.app_sales.model.CatelogyMainList;
import com.example.admin.app_sales.model.Catelory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentList extends Fragment{

    Button btnNam , btnNu;

    RecyclerView recyclerView;
    ArrayList<CatelogyMainList> array;
    ArrayList<CatelogyMainList> listMan;
    ArrayList<CatelogyMainList> listWoman;
    AdapterList adapterList;
    ProgressDialog progressDialog;


    String strList = MainActivity.strMain + "LoadDataList.php";
    public FragmentList() {
        //Detail
        listMan = new ArrayList<>();
        listWoman = new ArrayList<>();

        array = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_layout, container , false);
        progressDialog = ProgressDialog.show(getActivity() ,  "Bạn đợi chút nhé" , "Loading...");

        recyclerView = view.findViewById(R.id.recyclerView2);
        adapterList = new AdapterList(array , getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterList);

        loadData();

        btnNam = view.findViewById(R.id.btnNam);
        btnNu = view.findViewById(R.id.btnNu);

        btnNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNam.setBackgroundColor(Color.parseColor("#ed7878"));
                btnNu.setBackgroundColor(Color.parseColor("#c4c3c3"));
                array.clear();
                array.addAll(listMan);
                adapterList.notifyDataSetChanged();
            }
        });

        btnNu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNam.setBackgroundColor(Color.parseColor("#c4c3c3"));
                btnNu.setBackgroundColor(Color.parseColor("#ed7878"));
                array.clear();
                array.addAll(listWoman);
                adapterList.notifyDataSetChanged();
            }
        });

        return view;
    }

    public static FragmentList newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentList fragment = new FragmentList();
        fragment.setArguments(args);
        return fragment;
    }

    void loadData() {

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, strList, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0 ; i <response.length() ; i++)
                        {
                            try {
                                JSONObject jsonObject = (JSONObject) response.get(i);
                                int thoiTrang = Integer.parseInt(jsonObject.getString("LoaiThoiTrang"));
                                int maLoai = Integer.parseInt(jsonObject.getString("MaLoai"));
                                String tenLoai = jsonObject.getString("TenLoai");
                                JSONArray jsonArray = jsonObject.getJSONArray("LoaiCuThes");
                                ArrayList<Catelory> cate = new ArrayList<>();

                                for(int j = 0 ; j <jsonArray.length() ; j++)
                                {
                                    JSONObject object = (JSONObject) jsonArray.get(j);

                                    cate.add(new Catelory(
                                            Integer.parseInt(object.getString("MaLoaiCuThe")),
                                            object.getString("TenLoaiCuThe"),
                                            object.getString("Link").toString()
                                    ));
                                }
                                if(thoiTrang == 1) {
                                    listMan.add(new CatelogyMainList(thoiTrang, maLoai, tenLoai, cate));
                                    array.add(new CatelogyMainList(thoiTrang, maLoai, tenLoai, cate));
                                }
                                else
                                    listWoman.add(new CatelogyMainList(thoiTrang , maLoai , tenLoai , cate));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapterList.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error" , error.toString());
                progressDialog.dismiss();

            }
        });

        requestQueue.add(jsonArrayRequest);

    }
}
