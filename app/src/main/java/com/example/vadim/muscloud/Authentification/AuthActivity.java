package com.example.vadim.muscloud.Authentification;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Build;

import com.example.vadim.muscloud.R;

/**
 * Created by Vadim on 30.04.2018.
 */

public class AuthActivity extends FragmentActivity {


    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
    private FragmentManager fragmentManager = getSupportFragmentManager();

    protected Fragment getFragment() {
        return AuthFragment.newInstance();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_auth);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           getWindow().setStatusBarColor(Color.BLACK);
        }

        if (savedInstanceState == null) {

            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, getFragment())
                    .commit();
        }
    }



    @Override
    public void onBackPressed() {
        Fragment LoginFr = fragmentManager
                .findFragmentByTag(LoginFragment.class.getName());

        if (fragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            fragmentManager.popBackStack();
            if (LoginFr != null && LoginFr.isVisible()){
                fragmentManager
                        .beginTransaction()
                        .setCustomAnimations(R.anim.left_enter, R.anim.right_out)
                        .replace(R.id.fragmentContainer, new AuthFragment(),
                                AuthFragment.class.getName()).commit();
            }
        }

    }

}
