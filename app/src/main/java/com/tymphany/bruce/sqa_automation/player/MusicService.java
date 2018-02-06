package com.tymphany.bruce.sqa_automation.player;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.tymphany.bruce.sqa_automation.bluetoothUtil.BluetoothTools;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bruce zhu on 3/22/2017.
 */

public class MusicService {

    private static final File MUSIC_PATH = Environment
            .getExternalStorageDirectory();// 找到music存放的路径。
    public List<String> musicList;// 存放找到的所有mp3的绝对路径。
    public MediaPlayer player; // 定义多媒体对象
    public int songNum; // 当前播放的歌曲在List中的下标
    public String songName; // 当前播放的歌曲名
    public  String dataSource;

    class MusicFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (name.endsWith(".mp3"));//返回当前目录所有以.mp3结尾的文件
        }
    }

    public MusicService() {
        try{
            String file_path = "/storage/emulated/0/music/Billy Boyd - The Last Goodbye.mp3";
        }catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(BluetoothTools.TAG, "MusicService run!");

        musicList = new ArrayList<String>();
        player = new MediaPlayer();
        /*
        if (MUSIC_PATH.listFiles(new MusicFilter()).length > 0) {
            for (File file : MUSIC_PATH.listFiles(new MusicFilter())) {
                musicList.add(file.getAbsolutePath());
            }
        }
        */
    }


    public void setPlayName(String dataSource) {
        File file = new File(dataSource);//假设为D:\\mm.mp3
        String name = file.getName();//name=mm.mp3
        Log.d(BluetoothTools.TAG, "songName: "+name);
        int index = name.lastIndexOf(".");//找到最后一个.
        songName = name.substring(0, index);//截取为mm
        Log.d(BluetoothTools.TAG, "songs list: "+songName);
    }

    public void initPlayer(String dataSource) {
        try {
			if (!isFileExist(dataSource)) {
                return;
            }
            Log.d(BluetoothTools.TAG, "init mediaPlayer!");
            player.reset();
            //String dataSource = musicList.get(songNum);//得到当前播放音乐的路径
            setPlayName(dataSource);
            //player.setDataSource(dataSource);
            player.setDataSource(dataSource);
            player.prepare();
            Log.d(BluetoothTools.TAG, "start play!");
            player.start();
            //setOnCompletionListener 当当前多媒体对象播放完成时发生的事件
            /*
            player.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer arg0) {
                    next();//如果当前歌曲播放完毕,自动播放下一首.
                }
            });
            */
        } catch (Exception e) {
            Log.d(BluetoothTools.TAG, "MusicService"+e.getMessage());
            //Log.v("MusicService", e.getMessage());
        }
    }

    public void next() {
        Log.d(BluetoothTools.TAG, "next audio!");
        //songNum = songNum == musicList.size() - 1 ? 0 : songNum + 1;
        //initPlayer();
    }

    public void prev() {
        Log.d(BluetoothTools.TAG, "prev audio!");
        //songNum = songNum == 0 ? musicList.size() - 1 : songNum - 1;
        //initPlayer();
    }

    public void play() {
        if (player.isPlaying()) {
            Log.d(BluetoothTools.TAG, "pause audio!");
            player.pause();
        }
        else {
            Log.d(BluetoothTools.TAG, "play audio!");
            player.start();
        }
    }

    public Boolean stop() {
        try {
            if (player.isPlaying()) {
                Log.d(BluetoothTools.TAG, "stop audio!");
                player.stop();
            }
            return true;
        }catch (Exception e) {
            Log.d(BluetoothTools.TAG, ""+e.toString());
            return false;
        }
        //player.release();
    }
    //判断文件是否存在
    public boolean  isFileExist(String path){
        //String path = Environment.getExternalStorageDirectory() + File.separator + "TheLastGoodbye.mp3";
        File file = new File(path);
        Log.d(BluetoothTools.TAG, path);
        if(file.exists()){
            try {
//                mediaPlayer.setDataSource(file.getAbsolutePath());
//                mediaPlayer.prepare();//预加载音频
//                mediaPlayer.start();//播放音乐
            }catch (Exception e) {
                e.printStackTrace();
            }
            dataSource = path;
            Log.d(BluetoothTools.TAG, "file exist!");
            return true;
        }else{
            Log.d(BluetoothTools.TAG, "file does not exist!");
        }
        return false;
    }


}
