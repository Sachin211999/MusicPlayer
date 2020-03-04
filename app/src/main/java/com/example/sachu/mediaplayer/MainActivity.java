package com.example.sachu.mediaplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.MissingFormatArgumentException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
TextView textView,songlabel;
    Button play,pause,prev,next;
    int currentSong=0;
//ArrayList<Integer> mysongs;
    String[] mysongs;
    String[] songs;
SeekBar sb1,sb2;
//ListView lv;
static MediaPlayer mp;
double startTime=0;
String sname;
int i;
//ArrayList<String> songs;
//int[] songsid=new int[100];
//String[] songs={"Space Bound","So Bad","Not Afraid","Beautiful","25 to life","Stan"};
//int[] songsid={R.raw.s1,R.raw.s2,R.raw.s3,R.raw.s4,R.raw.s5,R.raw.s6};
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
songlabel=(TextView)findViewById(R.id.txt2);
        play = (Button) findViewById(R.id.play);
        prev = (Button) findViewById(R.id.prev);
        next = (Button) findViewById(R.id.next);
       // lv = (ListView) findViewById(R.id.list);
        sb1 = (SeekBar) findViewById(R.id.seekBar);
        sb2 = (SeekBar) findViewById(R.id.vol);
       textView=(TextView)findViewById(R.id.txt1);
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        play.setOnClickListener(this);
        sb2.setOnSeekBarChangeListener(this);
        sb1.setOnSeekBarChangeListener(this);
getSupportActionBar().setTitle("Now Playing");
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
getSupportActionBar().setDisplayShowHomeEnabled(true);

       /* lv.setAdapter(new ArrayAdapter<String>(this,R.layout.listrow,R.id.textView2,songs));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                   view.setSelected(true);
                    setSong(i);
            }
        });*/

    if (mp!=null) {
        mp.stop();
        mp.release();
    }


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
mysongs=intent.getStringArrayListExtra("songs").toArray(new String[0]);
        i=bundle.getInt("pos",0);
         songs= intent.getStringArrayListExtra("songsname").toArray(new String[0]);
        sname=songs[i];
        songlabel.setText(sname);
        songlabel.setSelected(true);

        mp=MediaPlayer.create(this, Uri.parse(mysongs[i]));
        sb1.setMax(mp.getDuration());
        mp.start();
        handler.post(runnable);
        currentSong=i;
    }

    Handler handler = new Handler();
    Runnable runnable =
            new Runnable() {
        @SuppressLint("DefaultLocale")
        @Override
        public void run() {

            sb1.setProgress(mp.getCurrentPosition());
            handler.postDelayed(runnable, 1000);
            startTime = mp.getCurrentPosition();
            textView.setText(String.format("%d:%d",
                    TimeUnit.MILLISECONDS.toMinutes((long) startTime),
                    TimeUnit.MILLISECONDS.toSeconds((long) startTime) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.
                                    toMinutes((long) startTime)))
            );
        }
    };
    public void setSong(int i)
    {
        sname=songs[i];
        mp = MediaPlayer.create(this, Uri.parse(mysongs[i]));
        sb1.setMax(mp.getDuration());
        songlabel.setText(sname);
        mp.start();
        handler.post(runnable);
        currentSong=i;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.play:
                if(mp.isPlaying())
                {
                    mp.pause();
                    play.setBackgroundResource(R.drawable.play);
                }
                else {
                    mp.start();
                    play.setBackgroundResource(R.drawable.pause);
                    }
                break;
            case R.id.next:
                if(mp!=null&&currentSong<songs.length-1)
                {
                    mp.stop();
                    mp.release();
                    setSong(++currentSong);

                }
                break;
            case R.id.prev:
                if(mp!=null&&currentSong>0)
                {
mp.stop();
mp.release();
setSong(--currentSong);


                }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
        switch(seekBar.getId())
        {
            case R.id.seekBar:
                if(fromuser)
                    mp.seekTo(progress);
                break;
            case R.id.vol:
                mp.setVolume(progress*0.01f,progress*0.01f);
        }
    }

    @Override

    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}

