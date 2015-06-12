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
    private String mDialogtext;
    private ProgressDialog mProgressDialog;
    private String mFile;

    public JSONRezepteValueAsyncTask(Activity a, String value, String farbe, String dialogtext) {
        mValue = value;
        mFarbe = farbe;
        mActivity = a;
        mDialogtext = dialogtext;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(mDialogtext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... afile) {
        mFile = new String(afile[0]);
        String mFilePath = MainActivity.FILE_PATH + mFile;
        File f = new File(mFilePath);
        if (f.exists() && !f.isDirectory()) {
            String[] arr = {};
            try {
                FileReader fileReader = new FileReader(mFilePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();
                fileReader.close();

                JSONObject obj = new JSONObject(stringBuilder.toString().trim());
                JSONObject obj2 = obj.getJSONObject(mValue);
                String value = obj2.getString(mFarbe);
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

    @Override
    protected void onPostExecute(String result){
        switch (mFile){
            case "rezepte.json":
                Rezepte.setTV(mFarbe, result);
                break;
        }
        mProgressDialog.dismiss();
    }
}