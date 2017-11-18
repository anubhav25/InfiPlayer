package com.ANUBHAV.INFI_Player;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *Created by ANUBHAV on 29-Dec-16.
 */

public class FolderView extends Activity {
    public  static FolderData temp;
    final static String theme_file_name="theme";
    final static String view_file_name="view";
    final static String TAG="anubhav";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;
  //  private FileManager fileManager;
    ListView lv;
    String path;

    boolean ans;
    static String folder="folder",item="item",viewtype=folder;


    List<FolderData> folder_values = new ArrayList<>();
    List<ItemData> item_values=new ArrayList<>();
    RelativeLayout loading;
    FolderAdapter adapter;
    ItemAdapter adapter2;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed()
    {
        if(doubleBackToExitPressedOnce)
        {
            this.finishAffinity();
        }
        doubleBackToExitPressedOnce=true;
        Toast.makeText(this,"Please Click Back Again to Exit",Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        },2000);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {




      //  setTheme(android.R.style.Theme_Holo);
       // setTheme(android.R.style.Theme_Holo);
        sharedPreferences=getSharedPreferences("INFI_Player", Context.MODE_PRIVATE);
        sharedPreferencesEditor=sharedPreferences.edit();
       // fileManager = new FileManager(getApplicationContext());
        try {
            Log.d(TAG,"reading theme file");
            // fileManager.createFile("theme",Integer.toString(Utils.current_theme));
           // Utils.sTheme = Integer.parseInt(fileManager.readFile(theme_file_name));
           Utils.sTheme=sharedPreferences.getInt(theme_file_name,0);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

       Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);

        loading=(RelativeLayout)findViewById(R.id.loading);


        try {
            Log.d(TAG,"reading view file");
            // fileManager.createFile("theme",Integer.toString(Utils.current_theme));
            //  String a= (fileManager.readFile(view_file_name));
                String a=sharedPreferences.getString(view_file_name,folder);
            if(a.equals(folder)||a.equals(item))
            {
                viewtype=a;
            }
            else viewtype=folder;
        }catch (Exception e)
        {
            viewtype=folder;
            e.printStackTrace();
        }


        lv = (ListView) findViewById(R.id.list);

        if (Build.VERSION.SDK_INT >= 23) {
            Log.d(TAG,"check permission");
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

            }
        }


