package com.example.qanh.appmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Q.Anh on 06/04/2017.
 */

public class ListViewAdapter extends BaseAdapter{
    private ArrayList<String> paths;
    public Context context;
    private LayoutInflater inflater;
    public ListViewAdapter(Context context, ArrayList<String> paths){
        this.paths = paths;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int i) {
        return paths.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent) {
        LisviewHolder lisviewHolder;
        if(converView == null){
            lisviewHolder = new LisviewHolder();
            converView = inflater.inflate(R.layout.information_music,parent,false);
            lisviewHolder.mTenBaiHat = (TextView) converView.findViewById(R.id.txtTenBaiHat);
            lisviewHolder.mTenCaSi = (TextView) converView.findViewById(R.id.txtCaSi);
            lisviewHolder.mHinhCaSi = (ImageView) converView.findViewById(R.id.imgCaSi);
            converView.setTag(lisviewHolder);
        }
        else {
            lisviewHolder = (LisviewHolder) converView.getTag();
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(paths.get(position));
        }catch (RuntimeException e) {
            e.printStackTrace();
        }
        String caSi = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String tenBaiHat = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        byte [] data = mediaMetadataRetriever.getEmbeddedPicture();
        if(data != null){
            InputStream is = new ByteArrayInputStream(mediaMetadataRetriever.getEmbeddedPicture());
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            lisviewHolder.mHinhCaSi.setImageBitmap(bitmap);
        }
        else{
            lisviewHolder.mHinhCaSi.setImageResource(R.drawable.ic_compact_disc);
        }
        lisviewHolder.mTenBaiHat.setText(tenBaiHat);
        lisviewHolder.mTenCaSi.setText(caSi);
        return converView;
    }
    public class LisviewHolder{
        TextView mTenBaiHat;
        TextView mTenCaSi;
        ImageView mHinhCaSi;
    }
}
