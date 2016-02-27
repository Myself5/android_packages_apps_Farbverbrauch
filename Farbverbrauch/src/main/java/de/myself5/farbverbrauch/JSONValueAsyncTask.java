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
import java.util.List;

public class JSONValueAsyncTask extends AsyncTask<String, String, String> {
    private Activity mActivity;
    private String[] mArray;
    private String mValue;
    private String mDialogtext;
    private ProgressDialog mProgressDialog;
    private String mFile;

    public JSONValueAsyncTask(Activity a, String[] array, String value, String dialogtext) {
        mActivity = a;
        mValue = value;
        mArray = array;
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
            try {
                FileReader fileReader = new FileReader(mFilePath);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                    stringBuilder.append(line).append("\n");
                bufferedReader.close();
                fileReader.close();

                List<String> list = Arrays.asList(mArray);
                int index = list.indexOf(mValue);

                JSONArray array = new JSONArray(stringBuilder.toString().trim());

                if (mFile.equals("rezepte.json")) {
                    Rezepte.Menge_1 = array.getJSONObject(index).getString("Menge_1");
                    Rezepte.Menge_2 = array.getJSONObject(index).getString("Menge_2");
                    Rezepte.Menge_3 = array.getJSONObject(index).getString("Menge_3");
                    Rezepte.Menge_4 = array.getJSONObject(index).getString("Menge_4");
                    Rezepte.Menge_5 = array.getJSONObject(index).getString("Menge_5");
                    Rezepte.Farbe_1 = array.getJSONObject(index).getString("Farbe_1");
                    Rezepte.Farbe_2 = array.getJSONObject(index).getString("Farbe_2");
                    Rezepte.Farbe_3 = array.getJSONObject(index).getString("Farbe_3");
                    Rezepte.Farbe_4 = array.getJSONObject(index).getString("Farbe_4");
                    Rezepte.Farbe_5 = array.getJSONObject(index).getString("Farbe_5");
                    return "done";
                } else if (mFile.equals("farbverbrauch.json")) {
                    return array.getJSONObject(index).getString("Wert");
                }
                return "";
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
    protected void onPostExecute(String result) {
        switch (mFile) {
            case "rezepte.json":
                Rezepte.setTV();
                break;
            case "farbverbrauch.json":
                Farbverbrauch.setTV(result);
                break;
        }
        mProgressDialog.dismiss();
    }
}