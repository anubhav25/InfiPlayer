package com.ANUBHAV.INFI_Player;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplashActivity extends Activity {

    private static final int SPLASH_DISPLAY_TIME=1500;

    public void onAttachedToWindow(){
        super.onAttachedToWindow();
        Window window=getWindow();
        window.setFormat(PixelFormat.RGBA_8888);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);
            StartAnimation();

            new Handler().postDelayed(new Runnable() {
                public void run() {

                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this,
                            FolderView.class);

                    SplashActivity.this.startActivity(intent);
                    SplashActivity.this.finish();

                }
            }, SPLASH_DISPLAY_TIME);

    if (Build.VERSION.SDK_INT >= 23) {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            Log.v("TAG","Permission is revoked");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
            // return true;
        } else {

            Log.v("TAG","Permission is granted");
            // return false;
        }
    }
    else { //permission is automatically granted on sdk<23 upon installation
        //Log.v(TAG,"Permission is granted");
        //return true;
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Settings.System.canWrite(SplashActivity.this)) {

            //Write code to feature for eg. set brightness or vibrate device
           /* ContentResolver cResolver = context.getContentResolver();
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS,brightness);*/
        }
        else {
            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + this.getPackageName()));
            // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }}

    }




    private void StartAnimation(){
        Animation anim= AnimationUtils.loadAnimation(this,R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim=AnimationUtils.loadAnimation(this,R.anim.translate);
        anim.reset();
        ImageView iv=(ImageView)findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
    }
    private  static  void showBrightnessPermissionDialog(final Context context) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        final AlertDialog alert = builder.create();
        builder.setMessage("Please give the permission to change brightness. \n Thanks ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + context.getPackageName()));
                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        alert.dismiss();
                    }
                });
        alert.show();
    }

}