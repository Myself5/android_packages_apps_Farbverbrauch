package de.myself5.farbrechner;

import android.app.Activity;
import android.util.Log;

import org.json.JSONException;

/**
 * Created by Myself5 on 2/06/2015.
 */

public class JSONHandler {

    public static String[] jsonarray;
    public static String jsonvalue;

    public static String[] getSelectionArray(Activity a, String array, String file) {
        //this fails sometimes, so do it until it did not fail.
        JSONArrayAsyncTask.loadJSON(a, a.getString(R.string.load_json), array, file);
        return jsonarray;
    }

    public static String getGewebeArrayValue(Activity a, String value, String file) throws JSONException {
        //this fails sometimes, so do it until it did not fail.
        JSONGewebeValueAsyncTask.loadJSON(a, a.getString(R.string.load_values), value, file);
        return jsonvalue;
    }
}
