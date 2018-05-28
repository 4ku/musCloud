package com.example.vadim.muscloud.Authentification;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;

import com.example.vadim.muscloud.R;

public class AuthFragment extends Fragment {
    final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 138;

    private Button mEnter;
    private Button mRegister;
    private SharedPreferencesHelper mSharedPreferencesHelper;
    ImageView musCloudText;

    VideoView videoview;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();
        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener mOnEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String tag = LoginFragment.class.getName();
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out,R.anim.left_enter,R.anim.right_out)
                    .replace(R.id.fragmentContainer, LoginFragment.newInstance(),tag)
                    .addToBackStack(tag)
                    .commit();
        }
    };

    private View.OnClickListener mOnRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String tag=SignUpFragment.class.getName();
            getFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(R.anim.right_enter, R.anim.left_out,R.anim.left_enter,R.anim.right_out)
                    .replace(R.id.fragmentContainer, SignUpFragment.newInstance(),tag)
                    .addToBackStack(tag)
                    .commit();

        }
    };


    public void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                }
//                return;
//            }
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        mEnter = v.findViewById(R.id.buttonEnter);
        mRegister = v.findViewById(R.id.buttonRegister);
        musCloudText = v.findViewById(R.id.musCloudTitle);

        mEnter.setOnClickListener(mOnEnterClickListener);
        mRegister.setOnClickListener(mOnRegisterClickListener);
       
        checkPermissions();

        videoview = v.findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://"+"com.example.vadim.muscloud/"+R.raw.preview);
        videoview.setVideoURI(uri);
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0f, 0f);
            }

        });
        videoview.start();
        return v;
    }
}

