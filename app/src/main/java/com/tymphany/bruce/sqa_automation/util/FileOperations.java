package com.tymphany.bruce.sqa_automation.util;

import android.os.Environment;
import android.util.Log;

import com.tymphany.bruce.sqa_automation.bluetoothUtil.BluetoothTools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Bruce Zhu on 3/31/2017.
 */

public class FileOperations {

    public FileOperations() {

    }
    /*****************************************************************************************/
    /**SD card file operation**/
    private boolean isExternalStoragePresent() {

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but
            // all we need
            // to know is we can neither read nor write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
        if (!((mExternalStorageAvailable) && (mExternalStorageWriteable))) {
            Log.d(BluetoothTools.TAG,"SD card not present");
        }
        return (mExternalStorageAvailable) && (mExternalStorageWriteable);
    }

    //create folder --> music, log
    public String createFolder(String name) {
        if (isExternalStoragePresent()) {
            String path = Environment.getExternalStorageDirectory().toString() + name;
            File folder = new File(path);
            if (!folder.exists()) {
                folder.mkdir();
                Log.d(BluetoothTools.TAG,"Folder create successfully "+path);
            }
            return path;
        }
        return "";
    }

    //create new file
    public String createFile(String name) {
        if (isExternalStoragePresent()) {
            String path = Environment.getExternalStorageDirectory().toString() + name;
            File file = new File(path);
            try {
                if (!file.createNewFile()) {
                    Log.d(BluetoothTools.TAG, "create file "+path+" already exists!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return path;
        }
        return "";
    }

    //If file exist
    public Boolean existFile(String path) {
        if (isExternalStoragePresent()) {
            File file = new File(path);
            if (file.exists())
                return true;
            else
                return false;
        }
        return false;
    }

    //read file
    public static String readFromAssets(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String txt = "";
        try {
            // do reading, usually loop until end of file reading
            StringBuilder sb = new StringBuilder();
            String mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine); // process line
                //sb.append('\n');
                mLine = reader.readLine();
            }
            txt = sb.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reader.close();
        }
        return txt;
    }

    public String readFile(String path) {
        String readStr;
        try {
            readStr = readFromAssets(path);
            return readStr;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    //write file
    public Boolean write(String path, String fcontent) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(fcontent);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    //delete file
    public Boolean delete(String path) {
        File file = new File(path);
        if (file.exists()) {
            boolean deleted = file.delete();
            return deleted;
        }
        Log.d(BluetoothTools.TAG, "delete file "+path+" does not exists!");
        return false;
    }

    //get sdcard path
    public String getPath() {
        if (isExternalStoragePresent()) {
            String path = Environment.getExternalStorageDirectory().toString();
            return path;
        }
        return "";
    }
}
