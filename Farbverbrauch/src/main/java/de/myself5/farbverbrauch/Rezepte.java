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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.File;
import java.util.concurrent.ExecutionException;


public class Rezepte extends Fragment {

    private OnFragmentInteractionListener mListener;

    static AutoCompleteTextView mRezepturen;
    Button mShow;
    static TextView mFarbe1;
    static TextView mFarbe2;
    static TextView mFarbe3;
    static TextView mFarbe4;
    static TextView mFarbe5;
    static TextView mMenge1;
    static TextView mMenge2;
    static TextView mMenge3;
    static TextView mMenge4;
    static TextView mMenge5;

    public static String Farbe_1;
    public static String Farbe_2;
    public static String Farbe_3;
    public static String Farbe_4;
    public static String Farbe_5;
    public static String Menge_1;
    public static String Menge_2;
    public static String Menge_3;
    public static String Menge_4;
    public static String Menge_5;
    static String[] mAvailRezepte;
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
        File f = new File(MainActivity.FILE_PATH + "rezepte.json");
        if (f.exists() && !f.isDirectory()) {
            new JSONArrayAsyncTask(getActivity(), "Farbnamen", getString(R.string.load_json)).execute("rezepte.json");
        } else {
            Toast.makeText(getActivity(), getString(R.string.noDL), Toast.LENGTH_SHORT).show();
        }

        mRezepturen = (AutoCompleteTextView) rootView.findViewById(R.id.rezept);
        mShow = (Button) rootView.findViewById(R.id.zeige);
        mFarbe1 = (TextView) rootView.findViewById(R.id.farbe1);
        mFarbe2 = (TextView) rootView.findViewById(R.id.farbe2);
        mFarbe3 = (TextView) rootView.findViewById(R.id.farbe3);
        mFarbe4 = (TextView) rootView.findViewById(R.id.farbe4);
        mFarbe5 = (TextView) rootView.findViewById(R.id.farbe5);
        mMenge1 = (TextView) rootView.findViewById(R.id.menge1);
        mMenge2 = (TextView) rootView.findViewById(R.id.menge2);
        mMenge3 = (TextView) rootView.findViewById(R.id.menge3);
        mMenge4 = (TextView) rootView.findViewById(R.id.menge4);
        mMenge5 = (TextView) rootView.findViewById(R.id.menge5);

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
        Farbe_1 = "0";
        Farbe_2 = "0";
        Farbe_3 = "0";
        Farbe_4 = "0";
        Farbe_5 = "0";
        Menge_1 = "0";
        Menge_2 = "0";
        Menge_3 = "0";
        Menge_4 = "0";
        Menge_5 = "0";
        mFarbe1.setText("");
        mFarbe2.setText("");
        mFarbe3.setText("");
        mFarbe4.setText("");
        mFarbe5.setText("");
        mMenge1.setText("");
        mMenge2.setText("");
        mMenge3.setText("");
        mMenge4.setText("");
        mMenge5.setText("");
        String rezept = mRezepturen.getText().toString();
        new JSONValueAsyncTask(getActivity(), mAvailRezepte, rezept, getString(R.string.load_values)).execute("rezepte.json");
    }

    public static void setTV() {

        if (!(Farbe_1 == null) && !(Menge_1 == null)) {
            if (!(Farbe_1.equals("0")) && !(Menge_1.equals("0"))) {
                mFarbe1.setText(Farbe_1);
                mMenge1.setText(Menge_1);
            }
        }

        if (!(Farbe_2 == null) && !(Menge_2 == null)) {
            if (!(Farbe_2.equals("0")) && !(Menge_2.equals("0"))) {
                mFarbe2.setText(Farbe_2);
                mMenge2.setText(Menge_2);
            }
        }

        if (!(Farbe_3 == null) && !(Menge_3 == null)) {
            if (!(Farbe_3.equals("0")) && !(Menge_3.equals("0"))) {
                mFarbe3.setText(Farbe_3);
                mMenge3.setText(Menge_3);
            }
        }

        if (!(Farbe_4 == null) && !(Menge_4 == null)) {
            if (!(Farbe_4.equals("0")) && !(Menge_4.equals("0"))) {
                mFarbe4.setText(Farbe_4);
                mMenge4.setText(Menge_4);
            }
        }

        if (!(Farbe_5 == null) && !(Menge_5 == null)) {
            if (!(Farbe_5.equals("0")) && !(Menge_5.equals("0"))) {
                mFarbe5.setText(Farbe_5);
                mMenge5.setText(Menge_5);
            }
        }
    }

    public static void getArray(String... result) {
        mAvailRezepte = result;
        if (mAvailRezepte != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(mActivity,
                    android.R.layout.simple_dropdown_item_1line, mAvailRezepte);

            mRezepturen.setAdapter(adapter);
            mRezepturen.setThreshold(1);
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
        }
    }
}