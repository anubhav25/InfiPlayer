package com.ANUBHAV.INFI_Player;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by ANUBHAV on 01-Jan-17.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.Locale;


class VideoFile//  extends Activity
 {
    private String path;
      private Cursor videocursor;
Activity a;
     String file;



     VideoFile(String path,Activity a) {
        this.path = path;
         file=path.substring(path.lastIndexOf("/")+1);
         this.a = a;
    }

     VideoFile(String path) {
         this.path = path;
        // file=path.substring(path.lastIndexOf("/")+1);
         file=new File(path).getName();
     }

     boolean isVideo()
    {
        if(new File(path).isFile()&&new File(path).length()>0) {

            if (!file.startsWith(".")&&file.endsWith(".mkv")||file.endsWith(".mp4")) {
                return true;
            }
        }
        return false;
    }

     ItemData getDetails()
      {
          ItemData item=new ItemData();
          String[] project = {MediaStore.Video.Media._ID,
                  MediaStore.Video.Media.DISPLAY_NAME,
                  MediaStore.Video.Media.SIZE,
                  MediaStore.Video.Media.DURATION

          };

          videocursor = a.getContentResolver().query(
                  MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                  project, null, null,null);
          if (videocursor != null) {
              for(int i=0;i<videocursor.getCount();i++)
              {

                      int video_column_index = videocursor
                              .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
                      videocursor.moveToPosition(i);
                      String name = videocursor.getString(video_column_index);

                      if(file.equals(name))
                      {
                          item.setName(file);
                          item.setPath(path);
                          video_column_index = videocursor
                                  .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
                          videocursor.moveToPosition(i);
                          double sz=Double.parseDouble(videocursor.getString(video_column_index) )/ (1024.0*1024.0);
                          String size;
                          if(sz<1000){
                              size= String.format(Locale.ENGLISH,"%.02f MB",sz);}
                          else
                          {
                              sz=sz/1024.0;
                              size= String.format(Locale.ENGLISH,"%.02f GB",sz);
                          }
                          item.setSize(size);
                          video_column_index = videocursor
                                  .getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                          videocursor.moveToPosition(i);
                          long length = videocursor.getLong(video_column_index);

                          item.setLength(MiliToTime.getTime(length));



                         long ids = videocursor.getLong(videocursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID));
                            item.setId(ids);

                          ContentResolver crThumb = a.getContentResolver();
                          BitmapFactory.Options options = new BitmapFactory.Options();
                          options.inSampleSize = 1;
                          Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
                                  crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND,
                                  options);
                          item.setImg(curThumb);


                      }




              }
          }


          return  item;
      }
    
    

}
