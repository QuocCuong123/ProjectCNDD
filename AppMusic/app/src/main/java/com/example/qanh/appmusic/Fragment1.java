package com.example.qanh.appmusic;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Q.Anh on 28/04/2017.
 */

public class Fragment1 extends Fragment implements AdapterView.OnItemClickListener{

    private ListView mListMusic;
    private ListViewAdapter mListViewAdapter = null;
    private ArrayList<File> mPaths = null;
    private ArrayList<String> mPathNameSongs = null;
    private int mPosition;
    OnDataPass1 mCallBack;

    public interface OnDataPass1 {
        public void khiClickVaoItemListView(int postion,ArrayList<String> mPathListSong);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_one,container,false);
        mListMusic = (ListView) view.findViewById(R.id.listViewMusic);
        mListMusic.setOnItemClickListener(this);
        initList();
        return view;
    }

    private void initList() {
        mPaths = findSongs(Environment.getExternalStorageDirectory());
        mPathNameSongs = new ArrayList<String>();
        for (int i = 0; i < mPaths.size(); i++) {
            mPathNameSongs.add(mPaths.get(i).getAbsolutePath().toString());
            //Toast.makeText(getApplicationContext(),pathNameSongs.get(i),Toast.LENGTH_SHORT).show();
        }
        mListViewAdapter = new ListViewAdapter(getActivity(), mPathNameSongs);
        mListMusic.setAdapter(mListViewAdapter);

    }
    // Get danh sách các file có đuôi là  mp3 or wav cho vào ArrayList
    public ArrayList<File> findSongs(File root) {
        ArrayList<File> path = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                path.addAll(findSongs(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith("wav")||singleFile.getName().endsWith(".MP3")) {
                    path.add(singleFile);
                }
            }
        }
        return path;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Log.d("TAG","Item " +position+ " selected!");
        this.mPosition = position;
        if(mCallBack!=null){
            mCallBack.khiClickVaoItemListView(mPosition,mPathNameSongs);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallBack = (OnDataPass1 )context ;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

}
