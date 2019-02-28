package com.example.admin.app_sales.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
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
import com.example.admin.app_sales.activity.ChangePassActivity;
import com.example.admin.app_sales.activity.HistoryActivity;
import com.example.admin.app_sales.activity.MainActivity;
import com.example.admin.app_sales.activity.YeuThichActivity;
import com.example.admin.app_sales.model.Customer;
import com.example.admin.app_sales.ultil.UltilMD5;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentUser extends Fragment{
    EditText edtName , edtEmail , edtSDT , edtPass;
    Button btnLogout , btnXacnhan , btnHuy;
    ImageButton btnSuaName , btnSuaEmail , btnSuaSDT , btnSuaPass;
    TextView btnYeuThich , btnLichSu;

    ImageView imageView;
    SharedPreferences sharedPreferences;
    String path = "";
    String urlUpload = MainActivity.strMain + "uploadimg.php";

    Customer customer = new Customer();
    Bitmap bitmap;
    public static int REQUEST_CODE_PICK = 100;
    public static int REQUEST_CODE_CAMERA = 101;

    boolean i = false;

    public boolean checkFirst = true;

    public FragmentUser() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_layout, container , false);

        edtName = view.findViewById(R.id.txtName);
        edtEmail = view.findViewById(R.id.txtEmail);
        edtSDT = view.findViewById(R.id.txtSDT);
        edtPass = view.findViewById(R.id.txtPassword);

        btnSuaName = view.findViewById(R.id.btnSuaName);
        btnSuaEmail = view.findViewById(R.id.btnSuaMail);
        btnSuaSDT = view.findViewById(R.id.btnSuaSDT);
        btnSuaPass = view.findViewById(R.id.btnSuaPass);

        btnLogout = view.findViewById(R.id.btnLogout);
        btnXacnhan = view.findViewById(R.id.btnXacnhan);
        btnHuy = view.findViewById(R.id.btnHuy);
        btnYeuThich = view.findViewById(R.id.btnYeuThich);
        btnLichSu = view.findViewById(R.id.btnLichSu);

        btnYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , YeuThichActivity.class));
            }
        });

        btnLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , HistoryActivity.class));
            }
        });

        imageView = view.findViewById(R.id.imgAvata);

        Animation anim = AnimationUtils.loadAnimation(getContext(),R.anim.anim_nghich);
        imageView.setAnimation(anim);
        btnLogout.setAnimation(anim);

        setTextEdt();

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId())
                {
                    case R.id.btnSuaName:
                        edtName.setEnabled(true);
                        btnXacnhan.setVisibility(View.VISIBLE);
                        btnHuy.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btnSuaMail:
                        xacNhanDoiEmail();
                        break;
                    case R.id.btnSuaSDT:
                        edtSDT.setEnabled(true);
                        btnXacnhan.setVisibility(View.VISIBLE);
                        btnHuy.setVisibility(View.VISIBLE);
                        break;
                    case R.id.btnSuaPass:
                        startActivity(new Intent(getActivity() , ChangePassActivity.class));
                        break;
                }

            }
        };

        btnSuaName.setOnClickListener(onClickListener);
        btnSuaEmail.setOnClickListener(onClickListener);
        btnSuaSDT.setOnClickListener(onClickListener);
        btnSuaPass.setOnClickListener(onClickListener);

        btnXacnhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!i)
                    xacnhanPass();
                else {
                    Toast.makeText(getContext(), "Bạn đợi chút nhé", Toast.LENGTH_SHORT).show();
                    uploadImage();
                    btnXacnhan.setVisibility(View.INVISIBLE);
                    i = false;
                }
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTextEdt();
                setEnable();
                btnXacnhan.setVisibility(View.INVISIBLE);
                btnHuy.setVisibility(View.INVISIBLE);
            }
        });

        //Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("Login" , Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("Username");
                editor.apply();
                MainActivity.soluongCart = 0;
                ((MainActivity)getActivity()).setCountCart();
                MainActivity.lsYeuThich = new ArrayList<>();
                MainActivity.khach_hang = new Customer();
                ((MainActivity)getActivity()).setSelectedHome(0);
            }
        });

