package com.wine.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.wine.restaurant.data.Constans;
import com.wine.restaurant.data.Session;
import com.wine.restaurant.model.LoginResponse;

public class ProfileActivity extends AppCompatActivity {
    Session session;
    TextView nama, userId;
    Button btnChange;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        session = new Session(this);
        progressDialog = new ProgressDialog(this);
        initBinding();
        loadItem();
        initClick();
    }
    //Method untuk load data dari api
    public void loadItem() {
        //show progress dialog
        Toast.makeText(this, "id "+ session.getUserId(USER_ID), Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Please Wait..");
        progressDialog.show();
        AndroidNetworking.get(Constans.GET_PROFIL_USER + "/" + session.getUserId(USER_ID)).build().getAsObject(LoginResponse.class, new ParsedRequestListener() {
            @Override
            public void onResponse(Object response) {
                progressDialog.dismiss();
                if (((LoginResponse) response).getLogin() != null)
                {
                    loadDataRes(((LoginResponse) response));
                }
            }
            @Override
            public void onError(ANError anError) {
                progressDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDataRes(LoginResponse response) {
        nama.setText(response.getLogin().getNama());
        userId.setText(response.getLogin().getUserid());
    }
    private void initBinding() {
        nama = findViewById(R.id.tv_name);
        userId = findViewById(R.id.tv_user_id);
        btnChange = findViewById(R.id.btn_change);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadItem();
    }
    private void initClick() {
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this,ChangePasswordActivity.class);
                i.putExtra("nama",nama.getText().toString());
                startActivity(i);
            }
        });
    }
}

