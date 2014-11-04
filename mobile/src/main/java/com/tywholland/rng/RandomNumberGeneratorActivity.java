package com.tywholland.rng;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;


public class RandomNumberGeneratorActivity extends Activity {
    private static final String PREFS_NAME = "randomnumbergenerator";
    private static final String LOWER_BOUND = "lowerbound";
    private static final String UPPER_BOUND = "upperbound";
    private EditText mLowerBound;
    private EditText mUpperBound;
    private TextView mDisplay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_number_generator);
        // Set lower bound view variable and focus listener
        mLowerBound = (EditText) findViewById(R.id.lowerbound_edittext);
        mLowerBound.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Clear the text when focused, hide keyboard when not focused
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputStatus = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    mLowerBound.setText("", TextView.BufferType.EDITABLE);
                    inputStatus.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
                else
                {
                    inputStatus.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        // Set the upper bound view variable and focus listener
        mUpperBound = (EditText) findViewById(R.id.upperbound_edittext);
        mUpperBound.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            // Clear the text when focused, hide keyboard when not focused
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputStatus = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (hasFocus) {
                    mUpperBound.setText("", TextView.BufferType.EDITABLE);
                    inputStatus.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
                }
                else
                {
                    inputStatus.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        });
        // Additionally, make the DONE button on the soft keyboard generate a
        // number
        mUpperBound.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    v.clearFocus();
                    displayRandomNumber();
                    return true;
                }
                return false;
            }
        });
        // Set display textview variable
        mDisplay = (TextView) findViewById(R.id.rng_display);
        // Add onclick listener to button to make it display a new random
        // number, and clear focus of bound edittexts
        final Button button = (Button) findViewById(R.id.generate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLowerBound.clearFocus();
                mUpperBound.clearFocus();
                displayRandomNumber();
            }
        });
    }

    public void displayRandomNumber() {
        Random rng = new Random();
        String upperText = mUpperBound.getText().toString();
        String lowerText = mLowerBound.getText().toString();
        // Make sure there are values in the edit text boxes
        if (upperText.length() > 0 && lowerText.length() > 0) {
            int upper = Integer.parseInt(upperText);
            int lower = Integer.parseInt(lowerText);
            if (lower <= upper) {
                // Valid input, display a new random number
                int result = rng.nextInt(upper - lower + 1) + lower;
                mDisplay.setText(String.valueOf(result));
            } else {
                Toast toast = Toast.makeText(this,
                        "Lower Bound is higher than Upper Bound",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Toast toast = Toast.makeText(this, "A text field is empty",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restore old values
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        mLowerBound.setText(prefs.getString(LOWER_BOUND, "1"));
        mUpperBound.setText(prefs.getString(UPPER_BOUND, "6"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Save current lower and upper bound values
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LOWER_BOUND, mLowerBound.getText().toString());
        editor.putString(UPPER_BOUND, mUpperBound.getText().toString());
        editor.commit();
    }
}
