package de.myself5.farbverbrauch;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class Farbverbrauch extends Fragment {

    private OnFragmentInteractionListener mListener;

    static AutoCompleteTextView mGewebe;
    EditText mAnzDrucke;
    EditText mDruckklaengeCM;
    EditText mDruckbreiteCM;
    EditText mBedruckungsgrad;
    static TextView mDruckflaeche;
    static TextView mFarbmengeCM3;
    static TextView mFarbmengeL;
    Button mCalc;
    static int anzDrucke;
    static int drucklaenge;
    static int druckbreite;
    static int bedruckungsgrad;
    static float cm3M2;
    static String[] mAvailGewebe;
    static Activity mActivity;
    View rootView;

    public Farbverbrauch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        mActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_farbverbrauch, container, false);
        File f = new File(MainActivity.FILE_PATH + "farbverbrauch.json");
        if (f.exists() && !f.isDirectory()) {
            new JSONArrayAsyncTask(getActivity(), "Gewebe", getString(R.string.load_json)).execute("farbverbrauch.json");
        } else {
            Toast.makeText(getActivity(), getString(R.string.noDL), Toast.LENGTH_SHORT).show();
        }
        mGewebe = (AutoCompleteTextView) rootView.findViewById(R.id.gewebe);
        mAnzDrucke = (EditText) rootView.findViewById(R.id.anzDrucke);
        mDruckklaengeCM = (EditText) rootView.findViewById(R.id.drucklaengeCM);
        mDruckbreiteCM = (EditText) rootView.findViewById(R.id.druckbreiteCM);
        mBedruckungsgrad = (EditText) rootView.findViewById(R.id.bedruckungsgrad);
        mDruckflaeche = (TextView) rootView.findViewById(R.id.druckflaecheM2);
        mFarbmengeCM3 = (TextView) rootView.findViewById(R.id.farbmengeCM3);
        mFarbmengeL = (TextView) rootView.findViewById(R.id.farbmengeL);
        mCalc = (Button) rootView.findViewById(R.id.berechnen);

        mCalc.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            calculate();
                        } catch (JSONException | InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void calculate() throws JSONException, ExecutionException, InterruptedException {
        String gewebe = mGewebe.getText().toString();
        View view = mActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        new JSONValueAsyncTask(getActivity(), mAvailGewebe, gewebe, getString(R.string.load_values)).execute("farbverbrauch.json");
        anzDrucke = Integer.parseInt(mAnzDrucke.getText().toString());
        drucklaenge = Integer.parseInt(mDruckklaengeCM.getText().toString());
        druckbreite = Integer.parseInt(mDruckbreiteCM.getText().toString());
        bedruckungsgrad = Integer.parseInt(mBedruckungsgrad.getText().toString());
    }

    public static void setTV(String result) {
        cm3M2 = Float.parseFloat(result);
        mDruckflaeche.setText(Float.toString((float) anzDrucke * drucklaenge * druckbreite / 10000));
        mFarbmengeCM3.setText(Float.toString(cm3M2 * (float) anzDrucke * drucklaenge * druckbreite * bedruckungsgrad / 1000000));
        mFarbmengeL.setText(Float.toString(cm3M2 * (float) anzDrucke * drucklaenge * druckbreite * bedruckungsgrad / 1000000000));
    }

    public static void getArray(String... result) {
        mAvailGewebe = result;
        if (mAvailGewebe != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mAvailGewebe);

            mGewebe.setAdapter(adapter);
            mGewebe.setThreshold(1);
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
        }
    }
}
