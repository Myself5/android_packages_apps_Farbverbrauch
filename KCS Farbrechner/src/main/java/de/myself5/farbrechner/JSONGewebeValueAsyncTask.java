package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    @Override
    protected String doInBackground(String... afile) {
        String file = new String(afile[0]);
        File f = new File(file);
        if (f.exists() && !f.isDirectory()) {
            BufferedReader input = null;
            try {
                FileInputStream fIn = new FileInputStream(file);
                input = new BufferedReader(new InputStreamReader(fIn));
                String line;
                StringBuffer content = new StringBuffer();
                char[] buffer = new char[1024];
                int num;
                while ((num = input.read(buffer)) > 0) {
                    content.append(buffer, 0, num);
                }
                JSONObject obj = new JSONObject(content.toString());
                String value = obj.getString(mValue);
                return value;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (JSONException e) {
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
        JSONHandler.jsonvalue = result;
    }

    public static void loadJSON(Activity activ, String text, String value, String file) {
        new JSONGewebeValueAsyncTask(activ, text, value).execute(file);
    }
}