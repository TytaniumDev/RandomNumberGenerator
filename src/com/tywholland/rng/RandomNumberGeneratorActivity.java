package com.tywholland.rng;

import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
		setContentView(R.layout.main);
		mLowerBound = (EditText) findViewById(R.id.lowerbound_edittext);
		mLowerBound.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mLowerBound.setText("", TextView.BufferType.EDITABLE);
				}
			}

		});
		mUpperBound = (EditText) findViewById(R.id.upperbound_edittext);
		mUpperBound.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					mUpperBound.setText("", TextView.BufferType.EDITABLE);
				}
			}

		});
		mDisplay = (TextView) findViewById(R.id.rng_display);
		Button button = (Button) findViewById(R.id.generate_button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayRandomNumber();
			}
		});
	}

	public void displayRandomNumber() {
		Random rng = new Random();
		String upperText = mUpperBound.getText().toString();
		String lowerText = mLowerBound.getText().toString();
		if (upperText.length() > 0 && lowerText.length() > 0) {
			int upper = Integer.parseInt(upperText);
			int lower = Integer.parseInt(lowerText);
			if (lower <= upper) {
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
		mLowerBound.setText(prefs.getString(LOWER_BOUND, "0"));
		mUpperBound.setText(prefs.getString(UPPER_BOUND, "6"));
	}

	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferences prefs = getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(LOWER_BOUND, mLowerBound.getText().toString());
		editor.putString(UPPER_BOUND, mUpperBound.getText().toString());
		editor.commit();
	}
}