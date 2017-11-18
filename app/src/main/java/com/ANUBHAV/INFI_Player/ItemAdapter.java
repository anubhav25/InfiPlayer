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



public class ItemAdapter extends BaseAdapter {
    Context context;
    List<ItemData> list;
    public ItemAdapter(Context c,List<ItemData> l) {
        context=c;
        list=l;


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
        ItemAdapter.ItemHolder holder;
        convertView= LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
        holder=new ItemHolder();
        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.size = (TextView) convertView.findViewById(R.id.size);
        holder.length = (TextView) convertView.findViewById(R.id.length);
        holder.image = (ImageView) convertView.findViewById(R.id.img);
        ItemData itemData=(ItemData) getItem(position);
        holder.title.setText(itemData.getName());
        holder.size.setText(itemData.getSize());
        holder.length.setText(itemData.getLength());
        holder.image.setImageBitmap(itemData.getImg());

        return convertView;
    }


    private static class ItemHolder {

        TextView title;
        TextView size;
        TextView length;
        ImageView image;
    }
}
