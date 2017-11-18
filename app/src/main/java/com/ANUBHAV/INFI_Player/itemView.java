package com.ANUBHAV.INFI_Player;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ANUBHAV.INFI_Player.FolderView.temp;

/**
 * Created by ANUBHAV on 29-Dec-16.
 */
public class itemView extends Activity {
    ListView iv;
    String path;
    FolderData folderData;
    List<ItemData> item_values;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_view);
        iv=(ListView)findViewById(R.id.item_view);

       // Intent in=getIntent();
        folderData = temp;


        // folderData=(FolderData) in.getSerializableExtra("folder");
        init_list_items();
    }

    private void init_list_items() {
        path=folderData.getPath();
        setTitle(folderData.getName());
       // final List<String> values = new ArrayList<>();
        item_values=folderData.getList();
if(item_values!=null){
            sortItemList();
            ItemAdapter adapter =new ItemAdapter(itemView.this,item_values);

         iv.setAdapter(adapter);}
        else {
            Toast.makeText(itemView.this,"Folder Empty",Toast.LENGTH_SHORT).show();
        }
            final String finalPath = path;

            iv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String filename= finalPath;

                    ItemData item  = (ItemData) iv.getAdapter().getItem(i);
                    filename=item.getPath();
                    Intent intent =new Intent(itemView.this,ViewVideo.class);
                    Uri uri=Uri.parse(filename);
                    long id=item.getId();
                    intent.putExtra("id",id);
                    intent.setData(uri);
                    startActivity(intent);

                }
            });
        }


    public void sortItemList() {
        // int m=name_values.size();
        for (int i = 0; i < item_values.size(); i++) {
            for (int j = 1; j < item_values.size() - i; j++) {




                int end;

                int len1 = item_values.get(j - 1).getName().length();
                int len2 = item_values.get(j).getName().length();
                end = len1 <= len2 ? len1 : len2;
                String p1, p2;
                p1 = item_values.get(j - 1).getName();
                //	list[j-1];
                p2 = item_values.get(j).getName();
                //list[j];
                String p3 = item_values.get(j - 1).getPath();
                String p4 = item_values.get(j).getPath();


                for (int k = 0; k < end; k++) {
                    int c1 = p1.codePointAt(k);
                    int c2 = p2.codePointAt(k);
                    if (c1 < 97&&c1>64) {
                        c1 += 32;
                    }
                    if (c2 < 97&&c2>64) {
                        c2 += 32;
                    }
                    // System.out.println(c1+c2+"");

                    if (c1 > c2) {
                        ItemData temp1 = item_values.get(j - 1);
                        item_values.remove(j - 1);
                        item_values.add(j, temp1);

                        break;

                    }
                    if (c1 < c2) {
                        break;
                    }

                }
            }

        }

    }
    }
