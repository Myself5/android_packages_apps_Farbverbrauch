package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class JSONGewebeValueAsyncTask extends AsyncTask<String, String, String> {

    //    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private String mDialogtext;
    private String mValue;

    public JSONGewebeValueAsyncTask(Activity a, String dialogtext, String value) {
        mActivity = a;
        mDialogtext = dialogtext;
        mValue = value;
    }

//    @Override
//    protected Dialog onCreateDialog(int id) {
//        switch (id) {
//            case DIALOG_DOWNLOAD_PROGRESS:
//                mProgressDialog = new ProgressDialog(mActivity);
//                mProgressDialog.setMessage(mActivity.getString(R.string.DownloadDialog));
//                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                mProgressDialog.setCancelable(true);
//                mProgressDialog.show();
//                return mProgressDialog;
//            default:
//                return null;
//        }
//    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage(mDialogtext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... afile) {
        String file = new String(afile[0]);
        File f = new File(file);
        Log.e("FILE:", "before if");
        Log.e("FILE:", "Filepath: " + file);
        if (f.exists() && !f.isDirectory()) {
            String[] arr = {};
            try {
                Log.e("FILE:", "Filepath" + file);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();
                fileReader.close();

                File logfile = new File(file + ".log");

                // if file doesnt exists, then create it
                if (!logfile.exists()) {
                    logfile.createNewFile();
                }
                FileWriter fw = new FileWriter(logfile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(stringBuilder.toString().trim());
                bw.close();

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

    protected void onProgressUpdate(String... progress) {
        Log.d("ANDRO_ASYNC", progress[0]);
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        mProgressDialog.dismiss();
    }
}