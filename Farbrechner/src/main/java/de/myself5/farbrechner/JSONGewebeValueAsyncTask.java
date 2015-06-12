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

public class JSONGewebeValueAsyncTask extends AsyncTask<String, String, String> {

    private Activity mActivity;
    private String mValue;
    private String mDialogtext;
    private ProgressDialog mProgressDialog;
    private String mFile;

    public JSONGewebeValueAsyncTask(Activity a, String value, String dialogtext) {
        mActivity = a;
        mValue = value;
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
                String value = obj.getString(mValue);
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
        Farbverbrauch.setTV(result);
        mProgressDialog.dismiss();
    }
}