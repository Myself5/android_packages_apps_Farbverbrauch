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
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        mProgressDialog.setCancelable(true);
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

                    Log.e("ARRAY", "cts= " + stringBuilder.toString().trim());
                    JSONObject obj = new JSONObject(stringBuilder.toString().trim());
                    JSONArray array = obj.getJSONArray(mArrayName);
                    for (int i = 0; i < array.length(); i++) {
                        Log.e("ARRAY", "i= " + i);
                        mProgressDialog.setProgress((i / array.length()) * 100);
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