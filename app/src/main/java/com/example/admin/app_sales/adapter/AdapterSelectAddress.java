package com.example.admin.app_sales.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.app_sales.R;
import com.example.admin.app_sales.activity.Create_Edit_CustomerActivity;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.activity.PaymentActivity;
import com.example.admin.app_sales.activity.SelectAddressActivity;
import com.example.admin.app_sales.model.Customer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdapterSelectAddress extends ArrayAdapter<Customer> {

    ArrayList<Customer> customers;
    Context context;

    public AdapterSelectAddress(Context context, ArrayList<Customer> objects) {
        super(context, 0, objects);
        this.context = context;
        customers = objects;
    }

    class ViewHolder{
        TextView txtName , txtPhone , txtAdd , txtDefalt;
        ImageButton ibtnEdit , ibtnRemove;
        RelativeLayout layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lv_select_address , null);
            holder.txtName = convertView.findViewById(R.id.txtNameAdd);
            holder.txtPhone = convertView.findViewById(R.id.txtPhoneAdd);
            holder.txtAdd = convertView.findViewById(R.id.txtAddressAdd);
            holder.txtDefalt = convertView.findViewById(R.id.txtDefaltAdd);
            holder.ibtnEdit = convertView.findViewById(R.id.ibtnEdit);
            holder.ibtnRemove = convertView.findViewById(R.id.ibtnRemove);
            holder.layout = convertView.findViewById(R.id.layout_item_lv_select);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        final Customer customer = customers.get(position);
        holder.txtName.setText(customer.getTenKH());
        holder.txtPhone.setText(customer.getSDT());
        holder.txtAdd.setText(customer.getDiaChi());

        if(customer.getMacDinh() == 1){
            holder.txtDefalt.setVisibility(View.VISIBLE);
            holder.ibtnRemove.setVisibility(View.GONE);
        }
        else {
            holder.txtDefalt.setVisibility(View.GONE);
            holder.ibtnRemove.setVisibility(View.VISIBLE);
        }

        if(customer.getKhach_chinh() == 0)
            holder.ibtnRemove.setVisibility(View.GONE);

        holder.ibtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc muốn xóa địa chỉ này");
                builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateUser(customer);
                    }
                });

                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        holder.ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context , Create_Edit_CustomerActivity.class);
                intent.putExtra("Edit" , customer);
                ((SelectAddressActivity)context).startActivityForResult(intent , 101);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PaymentActivity.cusOrder = new Customer(customer);
                ((SelectAddressActivity)context).setResult(Activity.RESULT_OK);
                ((SelectAddressActivity)context).finish();
            }
        });
        return convertView;
    }

    public void updateUser(final Customer user){
        String str = MainActivity.strMain + "updateUser.php";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.trim()) == 1){
                    Toast.makeText(context, "Thành công", Toast.LENGTH_SHORT).show();
                    customers.remove(user);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Thất bại", Toast.LENGTH_SHORT).show();
                }
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
                map.put("khach_hang" , user.getID() +"");
                map.put("Xoa", "1");

                return map;
            }
        };
        requestQueue.add(stringRequest);
    }
}
