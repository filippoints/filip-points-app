package com.filippoints.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.filippoints.R;
import com.filippoints.controller.Controller;
import com.filippoints.model.AssignedPoints;
import com.filippoints.util.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hlib on 6/19/18.
 */

public class PointsReasonActivity extends AppCompatActivity {
    private static final String PERSON_STRING = "person";
    private static final String POINTS_STRING = "points";

    private int person_id;
    private int points;
    private EditText reasonEditText;

    public static Intent buildIntent(Context context, int personId, int points) {
        Intent intent = new Intent(context, PointsReasonActivity.class);
        intent.putExtra(PERSON_STRING, personId);
        intent.putExtra(POINTS_STRING, points);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_points_reason);

        points = getIntent().getIntExtra(POINTS_STRING, -1);
        person_id = getIntent().getIntExtra(PERSON_STRING, -1);
        if (points == -1 || person_id == -1) {
            throw new RuntimeException();
        }

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reasonEditText = PointsReasonActivity.this.findViewById(R.id.points_reason_edit_text);
                String points_reason = reasonEditText.getText().toString();
                if ("".equals(points_reason.trim())) {
                    showNoReasonError();
                } else {
                    submit_points_to_backend(PointsReasonActivity.this.person_id, PointsReasonActivity.this.points, points_reason, new PointsAssignmentCallback() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(PointsReasonActivity.this,
                                    R.string.points_submitted_success, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(PointsReasonActivity.this,
                                    R.string.points_submitted_fail, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void always() {
                            Intent intent = ChoosePointsActivity.buildIntent(PointsReasonActivity.this);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showNoReasonError() {
        hideKeyboard(this);
        Toast.makeText(PointsReasonActivity.this, R.string.reason_is_empty, Toast.LENGTH_LONG)
                .show();
    }

    private interface PointsAssignmentCallback {
        void onSuccess();
        void onFail();
        void always();
    }

    private void submit_points_to_backend(int person_id, int points, String points_reason, PointsAssignmentCallback callback) {
        AssignedPoints assignedPoints = new AssignedPoints(points, points_reason, person_id, System.currentTimeMillis());
        Call<Void> call = Controller.getWebAPI(this).submitPoints(assignedPoints);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFail();
                }
                callback.always();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                hideKeyboard(PointsReasonActivity.this);
                Util.offlinePointsWillBeAssignedLaterSnackbar(findViewById(android.R.id.content));
            }
        });
    }
}
