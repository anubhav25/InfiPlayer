package com.ANUBHAV.INFI_Player;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

public class Utils
{   public static int current_theme=1;
    public static int sTheme=1;

    public final static int THEME_DARK = 1;
    public final static int THEME_WHITE = 0;




    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
       activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {Log.i("anubhav",sTheme+"");
        Application myApplication=activity.getApplication();

        switch (sTheme)
        {
            default:

            case THEME_WHITE:
             // activity.setTheme(android.R.style.Theme_Holo_Light);
              activity.setTheme(R.style.AppTheme);
             //   myApplication.setTheme(R.style.AppTheme);
                current_theme=THEME_WHITE;

                break;
            case THEME_DARK:
               // activity.setTheme(android.R.style.Theme_Holo);
                activity.setTheme(R.style.AppThemeDark);
               //myApplication.setTheme(R.style.AppThemeDark);
                current_theme=THEME_DARK;
                break;

        }
    }

}