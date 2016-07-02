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
    public static int mMaxFarbe = 5;
    static TextView[] mFarbeTV = new TextView[mMaxFarbe];
    static TextView[] mMengeTV = new TextView[mMaxFarbe];

    public static String[] mFarbeS = new String[mMaxFarbe];
    public static String[] mMengeS = new String[mMaxFarbe];
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
        mFarbeTV[0] = (TextView) rootView.findViewById(R.id.farbe1);
        mFarbeTV[1] = (TextView) rootView.findViewById(R.id.farbe2);
        mFarbeTV[2] = (TextView) rootView.findViewById(R.id.farbe3);
        mFarbeTV[3] = (TextView) rootView.findViewById(R.id.farbe4);
        mFarbeTV[4] = (TextView) rootView.findViewById(R.id.farbe5);
        mMengeTV[0] = (TextView) rootView.findViewById(R.id.menge1);
        mMengeTV[1] = (TextView) rootView.findViewById(R.id.menge2);
        mMengeTV[2] = (TextView) rootView.findViewById(R.id.menge3);
        mMengeTV[3] = (TextView) rootView.findViewById(R.id.menge4);
        mMengeTV[4] = (TextView) rootView.findViewById(R.id.menge5);

        mShow.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        try {
                            show();
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
        public void onFragmentInteraction(Uri uri);
    }

    public void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void show() throws JSONException, ExecutionException, InterruptedException {
        hideSoftKeyboard(rootView);
        for(int i = 0; i < mMaxFarbe; i++)
        {
            mFarbeS[i] = "0";
            mMengeS[i] = "0";
            mFarbeTV[i].setText("");
            mMengeTV[i].setText("");
        }
        String rezept = mRezepturen.getText().toString();
        new JSONValueAsyncTask(getActivity(), mAvailRezepte, rezept, getString(R.string.load_values)).execute("rezepte.json");
    }

    public static void setTV() {

        for(int i = 0; i < mMaxFarbe; i++) {
            if (!(mFarbeS[i] == null) && !(mMengeS[i] == null)) {
                if (!(mFarbeS[i].equals("0")) && !(mFarbeS[i].equals("0"))) {
                    mFarbeTV[i].setText(mFarbeS[i]);
                    mMengeTV[i].setText(mMengeS[i]);
                }
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