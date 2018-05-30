package com.example.vadim.muscloud;

import android.animation.ArgbEvaluator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.audiofx.AudioEffect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.vadim.muscloud.Authentification.AuthActivity;
import com.example.vadim.muscloud.Authentification.SharedPreferencesHelper;
import com.example.vadim.muscloud.Entities.AccountInfo;
import com.example.vadim.muscloud.Entities.AccountState;
import com.example.vadim.muscloud.Entities.AccountType;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.PlaylistManager;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.Entities.User;
import com.example.vadim.muscloud.Extra.NavigationMenu;
import com.example.vadim.muscloud.Extra.OnBackPressedListener;
import com.example.vadim.muscloud.Extra.SectionsPagerAdapter;
import com.example.vadim.muscloud.Tabs.AlbumsFragment;
import com.example.vadim.muscloud.Tabs.AllSongsFragment;
import com.example.vadim.muscloud.Tabs.ArtistsFragment;
import com.example.vadim.muscloud.Tabs.FoldersFragment;
import com.example.vadim.muscloud.Tabs.GenresFragment;
import com.example.vadim.muscloud.Tabs.PlayListFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.Toast;


public class MusicActivity extends AppCompatActivity {
    final public static File MAIN_DIR = new File("/sdcard");
    public static String intentMessage="USER";
    public static boolean LoggedIn=false;
    private List<String> songPathes = new ArrayList<String>();
    private SharedPreferencesHelper mSharedPreferencesHelper;

    private SectionsPagerAdapter adapter;
    private TabLayout tabLayout;

    private AudioPlayer playerFragment;
    private DrawerLayout mDrawerLayout;

    private static AccountInfo accountInfo;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_music);

        if(getIntent()!=null && getIntent().getExtras()!=null) {
            Bundle bundle = getIntent().getExtras();
            User user = (User) bundle.get(intentMessage);
            accountInfo=new AccountInfo(user.getLogin(), AccountType.free, AccountState.Active);
        }

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(LoggedIn);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationMenu navBar = new NavigationMenu(this);
        navBar.setupNavigationMenu(LoggedIn);


        playerFragment = new AudioPlayer();
        FragmentManager fragmentManager1 = getSupportFragmentManager();
        fragmentManager1.beginTransaction()
                .replace(R.id.musicPlayerFragment, playerFragment).commitAllowingStateLoss();


        final ViewPager viewPager = findViewById(R.id.viewpager);
        setUpViewPager(viewPager);

        final Integer[] colors = new Integer[adapter.getCount()];
        for (int i = 0; i < adapter.getCount(); i++) {
            colors[i] = Color.TRANSPARENT;
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

                    int curColor = (Integer) (new ArgbEvaluator()).evaluate(positionOffset, colors[position], colors[position + 1]);
                    adapter.getItem(position).getView().setBackgroundColor(curColor);
                    if (tabView != null)
                        tabView.setBackgroundColor(curColor);


//                    changeTabsColor(curColor);
                    playerFragment.getView().setBackgroundColor(darkenColor(curColor, 0.70f));
                    tabLayout.setBackgroundColor(darkenColor(curColor, 0.90f));
                    getSupportActionBar().setBackgroundDrawable(new ColorDrawable(darkenColor(curColor, 0.8f)));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        getWindow().setStatusBarColor(curColor);
                    }

                }
            }

            private int darkenColor(int color, float hue) {
                float[] hsv = new float[3];
                Color.colorToHSV(color, hsv);
                hsv[2] *= hue;
                return Color.HSVToColor(hsv);
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
        if (mSharedPreferencesHelper.getSongs().isEmpty()) searchMusic(MAIN_DIR);
    }

    private void setUpViewPager(ViewPager p) {
        adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllSongsFragment(), "All songs");
        adapter.addFragment(new FoldersFragment(), "Folders");
        adapter.addFragment(new PlayListFragment(), "Playlists");
        adapter.addFragment(new AlbumsFragment(), "Albums");
        adapter.addFragment(new ArtistsFragment(), "Artists");
        adapter.addFragment(new GenresFragment(), "Genres");
        p.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_music_activity, menu);
        if(!LoggedIn){
            menu.findItem(R.id.menu_profile).setVisible(false);
//            menu.findItem(R.id.menu_exit).setTitle("Authorize");
        }
            return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_profile:
                Intent startAccountInfoIntent =
                        new Intent(MusicActivity.this, AccountInfoActivity.class);
                startAccountInfoIntent.putExtra(AccountInfoActivity.intentMessage,accountInfo.getLogin());
                startActivity(startAccountInfoIntent);
                this.finish();
                break;

            case R.id.menu_equalizer:
                try {
                    final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, AudioPlayer.getAudioSessionId());
                    startActivityForResult(effects, 666);
                } catch (final ActivityNotFoundException notFound) {
                    Toast.makeText(this, "Equalizer not found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_settings:

                break;

            case R.id.menu_exit:
//                Intent startMusicIntent =
//                        new Intent(MusicActivity.this, AuthActivity.class);
//                startActivity(startMusicIntent);
//                this.finish();
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

            case android.R.id.home: {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
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
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
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


