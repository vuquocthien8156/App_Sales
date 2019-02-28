package com.example.admin.app_sales.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.app_sales.R;
import com.example.admin.app_sales.model.CatelogyMainList;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.util.ArrayList;

public class AdapterList extends RecyclerView.Adapter<AdapterList.ViewHolder>{
    ArrayList<CatelogyMainList> txtRoot;
    SparseBooleanArray booleanArray = new SparseBooleanArray();
    Context context;

    public AdapterList(ArrayList<CatelogyMainList> txtRoot  , Context context) {
        this.txtRoot = txtRoot;
        for(int i = 0 ; i < txtRoot.size() ; i++)
            booleanArray.append(i , false);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_expand
                                                    , parent , false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.txt.setText(txtRoot.get(position).getTenLoai());

//        holder.expandableLinearLayout.expand();
        holder.setIsRecyclable(false);

        holder.expandableLinearLayout.setInRecyclerView(false);

        //set effect and expand collap
        holder.expandableLinearLayout.setListener(new ExpandableLayoutListenerAdapter(){

            @Override
            public void onPreOpen() {
                changeRotate(holder.ibtn , 0f , 180f).start();
                booleanArray.put(position , true);
            }

            @Override
            public void onPreClose() {
//                holder.expandableLinearLayout.initLayout();

                changeRotate(holder.ibtn , 180f , 0f).start();
                booleanArray.put(position , false);
            }

        });
            // kich hoat btn
        holder.ibtn.setRotation(booleanArray.get(position)?180f:0f);
        holder.ibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // toggle vua dong vua mo?
                    holder.expandableLinearLayout.toggle();
                holder.expandableLinearLayout.initLayout();

            }
        });

        ItemDetailAdapter itemDetailAdapter = new ItemDetailAdapter(context ,
                txtRoot.get(position).getCatelories());
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context , 3));
        holder.recyclerView.setAdapter(itemDetailAdapter);



        //ibtn không nằm trong layout thỳ quay lên
        if(!holder.ibtn.isInLayout()) {
            booleanArray.put(position, false);
            holder.ibtn.setRotation(booleanArray.get(position)?180f:0f);
        }
        else
            holder.expandableLinearLayout.initLayout();

//        holder.setClickListener(new ItemClickListener() {
//            @Override
//            public void onClick(View view, int Position, boolean isLongClick) {
//                Toast.makeText(context, "" + txtRoot.get(position), Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    //hieu ung cho expand
    private ObjectAnimator changeRotate(ImageButton ibtn, float from, float to) {
        ObjectAnimator animator =
                ObjectAnimator.ofFloat(ibtn , "rotation" , from , to );
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public int getItemCount() {
        return txtRoot.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        // implements View.OnClickListener

        TextView txt;
        ImageButton ibtn;
        ExpandableLinearLayout expandableLinearLayout;
        RecyclerView recyclerView;

//        ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.txtParent);
            expandableLinearLayout = itemView.findViewById(R.id.expandLinear);
            ibtn = itemView.findViewById(R.id.ibtnExpand);
            recyclerView = itemView.findViewById(R.id.Detail);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, txtRoot.get(getAdapterPosition()) + "Ahihi", Toast.LENGTH_SHORT).show();
//                }
//            });
        }

//        public void setClickListener(ItemClickListener clickListener) {
//            this.clickListener = clickListener;
//        }
//
//        @Override
//        public void onClick(View view) {
//            clickListener.onClick(view , getAdapterPosition() , false);
//        }
    }
}
