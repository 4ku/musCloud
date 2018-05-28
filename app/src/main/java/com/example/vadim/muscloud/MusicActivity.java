package com.example.vadim.muscloud;

import android.animation.ArgbEvaluator;
import android.app.ActionBar;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vadim.muscloud.Authentification.SharedPreferencesHelper;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.PlaylistManager;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.Extra.SectionsPagerAdapter;
import com.example.vadim.muscloud.Tabs.AllSongsFragment;
import com.example.vadim.muscloud.Tabs.FoldersFragment;
import com.example.vadim.muscloud.Tabs.PlayListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.view.ViewGroup;


public class MusicActivity extends AppCompatActivity {
    final public static File MAIN_DIR = new File("/sdcard");
    private List<String> songPathes = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;

    SectionsPagerAdapter adapter;
    TabLayout tabLayout;
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.ac_music);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);

        final Integer[] colors= new Integer[adapter.getCount()];
//        final Integer[] colors= {Color.GREEN,Color.TRANSPARENT,Color.MAGENTA};
        for(int i=0;i<adapter.getCount();i++ ) {
            int color = Color.TRANSPARENT;
//            Drawable background = adapter.getItem(i).getView().getBackground();
//            if (background instanceof ColorDrawable)
//                color = ((ColorDrawable) background).getColor();
            colors[i]=color;
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (position < (adapter.getCount() - 1)) {
                    View tabView = adapter.getItem(position + 1).getView();

                    if (colors[position] == Color.TRANSPARENT) {
                        Drawable background = adapter.getItem(position).getView().getBackground();
                        if (background instanceof ColorDrawable)
                            colors[position] = ((ColorDrawable) background).getColor();
                    }
                    if (colors[position + 1] == Color.TRANSPARENT && tabView != null) {
                        Drawable background = adapter.getItem(position + 1).getView().getBackground();
                        if (background instanceof ColorDrawable)
                            colors[position + 1] = ((ColorDrawable) background).getColor();
                    }

                    int curColor=(Integer) (new ArgbEvaluator()).evaluate(positionOffset, colors[position], colors[position + 1]);
                    adapter.getItem(position).getView().setBackgroundColor(curColor);
                    if (tabView != null)
                        tabView.setBackgroundColor(curColor);

                    float[] hsv = new float[3];
                    Color.colorToHSV(curColor, hsv);
                    hsv[2] *= 0.8f;

                    changeTabsColor(curColor);
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.HSVToColor(hsv)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(curColor);
                    }

                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        if(mSharedPreferencesHelper.getSongs().isEmpty())searchMusic(MAIN_DIR);
    }
private void changeTabsColor(int color){
    for(int n = 0; n < tabLayout.getTabCount(); n++){
        View tab = ((ViewGroup)tabLayout.getChildAt(0)).getChildAt(n);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(tab != null && tab.getBackground() instanceof RippleDrawable){
                RippleDrawable rippleDrawable = (RippleDrawable)tab.getBackground();
                if (rippleDrawable != null) {
                    rippleDrawable.setColor(ColorStateList.valueOf(color));
                }
            }
        }
    }
}


    private void setUpViewPager(ViewPager p) {
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllSongsFragment(), "All songs");
        adapter.addFragment(new FoldersFragment(), "Folders");
        adapter.addFragment(new PlayListFragment(), "Playlists");
        p.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ActionSettings:

                break;

            case R.id.ActionSearch:

                break;

            case R.id.ActionExit:
                finish();
                break;
            case R.id.update:
                searchMusic(MAIN_DIR);

                FragmentManager fm = getSupportFragmentManager();
                for (Fragment fragment: fm.getFragments()) {
                    fm.beginTransaction()
                            .detach(fragment)
                            .attach(fragment)
                            .commit();
                }

                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void searchMusic(final File dir) {
        this.songPathes.clear();
        mSharedPreferencesHelper = new SharedPreferencesHelper(this);
        mSharedPreferencesHelper.deleteAllSongs();

        fill(dir);
        ArrayList<Song> curSongs=new ArrayList<>();
        for(String x : songPathes) {
            try {
                curSongs.add(new Song(x));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mSharedPreferencesHelper.addSongs(curSongs);

        PlaylistManager playlistManager=new PlaylistManager(this);
        Playlist pl=new Playlist("Default",curSongs);
        playlistManager.deleteAllPlaylists();
        playlistManager.addPlaylist(pl);
    }

    private void fill(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                fill(file);
            }
        } else {
            Log.d("myLogs", dir.getName());
            String s = dir.getAbsolutePath();
            if (s.substring(s.length() - 3).equals("mp3")) {
                this.songPathes.add(dir.getAbsolutePath());
            }
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {

            if (fragment instanceof  OnBackPressedListener &&  adapter.getItem(tabLayout.getSelectedTabPosition()).equals(fragment)) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}


