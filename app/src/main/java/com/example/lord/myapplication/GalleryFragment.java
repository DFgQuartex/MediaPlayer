package com.example.lord.myapplication;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {
    private List<Item> itemList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ToggleButton playPause;
    private MyAdapter myAdapter;
    private ToggleButton stopButton;
    private ToggleButton startButton;
    private MediaPlayer mediaPlayer;
    private String STREAM_URL="http://stream6.radiostyle.ru:8006/priboyfm";

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        //playPause = (ToggleButton)view.findViewById(R.id.playpause);
        myAdapter = new MyAdapter(itemList);
        //myAdapter.setClickListener(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(myAdapter);
        prepareItem();
        playPause = (ToggleButton) view.findViewById(R.id.playpause);
        stopButton = (ToggleButton)view.findViewById(R.id.stopButton);
        startButton = (ToggleButton)view.findViewById(R.id.play);
        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (mediaPlayer.isPlaying()){
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
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releaseMP();
                mediaPlayer = new MediaPlayer();
                Toast.makeText(getActivity(),"Идет подключение к радиостанции...", Toast.LENGTH_LONG).show();
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
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                }
            }
        });
        registerForContextMenu(startButton);
        return view;
    }

    private void prepareItem(){
        Item item = new Item(R.drawable.peri, "Радио Прибой", "100,7 FM");
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
            } catch (IOException e) {
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
