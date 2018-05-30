package com.example.vadim.muscloud;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.vadim.muscloud.Entities.User;

public class AccountInfoActivity extends AppCompatActivity {
    public static String intentMessage="LOGIN";
    TextView tvLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        tvLogin=findViewById(R.id.tvEmail);

        if(getIntent()!=null && getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            tvLogin.setText((String)bundle.get(intentMessage));
        }
    }



    @Override
    public void onBackPressed() {
        Intent startProfileIntent =
                new Intent(AccountInfoActivity.this, MusicActivity.class);
        startActivity(startProfileIntent);
        this.finish();
    }
}
