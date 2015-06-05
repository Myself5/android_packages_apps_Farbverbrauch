package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class JSONRezepteValueAsyncTask extends AsyncTask<String, String, String> {
    private String mValue;
    private String mFarbe;
    private Activity mActivity;

    public JSONRezepteValueAsyncTask(Activity a, String value, String farbe) {
        mValue = value;
        mFarbe = farbe;
        mActivity = a;

    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... afile) {
        String file = new String(afile[0]);
        File f = new File(file);
        if (f.exists() && !f.isDirectory()) {
            String[] arr = {};
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();
                fileReader.close();
                publishProgress("" + 20);

                JSONObject obj = new JSONObject(stringBuilder.toString().trim());
                publishProgress("" + 60);
                JSONObject obj2 = obj.getJSONObject(mValue);
                publishProgress("" + 80);
                String value = obj2.getString(mFarbe);
                publishProgress("" + 100);
                return value;

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
    }
}