package com.dmitriev.ivan.tapreaction;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Button mButton0;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    Button mButton6;
    Button mButton7;
    Button mButton8;
    Button mRandomButton;

    TextView mTimer;
    TextView mCurrentResult;
    TextView mBestResult;

    long mVisibleMoment;
    long mInvisibleMoment;

    CountDownTimer mTimerBeforeStart;

    private static final int REMAIN_TIME = 3100;
    private static final int BUTTONS_QUANTITY = 9;
    private static final int NUMBER_OF_TRIES = 10;

    Random mRandom = new Random();

    private static int mClicksCount;

    ArrayList<Double> mTimesOfReaction = new ArrayList<>();

    Intent mGoToResultActivityAndSentResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
        startTimer(REMAIN_TIME);
    }

    private void initViews() {
        mButton0 = (Button) findViewById(R.id.tap_button_0);
        mButton1 = (Button) findViewById(R.id.tap_button_1);
        mButton2 = (Button) findViewById(R.id.tap_button_2);
        mButton3 = (Button) findViewById(R.id.tap_button_3);
        mButton4 = (Button) findViewById(R.id.tap_button_4);
        mButton5 = (Button) findViewById(R.id.tap_button_5);
        mButton6 = (Button) findViewById(R.id.tap_button_6);
        mButton7 = (Button) findViewById(R.id.tap_button_7);
        mButton8 = (Button) findViewById(R.id.tap_button_8);

        mTimer = (TextView) findViewById(R.id.timer_text_view);
        mCurrentResult = (TextView) findViewById(R.id.current_result_text_view);
        mBestResult = (TextView) findViewById(R.id.best_result_text_view);
    }

    private void actionViews(final Button button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonIsInvisible(button);
                    mCurrentResult.setText(getText(R.string.current_result)
                            + formattedTimeCount(timeCount(mVisibleMoment, mInvisibleMoment))
                            + getString(R.string.sec_for_result));
                    double time = timeCount(mVisibleMoment, mInvisibleMoment);
                    mTimesOfReaction.add(time);
                    showButton();
                }
                return false;
            }
        });
    }

    //Установка метки времени
    private long timeSearch() {
        Date date = new Date();
        return date.getTime();
    }

    //Расчет времени реакции от появления кнопки до нажатия на нее
    private double timeCount(long visibleMoment, long invisibleMoment) {
        return (invisibleMoment - visibleMoment) / 1000.0;
    }

    //Отформатированное время
    private String formattedTimeCount(double time) {
        return String.format("%.3f", time);
    }

    //Временная матка, когда кнопка повилась
    private void buttonIsVisible(Button button) {
        button.setVisibility(View.VISIBLE);
        mVisibleMoment = timeSearch();
    }

    //Временная метка, когда кнопки коснулись
    private void buttonIsInvisible(Button button) {
        button.setVisibility(View.INVISIBLE);
        mInvisibleMoment = timeSearch();
    }

    //Генератор случайных чисел
    private int generateRandom() {
        return mRandom.nextInt(BUTTONS_QUANTITY);
    }

    //Выбор кнопки на основании generateRandom()
    private void selectRandomButton(int random) {
        switch (random) {
            case 0:
                buttonIsVisible(mButton0);
                mRandomButton = mButton0;
                break;
            case 1:
                buttonIsVisible(mButton1);
                mRandomButton = mButton1;
                break;
            case 2:
                buttonIsVisible(mButton2);
                mRandomButton = mButton2;
                break;
            case 3:
                buttonIsVisible(mButton3);
                mRandomButton = mButton3;
                break;
            case 4:
                buttonIsVisible(mButton4);
                mRandomButton = mButton4;
                break;
            case 5:
                buttonIsVisible(mButton5);
                mRandomButton = mButton5;
                break;
            case 6:
                buttonIsVisible(mButton6);
                mRandomButton = mButton6;
                break;
            case 7:
                buttonIsVisible(mButton7);
                mRandomButton = mButton7;
                break;
            case 8:
                buttonIsVisible(mButton8);
                mRandomButton = mButton8;
                break;
            default:
                mRandomButton = null;
                break;
        }
    }

    //Таймер перед запуском
    private void startTimer(final long time) {
        mTimerBeforeStart = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimer.setText(getText(R.string.time_before_start) + (millisUntilFinished / 1000 + ""));
            }

            public void onFinish() {
                mTimer.setText(R.string.start_tapping);
                showButton();
            }
        }.start();
    }

    //Показываем случайную кнопку и выводим среднее время по окончании цикла
    private void showButton() {
        if (mClicksCount < NUMBER_OF_TRIES) {
            selectRandomButton(generateRandom());
            actionViews(mRandomButton);
            mClicksCount++;
        } else {
            mBestResult.setText(getText(R.string.current_average_result)
                    + formattedCalculateAverageTime(calculateAverageTime(mTimesOfReaction))
                    + getText(R.string.sec_for_result));
            sentResult();
        }
    }

    //Считаем среднее значение
    private double calculateAverageTime(ArrayList<Double> list) {
        double sumOfTimes = 0;
        for (int i = 0; i < list.size(); i++) {
            sumOfTimes = sumOfTimes + list.get(i);
        }
        return (sumOfTimes / list.size());
    }

    //Отформатированное среднее значение
    private String formattedCalculateAverageTime(double averageTime) {
        return String.format("%.3f", averageTime);
    }

    private void sentResult() {
        mGoToResultActivityAndSentResult.setClass(this, ResultActivity.class);
        mGoToResultActivityAndSentResult.putExtra("awerageResult", calculateAverageTime(mTimesOfReaction));
        startActivity(mGoToResultActivityAndSentResult);
    }


}