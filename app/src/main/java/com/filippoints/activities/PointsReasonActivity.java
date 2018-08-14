package com.filippoints.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
                EditText viewById = PointsReasonActivity.this.findViewById(R.id.points_reason_edit_text);
                String points_reason = viewById.getText().toString();
                submit_points_to_backend(PointsReasonActivity.this.person_id, PointsReasonActivity.this.points, points_reason);

                Intent intent = ChoosePointsActivity.buildIntent(PointsReasonActivity.this);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void submit_points_to_backend(int person_id, int points, String points_reason) {
        AssignedPoints assignedPoints = new AssignedPoints(points, points_reason, person_id, System.currentTimeMillis());
        Call<Void> call = Controller.getWebAPI(this).submitPoints(assignedPoints);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PointsReasonActivity.this,
                            R.string.points_submitted, Toast.LENGTH_SHORT).show();
                } else {
                    Util.displayCheckConnectionToast(PointsReasonActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Util.displayCheckConnectionToast(PointsReasonActivity.this);
            }
        });
    }
}
