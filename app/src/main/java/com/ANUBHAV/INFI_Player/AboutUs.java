package com.ANUBHAV.INFI_Player;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutUs extends Activity {


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        TextView tv = (TextView) findViewById(R.id.infitxt);
        Typeface face = Typeface.createFromAsset(getAssets(),
                "fonts/arista.ttf");



    }

    public void facebook(View view){
        String fbpage="http://www.facebook.com/infiplayer2523";
        Intent i=new Intent(Intent.ACTION_VIEW,Uri.parse(fbpage));
        startActivity(i);
    }


    public void email(View view){

        Intent in=new Intent(AboutUs.this,EmailActivity.class);
        startActivity(in);

    }


}

