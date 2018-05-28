package com.example.vadim.muscloud.Extra;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vadim.muscloud.Entities.Song;
import com.example.vadim.muscloud.R;

import java.io.File;
import java.util.List;

public class FolderListAdapter extends BaseAdapter {

    private List<String> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public FolderListAdapter(Context aContext, List<String> listData) {
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

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, null);
            holder = new ViewHolder();

            holder.folderCover = convertView.findViewById(R.id.musCover);
            holder.folderName = convertView.findViewById(R.id.musTitle);
            holder.fullPath = convertView.findViewById(R.id.tvAuthor);
            holder.extraBut = convertView.findViewById(R.id.specBut);
            holder.specIcon = convertView.findViewById(R.id.specSign);
            holder.folderItemSize = convertView.findViewById(R.id.tvDuration);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String folderPath=listData.get(position);
        File file = new File(folderPath);
        holder.folderName.setText(file.getName());
        holder.fullPath.setText(folderPath.substring(0,folderPath.lastIndexOf('/')+1));
        holder.extraBut.setVisibility(View.INVISIBLE);
        holder.folderItemSize.setVisibility(View.INVISIBLE);
        holder.folderName.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
        holder.fullPath.setTextSize(TypedValue.COMPLEX_UNIT_SP,12);
        holder.folderName.bringToFront();



        int imageId = this.getMipmapResIdByName("music_folder");
        holder.folderCover.setImageResource(imageId);

        return convertView;
    }

    public int getMipmapResIdByName(String resName) {
        String pkgName = context.getPackageName();
        int resID = context.getResources().getIdentifier(resName, "mipmap", pkgName);
        Log.i("CustomListView", "Res Name: " + resName + "==> Res ID = " + resID);
        return resID;
    }

    static class ViewHolder {
        ImageView folderCover;
        TextView folderName;
        TextView fullPath;
        Button extraBut;
        ImageView specIcon;
        TextView folderItemSize;
    }

}