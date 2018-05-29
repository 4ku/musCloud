package com.example.vadim.muscloud.Tabs;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.PlaylistManager;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.Extra.PlaylistAdapter;
import com.example.vadim.muscloud.Extra.RecyclerItemClickListener;
import com.example.vadim.muscloud.Extra.OnBackPressedListener;
import com.example.vadim.muscloud.R;

import java.util.ArrayList;

public class PlayListFragment extends Fragment implements OnBackPressedListener {
    private Playlist curPlaylist;
    private ArrayList<Song> curSongs;
    private ArrayList<Playlist> playlists;
    private PlaylistManager playlistManager;
    boolean inSongs;

    private RecyclerView recyclerView;
    private PlaylistAdapter playlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_playlist, container, false);
        playlistManager = new PlaylistManager(getActivity());
        curSongs=new ArrayList<Song>();
        playlists =new ArrayList<Playlist>();
        inSongs=false;
        playlists=playlistManager.getPlaylists();

        recyclerView = v.findViewById(R.id.recycler_view);
        playlistAdapter = new PlaylistAdapter(getActivity(), playlists);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(playlistAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        AllSongsFragment fragment = new AllSongsFragment();
                        Bundle args = new Bundle();
                        args.putSerializable("Playlist",playlists.get(position));
                        fragment.setArguments(args);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.playListFragment,fragment)
                                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                .commit();

                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );
        return v;
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onBackPressed() {
            Fragment fragment=new PlayListFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.playListFragment,fragment)
                    .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
    }
}
