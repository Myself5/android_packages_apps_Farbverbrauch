package de.myself5.farbrechner;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    AutoCompleteTextView mGewebe;
    EditText mAnzDrucke;
    EditText mDruckklaengeCM;
    EditText mDruckbreiteCM;
    EditText mBedruckungsgrad;
    TextView mDruckflaeche;
    TextView mFarbmengeCM3;
    TextView mFarbmengeL;
    Button mCalc;

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

        View rootView = inflater.inflate(R.layout.fragment_farbverbrauch, container, false);
        String[] GEWEBE = null;
        File f = new File(MainActivity.FILE_PATH + "farbverbrauch.json");
        if (f.exists() && !f.isDirectory()) {
            try {
                GEWEBE = new JSONArrayAsyncTask(getActivity(), "GEWEBE").execute(MainActivity.FILE_PATH + "farbverbrauch.json").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (GEWEBE != null) {
                mGewebe = (AutoCompleteTextView) rootView.findViewById(R.id.gewebe);
                ArrayAdapter<String> adapter = null;

                adapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line, GEWEBE);

                mGewebe.setAdapter(adapter);
                mGewebe.setThreshold(1);
            } else {
                Toast.makeText(getActivity(), getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.noDL), Toast.LENGTH_SHORT).show();
        }
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        public void onFragmentInteraction(Uri uri);
    }

    public void calculate() throws JSONException, ExecutionException, InterruptedException {
        String gewebe = mGewebe.getText().toString();
        float cm3M2 = Float.parseFloat(new JSONGewebeValueAsyncTask(getActivity(), gewebe).execute(MainActivity.FILE_PATH + "farbverbrauch.json").get());
        int anzDrucke = Integer.parseInt(mAnzDrucke.getText().toString());
        int drucklaenge = Integer.parseInt(mDruckklaengeCM.getText().toString());
        int druckbreite = Integer.parseInt(mDruckbreiteCM.getText().toString());
        int bedruckungsgrad = Integer.parseInt(mBedruckungsgrad.getText().toString());
        mDruckflaeche.setText(getString(R.string.druckflaecheM2) + ": " + ((float) anzDrucke * drucklaenge * druckbreite / 10000));
        mFarbmengeCM3.setText(getString(R.string.farbmengeCM3) + ": " + (cm3M2 * anzDrucke * drucklaenge * druckbreite * bedruckungsgrad / 1000000));
        mFarbmengeL.setText(getString(R.string.farbmengeL) + ": " + (cm3M2 * anzDrucke * drucklaenge * druckbreite * bedruckungsgrad / 1000000000));
    }
}