//        getUser(url);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPopupMenu();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        customer = MainActivity.khach_hang;

        super.onCreate(savedInstanceState);
    }

    private void xacNhanDoiEmail() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có chắc chắn muốn đổi email?");
        builder.setPositiveButton("Chắc chắn", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
    }

    private void setPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext() , imageView);
        popupMenu.getMenuInflater().inflate(R.menu.menu_img , popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case R.id.menuChonHinh:

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                getActivity()
                                        .checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}
                                    , REQUEST_CODE_PICK);

                        }
                        else{
                            Intent intentChonHinh = new Intent(Intent.ACTION_GET_CONTENT);
                            intentChonHinh.setType("image/*");
                            startActivityForResult(intentChonHinh, REQUEST_CODE_PICK);
                        }

                        break;
                    case R.id.menuChupHinh:
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                                getActivity()
                                        .checkSelfPermission(Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(
                                     new String[]{Manifest.permission.CAMERA}
                                    , REQUEST_CODE_CAMERA);
                        }
                        else{
                            Intent intentChupHinh = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intentChupHinh , REQUEST_CODE_CAMERA);

                        }
                        break;
                }

                return false;
            }
        });

        popupMenu.show();
    }

    void uploadImage(){
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("uppp" , response);
                if(response.trim().equals("1")){
                    Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show()
                    ;
                }
            }
        }, new Response.ErrorListener() {
            @Override

            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> map = new HashMap<>();
                String str = convertImagetoString(bitmap);
                map.put("image" , str);
                map.put("Email" , MainActivity.khach_hang.getEmail());

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    private String convertImagetoString(Bitmap bitmap1){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.JPEG , 100 , byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        String str = Base64.encodeToString(bytes , Base64.DEFAULT);
        return  str;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_PICK) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentChonHinh = new Intent(Intent.ACTION_GET_CONTENT);
                intentChonHinh.setType("image/*");
                startActivityForResult(intentChonHinh, REQUEST_CODE_PICK);
            }
            else
                Toast.makeText(getContext(), "You don't have permission", Toast.LENGTH_SHORT).show();
            return;
        }

        if(requestCode == REQUEST_CODE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intentChupHinh = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentChupHinh , REQUEST_CODE_CAMERA);
            }
            else
                Toast.makeText(getContext(), "You don't have permission", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_PICK && resultCode == getActivity().RESULT_OK && data !=null)
        {
            Uri uri = data.getData();
            try {

                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver() , uri);
                RoundedBitmapDrawable roundedBitmapDrawable
                        = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                roundedBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(roundedBitmapDrawable);
                i = true;
                btnXacnhan.setVisibility(View.VISIBLE);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(requestCode == REQUEST_CODE_CAMERA && resultCode == getActivity().RESULT_OK && data !=null)
        {
            bitmap = (Bitmap) data.getExtras().get("data");
            RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
            i = true;
            btnXacnhan.setVisibility(View.VISIBLE);
        }
    }

    private void xacnhanPass() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm_password);

        final EditText editText = dialog.findViewById(R.id.edtXacnhan);

        Button btnXacnhan1 = dialog.findViewById(R.id.btnXacnhan);

        btnXacnhan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences = getActivity().getSharedPreferences("Login" , Context.MODE_PRIVATE);
                final String pass = sharedPreferences.getString("Password" , null);
                Log.d("pass" , pass);
                if(UltilMD5.md5(editText.getText().toString().trim()).equals(pass)) {
                    updateUser();
                    btnXacnhan.setVisibility(View.INVISIBLE);
                    btnHuy.setVisibility(View.INVISIBLE);
                    setEnable();
                    dialog.dismiss();
                }
                else
                    Toast.makeText(getContext(), "Mật khẩu sai rồi kìa", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();


    }

    private void updateUser() {
        String str = MainActivity.strMain + "updateUser.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, str, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(Integer.parseInt(response.toString().trim()) == 1){
                    Toast.makeText(getContext(), "Sửa thông tin thành công", Toast.LENGTH_SHORT).show();
//                    getUser(url);
                    ((MainActivity)getActivity()).checkingLogin();
                }else {
                    Toast.makeText(getContext(), "Sửa thông tin thất bại xin thử lại sau", Toast.LENGTH_SHORT).show();
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
                map.put("khach_hang" , String.valueOf(MainActivity.khach_hang.getID()));
                map.put("Name", edtName.getText().toString().trim());
                map.put("Phone", edtSDT.getText().toString().trim());

                return map;
            }
        };
        requestQueue.add(stringRequest);

    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
    }

    private void setTextEdt() {
        edtName.setText(customer.getTenKH());
        edtEmail.setText(customer.getEmail());
        edtSDT.setText(customer.getSDT());
        edtPass.setText(UltilMD5.md5("1"));


        if(customer.getAvata() != ""){
            Picasso.get().load(customer.getAvata()).into(imageView);
            if(imageView.getDrawable() == null)
                imageView.setImageResource(R.drawable.ic_user);
            Bitmap b = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            RoundedBitmapDrawable roundedBitmapDrawable
                    = RoundedBitmapDrawableFactory.create(getResources(), b);
            roundedBitmapDrawable.setCircular(true);
            imageView.setImageDrawable(roundedBitmapDrawable);
        }
        else{

            imageView.setImageResource(R.drawable.ic_user);
        }

    }

    private void setEnable() {
        edtName.setEnabled(false);
        edtEmail.setEnabled(false);
        edtSDT.setEnabled(false);
        edtPass.setEnabled(false);


    }

    public static FragmentUser newInstance() {
        
        Bundle args = new Bundle();
        
        FragmentUser fragment = new FragmentUser();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        if(checkFirst) {

            checkFirst = false;
            edtEmail.setText("");
            edtSDT.setText("");
            edtName.setText("");
            edtPass.setText("");

            customer = MainActivity.khach_hang;
            setTextEdt();
        }
        super.onStart();

    }


}
