/*{
        package
         com.ANUBHAV.INFI_Player;

/////////////////////IMPORTS/////////////////

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ViewVideo_vr extends Activity implements
        GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    /////////DECLARATIONS///////////
     static final String TAG = "ANUBHAV";
    GestureDetector detector;
    private VideoView mVideoView1,mVideoView2;
    private TextView current_position, total_duration;
    private AudioManager audio;
    private ImageButton mPlay;
    private ImageButton mPause;
    private ImageButton mNext;
    private ImageButton mPrev;
    private ImageButton mFfwd;
    private ImageButton mBack;
    private ImageButton paused;
    private ImageView speaker;
    private ImageView bright;
    private LinearLayout ll;
    private String current;
    String path;
    int id;
    int file_id;
    FileManager fileManager;
    private SeekBar prog_Bar, audio_bar, bright_bar;
    private final Handler handler = new Handler();
    private final Runnable r = new Runnable() {
        @
                Override
        public void run() {
            updateSeekProgress();
        }
    };
     final static String position_file_name = "left_position";
    private static final int SWIPE_MIN_DISTANCE = 5;
    private static final int SWIPE_THRESHOLD_VELOCITY = 10;
    static int left_id,extraData;
    final static String id_file_name = "id";
    boolean playingInternalvideo=true;

    //////////////////ONCREATE//////////////
    @Override
    public void onCreate(Bundle bundle) {
        Utils.onActivityCreateSetTheme(this);
        super.onCreate(bundle);
        setContentView(R.layout.activity_view_vr);
        /////////////////findview by ID///////////////
        mVideoView1 = (VideoView) findViewById(R.id.surface1);
        mVideoView2 = (VideoView) findViewById(R.id.surface2);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        detector = new GestureDetector(this);
        mPlay = (ImageButton) findViewById(R.id.play);
        mPause = (ImageButton) findViewById(R.id.pause);
        mNext = (ImageButton) findViewById(R.id.next);
        mFfwd = (ImageButton) findViewById(R.id.forward);
        mPrev = (ImageButton) findViewById(R.id.previous);
        mBack = (ImageButton) findViewById(R.id.back);
        speaker = (ImageView) findViewById(R.id.speaker);
        current_position = (TextView) findViewById(R.id.cur_pos);
        total_duration = (TextView) findViewById(R.id.total_length);
        bright = (ImageView) findViewById(R.id.bright);
        bright_bar = (SeekBar) findViewById(R.id.brightness);
        ll = (LinearLayout) findViewById(R.id.lin_lay);
        fileManager = new FileManager(getApplicationContext());
        paused = (ImageButton) findViewById(R.id.paused);
/////////////////////seekbars used////////////////
        inti_progress();
        init_vol();
        init_brightness();

////////////////////////button listners/////////////////////
        mPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
              //  playVideo();
                mVideoView1.start(); mVideoView2.start();
                mPlay.setVisibility(View.GONE);
                mPause.setVisibility(View.VISIBLE);
                paused.setVisibility(View.GONE);
            }
        });


        mPause.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mVideoView1 != null&&mVideoView2 != null) {
                    mVideoView1.pause(); mVideoView2.pause();
                    mPause.setVisibility(View.GONE);
                    mPlay.setVisibility(View.VISIBLE);
                    paused.setVisibility(View.VISIBLE);
                }
            }
        });

        paused.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                playVideo();
                mPlay.setVisibility(View.GONE);
                mPause.setVisibility(View.VISIBLE);
                paused.setVisibility(View.GONE);
            }
        });

        mFfwd.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mVideoView1 != null&&mVideoView2 != null) {
                    int a = mVideoView1.getCurrentPosition();
                    int z = mVideoView1.getDuration();
                    a = a + z / 20;

                    if (z > a) {
                        mVideoView1.seekTo(a);
                        mVideoView2.seekTo(a);
                    }
                }
            }
        });
        mBack.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (mVideoView != null) {
                    current = null;
                    mVideoView.stopPlayback();
                    seekBar.setProgress(0);

                if (mVideoView1 != null&&mVideoView2 != null) {
                    // mVideoView.seekTo(0);
                    int a = mVideoView1.getCurrentPosition();
                    int z = mVideoView1.getDuration();

                    if (a > z / 20) {
                        a = a - z / 20;
                    }
                    mVideoView1.seekTo(a); mVideoView2.seekTo(a);

                }
            }
        });
        mNext.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ++id;
                Intent next = new Intent(ViewVideo_vr.this, VideoStoredInSDCard.class);
                next.putExtra("position", id);
                startActivity(next);
            }
        });
        mPrev.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                --id;
                Intent next = new Intent(ViewVideo_vr.this, VideoStoredInSDCard.class);
                next.putExtra("position", id);
                startActivity(next);
            }
        });


        /////////////////////////////main Thread//////////////////////
        runOnUiThread(new Runnable() {
            public void run() {
                playVideo();

                updateSeekProgress();
                toolbar();
            }
        });
        /////////////////read FILE////////////////////
        if(playingInternalvideo) {
            try {
                int a = Integer.parseInt(fileManager.readFile(position_file_name));
                int b = Integer.parseInt(fileManager.readFile(id_file_name));
                if (b == left_id) {
                    mVideoView1.seekTo(a - 3000);
                    mVideoView2.seekTo(a - 3000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //////////////on pause//////////
    protected void onPause() {
        Log.i(TAG,"onPause");
        if(playingInternalvideo) {
            Log.i(TAG,"position file saved");

            fileManager.createFile(position_file_name, Integer.toString(mVideoView1.getCurrentPosition()));
        }
        else {
            Log.i(TAG,"position file saved");

            fileManager.createFile(position_file_name, Integer.toString(extraData));
        }


        mVideoView1.pause(); mVideoView2.pause();


        // mp.release();
        super.onPause();

    }

    //////////////on resume//////////////
    protected void onResume() {

        mVideoView1.start();mVideoView2.start();
        super.onResume();
    }

    ///////////////////SEEKBAR  definations/////////////////
    private void inti_progress() {

        try {
            prog_Bar = (SeekBar) findViewById(R.id.prog);
            // updateSeekProgress();
            prog_Bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    // mVideoView.seekTo(seekBar.getProgress());
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    mPause.performClick();
                    ll.setVisibility(View.VISIBLE);
                    toolbar();

                    //   mVideoView.seekTo(seekBar.getProgress());
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mPlay.performClick();
                    ll.setVisibility(View.VISIBLE);
                    toolbar();
                    mVideoView1.seekTo(seekBar.getProgress());
                    mVideoView2.seekTo(seekBar.getProgress());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init_brightness() {
        bright_bar.setMax(255);
        float curBrightnessValue = 0;
        try {
            curBrightnessValue = Settings.System.getInt
                    (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        int screen_brightness = (int) curBrightnessValue;
        bright_bar.setProgress(screen_brightness);
        bright_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progress = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bright_bar.setVisibility(View.INVISIBLE);
                        bright.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                Settings.System.putInt
                        (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, progress);
            }
        });
    }

    private void init_vol() {
        try {
            audio_bar = (SeekBar) findViewById(R.id.audio);
            audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            audio_bar.setMax(audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            audio_bar.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));
            audio_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    audio.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speaker.setVisibility(View.INVISIBLE);
                            audio_bar.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /////////////////toolbarr control/////////
    private void toolbar() {
        if (ll.getVisibility() == View.VISIBLE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ll.setVisibility(View.GONE);
                }
            }, 5000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    ll.setVisibility(View.VISIBLE);
                }
            }, 5000);
        }

    }

    ///////////////////////main fuction///////////////
    private void playVideo() {
        try {
            int pos=0;
            System.gc();
            Intent i = getIntent();
            Uri a=i.getData();

            if(a!=null)
            {   try {
                Log.i(ViewVideo_vr.TAG,"reading position from fie");
                ViewVideo_vr.extraData = Integer.parseInt(fileManager.readFile(ViewVideo_vr.position_file_name));
               } catch (Exception e) {
                e.printStackTrace();
            }

                path=a.toString();
                Intent intent=new Intent(this,VideoStoredInSDCard.class);
                intent.setData(a);
                Log.i(TAG,"intent at View Vdeo");
                startActivity(intent);

            }
            else {
                Bundle extras = i.getExtras();
                path = extras.getString("videofilename");
                id = extras.getInt("position");
                file_id = extras.getInt("id");
                String extraPath=extras.getString("matchnotfound");
                if(extraPath!=null) {
                    Uri newq = Uri.parse(extraPath);
                    path=extraPath;
                    mVideoView1.setVideoURI(newq);
                    mVideoView2.setVideoURI(newq);
                    mNext.setVisibility(View.GONE);
                    mPrev.setVisibility(View.GONE);
                    playingInternalvideo=false;



                    mVideoView1.start();
                    mVideoView2.start();
                }
                else {
                    try {
                        Log.i(TAG, "reading files");
                        left_id = Integer.parseInt(fileManager.readFile(id_file_name));
                        pos = Integer.parseInt(fileManager.readFile(position_file_name));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    fileManager.createFile(id_file_name, Integer.toString(file_id));
                    if (file_id == left_id && pos > 1000) {
                        mVideoView1.seekTo(pos - 1000);
                        mVideoView2.seekTo(pos - 1000);
                    }
                    mVideoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mNext.performClick();
                        }
                    });
                }

            //Uri fileuri = intent.getData();
            // filepath=fileuri.getPath();


            Log.v(TAG, "path: " + path);
            if (path == null || path.length() == 0) {
                Toast.makeText(this, "File URL/path is empty",
                        Toast.LENGTH_LONG).show();

            } else {
                // If the path has not changed, just start the media player
                if (path.equals(current) && mVideoView1 != null&& mVideoView2 != null) {
                    mVideoView1.start();
                    mVideoView2.start();
                    //mVideoView.requestFocus();
                    return;
                }
                current = path;
                mVideoView1.setVideoPath(getDataSource(path));
                mVideoView2.setVideoPath(getDataSource(path));


                mVideoView1.start();
                mVideoView2.start();

                mVideoView1.requestFocus();
                mVideoView2.requestFocus();

            }
            }
        } catch (Exception e) {

            if (mVideoView1 != null&&mVideoView2 != null) {
                mVideoView1.stopPlayback();
                mVideoView2.stopPlayback();
            }
        }


    }
    private String getDataSource(String path) throws IOException {
        if (!URLUtil.isNetworkUrl(path)) {
            return path;
        } else {
            URL url = new URL(path);
            URLConnection cn = url.openConnection();
            cn.connect();
            InputStream stream = cn.getInputStream();
            if (stream == null)
                throw new RuntimeException("stream is null");
            File temp = File.createTempFile("mediaplayertmp", "dat");
            temp.deleteOnExit();
            String tempPath = temp.getAbsolutePath();
            FileOutputStream out = new FileOutputStream(temp);
            byte buf[] = new byte[128];
            do {
                int numread = stream.read(buf);
                if (numread <= 0)
                    break;
                out.write(buf, 0, numread);
            } while (true);
            try {
                stream.close();
            } catch (IOException ex) {
                Log.e(TAG, "error: " + ex.getMessage(), ex);
            }
            return tempPath;
        }
    }
    ///////////update seekbar every .1 sec/////////////
    private void updateSeekProgress() {

        handler.postDelayed(updateTimeTask, 100);

    }

    private Runnable updateTimeTask = new Runnable() {
        @Override
        public void run() {
            prog_Bar.setProgress(mVideoView1.getCurrentPosition());

            current_position.setText(MiliToTime.getTime(mVideoView1.getCurrentPosition()));
            total_duration.setText(MiliToTime.getTime(mVideoView1.getDuration()));

            prog_Bar.setMax(mVideoView1.getDuration());
            if (mVideoView1.getCurrentPosition() == mVideoView1.getDuration())
                mNext.performClick();
            handler.postDelayed(this, 100);
        }
    };




    //////////////////gestures//////////////////////
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        if (ll.getVisibility() == View.GONE) {
            ll.setVisibility(View.VISIBLE);
            toolbar();
        } else
            ll.setVisibility(View.GONE);
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float vX, float vY) {

        if (e1.getPointerCount() == 1 && e2.getPointerCount() == 1) {


            float deltaY = e2.getY() - e1.getY();
            float deltaX = e2.getX() - e1.getX();
            if (Math.abs(deltaX) > Math.abs(deltaY)) {



                if (deltaX > SWIPE_MIN_DISTANCE) {
                    // right to left swipe

                    onSwipeLeft();
                    return true;
                } else if (deltaX < -SWIPE_MIN_DISTANCE) {
                    // left to right swipe
                    onSwipeRight();
                    return true;
                }
            } else if (Math.abs(deltaX) < Math.abs(deltaY)) {



                if (deltaY > SWIPE_MIN_DISTANCE ) {
                    // right to left swipe
                    onSwipeUp();
                    return true;
                } else if (deltaY < -SWIPE_MIN_DISTANCE ) {
                    // left to right swipe
                    onSwipeDown();
                    return true;
                }
            }
            return false;
        } else {

            float deltaY = e2.getY() - e1.getY();
            //Toast.makeText(ViewVideo.this, "Two finger detected", Toast.LENGTH_SHORT).show();

            final boolean enoughSpeed = Math.abs(vY) > SWIPE_THRESHOLD_VELOCITY;
            if (deltaY > SWIPE_MIN_DISTANCE && enoughSpeed) {
                // right to left swipe
                onSwipeUpTwo();
                return true;
            } else if (deltaY < -SWIPE_MIN_DISTANCE && enoughSpeed) {
                // left to right swipe
                onSwipeDownTwo();
                return true;
            }
        }
        return false;

    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float vX, float vY) {


        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {

        if (mPlay.getVisibility() == View.VISIBLE) {
            mPlay.performClick();
            return false;
        }
        if (mPause.getVisibility() == View.VISIBLE)
            mPause.performClick();
        return false;

    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }


    ///////////////gesture functions////////////
    protected void onSwipeLeft() {
        // do your stuff here
        int current = prog_Bar.getProgress();
        int max = prog_Bar.getMax();
        int scale = max / 40;
        mVideoView1.seekTo(current + scale);
        mVideoView2.seekTo(current + scale);
    }

    protected void onSwipeRight() {
        int current = prog_Bar.getProgress();
        int max = prog_Bar.getMax();
        int scale = max / 40;
        mVideoView1.seekTo(current - scale);
        mVideoView2.seekTo(current - scale);
    }

    protected void onSwipeUp() {


        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void run() {
                speaker.setVisibility(View.VISIBLE);
                audio_bar.setVisibility(View.VISIBLE);
                // do your stuff here
                int max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int current = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                int scale = max / 15;

                if (current - scale > 0)
                    current -= scale;
                else current = 0;
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, current - scale, 0);
                audio_bar.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));

                init_vol();
            }
        }, 100);

    }

    protected void onSwipeDown() {


        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void run() {
                //   audio_bar.setVisibility(View.VISIBLE);
                // do your stuff here
                speaker.setVisibility(View.VISIBLE);
                audio_bar.setVisibility(View.VISIBLE);
                int max = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int current = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
                int scale = max / 15;
                if (current + scale < max)
                    current = current + scale;
                else current = max;
                audio.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
                audio_bar.setProgress(audio.getStreamVolume(AudioManager.STREAM_MUSIC));


                init_vol();
            }
        }, 100);
        // do your stuff here

    }

    protected void onSwipeUpTwo() {
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void run() {
                bright.setVisibility(View.VISIBLE);
                bright_bar.setVisibility(View.VISIBLE);
                // do your stuff here
                float current = 0;
                try {
                    current = Settings.System.getInt
                            (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                float scale = 255 / 15;

                if (current - scale > 0) {
                    int a = (int) current - (int) scale;
                    Settings.System.putInt
                            (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, a);
                    try {
                        bright_bar.setProgress(Settings.System.getInt
                                (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
                    } catch (Settings.SettingNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                init_brightness();
            }
        }, 100);
    }

    protected void onSwipeDownTwo() {
        handler.postDelayed(new Runnable() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void run() {
                bright.setVisibility(View.VISIBLE);
                bright_bar.setVisibility(View.VISIBLE);
                // do your stuff here
                float current = 0;
                try {
                    current = Settings.System.getInt
                            (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);

                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                float scale = 255 / 15;
                int a;
                if (current + scale < 255) {
                    a = (int) current + (int) scale;
                } else a = 255;
                Settings.System.putInt
                        (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, a);
                try {
                    bright_bar.setProgress(Settings.System.getInt
                            (getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }

                init_brightness();
            }
        }, 100);
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(!playingInternalvideo) {
                finishAffinity();

            }
            else
                return super.onKeyDown(keyCode, event);

        }
        return super.onKeyDown(keyCode, event);
    }

}
}*/