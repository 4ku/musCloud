package com.example.vadim.muscloud;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.vadim.muscloud.Authentification.AuthFragment;
import com.example.vadim.muscloud.Authentification.LoginFragment;

public class OnlineServiceActivity extends AppCompatActivity {

    public static String intentMessage="SERVICE_NAME";
    TextView tv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_service);
        tv=findViewById(R.id.tvConnectService);

        Bundle bundle = getIntent().getExtras();
        tv.setText("Please, authorize in "+bundle.getString(intentMessage));

    }



    @Override
    public void onBackPressed() {
        Intent startProfileIntent =
                new Intent(OnlineServiceActivity.this, MusicActivity.class);
        startActivity(startProfileIntent);
        this.finish();

    }
}
