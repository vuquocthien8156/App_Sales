package com.example.admin.app_sales.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.Snap;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{
    private Context context;
    private ArrayList<Snap> snaps;

    static int Horizontal = 1;
    static int Vertical = 2;

    public Adapter(Context context, ArrayList<Snap> snaps) {
        this.context = context;
        this.snaps = snaps;
    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = null;
        if(viewType == Horizontal)
            view = layoutInflater.inflate(R.layout.item_snap_recyleview , parent , false);
        else
            view = layoutInflater.inflate(R.layout.item_snap_recyleview_vertical , parent , false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Snap snap = snaps.get(position);
        if(snaps.get(position).getOrientation() == Horizontal) {
            holder.textView.setText(snap.getSnapText());
            ItemSnapAdapter itemSnapAdapter;
            itemSnapAdapter = new ItemSnapAdapter(context, snap.getProducts());
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(itemSnapAdapter);
        }
        else {
            holder.textView.setText(snap.getSnapText());
            ItemSnapAdapter2 itemSnapAdapter2;
            itemSnapAdapter2 = new ItemSnapAdapter2(context, snap.getProducts());
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context , 2));
            holder.recyclerView.setAdapter(itemSnapAdapter2);

        }

    }

    @Override
    public int getItemCount() {
        return snaps.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(snaps.get(position).getOrientation() == Horizontal){
            return Horizontal;
        }else {
            return Vertical;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        RecyclerView recyclerView;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txtSnapRootItem);
            recyclerView = itemView.findViewById(R.id.recyclerSnapItemm);
        }
    }
}
