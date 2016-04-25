package com.weclass.audio;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.net.Uri;

public class AudioJoin extends CordovaPlugin {
    public static String TAG = "AudioJoin";

    private CallbackContext callbackContext;
    private JSONObject params;
    /**
     * @param audio files list
     */
    public boolean join(String dstFilePath, String[] list) {
        long dateTaken = System.currentTimeMillis();
        String strTempFile = Long.toString(dateTaken);
        Uri uri = Uri.parse(dstFilePath);
        File mRecordAudioFile = new File(uri.getPath());

        FileOutputStream fileOutputStream = null;
        boolean headerexist = true;
        if (!mRecordAudioFile.exists()) {
            try {
                mRecordAudioFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            headerexist = false;
        }
        try {
            fileOutputStream = new FileOutputStream(mRecordAudioFile, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.length; i++) {
            Uri suri = Uri.parse(list[i]);
            File tempfile = new File(suri.getPath());
            try {
                FileInputStream fileInputStream = new FileInputStream(tempfile);
                byte[] myByte = new byte[fileInputStream.available()];
                // file length
                int length = myByte.length;

                // first audio file
                if (i == 0 && !headerexist) {
                    while (fileInputStream.read(myByte) != -1) {
                        fileOutputStream.write(myByte, 0, length);
                    }
                }

                // delete file head information
                else {
                    while (fileInputStream.read(myByte) != -1) {

                        fileOutputStream.write(myByte, 6, length - 6);
                    }
                }

                fileOutputStream.flush();
                fileInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean execute(String action, final JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.e(TAG, "AudioJoin execute begin");
        this.callbackContext = callbackContext;
        this.params = args.getJSONObject(0);
        if (action.equals("join")) {
            Log.e(TAG, "join");
            //srcPaths = this.params.getString("srcPaths");
            //srcPaths = this.params.getJSONArray("srcPaths");
            JSONArray srcPathsJA = this.params.getJSONArray("srcPaths");
            final String[] srcPaths = new String[srcPathsJA.length()];
            for(int i=0; i<srcPathsJA.length(); i++) {
                srcPaths[i] = srcPathsJA.getString(i);
            }
            Log.e(TAG, srcPaths.toString());
            final String dstPath = this.params.getString("dstPath");
            Log.e(TAG, "begin Run");
            Log.e(TAG, "begin Run:" + dstPath);

            if (this.cordova != null) {
                cordova.getThreadPool().execute(new Runnable() {
                    public void run() {
                        boolean result = join(dstPath, srcPaths);
                        if(result) {
                            JSONObject audioInfo = new JSONObject();
                            try{
                                audioInfo.put("dstPath", dstPath);
                            }catch(JSONException e){
                                Log.e("onActivityResult", "faied", e);
                            }
                            callbackContext.success(audioInfo);
                        }else{
                            callbackContext.error("join failed");
                        }
                    }
                });
            }
        }
        return true;
    }

}
