package com.filippoints.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import com.filippoints.R;
import com.filippoints.controller.Controller;
import com.filippoints.model.AwardedPoints;
import com.filippoints.util.Util;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePointsActivity extends AppCompatActivity {

    private static final int MAX_VALUE = 3500;
    private static final int INIT_NUMBER_PICKER_VALUE = 50;
    private List<Integer> fixedPoints = Arrays.asList(10, 20, 50);

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
        Call<List<AwardedPoints>> pointsCall = Controller.getWebAPI(this)
                .getPoints(fixedButtonsIds.size() + popularButtonsIds.size());
        pointsCall.enqueue(new Callback<List<AwardedPoints>>() {
            @Override
            public void onResponse(Call<List<AwardedPoints>> call,
                                   Response<List<AwardedPoints>> response) {
                if (response.isSuccessful()) {
                    List<AwardedPoints> awardedPointsList = response.body();
                    int c=0;
                    for (int buttonId : popularButtonsIds) {
                        Button button = findViewById(buttonId);
                        int awardedPoints = awardedPointsList.get(c++).getAwarded_points();
                        while (fixedPoints.contains(awardedPoints)) {
                            awardedPoints = awardedPointsList.get(c++
                            ).getAwarded_points();
                        }
                        button.setText(Long.toString(awardedPoints));
                        button.setOnClickListener(listener);
                    }
                } else {
                    onNoConnection();
                }
            }

            @Override
            public void onFailure(Call<List<AwardedPoints>> call, Throwable t) {
                onNoConnection();
            }

            private void onNoConnection() {
                setValuesToButtons(popularButtonsIds, fixedPoints);
                Util.displayCheckConnectionToast(ChoosePointsActivity.this);
            }
        });
    }


    public static Intent buildIntent(Context context) {
        return new Intent(context, ChoosePointsActivity.class);
    }
}
