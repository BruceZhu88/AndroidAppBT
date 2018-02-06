package com.tymphany.bruce.sqa_automation;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Instrumentation;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tymphany.bruce.sqa_automation.bluetoothUtil.BluetoothManager;
import com.tymphany.bruce.sqa_automation.bluetoothUtil.BluetoothTools;
import com.tymphany.bruce.sqa_automation.player.MusicService;
import com.tymphany.bruce.sqa_automation.util.FileOperations;

public class MainActivity extends AppCompatActivity {

    //protected EditText runTimes;
    protected Button btnRun , btnStop, btnSpeak;
    protected TextView textPrint;

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private BluetoothA2dp mBluetoothA2dp;

    BluetoothManager bluetoothManager = new BluetoothManager();
    MusicService musicService = new MusicService();
    FileOperations fileOperations = new FileOperations();

    public String appPath;
	public String musicPath;
    public String cmdPath;
    public String returnPath;
    public String sdcardPath;
    public AudioManager audio;
    private boolean runFlag;
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    Timer timer = new Timer();
    private int recLen = 11;
    private int count;
    private boolean TimeStart=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        btnRun = (Button) this.findViewById(R.id.btnRun);
        btnRun.setOnClickListener(new ClickEvent());
        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());
        btnSpeak = (Button) this.findViewById(R.id.btnSpeak);
        /***
        btnConnect = (Button) this.findViewById(R.id.btnConnect);
        btnConnect.setOnClickListener(new ClickEvent());
        btnDisconnect = (Button) this.findViewById(R.id.btnDisconnect);
        btnDisconnect.setOnClickListener(new ClickEvent());
        btnStart = (Button) this.findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new ClickEvent());
        btnStop = (Button) this.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new ClickEvent());
        btnPlay = (Button) this.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new ClickEvent());
        btnNext = (Button) this.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new ClickEvent());
        btnPrev = (Button) this.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new ClickEvent());
        runTimes = (EditText) findViewById(R.id.runTimes);
         **/
        //Log.d(BluetoothTools.TAG, "Over create UI");

        sdcardPath = fileOperations.getPath();
        appPath = fileOperations.createFolder("/autoSQA");
		musicPath = fileOperations.createFolder("/autoSQA/music");
        fileOperations.createFolder("/autoSQA/logfile");
        fileOperations.createFolder("/autoSQA/data");
        cmdPath = fileOperations.createFile("/autoSQA/data/cmd.ini");
        returnPath = fileOperations.createFile("/autoSQA/data/return.ini");

        btnStop.setEnabled(false);
        btnSpeak.setEnabled(false);

        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(
                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0); //local recognition
        if (activities.size() != 0) {
            btnSpeak.setOnClickListener(new ClickEvent());
        } else {                 // if can't identify any recognition program, then disable Speak
            btnSpeak.setEnabled(false);
            textPrint.setText("Recognizer not present");
            Log.d(BluetoothTools.TAG, "Recognizer not present");
            //btnSpeak.setText("Recognizer not present");
        }

    }
    /*****************************************************************************************/
    /** voice recognition**/
    private void startVoice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech recognition demo");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
        Log.d(BluetoothTools.TAG, "start voice");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK) {
            // Fill the list view with the strings the recognizer thought it could have heard
            //Log.d(BluetoothTools.TAG, "requestCode="+requestCode+"  resultCode="+resultCode+" data="+data);
            ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            Log.d(BluetoothTools.TAG, "result = "+matches);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    class ClickEvent implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v == btnRun){
                runFlag = true;
                btnStop.setEnabled(true);
                btnRun.setEnabled(false);
                runThread run = new runThread();
                run.start();
            }
            else if (v == btnStop) {
                runFlag = false;
                btnRun.setEnabled(true);
                btnStop.setEnabled(false);
            }
            else if (v == btnSpeak) {
                startVoice();
            }
        }
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if ( !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
    /*****************************************************************************************/
    /**Timer thread
     * For voice has no return value
     *
     * **/
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recLen--;
                    Log.d(BluetoothTools.TAG, "recLen = "+recLen);
                    if(recLen <= 0) {
                        timer.cancel();
                        startVoice();
                    }
                }
            });
        }
    };



    /*****************************************************************************************/
    /**Run thread**/
    public class runThread extends Thread {
        @Override
        public void run() {
            Log.d(BluetoothTools.TAG, "run thread start");
            while (runFlag) {
                if (fileOperations.existFile(cmdPath)) {
                    fileOperations.delete(returnPath);
                    String cmd = "";
                    while (cmd.equals("")) { // avoid read too quick (for reading after file update successfully)
                        cmd = fileOperations.readFile(cmdPath);
                    }
                    String value = execute(cmd);
                    fileOperations.delete(cmdPath);
                    if (!value.equals("")) {
                        fileOperations.write(returnPath, value);
                    }
                }
            }
            Log.d(BluetoothTools.TAG, "run thread stop");
        }
    }


    public String execute(String cmd) {
        if (!cmd.contains("(") || !cmd.contains(")")) {
            Log.e(BluetoothTools.TAG, "command error");
            return "";
        }

        String para="";
        String[] str1 = cmd.split("\\(");
        if (!str1[1].equals(")")) {
            String[] str2 = str1[1].split("\\)");
            para = str2[0];
        }
        cmd = str1[0];

        if (cmd.equals("connect bluetooth")) {
            if (btConnect(para))
                return "1";
            else
                return "0";
        }
        else if (cmd.equals("disconnect bluetooth")) {
            if (btDisconnect(para))
                return "1";
            else
                return "0";
        }
        else if (cmd.equals("bluetooth status")) {
            if (bluetoothManager.isBTConnected())
                return "1";
            else
                return "0";
        }
        else if (cmd.equals("PlayAudio")) {
            musicService.initPlayer(sdcardPath + "/" +para);
            return "1";
        }
        else if (cmd.equals("StopAudio")) {
            if (musicService.stop()) {
                return "1";
            }
            return "0";
        }
        else if (cmd.equals("mediaVolumeUp")) {
            mediaVolumeUp();
            return "1";
        }
        else if (cmd.equals("mediaVolumeDown")) {
            mediaVolumeDown();
            return "1";
        }

        else{
            Log.e(BluetoothTools.TAG, "Cannot find related command!");
            return "";
        }
    }

    public void sleep(int time) {
        try {
            Thread.sleep(time);
            Log.d(BluetoothTools.TAG, "sleep "+ time +"ms");
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /*****************************************************************************************/
    public Boolean btConnect(String name) {
        String BTlist = bluetoothManager.scanBTlist(name);
        if (BTlist == ""){
            Log.d(BluetoothTools.TAG, "Cannot find "+name);
            return false;
        }
        String address = BTlist;
        BluetoothDevice btDev = bluetoothAdapter.getRemoteDevice(address);
        try {
            connect(bluetoothAdapter, btDev);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(BluetoothTools.TAG, "connect error:"+e.toString());
            return false;
        }
        return true;
        //setTitle("Local：" + bluetoothAdapter.getAddress());
        //bluetoothAdapter.startDiscovery();
    }

    public Boolean btDisconnect(String name) {
        String BTlist = bluetoothManager.scanBTlist(name);
        if (BTlist == ""){
            Log.d(BluetoothTools.TAG, "Cannot find "+name);
            return false;
        }
        String address = BTlist;
        BluetoothDevice btDev = bluetoothAdapter.getRemoteDevice(address);
        disconnect(bluetoothAdapter, btDev);
        return true;
    }

    //private void getBluetoothA2dp(BluetoothAdapter mBluetoothAdapter, final BluetoothDevice mBluetoothDevice){
    public void connect(BluetoothAdapter mBluetoothAdapter, final BluetoothDevice mBluetoothDevice){
        mBluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                //Log.d(BluetoothTools.TAG, "onServiceConnected run");
                if(profile==BluetoothProfile.A2DP){
                    mBluetoothA2dp=(BluetoothA2dp)proxy;
                    //Log.d(BluetoothTools.TAG, "onServiceConnected: get A2dp");
                    try {
                        Log.d(BluetoothTools.TAG, "bluetooth connect run");
                        Method connect = mBluetoothA2dp.getClass().getDeclaredMethod("connect", BluetoothDevice.class);
                        connect.setAccessible(true);
                        connect.invoke(mBluetoothA2dp,mBluetoothDevice);
                    } catch (Exception e) {
                        Log.e(BluetoothTools.TAG,"connect exception:"+e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onServiceDisconnected(int profile) {   }
        },BluetoothProfile.A2DP);
    }

    public void disconnect(BluetoothAdapter mBluetoothAdapter, final BluetoothDevice mBluetoothDevice){
        mBluetoothAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                //Log.d(BluetoothTools.TAG, "onServiceConnected run");
                if(profile==BluetoothProfile.A2DP){
                    mBluetoothA2dp=(BluetoothA2dp)proxy;
                    //Log.d(BluetoothTools.TAG, "onServiceConnected: get A2dp");
                    try {
                        Log.d(BluetoothTools.TAG, "bluetooth disconnect run");
                        Method connect = mBluetoothA2dp.getClass().getDeclaredMethod("disconnect", BluetoothDevice.class);
                        connect.setAccessible(true);
                        connect.invoke(mBluetoothA2dp,mBluetoothDevice);
                    } catch (Exception e) {
                        Log.e(BluetoothTools.TAG,"connect exception:"+e.toString());
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onServiceDisconnected(int profile) {   }
        },BluetoothProfile.A2DP);
    }

    /*****************************************************************************************/
    /**media player volume control**/
    public void mediaVolumeUp() {
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
        int currentVolume = currentVol();
        Log.d(BluetoothTools.TAG,"Volume up "+currentVolume);
    }

    public void mediaVolumeDown() {
        audio.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
        int currentVolume = currentVol();
        Log.d(BluetoothTools.TAG,"Volume down "+currentVolume);
    }

    public int currentVol() {
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        return currentVolume;
    }



}
