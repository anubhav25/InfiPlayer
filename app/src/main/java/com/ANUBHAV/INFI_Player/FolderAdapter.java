package com.ANUBHAV.INFI_Player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ANUBHAV on 01-Jan-17.
 */

public class FolderAdapter extends BaseAdapter {

    Context context;
    List<FolderData> list;

    public FolderAdapter(Context context, List<FolderData> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.gc();
        FolderAdapter.FolderHolder holder;
        convertView= LayoutInflater.from(context).inflate(R.layout.folder_view,parent,false);
        holder=new FolderHolder();
        holder.title = (TextView) convertView.findViewById(R.id.folder_title);
        holder.item_count = (TextView) convertView.findViewById(R.id.item_count);

        FolderData itemData=(FolderData) getItem(position);
        holder.title.setText(itemData.getName());
        holder.item_count.setText(""+itemData.getList().size());

        return convertView;
    }
    private static class FolderHolder {

        TextView title,item_count;

    }
}
