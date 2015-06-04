package de.myself5.farbrechner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class JSONArrayAsyncTask extends AsyncTask<String, String, String[]> {

    //    public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private String mDialogtext;
    private String mArrayName;

    public JSONArrayAsyncTask(Activity a, String dialogtext, String array) {
        mActivity = a;
        mDialogtext = dialogtext;
        mArrayName = array;
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

    static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    @Override
    protected String[] doInBackground(String... afile) {
        String file = new String(afile[0]);
        File f = new File(file);
        Log.e("FILE:", "before if");
        Log.e("FILE:", "Filepath" + file);
        if (f.exists()) {
            BufferedReader input = null;
                try {
                    Log.e("FILE:", "Filepath" + file);
                    FileInputStream fIn = new FileInputStream(file);
                    input = new BufferedReader(new InputStreamReader(fIn));
                    String line;
                    Log.e("FILE:", "input");
                    StringBuffer content = new StringBuffer();
                    char[] buffer = new char[1024];
                    int num;
                    while ((num = input.read(buffer)) > 0) {
                        content.append(buffer, 0, num);
                    }
                    Log.e("ARRAY", "cts= " + content.toString());
                    JSONObject obj = new JSONObject(content.toString());
                    JSONArray array = null;
                    array = obj.getJSONArray(mArrayName);
                    String[] arr = {};
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("ARRAY", "i= " + i);
                        arr = append(arr, array.getString(i));
                    }
                    return arr;
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
    protected void onPostExecute(String[] result) {
        mProgressDialog.dismiss();
        JSONHandler.jsonarray = result;
    }

    public static void loadJSON(Activity activ, String text, String array, String file) {

        new JSONArrayAsyncTask(activ, text, array).execute(file);
    }
}