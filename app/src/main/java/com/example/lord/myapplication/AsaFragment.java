package com.example.lord.myapplication;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsaFragment extends Fragment {
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ToggleButton playPause;
    private ToggleButton stopButton;
    //private ToggleButton startButton;
    private MyAdapter myAdapter;
    private MediaPlayer mediaPlayer;
    private int _state;
    private String STREAM_URL = "http://stream3.radiostyle.ru:8003/radioacca";

    public AsaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_asa, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view1);
        myAdapter = new MyAdapter(itemList);
        //myAdapter.setClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        prepareItem();
        playPause = (ToggleButton)view.findViewById(R.id.playpause1);
        stopButton = (ToggleButton)view.findViewById(R.id.stopButton1);
        //startButton = (ToggleButton)view.findViewById(R.id.play1);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                    Toast.makeText(getActivity(), "Идет подключение к радиостанции", Toast.LENGTH_LONG).show();
                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(STREAM_URL);
                        mediaPlayer.prepareAsync();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
                    else if(mediaPlayer.isPlaying()){
                        mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                //Toast.makeText(getActivity(), "Идет подключение к радиостанции..", Toast.LENGTH_LONG).show();
            }
        });
        registerForContextMenu(playPause);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.stop();
            }
        });
        registerForContextMenu(stopButton);
        return view;
    }

    private void prepareItem() {
        Item item = new Item(R.drawable.assa,"Радио Асса", "104,8 FM");
        itemList.add(item);
    }


    /*@Override
    public void itemClicked(View view, int position) {
        releaseMP();
        if (position == 0){
            mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(STREAM_URL);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                Toast.makeText(getActivity(), "Идет подключение к радиостанции..", Toast.LENGTH_LONG).show();
            } catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
            }
        }
        else {
            System.out.println("position" + position);
        }
    } */

    private void releaseMP() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
