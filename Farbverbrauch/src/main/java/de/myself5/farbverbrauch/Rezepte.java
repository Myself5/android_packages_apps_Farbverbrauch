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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.concurrent.ExecutionException;


public class Rezepte extends Fragment {

    private OnFragmentInteractionListener mListener;

    private static AutoCompleteTextView mRezepturen;
    private Button mShow;
    static int mMaxFarbe = 5;
    private static TextView[] mFarbeTV = new TextView[mMaxFarbe];
    private static TextView[] mMengeTV = new TextView[mMaxFarbe];
    private static ImageButton[] mHelpIB = new ImageButton[2];
    private static boolean mViewLoaded = false;

    static String[] mFarbeS = new String[mMaxFarbe];
    static String[] mMengeS = new String[mMaxFarbe];
    private static String[] mAvailRezepte;
    private static Activity mActivity;
    private View rootView;
    static private boolean mUnlockButton;

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
        mViewLoaded = true;
        loadJson();
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
        mHelpIB[0] = (ImageButton) rootView.findViewById(R.id.rezepthelp);
        mHelpIB[1] = (ImageButton) rootView.findViewById(R.id.clickhelp);

        mHelpIB[0].setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        helpRezepteTV();
                    }
                });

        mHelpIB[1].setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        helpRezepteButton();
                    }
                });

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
        mViewLoaded = false;
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
    interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void hideSoftKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void show() throws JSONException, ExecutionException, InterruptedException {
        hideSoftKeyboard(rootView);
        for (int i = 0; i < mMaxFarbe; i++) {
            mFarbeS[i] = "0";
            mMengeS[i] = "0";
            mFarbeTV[i].setText("");
            mMengeTV[i].setText("");
        }
        String rezept = mRezepturen.getText().toString();
        new JSONValueAsyncTask(getActivity(), mAvailRezepte, rezept, getString(R.string.load_values)).execute("rezepte.json");
    }

    static void setTV() {
        if (!(mFarbeS[0] == null) && !(mMengeS[0] == null)) {
            if (!(mFarbeS[0].equals("0")) && !(mMengeS[0].equals("0"))) {
                for (int i = 0; i < mMaxFarbe; i++) {
                    if (!(mFarbeS[i] == null) && !(mMengeS[i] == null)) {
                        if (!(mFarbeS[i].equals("0")) && !(mMengeS[i].equals("0"))) {
                            mFarbeTV[i].setText(mFarbeS[i]);
                            mMengeTV[i].setText(mMengeS[i]);
                        }
                    }
                }
                return;
            }
        }
        MainActivity.showHelpDialog(mActivity.getString(R.string.invalid_entry));
    }

    static void showHelp() {
        if (mViewLoaded) {
            mUnlockButton = false;
            for (int i = 0; i < 2; i++) {
                mHelpIB[i].setVisibility(View.VISIBLE);
                mHelpIB[i].setClickable(true);
            }
        }
    }

    private void helpRezepteTV() {
        if (mAvailRezepte != null) {
            mRezepturen.setText(mAvailRezepte[0]);
            mUnlockButton = true;
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.fail_load), Toast.LENGTH_SHORT).show();
        }
    }

    private void helpRezepteButton() {
        if (mUnlockButton) {
            mShow.performClick();
            mShow.setPressed(true);
            mShow.invalidate();
            mShow.setPressed(false);
            mShow.invalidate();
        } else {
            Toast.makeText(mActivity, mActivity.getString(R.string.previous_step), Toast.LENGTH_SHORT).show();
        }
    }

    static void getArray(String... result) {
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

    static void loadJson() {
        if (mViewLoaded)
            MainActivity.loadJsonArray("Farbnamen", "rezepte.json");
    }
}