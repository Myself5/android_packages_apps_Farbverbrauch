package de.myself5.farbverbrauch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class JSONArrayAsyncTask extends AsyncTask<String, String, String[]> {
    private Activity mActivity;
    private String mArrayName;
    private String mDialogtext;
    private ProgressDialog mProgressDialog;
    private String mFile;

    public JSONArrayAsyncTask(Activity a, String array, String dialogtext) {
        mActivity = a;
        mArrayName = array;
        mDialogtext = dialogtext;
    }

    @Override
    public void onPreExecute() {
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

                JSONArray array = new JSONArray(stringBuilder.toString().trim());

                // Process each result in json array, decode and convert to business object
                for (int i=0; i < array.length(); i++) {
                    try {
                        arr = append(arr, array.getJSONObject(i).getString(mArrayName));
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
                return arr;

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
        mProgressDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String... result) {
        switch (mFile) {
            case "farbverbrauch.json":
                Farbverbrauch.getArray(result);
                break;
            case "rezepte.json":
                Rezepte.getArray(result);
                break;
        }
        mProgressDialog.dismiss();
    }
}