    start_init_list();
     init_adapters();
        getIntents();
    }

    private void getIntents() {

        try {
            Intent intent = getIntent();
          if(!(getCallingActivity() == null)) {

              String temp = intent.getExtras().getString("path", "/");
              Intent ii = new Intent();
              for (ItemData item : item_values) {

                  if (temp.equals(item.getPath())) {

                      ii.putExtra("id", item.getId());
                      ii.putExtra("path", item.getPath());
                      setResult(RESULT_OK, ii);
                      finish();
                  }


              }
              ii.putExtra("path", intent.getExtras().getString("path"));
              setResult(RESULT_CANCELED, ii);

              finish();

          }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void start_init_list() {

        Log.d(TAG,"scanning start");

                path = Environment.getExternalStorageDirectory().getAbsolutePath();//+"/Folder";

                ans = init_list(path);
                if (ans) {
                    final FolderData folderData = new FolderData();
                    folderData.setName(path.substring(path.lastIndexOf("/") + 1));
                    folderData.setPath(path);
                    // values.add(0, folderData);
                    final String a = path;

                    List<ItemData> list1 = new ArrayList<>();
                    File dir = new File(a);

                    String[] list = dir.list();
                    if (list != null) {
                        for (String file1 : list) {

                            String filename1 = file1;

                            if (path.endsWith(File.separator)) {
                                filename1 = path + filename1;
                            } else {
                                filename1 = a + File.separator + filename1;
                            }
                            VideoFile videoFile = new VideoFile(filename1, FolderView.this);
                            if(videoFile.isVideo()) {
                                ItemData it=videoFile.getDetails();
                                try {


                                    if (!it.getLength().isEmpty() ||!it.getName().isEmpty() || !it.getSize().isEmpty())
                                    {
                                        list1.add(it);
                                    }
                                }catch (NullPointerException e)
                                {
                                    e.printStackTrace();
                                }



                            }


                        }}
                    if(!list1.isEmpty()) {
                        folderData.setList(list1);
                        for(ItemData item : list1)
                        { item_values.add(item);}
                        folder_values.add(folderData);
                        // refresList();

                    }
                    // refresList();



                }





        GetPaths getPaths = new GetPaths();
        final List<String> paths = getPaths.getSdCardPaths(FolderView.this, false);
        for (int i = 0; i < paths.size(); i++) {
            final int ii=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    path = paths.get(ii);

                    ans = init_list(path);
                    if (ans) {

                        final FolderData folderData = new FolderData();
                        folderData.setName(path.substring(path.lastIndexOf("/") + 1));
                        folderData.setPath(path);
                        // values.add(0, folderData);
                        final String a = path;

                        List<ItemData> list1 = new ArrayList<>();
                        File dir = new File(a);

                        String[] list = dir.list();
                        if (list != null) {
                            for (String file1 : list) {

                                String filename1 = file1;

                                if (path.endsWith(File.separator)) {
                                    filename1 = path + filename1;
                                } else {
                                    filename1 = a + File.separator + filename1;
                                }
                                VideoFile videoFile = new VideoFile(filename1, FolderView.this);
                                if(videoFile.isVideo()) {
                                    ItemData it=videoFile.getDetails();
                                    try {


                                        if (!it.getLength().isEmpty() ||!it.getName().isEmpty() || !it.getSize().isEmpty())
                                        {
                                            list1.add(it);
                                        }
                                    }catch (NullPointerException e)
                                    {
                                        e.printStackTrace();
                                    }



                                }


                            }}
                        if(!list1.isEmpty()) {
                            folderData.setList(list1);

                            for(ItemData item : list1)
                            { item_values.add(item);}
                            folder_values.add(folderData);
                            refresList();

                        }



                    }
                }
            }).start();


        }

        refresList();




    }
    private void init_adapters() {
        Log.d(TAG,"seting adapters");
        lv.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        if(viewtype.equals(folder))
        {  Log.d(TAG,"folder view selected");
            sortFolderList();
            adapter= new FolderAdapter(FolderView.this,folder_values);


            lv.setAdapter(adapter);


            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String filename;

                    FolderData folderData= folder_values.get(i);
                    filename=folderData.getPath();



                    if (new File(filename).isDirectory()) {
                        Intent intent = new Intent(FolderView.this, itemView.class);
                        //intent.putExtra("folder",folderData);
                        temp=folderData;

                        startActivity(intent);
                    } else {
                        Toast.makeText(FolderView.this, filename + " is not a directory", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
        else
        if(viewtype.equals(item))
        {
            sortItemList();
            adapter2=new ItemAdapter(FolderView.this,item_values);
            Log.d(TAG,"item view selected");

            lv.setAdapter(adapter2);

            final String finalPath = path;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    String filename= finalPath;

                    ItemData item  = (ItemData) lv.getAdapter().getItem(i);
                    filename=item.getPath();

                    Intent intent =new Intent(FolderView.this,ViewVideo.class);
                    Uri uri=Uri.parse(filename);
                    long id=item.getId();
                    intent.putExtra("id",id);
                    intent.setData(uri);
                    startActivity(intent);
                }
            });
        }

    }




    public boolean init_list(final String path) {
        Log.d(TAG,"list making start");
            boolean response=false;

        //  setTitle(path);
        // Read all files sorted into the values-array
        File dir = new File(path);

        final String[] list = dir.list();
        if (list != null) {





                   for (int k=0;k<list.length;k++) {
                       String file=list[k];
                       final String filet=file;


                               String filename = filet;
                               if (path.endsWith(File.separator)) {
                                   filename = path + filename;
                               } else {
                                   filename = path + File.separator + filename;
                               }
                               if (new File(filename).isDirectory() && !filet.startsWith(".")) {

                                   ans=init_list(filename);



                                   if (ans) {

                                       final FolderData folderData=new FolderData();
                                       folderData.setName(filet);
                                       folderData.setPath(filename);


                                       final String a=filename;
                                       final List<ItemData>  list1= new ArrayList<>();

                                       new Thread(new Runnable() {
                                           @Override
                                           public void run() {
                                               File filedir = new File(a);

                                               String[] filelist = filedir.list();
                                               if (list != null) {
                                                   for (final String file1 : filelist) {
                                                 /*  new Thread(new Runnable() {
                                                       @Override
                                                       public void run() {
*/
                                                       String filename1=file1;

                                                       if (path.endsWith(File.separator)) {
                                                           filename1 = path + filename1;
                                                       } else {
                                                           filename1 = a + File.separator + filename1;
                                                       }
                                                       VideoFile videoFile=new VideoFile(filename1,FolderView.this);
                                                       if(videoFile.isVideo()) {
                                                           ItemData i=videoFile.getDetails();
                                                           try {


                                                               if (!i.getLength().isEmpty() ||!i.getName().isEmpty() || !i.getSize().isEmpty())
                                                               {
                                                                   list1.add(i);
                                                               }
                                                           }catch (NullPointerException e)
                                                           {
                                                               e.printStackTrace();
                                                           }



                                                       }
                                                       // }
                                                       //}).start();




                                                   }
                                               }
                                               if(!list1.isEmpty()) {
                                                   folderData.setList(list1);

                                                   for(ItemData item : list1)
                                                   { item_values.add(item);}
                                                   folder_values.add(folderData);
                                                   refresList();

                                               }
                                           }
                                       }).start();



                                   }


                               }





                   }



            for (String file : list) {
                String filename = file;
                if (path.endsWith(File.separator)) {
                    filename = path + filename;
                } else {
                    filename = path + File.separator + filename;
                }

                if (new VideoFile(filename).isVideo())
                {
                      response=true;
                    break;
                }
                else response=false;
            }

        }


        return response;
    }

    private void refresList() {
        Log.d(TAG,"list refreshed");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(viewtype.equals(folder)) {
                    adapter.notifyDataSetChanged();

                    sortFolderList();

                        adapter = new FolderAdapter(FolderView.this, folder_values);
                        lv.setAdapter(adapter);
                   }
                    else if(viewtype.equals(item))
                    {
                    adapter2.notifyDataSetChanged();

                        sortItemList();
                    adapter2=new ItemAdapter(FolderView.this,item_values);
                    lv.setAdapter(adapter2);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    public void sortFolderList() {//List<String> list) {
                // int m=name_values.size();
        for (int i = 0; i < folder_values.size(); i++) {
            for (int j = 1; j < folder_values.size() - i; j++) {
                if(folder_values.get(j-1).getList().isEmpty())
                {
                    folder_values.remove(j-1);
                    j--;
                    continue;
                }



                int end;

                int len1 = folder_values.get(j - 1).getName().length();
                int len2 = folder_values.get(j).getName().length();
                end = len1 <= len2 ? len1 : len2;
                String p1, p2;
                p1 = folder_values.get(j - 1).getName();
                //	list[j-1];
                p2 = folder_values.get(j).getName();
                //list[j];
                String p3 = folder_values.get(j - 1).getPath();
                String p4 = folder_values.get(j).getPath();
                if (p3.equals(p4)) {
                    folder_values.remove(j);
                    j = j - 1;
                    continue;
                }

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
                        FolderData temp1 = folder_values.get(j - 1);
                        folder_values.remove(j - 1);
                        folder_values.add(j, temp1);

                        break;

                    }
                    if (c1 < c2) {
                        break;
                    }

                }
            }

        }

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



    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // if(Utils.current_theme==Utils.THEME_WHITE)
        getMenuInflater().inflate(R.menu.menu, menu);
      /*  else
        if(Utils.sTheme==Utils.THEME_DARK )
            getMenuInflater().inflate(R.menu.menu_night, menu);
*/
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.set_tings:
                // status=toggle.getText().toString();
                change_theme(Utils.current_theme);

                return true;

            case R.id.view_f_or_i:
                // status=toggle.getText().toString();
               change_view();
                return true;


            case R.id.youTube:
                Toast.makeText(this,"YOUTUBE Selected",Toast.LENGTH_LONG).show();
                Intent youtube = new Intent(FolderView.this,YouTube.class);
                startActivity(youtube);
                return true;


            case R.id.About_Us:
                Intent intent2=new Intent(this,AboutUs.class);
                this.startActivity(intent2);
                // another startActivity, this is for item with id "menu_item2"

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }
    void change_theme(int a)
    {
       // getApplication().setTheme(android.R.style.Theme_Holo);
        //startActivity(new Intent(this,SplashActivity.class));

        if(a==Utils.THEME_WHITE)
        {
            Toast.makeText(this,"Theme changed",Toast.LENGTH_SHORT).show();
          // fileManager.createFile(theme_file_name,Integer.toString(Utils.THEME_DARK));
            sharedPreferencesEditor.putInt(theme_file_name,Utils.THEME_DARK);
            sharedPreferencesEditor.commit();
            Utils.changeToTheme(FolderView.this,Utils.THEME_DARK);


        }
        else
        if(a==Utils.THEME_DARK )
        {
            Toast.makeText(this,"Theme changed",Toast.LENGTH_SHORT).show();
            //fileManager.createFile(theme_file_name,Integer.toString(Utils.THEME_WHITE));
            sharedPreferencesEditor.putInt(theme_file_name,Utils.THEME_WHITE);
            sharedPreferencesEditor.commit();
            Utils.changeToTheme(FolderView.this,Utils.THEME_WHITE);


        }

    }
    void change_view()
    {

        if(viewtype.equals(folder))
        {
            viewtype=FolderView.item;
            Intent intent=new Intent(FolderView.this,FolderView.class);

            Toast.makeText(this,"View changed",Toast.LENGTH_SHORT).show();
           // fileManager.createFile(view_file_name,item);
            sharedPreferencesEditor.putString(view_file_name,item);
            sharedPreferencesEditor.commit();
            startActivity(intent);
        }
        else
        if(viewtype.equals(FolderView.item))
        {
            viewtype=FolderView.folder;
            Intent intent=new Intent(FolderView.this,FolderView.class);

            Toast.makeText(this,"View changed",Toast.LENGTH_SHORT).show();
           // fileManager.createFile(view_file_name,folder);
            sharedPreferencesEditor.putString(view_file_name,folder);
            sharedPreferencesEditor.commit();
            startActivity(intent);
        }






    }


    public List<FolderData> getFolderDataInViewVideo ()
    {
       // onCreate(Bundle.EMPTY);
        return folder_values;
    }

    public List<ItemData> getItemDataInViewVideo ()
    {
        return item_values;
    }



}

