package com.lucky.androidlearn.multithread;

import android.os.AsyncTask;
import android.util.Log;

public class MultiAsyncTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "MultiAsyncTask";

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {
        String taskName = strings[0];
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "doInBackground: "+taskName);
        return taskName;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e(TAG, "onPostExecute: "+s);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



}
