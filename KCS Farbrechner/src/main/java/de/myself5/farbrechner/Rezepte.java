package de.myself5.farbrechner;

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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class Rezepte extends Fragment {

    private OnFragmentInteractionListener mListener;

    static AutoCompleteTextView mRezepturen;
    Button mShow;
    static TextView mTBLH;
    static TextView mFarbe1;
    static TextView mFarbe2;
    static TextView mFarbe3;
    static TextView mFarbe4;
    static TextView mFarbe5;
    static String Farbe;
    static String Menge;

    static String Farbe_1;
    static String Farbe_2;
    static String Farbe_3;
    static String Farbe_4;
    static String Farbe_5;
    static String Menge_1;
    static String Menge_2;
    static String Menge_3;
    static String Menge_4;
    static String Menge_5;
    static String[] REZEPTE;
    static Activity mActivity;
    View rootView;

    public Rezepte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mActivity = getActivity();
        rootView = inflater.inflate(R.layout.fragment_rezepte, container, false);
        Farbe = getString(R.string.farbe);
        Menge = getString(R.string.menge);
        File f = new File(MainActivity.FILE_PATH + "rezepte.json");
        if (f.exists() && !f.isDirectory()) {
            new JSONArrayAsyncTask(getActivity(), "REZEPTE", getString(R.string.load_json)).execute(MainActivity.FILE_PATH + "rezepte.json");
        } else {
            Toast.makeText(getActivity(), getString(R.string.noDL), Toast.LENGTH_SHORT).show();
        }

        mRezepturen = (AutoCompleteTextView) rootView.findViewById(R.id.rezept);
        mShow = (Button) rootView.findViewById(R.id.zeige);
        mTBLH = (TextView) rootView.findViewById(R.id.tabellen_header);
        mFarbe1 = (TextView) rootView.findViewById(R.id.farbe1);
        mFarbe2 = (TextView) rootView.findViewById(R.id.farbe2);
        mFarbe3 = (TextView) rootView.findViewById(R.id.farbe3);
        mFarbe4 = (TextView) rootView.findViewById(R.id.farbe4);
        mFarbe5 = (TextView) rootView.findViewById(R.id.farbe5);

        mShow.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            show();
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

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void show() throws JSONException, ExecutionException, InterruptedException {
        hideSoftKeyboard(rootView);
        String rezept = mRezepturen.getText().toString();
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_1", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_2", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_3", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_4", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_5", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_1", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_2", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_3", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_4", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_5", getString(R.string.load_values)).execute(MainActivity.FILE_PATH + "rezepte.json");
    }

    public static void setTV(String value, String result) {
        switch (value) {
            case "Farbe_1":
                Farbe_1 = result;
                break;
            case "Farbe_2":
                Farbe_2 = result;
                break;
            case "Farbe_3":
                Farbe_3 = result;
                break;
            case "Farbe_4":
                Farbe_4 = result;
                break;
            case "Farbe_5":
                Farbe_5 = result;
                break;
            case "Menge_1":
                Menge_1 = result;
                break;
            case "Menge_2":
                Menge_2 = result;
                break;
            case "Menge_3":
                Menge_3 = result;
                break;
            case "Menge_4":
                Menge_4 = result;
                break;
            case "Menge_5":
                Menge_5 = result;
                break;
        }

        mTBLH.setText(Farbe + "   " + Menge);
        if (!(Farbe_1 == null) && !(Menge_1 == null)) {
            if (!(Farbe_1.equals("0")) && !(Menge_1.equals("0"))) {
                mFarbe1.setText(Farbe_1 + "   " + Menge_1);
            }
        }
        if (!(Farbe_2 == null) && !(Menge_2 == null)) {
            if (!(Farbe_2.equals("0")) && !(Menge_2.equals("0"))) {
                mFarbe2.setText(Farbe_2 + "   " + Menge_2);
            }
        }
        if (!(Farbe_3 == null) && !(Menge_3 == null)) {
            if (!(Farbe_3.equals("0")) && !(Menge_3.equals("0"))) {
                mFarbe3.setText(Farbe_3 + "   " + Menge_3);
            }
        }
        if (!(Farbe_4 == null) && !(Menge_4 == null)) {
            if (!(Farbe_4.equals("0")) && !(Menge_4.equals("0"))) {
                mFarbe4.setText(Farbe_4 + "   " + Menge_4);
            }
        }
        if (!(Farbe_5 == null) && !(Menge_5 == null)) {
            if (!(Farbe_5.equals("0")) && !(Menge_5.equals("0"))) {
                mFarbe5.setText(Farbe_5 + "   " + Menge_5);
            }
        }
    }

    public static void getArray(String... result){
        REZEPTE = result;
        if (REZEPTE != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, REZEPTE);

            mRezepturen.setAdapter(adapter);
            mRezepturen.setThreshold(1);
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
        }
    }
}