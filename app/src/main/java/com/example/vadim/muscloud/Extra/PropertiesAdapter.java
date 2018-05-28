package com.example.vadim.muscloud.Extra;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.vadim.muscloud.R;

import java.util.ArrayList;
import java.util.List;

public class PropertiesAdapter extends BaseAdapter {

    private List<Pair<String,String>> pairData;
    private LayoutInflater layoutInflater;
    private Context context;

    public PropertiesAdapter(Context aContext,  List<Pair<String,String>> pairData) {
        this.context = aContext;
        this.pairData = pairData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return pairData.size();
    }

    @Override
    public Object getItem(int position) {
        return pairData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.properties, null);
            holder = new ViewHolder();

            holder.propName = convertView.findViewById(R.id.prop);
            holder.value=convertView.findViewById(R.id.propValue);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Pair<String,String> pair= pairData.get(position);

        holder.propName.setText(pair.first);
        holder.value.setText(pair.second);

        return convertView;
    }

    static class ViewHolder {
        TextView propName;
        TextView value;
    }

}