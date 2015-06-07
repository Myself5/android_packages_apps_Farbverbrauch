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


public class T_Rezepte extends Fragment {

    private OnFragmentInteractionListener mListener;

    static AutoCompleteTextView tmRezepturen;
    Button tmShow;
    static TextView tmTBLH;
    static TextView tmFarbe1;
    static TextView tmFarbe2;
    static TextView tmFarbe3;
    static TextView tmFarbe4;
    static TextView tmFarbe5;
    static String tFarbe;
    static String tMenge;

    static String tFarbe_1;
    static String tFarbe_2;
    static String tFarbe_3;
    static String tFarbe_4;
    static String tFarbe_5;
    static String tMenge_1;
    static String tMenge_2;
    static String tMenge_3;
    static String tMenge_4;
    static String tMenge_5;
    static String[] tREZEPTE;
    static Activity tmActivity;
    View trootView;

    public T_Rezepte() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tmActivity = getActivity();
        trootView = inflater.inflate(R.layout.fragment_t_rezepte, container, false);
        tFarbe = getString(R.string.farbe);
        tMenge = getString(R.string.menge);
        File f = new File(MainActivity.FILE_PATH + "t_rezepte.json");
        if (f.exists() && !f.isDirectory()) {
            new JSONArrayAsyncTask(getActivity(), "REZEPTE", getString(R.string.load_json)).execute("t_rezepte.json");
        } else {
            Toast.makeText(getActivity(), getString(R.string.noDL), Toast.LENGTH_SHORT).show();
        }

        tmRezepturen = (AutoCompleteTextView) trootView.findViewById(R.id.t_rezept);
        tmShow = (Button) trootView.findViewById(R.id.t_zeige);
        tmTBLH = (TextView) trootView.findViewById(R.id.t_tabellen_header);
        tmFarbe1 = (TextView) trootView.findViewById(R.id.t_farbe1);
        tmFarbe2 = (TextView) trootView.findViewById(R.id.t_farbe2);
        tmFarbe3 = (TextView) trootView.findViewById(R.id.t_farbe3);
        tmFarbe4 = (TextView) trootView.findViewById(R.id.t_farbe4);
        tmFarbe5 = (TextView) trootView.findViewById(R.id.t_farbe5);

        tmShow.setOnClickListener(
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
        return trootView;
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
        hideSoftKeyboard(trootView);
        tFarbe_1 = "0";
        tFarbe_2 = "0";
        tFarbe_3 = "0";
        tFarbe_4 = "0";
        tFarbe_5 = "0";
        tMenge_1 = "0";
        tMenge_2 = "0";
        tMenge_3 = "0";
        tMenge_4 = "0";
        tMenge_5 = "0";
        tmTBLH.setText("");
        tmFarbe1.setText("");
        tmFarbe2.setText("");
        tmFarbe3.setText("");
        tmFarbe4.setText("");
        tmFarbe5.setText("");
        String rezept = tmRezepturen.getText().toString();
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_1", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_2", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_3", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_4", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Farbe_5", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_1", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_2", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_3", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_4", getString(R.string.load_values)).execute("t_rezepte.json");
        new JSONRezepteValueAsyncTask(getActivity(), rezept, "Menge_5", getString(R.string.load_values)).execute("t_rezepte.json");
    }

    public static void setTV(String value, String result) {
        switch (value) {
            case "Farbe_1":
                tFarbe_1 = result;
                break;
            case "Farbe_2":
                tFarbe_2 = result;
                break;
            case "Farbe_3":
                tFarbe_3 = result;
                break;
            case "Farbe_4":
                tFarbe_4 = result;
                break;
            case "Farbe_5":
                tFarbe_5 = result;
                break;
            case "Menge_1":
                tMenge_1 = result;
                break;
            case "Menge_2":
                tMenge_2 = result;
                break;
            case "Menge_3":
                tMenge_3 = result;
                break;
            case "Menge_4":
                tMenge_4 = result;
                break;
            case "Menge_5":
                tMenge_5 = result;
                break;
        }

        tmTBLH.setText(tFarbe + "   " + tMenge);
        if (!(tFarbe_1 == null) && !(tMenge_1 == null)) {
            if (!(tFarbe_1.equals("0")) && !(tMenge_1.equals("0"))) {
                tmFarbe1.setText(tFarbe_1 + "   " + tMenge_1);
            }
        }

        if (!(tFarbe_2 == null) && !(tMenge_2 == null)) {
            if (!(tFarbe_2.equals("0")) && !(tMenge_2.equals("0"))) {
                tmFarbe2.setText(tFarbe_2 + "   " + tMenge_2);
            }
        }

        if (!(tFarbe_3 == null) && !(tMenge_3 == null)) {
            if (!(tFarbe_3.equals("0")) && !(tMenge_3.equals("0"))) {
                tmFarbe3.setText(tFarbe_3 + "   " + tMenge_3);
            }
        }

        if (!(tFarbe_4 == null) && !(tMenge_4 == null)) {
            if (!(tFarbe_4.equals("0")) && !(tMenge_4.equals("0"))) {
                tmFarbe4.setText(tFarbe_4 + "   " + tMenge_4);
            }
        }

        if (!(tFarbe_5 == null) && !(tMenge_5 == null)) {
            if (!(tFarbe_5.equals("0")) && !(tMenge_5.equals("0"))) {
                tmFarbe5.setText(tFarbe_5 + "   " + tMenge_5);
            }
        }
    }

    public static void getArray(String... result){
        tREZEPTE = result;
        if (tREZEPTE != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(tmActivity,
                    android.R.layout.simple_dropdown_item_1line, tREZEPTE);

            tmRezepturen.setAdapter(adapter);
            tmRezepturen.setThreshold(1);
        } else {
            Toast.makeText(tmActivity, tmActivity.getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
        }
    }
}