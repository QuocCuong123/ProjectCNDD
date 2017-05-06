package com.example.qanh.appmusic;

import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements Fragment1.OnDataPass1,Fragment2.OnDataPass2,View.OnClickListener, SeekBar.OnSeekBarChangeListener,MusicPlayer.OnCompletionListener{
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private ArrayList<String> mPathNameSongs = null;
    private int mPosition ;
    private int UPDATE_TIME = 1;
    private boolean isRunning;
    private int timeCurrent;
    private MusicPlayer musicPlayer;

    private ImageButton mPlay;
    private ImageButton mNext;
    private ImageButton mPrev;
    private ImageButton mPause;
    private ImageButton mRepeat;
    private ImageButton mRepeat1;
    private SeekBar mProcess;
    private TextView mTimeProcess;
    private TextView mTimeTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*//Không hiện tiêu đề
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //Hiện nút back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getId();
        initList();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    private void initList() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);

        musicPlayer = new MusicPlayer();
        musicPlayer.setOnCompletionListener(this);
    }
    public void getId() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mPlay = (ImageButton) findViewById(R.id.imgBtnPlay);
        mPrev = (ImageButton) findViewById(R.id.imgBtnPrev);
        mNext = (ImageButton) findViewById(R.id.imgBtnNext);
        mPause = (ImageButton) findViewById(R.id.imgBtnPause);
        mProcess = (SeekBar) findViewById(R.id.seekBar);
        mRepeat = (ImageButton) findViewById(R.id.imgBtnRepeat);
        mRepeat1 = (ImageButton) findViewById(R.id.imgBtnRepeat1);
        mTimeProcess = (TextView) findViewById(R.id.txtProcess);
        mTimeTotal = (TextView) findViewById(R.id.txtTotal);

        mPlay.setOnClickListener(this);
        mPrev.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPause.setOnClickListener(this);
        mRepeat.setOnClickListener(this);
        mRepeat1.setOnClickListener(this);
        mProcess.setOnSeekBarChangeListener(this);
    }

    //Phương thúc lọc khi search


    @Override
    public void khiClickVaoItemListView(int postion, ArrayList<String> mPathListSong) {
        mPosition = postion;
        mPathNameSongs = mPathListSong;
        Log.d("TAG","Activity Item " +mPosition+ " selected!");
        if(mPosition != -1){
            playMusic(mPathNameSongs.get(mPosition));
        }
        mPagerAdapter.mFragment2.updateText(musicPlayer.getTimeTotal()*1000);
    }

    @Override
    public String setText() {
        return mPathNameSongs.get(mPosition);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBtnNext:
                nextMusic();
                break;
            case R.id.imgBtnPlay: {
                mPlay.setVisibility(View.GONE);
                mPause.setVisibility(View.VISIBLE);
                musicPlayer.play();
                mPagerAdapter.mFragment2.resumeAnimation();
                break;
            }
            case R.id.imgBtnPause: {
                pauseMusic();
                break;
            }
            case R.id.imgBtnPrev:
                prevMusic();
                break;
            case R.id.imgBtnRepeat:
                break;
            case R.id.imgBtnRepeat1:
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (timeCurrent != progress && timeCurrent != 0)
            musicPlayer.seek(mProcess.getProgress() * 1000);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void OnEndMusic() {
        nextMusic();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == UPDATE_TIME) {
                timeCurrent = musicPlayer.getTimeCurrent();
                mTimeProcess.setText(getTimeFormat(timeCurrent));
                mProcess.setProgress(timeCurrent);
            }
        }
    };

    public void playMusic(String path) {
        mPlay.setVisibility(View.GONE);
        mPause.setVisibility(View.VISIBLE);

        if(musicPlayer.getState() == MusicPlayer.PLAYER_PLAY){
            musicPlayer.stop();
        }
        musicPlayer.setup(path);
        musicPlayer.play();

        // Set tên bài hát
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try{
            mediaMetadataRetriever.setDataSource(mPathNameSongs.get(mPosition));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Rất tiếc không thể mở bài hát này!\n   Không có thông tin về bài hát",Toast.LENGTH_SHORT).show();
        }
        mTimeTotal.setText(getTimeFormat(musicPlayer.getTimeTotal()));
        isRunning = true;
        //Set thanh  tiến trình có độ dài max băng với thời gian chạy bài hát
        mTimeTotal.setText(getTimeFormat(musicPlayer.getTimeTotal()));
        mProcess.setMax(musicPlayer.getTimeTotal());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isRunning) {
                    Message message = new Message();
                    message.what = UPDATE_TIME;
                    handler.sendMessage(message);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();

    }
    // Format thời gian về h , p ,s
    private String getTimeFormat(long time) {
        String tm = "";
        int s;
        int m;
        int h;
        //giây
        s = (int) (time % 60);
        m = (int) ((time - s) / 60);
        if (m >= 60) {
            h = m / 60;
            m = m % 60;
            if (h > 0) {
                if (h < 10)
                    tm += "0" + h + ":";
                else
                    tm += h + ":";
            }
        }
        if (m < 10)
            tm += "0" + m + ":";
        else
            tm += m + ":";
        if (s < 10)
            tm += "0" + s;
        else
            tm += s + "";
        return tm;
    }
    public void pauseMusic() {
        mPlay.setVisibility(View.VISIBLE);
        mPause.setVisibility(View.GONE);
        musicPlayer.pause();

        mPagerAdapter.mFragment2.pauseAnimation();
    }

    private void nextMusic(){
        mPosition++;
        if (mPosition >= mPathNameSongs.size()) {
            mPosition = 0;
        }
        String path = mPathNameSongs.get(mPosition);
        playMusic(path);

        mPagerAdapter.mFragment2.cancelAnimation();
        mPagerAdapter.mFragment2.updateText(musicPlayer.getTimeTotal()*1000);
    }

    private  void prevMusic(){
        mPosition--;
        if (mPosition < 0) {
            mPosition = mPathNameSongs.size() - 1;
        }
        String path = mPathNameSongs.get(mPosition);
        playMusic(path);
        mPagerAdapter.mFragment2.cancelAnimation();
        mPagerAdapter.mFragment2.updateText(musicPlayer.getTimeTotal()*1000);
    }
}
