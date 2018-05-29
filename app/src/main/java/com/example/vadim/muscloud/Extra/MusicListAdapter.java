package com.example.vadim.muscloud.Extra;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vadim.muscloud.Authentification.SharedPreferencesHelper;
import com.example.vadim.muscloud.Entities.Playlist;
import com.example.vadim.muscloud.Entities.PlaylistManager;
import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicListAdapter extends BaseAdapter {

    private List<Song> listData;
    private LayoutInflater layoutInflater;
    private Context context;
    private Song curSong;
    private ListView contextListMenu;

    private final String[] menuItems = new String[]{"Play next", "Edit track","Add to playlist", "Remove", "Information" };
    private int curPos;

    public MusicListAdapter(Context aContext, List<Song> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_custom_music_list, null);
            holder = new ViewHolder();

            holder.musCover = convertView.findViewById(R.id.musCover);
            holder.musTitle = convertView.findViewById(R.id.musTitle);
            holder.tvAuthor = convertView.findViewById(R.id.tvAuthor);
            holder.extraBut = convertView.findViewById(R.id.specBut);
            holder.specIcon = convertView.findViewById(R.id.specSign);
            holder.tvDuration = convertView.findViewById(R.id.tvDuration);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        curSong = this.listData.get(position);
        curPos=position;
        if (curSong.getTitle() == null) {
            int start = curSong.getSongPath().lastIndexOf('/') + 1;
            int end = curSong.getSongPath().lastIndexOf('.');
            holder.musTitle.setText(curSong.getSongPath().substring(start, end));
        } else
            holder.musTitle.setText(curSong.getTitle());

        if (curSong.getArtist() == null)
            holder.tvAuthor.setText("Unknown");
        else
            holder.tvAuthor.setText(curSong.getArtist());

        holder.tvDuration.setText(curSong.getDuration());

        int imageId = this.getMipmapResIdByName("music_icon");
        holder.musCover.setImageResource(imageId);

        imageId = this.getMipmapResIdByName("cached_icon");
        holder.specIcon.setImageResource(imageId);


        holder.extraBut.setOnClickListener(new android.view.View.OnClickListener() {

            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View contextMenuView =  layoutInflater.inflate(R.layout.list_extra_music_item_actions,null);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                        android.R.layout.simple_list_item_1, menuItems);

                contextListMenu = contextMenuView.findViewById(R.id.myListView);
                contextListMenu.setOnItemClickListener(OnItemClickSongMenu);
                contextListMenu.setAdapter(adapter);

                AlertDialog dialog= mBuilder.create();

                ArrayList<Object> sendInfo=new ArrayList<>();
                sendInfo.add(position);
                sendInfo.add(dialog);
                contextListMenu.setTag(sendInfo);

                dialog.setView(contextMenuView);
                dialog.show();


            }
        });
        return convertView;
    }

    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();
        int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
        return resID;
    }


    private AdapterView.OnItemClickListener OnItemClickSongMenu =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final String selectedItem = (String) parent.getItemAtPosition(position);

            ArrayList<Object> getInfo=( ArrayList<Object>)parent.getTag();
            final Song selectedSong=(Song)getItem((int)getInfo.get(0));
            AlertDialog songMenuDialog=(AlertDialog) getInfo.get(1);


            if (selectedItem.equals("Play next")) {


            } else if (selectedItem.equals("Edit track")) {

            } else if (selectedItem.equals("Add to playlist")) {

                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                    View propView =  layoutInflater.inflate(R.layout.list_extra_music_item_actions,null);
                    ListView lv=propView.findViewById(R.id.myListView);
                    ArrayList<String> names=new ArrayList<>();
                    PlaylistManager sh=new PlaylistManager(context);

                    for(Playlist x : sh.getPlaylists())names.add(x.getName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                            android.R.layout.simple_list_item_1, names);

                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(onClickAddPlaylist);

                    mBuilder.setMessage("Add to playlist")
                            .setPositiveButton("Новый", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    View addPlView =  layoutInflater.inflate(R.layout.dialog_new_playlist,null);
                                    final EditText et=addPlView.findViewById(R.id.enterNewPlaylistName);
                                    builder.setMessage("Add to new playlist")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    if(!TextUtils.isEmpty(et.getText())) {
                                                        Playlist newPlaylist = new Playlist();
                                                        newPlaylist.setName(et.getText().toString());
                                                        newPlaylist.addSong(selectedSong);

                                                        PlaylistManager sh = new PlaylistManager(context);
                                                        if (sh.addPlaylist(newPlaylist)) {
                                                            Toast.makeText(context, "Playlist is successfully added", Toast.LENGTH_SHORT).show();
                                                            dialog.cancel();
                                                        } else
                                                            Toast.makeText(context, "Playlist is already exist with that name", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
//                                  dialog.cancel();
                                    builder.setView(addPlView);
                                    builder.create().show();
                                }
                            });


                    mBuilder.setView(propView);

                    AlertDialog dialog= mBuilder.create();
                    ArrayList<Object> sendInfo=new ArrayList<>();
                    sendInfo.add((int)getInfo.get(0));
                    sendInfo.add(dialog);
                    lv.setTag(sendInfo);

                    dialog.show();

            } else if (selectedItem.equals("Remove"))  {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("Remove")
                            .setPositiveButton("From playlist", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                   SharedPreferencesHelper sh = new SharedPreferencesHelper(context);
                                   sh.deleteSong(selectedSong);
                                   listData.remove(selectedSong);
                                   notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("From device", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User cancelled the dialog
                                    SharedPreferencesHelper sh = new SharedPreferencesHelper(context);
                                    sh.deleteSong(selectedSong);
                                    listData.remove(selectedSong);
                                    notifyDataSetChanged();

                                    File file = new File(selectedSong.getSongPath());
                                    boolean deleted = file.delete();
                                }
                            });
                   builder.create().show();

            } else if (selectedItem.equals("Information")) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(context);
                View propView =  layoutInflater.inflate(R.layout.list_extra_music_item_actions,null);
                ListView lv=propView.findViewById(R.id.myListView);
                lv.setSelector(android.R.color.transparent);
                lv.setAdapter(new PropertiesAdapter(context, selectedSong.getInfo()));

                mBuilder.setView(propView);
                AlertDialog dialog= mBuilder.create();
                dialog.show();
            } else if (selectedItem.equals("Download")) {

            }
            songMenuDialog.cancel();

        }
    };

    static class ViewHolder {
        ImageView musCover;
        TextView musTitle;
        TextView tvAuthor;
        Button extraBut;
        ImageView specIcon;
        TextView tvDuration;
    }



    private AdapterView.OnItemClickListener onClickAddPlaylist=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String playlistName=(String)parent.getItemAtPosition(position);
            PlaylistManager sh=new PlaylistManager(context);

            ArrayList<Object> getInfo=( ArrayList<Object>)parent.getTag();

            final Song selectedSong=(Song)getItem((int)getInfo.get(0));
            AlertDialog addPlaylistDialog=(AlertDialog) getInfo.get(1);


            ArrayList<Playlist> playlists=sh.getPlaylists();
            for(Playlist x:playlists){
                if(x.getName().equals(playlistName)){
                    x.addSong(selectedSong);
                    break;
                }
            }

            sh.addPlaylists(playlists);
            addPlaylistDialog.cancel();
        }
    };

}