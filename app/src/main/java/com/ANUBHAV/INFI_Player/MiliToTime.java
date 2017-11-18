package com.ANUBHAV.INFI_Player;

import android.util.Log;

/**
 * Created by ANUBHAV on 7/18/2016.
 */
class MiliToTime {


    static String getTime(long a)

    {
        String ab;
        String second,minute;
        long sec=0,min=0,hr=0;
        sec=a/1000;
        for (;sec>59;sec-=60)
        {
            min++;
        }
        for (;min>59;min-=60)
        {
            hr++;
        }

        if(sec<10)
            second="0"+Long.toString(sec);
        else
            second= Long.toString(sec);
       if(min<10)
            minute="0"+Long.toString(min);
        else
            minute=Long.toString(min);
        ab=minute+":"+second;

        if(hr>0)
        {
            ab=Long.toString(hr)+":"+ab;
        }
        return ab;
    }


}
