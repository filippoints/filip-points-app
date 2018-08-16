package com.filippoints.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.filippoints.R;
import com.filippoints.controller.Controller;
import com.filippoints.model.AwardedPoints;
import com.filippoints.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class ChoosePointsActivity extends AppCompatActivity {

    private static final int MAX_VALUE = 3500;
    private static final int INIT_NUMBER_PICKER_VALUE = 50;
    private static final String POINTS_PREF_NAME = "points_prefs";
    private static final String POINTS_KEY = "points";
    private static final String LOG_TAG = "ChoosePointsActivity";
    private List<Integer> fixedPoints = Arrays.asList(10, 20, 50);
    private List<Integer> DEFAULT_MOST_OFTEN_POINTS = Arrays.asList(1, 5, 10, 20, 25, 50);

    private List<Integer> fixedButtonsIds = Arrays.asList(
            R.id.button1
            ,R.id.button2
            ,R.id.button3
    );

    private List<Integer> popularButtonsIds = Arrays.asList(
            R.id.button4
            ,R.id.button5
            ,R.id.button6
    );

    private Button buttonSubmitCustom;

    private View.OnClickListener listener = view -> {
        int points = Integer.parseInt(((Button)view).getText().toString());
        Intent intent = ChoosePersonActivity.buildIntent(ChoosePointsActivity.this, points);
        startActivity(intent);
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_points);

        setTextToButtons();
        initNumberPicker();

        buttonSubmitCustom = findViewById(R.id.button_submit_custom);
        buttonSubmitCustom.setOnClickListener(listener);
        updateSubmitCustomValue(INIT_NUMBER_PICKER_VALUE);
    }

    private void updateSubmitCustomValue(int value) {
        buttonSubmitCustom.setText(Integer.toString(value));
    }

    private void initNumberPicker() {
        NumberPicker numberPicker = findViewById(R.id.number_picker);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(MAX_VALUE);
        numberPicker.setClickable(true);
        numberPicker.setValue(INIT_NUMBER_PICKER_VALUE);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                updateSubmitCustomValue(i1);
            }
        });
    }

    private void setValuesToButtons(List<Integer> buttonIds, List<Integer> pointsList) {
        for (int i = 0; i < buttonIds.size(); i++) {
            Button button = findViewById(buttonIds.get(i));
            button.setText(Integer.toString(pointsList.get(i)));
            button.setOnClickListener(listener);
        }
    }

    private void setTextToButtons() {
        setValuesToButtons(fixedButtonsIds, fixedPoints);
        updatePointsInSharedPreferences();

        SharedPreferences prefs = getSharedPreferences(POINTS_PREF_NAME, MODE_PRIVATE);
        Set<String> pointsStrSet = prefs.getStringSet(POINTS_KEY, Util.intListToStrSet(
                DEFAULT_MOST_OFTEN_POINTS
        ));
        List<Integer> points = Util.stringSetToIntList(pointsStrSet);

        int c=0;
        for (int buttonId : popularButtonsIds) {
            Button button = findViewById(buttonId);
            int awardedPoints = points.get(c++);
            while (fixedPoints.contains(awardedPoints)) {
                awardedPoints = points.get(c++);
            }
            button.setText(Long.toString(awardedPoints));
            button.setOnClickListener(listener);
        }
    }

    private void updatePointsInSharedPreferences() {
        Call<List<AwardedPoints>> pointsCall = Controller.getWebAPI(this)
                .getPoints(fixedButtonsIds.size() + popularButtonsIds.size());
        pointsCall.enqueue(new Callback<List<AwardedPoints>>() {
            @Override
            public void onResponse(Call<List<AwardedPoints>> call,
                                   Response<List<AwardedPoints>> response) {
                if (response.isSuccessful()) {
                    List<AwardedPoints> awardedPointsList = response.body();
                    //TODO clean this up
                    List<Integer> pointsList = new ArrayList<>();
                    for (AwardedPoints ap: awardedPointsList) {
                        pointsList.add(ap.getAwarded_points());
                    }
                    SharedPreferences.Editor prefsEditor = getSharedPreferences(POINTS_PREF_NAME, MODE_PRIVATE).edit();
                    prefsEditor.putStringSet(POINTS_KEY, Util.intListToStrSet(pointsList));
                    Log.d(LOG_TAG, String.format("Saving the following points into shared preferences: %s",
                                    pointsList));
                    prefsEditor.apply();
                }
            }

            @Override
            public void onFailure(Call<List<AwardedPoints>> call, Throwable t) {
                /* Do nothing */
            }
        });
    }


    public static Intent buildIntent(Context context) {
        return new Intent(context, ChoosePointsActivity.class);
    }
}
