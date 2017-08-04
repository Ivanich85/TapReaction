package com.dmitriev.ivan.tapreaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity{

    TextView mShowResultTextView;
    TextView mBestResultTextView;
    TextView mDifferenceBetweenResults;
    TextView mNumberOfLicense;

    Button mNewGameButton;
    Button mExitButton;

    protected static final String BEST_AVERAGE_RESULT = "BEST AVERAGE RESULT";
    protected static final String AVERAGE_RESULT_FLOAT = "average result float";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initViews();
        actionViews();
        newBestResult(loadPreferences(BEST_AVERAGE_RESULT), getResult(), BEST_AVERAGE_RESULT);
    }

    private void initViews() {
        mShowResultTextView = (TextView) findViewById(R.id.show_result_text_view);
        mBestResultTextView = (TextView) findViewById(R.id.best_result_text_view);
        mDifferenceBetweenResults = (TextView) findViewById(R.id.difference_between_results_text_view);
        mNewGameButton = (Button) findViewById(R.id.new_game_button);
        mExitButton = (Button) findViewById(R.id.exit_button);
        mNumberOfLicense = (TextView) findViewById(R.id.hunter_licenses_text_view);
    }

    private void actionViews() {
        mShowResultTextView.setText(getText(R.string.current_result) + formattedDouble(getResult())
                + getText(R.string.sec_for_result));
        mBestResultTextView.setText(setTextForBestResult());
        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGameActivity();
                NumberOfHuntingLicences.setHuntingLicense(NumberOfHuntingLicences.getHuntingLicense() - 1);
                finish();
            }
        });
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mNumberOfLicense.setText(NumberOfHuntingLicences.getHuntingLicense() + "");
    }

    private Double getResult() {
        Intent mGetAverageResultFromGameActivity = getIntent();
        return mGetAverageResultFromGameActivity.getDoubleExtra(GameActivity.AVERAGE_RESULT_INTENT, 0.0);
    }

    private String formattedDouble(double time) {
        return String.format("%.3f", time);
    }

    private String formattedFloat(float time) {
        return String.format("%.3f", time);
    }

    //Загружаем результат из SharedPreferences
    private float loadPreferences (String bestResult) {
        SharedPreferences myBestResult = getSharedPreferences(bestResult, Context.MODE_PRIVATE);
        float averageBestResult = myBestResult.getFloat(AVERAGE_RESULT_FLOAT, 0);
        return averageBestResult;
    }

    //Проверяем, лучше ли новый результат, чем предыдущий
    private boolean newBestResult(float bestResult, double currentResult, String bestAverageResult) {
        if (bestResult != 0) {
            if (currentResult <= bestResult) {
                savePreferences(currentResult, bestAverageResult);
                mDifferenceBetweenResults.setText("Новый рекорд!");
            } return true;
        } else savePreferences(currentResult, bestAverageResult);
        return false;
    }

    //Обновляем текст лучшего результата
    private String setTextForBestResult() {
        if (newBestResult(loadPreferences(BEST_AVERAGE_RESULT), getResult(), BEST_AVERAGE_RESULT)) {
            return getText(R.string.best_result_is)
                    + formattedFloat(loadPreferences(BEST_AVERAGE_RESULT))
                    + getText(R.string.sec_for_result);
        } else {
            return getText(R.string.best_result_is)
                    + formattedDouble(getResult())
                    + getText(R.string.sec_for_result);
        }
    }

    //Сохраняем результат
    private double savePreferences(double bestAverageResult, String bestResult) {
        SharedPreferences mBestResult = getSharedPreferences(bestResult, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mBestResult.edit();
        mEditor.putFloat(AVERAGE_RESULT_FLOAT, (float)bestAverageResult);
        mEditor.apply();
        return bestAverageResult;
    }

    private void goToGameActivity() {
        Intent mGoToGameActivity = new Intent(ResultActivity.this, GameActivity.class);
        startActivity(mGoToGameActivity);
    }
}
