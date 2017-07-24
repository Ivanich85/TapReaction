package com.dmitriev.ivan.tapreaction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.dmitriev.ivan.tapreaction.GameActivity.AVERAGE_RESULT;


public class ResultActivity extends AppCompatActivity{

    TextView mShowResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        actionViews();
    }

    private void initViews() {
        mShowResultTextView = (TextView) findViewById(R.id.show_result_text_view);
    }

    private void actionViews() {
        mShowResultTextView.setText(formattedDouble(getResult()));
    }

    private Double getResult() {
        Intent mGetAverageResultFromGameActivity = getIntent();
        return mGetAverageResultFromGameActivity.getDoubleExtra(AVERAGE_RESULT, 0.0);
    }

    private String formattedDouble(double time) {
        return String.format("%.3f", time);
    }
}
