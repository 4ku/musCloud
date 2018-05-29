package com.example.vadim.muscloud.Extra;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;

import com.example.vadim.muscloud.OnlineServiceActivity;
import com.example.vadim.muscloud.R;

import java.util.HashMap;
import java.util.Map;

public class NavigationMenu {

    private NavigationView navigationView;
    private Runnable runnable;
    private DrawerLayout mDrawerLayout;
    private Handler navDrawerRunnable = new Handler();
    private Context context;

    public NavigationMenu(Context context){
        this.context=context;
        navigationView=((Activity)context).findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.nav_phone).setChecked(true);
        mDrawerLayout =((Activity)context).findViewById(R.id.drawer_layout);
    }


    public void setupNavigationMenu(boolean LoggedIn){
        if(!LoggedIn){
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            return;
        }
        navDrawerRunnable.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupDrawerContent(navigationView);
                setupNavigationIcons(navigationView);
            }
        }, 700);
    }






    private Runnable navigatePhone = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_phone).setChecked(true);

        }
    };

    private Runnable navigateMusCloudLib = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_musCloudLib).setChecked(true);
            Intent startProfileIntent =
                    new Intent(context, OnlineServiceActivity.class);
            startProfileIntent.putExtra(OnlineServiceActivity.intentMessage,"musCloud Library");
            ((Activity)context).startActivity(startProfileIntent);
        }
    };

    private Runnable navigateVk = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_vk).setChecked(true);
            Intent startProfileIntent =
                    new Intent(context, OnlineServiceActivity.class);
            startProfileIntent.putExtra(OnlineServiceActivity.intentMessage,"Vk");
            ((Activity)context).startActivity(startProfileIntent);

        }
    };

    private Runnable navigateSpotify = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_spotify).setChecked(true);
            Intent startProfileIntent =
                    new Intent(context, OnlineServiceActivity.class);
            startProfileIntent.putExtra(OnlineServiceActivity.intentMessage,"Spotify");
            ((Activity)context).startActivity(startProfileIntent);

        }
    };
    private Runnable navigateSoundCloud = new Runnable() {
        public void run() {
            navigationView.getMenu().findItem(R.id.nav_soundCloud).setChecked(true);
            Intent startProfileIntent =
                    new Intent(context, OnlineServiceActivity.class);
            startProfileIntent.putExtra(OnlineServiceActivity.intentMessage,"Sound Cloud");
            ((Activity)context).startActivity(startProfileIntent);

        }
    };


    private void setupNavigationIcons(NavigationView navigationView) {
        navigationView.getMenu().findItem(R.id.nav_phone).setIcon(R.drawable.phone_icon);
        navigationView.getMenu().findItem(R.id.nav_musCloudLib).setIcon(R.drawable.music_icon);
        navigationView.getMenu().findItem(R.id.nav_vk).setIcon(R.drawable.vk_icon);
        navigationView.getMenu().findItem(R.id.nav_spotify).setIcon(R.drawable.spotify_icon);
        navigationView.getMenu().findItem(R.id.nav_soundCloud).setIcon(R.drawable.soundcloud_icon);

    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(final MenuItem menuItem) {
                        updatePosition(menuItem);
                        return true;

                    }
                });
    }
    private void updatePosition(final MenuItem menuItem) {
        runnable = null;

        switch (menuItem.getItemId()) {
            case R.id.nav_phone:
                runnable = navigatePhone;

                break;
            case R.id.nav_musCloudLib:
                runnable = navigateMusCloudLib;

                break;
            case R.id.nav_vk:
                runnable = navigateVk;

                break;
            case R.id.nav_spotify:
                runnable = navigateSpotify;

                break;
            case R.id.nav_soundCloud:
                runnable = navigateSoundCloud;
                break;

        }

        if (runnable != null) {
            menuItem.setChecked(true);
            mDrawerLayout.closeDrawers();
            (new Handler()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    runnable.run();
                }
            }, 350);
        }
    }



}
