package com.example.qanh.appmusic;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Q.Anh on 28/04/2017.
 */

public class Fragment2 extends Fragment{
    private ImageView mHinhCaSi;
    private TextView mTenBaiHat;
    private OnDataPass2 mCallBack;
    //private Animation mAnimation;
    private ObjectAnimator mAnimation;
    private boolean isPause;

    public interface OnDataPass2 {
        public String setText();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_two,container,false);
        mHinhCaSi = (ImageView) view.findViewById(R.id.imgHinhCaSi);
        mTenBaiHat = (TextView) view.findViewById(R.id.txtTenBaiHatt);
        mAnimation = ObjectAnimator.ofFloat(mHinhCaSi, "rotation", 0, 360);

        return view;
    }
    public void updateText(long timeQuayVong){
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(mCallBack.setText());
        }catch (RuntimeException e) {
            e.printStackTrace();
        }

        //mAnimation.setDuration(timeQuayVong);

        byte [] data = mediaMetadataRetriever.getEmbeddedPicture();
        String tenBaiHat = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        mTenBaiHat.setText(tenBaiHat);
        if(data != null){
            InputStream is = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            mHinhCaSi.setImageBitmap(bitmap);
        }
        else{
            mHinhCaSi.setImageResource(R.drawable.disc256);
        }
        mAnimation.setDuration(10000);
        mAnimation.setRepeatCount((int) (timeQuayVong/100));
        mAnimation.setRepeatMode(ObjectAnimator.RESTART);
        startAnimation();
    }
    public void startAnimation() {
        mAnimation.start();
        isPause = false;
    }


    public void cancelAnimation() {
        mAnimation.cancel();
        isPause = false;
    }

    public void pauseAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mAnimation.pause();
            isPause = true;
        }
    }

    public void resumeAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if(isPause){
                mAnimation.resume();
                isPause = false;
            }
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (OnDataPass2)context ;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }
}
