package com.dmitriev.ivan.tapreaction;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    Button mButton0;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    Button mRandomButton;

    TextView mTimer;
    TextView mCurrentResult;
    TextView mBestResult;

    Date mVisibleTime;
    Date mInvisibleTime;

    long mVisibleMoment;
    long mInvisibleMoment;

    CountDownTimer mTimerBeforeStart;

    public static final int REMAIN_TIME = 4000;
    private static final int NUMBER_OF_TRIES = 5;

    Random mRandom = new Random();

    private static int clicksCount = 0;

    ArrayList<Double> mTimesOfReaction = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
        startTimer(REMAIN_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initViews() {
        mButton0 = (Button) findViewById(R.id.tap_button_0);
        mButton1 = (Button) findViewById(R.id.tap_button_1);
        mButton2 = (Button) findViewById(R.id.tap_button_2);
        mButton3 = (Button) findViewById(R.id.tap_button_3);
        mButton4 = (Button) findViewById(R.id.tap_button_4);
        mButton5 = (Button) findViewById(R.id.tap_button_5);

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
                            + getString(R.string.sec_for_current_result));
                    showButton();
                }
                return false;
            }
        });
    }

    //Установка метки времени
    private long timeSearch(Date date) {
        date = new Date();
        long seconds = date.getTime();
        return seconds;
    }

    //Расчет времени реакции от появления кнопки до нажатия на нее
    private double timeCount(long visibleMoment, long invisibleMoment) {
        return (invisibleMoment - visibleMoment) * 1.0 / 1000;
    }

    private String formattedTimeCount(double time){
        return String.format("%.3f", time);
    }

    //Временная матка, когда кнопка повилась
    private void buttonIsVisible(Button button) {
        button.setVisibility(View.VISIBLE);
        mVisibleMoment = timeSearch(mVisibleTime);
    }

    private void buttonIsInvisible(Button button) {
        button.setVisibility(View.INVISIBLE);
        mInvisibleMoment = timeSearch(mInvisibleTime);
    }

    //Генератор случайных чисел
    private int generateRandom() {
        return mRandom.nextInt(6);
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
            default:
                mRandomButton = null;
                break;
        }
    }

    //Таймер перед запуском
    private void startTimer(long time) {
        mTimerBeforeStart = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                mTimer.setText(getText(R.string.time_before_start).toString() + millisUntilFinished / 1000 + "");
            }

            public void onFinish() {
                mTimer.setText(R.string.start_tapping);
                showButton();
            }
        }.start();
    }

    private void showButton() {
        if (clicksCount < NUMBER_OF_TRIES) {
            selectRandomButton(generateRandom());
            actionViews(mRandomButton);
            mTimesOfReaction.add(timeCount(mVisibleMoment, mInvisibleMoment));
            mBestResult.setText(mTimesOfReaction.get(clicksCount) + "");
            clicksCount++;
        } else {
            //должно выводиться среднее время
        }
    }

    private String calculateAverageTime(ArrayList<Double> list) {
        double sumOfTimes = 0;
        for (int i = 0; i < list.size(); i++) {
            sumOfTimes = sumOfTimes + list.get(i);
        }
        return String.format("%.3f", sumOfTimes);
    }
}