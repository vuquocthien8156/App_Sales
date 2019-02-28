package com.example.admin.app_sales.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.admin.app_sales.R;

public class Sheet_Bottom_DetailProduct extends BottomSheetDialogFragment {

    int soluong = 1;
    setOnClickThemVaoGioHang onClickThemVaoGioHang;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_bottom_detail , null);
        final ImageButton btnCong = view.findViewById(R.id.btnSheet_Cong);
        final ImageButton btnTru = view.findViewById(R.id.btnSheet_Tru);
        Button btnThemVaoGH = view.findViewById(R.id.btnThemVaoGH_Sheet);
        final TextView txtSoluong = view.findViewById(R.id.txtSoLuongSheet);


        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong++;
                if(soluong < 11)
                    txtSoluong.setText(soluong + "");
                if(soluong == 10)
                    btnCong.setVisibility(View.INVISIBLE);
                if(soluong > 0)
                    btnTru.setVisibility(View.VISIBLE);
            }
        });


        btnTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                soluong--;
                if(soluong > 0)
                    txtSoluong.setText(soluong + "");
                if(soluong == 1)
                    btnTru.setVisibility(View.INVISIBLE);
                if(soluong < 10)
                    btnCong.setVisibility(View.VISIBLE);

            }
        });

        btnThemVaoGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickThemVaoGioHang.truyenSoLuong(soluong);
                dismiss();
            }
        });

        return view;
    }

    public interface setOnClickThemVaoGioHang{
        void truyenSoLuong(int soLuong);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onClickThemVaoGioHang = (setOnClickThemVaoGioHang) context;
        }catch (ClassCastException e) {

        }
    }
}
