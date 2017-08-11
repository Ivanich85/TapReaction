package com.dmitriev.ivan.tapreaction;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Vibrator;
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

    MediaPlayer mTappedMosquito;

    Button mButton0;
    Button mButton1;
    Button mButton2;
    Button mButton3;
    Button mButton4;
    Button mButton5;
    Button mButton6;
    Button mButton7;
    Button mButton8;
    Button mButton9;
    Button mButton10;
    Button mButton11;
    Button mRandomButton;

    TextView mTimer;
    TextView mCurrentResult;
    TextView mBestResult;

    long mVisibleMoment;
    long mInvisibleMoment;

    CountDownTimer mTimerBeforeStart;

    private Vibrator mVibrator;

    private static final int REMAIN_TIME = 3100;
    private static final int BUTTONS_QUANTITY = 12;
    private static final int QUANTITY_OF_MOSQUITOS = 3;//Количество комаров
    private static final int VIBRATION_DURING = 35;//Длительность вибрации в миллисекундах

    protected static final String AVERAGE_RESULT_INTENT = "average result Intent";

    Random mRandom = new Random();

    private static int mClicksCount;

    ArrayList<Double> mTimesOfReaction = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
        startTimer(REMAIN_TIME);
    }

    private void initViews() {
        mClicksCount = 0;

        mButton0 = (Button) findViewById(R.id.tap_button_0);
        mButton1 = (Button) findViewById(R.id.tap_button_1);
        mButton2 = (Button) findViewById(R.id.tap_button_2);
        mButton3 = (Button) findViewById(R.id.tap_button_3);
        mButton4 = (Button) findViewById(R.id.tap_button_4);
        mButton5 = (Button) findViewById(R.id.tap_button_5);
        mButton6 = (Button) findViewById(R.id.tap_button_6);
        mButton7 = (Button) findViewById(R.id.tap_button_7);
        mButton8 = (Button) findViewById(R.id.tap_button_8);
        mButton9 = (Button) findViewById(R.id.tap_button_9);
        mButton10 = (Button) findViewById(R.id.tap_button_10);
        mButton11 = (Button) findViewById(R.id.tap_button_11);

        mTimer = (TextView) findViewById(R.id.timer_text_view);
        mCurrentResult = (TextView) findViewById(R.id.current_result_text_view);
        mBestResult = (TextView) findViewById(R.id.best_result_text_view);

        Typeface typeface1 = Typeface.createFromAsset(getAssets(), MainScreenActivity.FONT_PATH_1);

        mTimer.setTypeface(typeface1);

        mTappedMosquito = MediaPlayer.create(this, R.raw.trueanswer);

        mVibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
    }

    private void actionViews(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonIsInvisible(button);
                double time = timeCount(mVisibleMoment, mInvisibleMoment);
                mTimesOfReaction.add(time);
                showButton();
            }
        });
    }

    private void whenMosquitoButtonTapped(final Button button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mVibrator.vibrate(VIBRATION_DURING);
                    mTappedMosquito.start();
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
        return mRandom.nextInt(BUTTONS_QUANTITY - 1);
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
            case 9:
                buttonIsVisible(mButton9);
                mRandomButton = mButton9;
                break;
            case 10:
                buttonIsVisible(mButton10);
                mRandomButton = mButton10;
                break;
            case 11:
                buttonIsVisible(mButton11);
                mRandomButton = mButton11;
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
                mTimer.setText(millisUntilFinished / 1000 + "");
                mTappedMosquito.start();
            }

            public void onFinish() {
                mTimer.setText("");
                showButton();
            }
        }.start();
    }

    //Показываем случайную кнопку и выводим среднее время по окончании цикла
    private void showButton() {
        if (mClicksCount < QUANTITY_OF_MOSQUITOS) {
            selectRandomButton(generateRandom());
            actionViews(mRandomButton);
            whenMosquitoButtonTapped(mRandomButton);
            mClicksCount++;
        } else {
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

    //Переходим в Result Activity и отправляем в него средний результат
    private void sentResult() {
        Intent mGoToResultActivityAndSentResult = new Intent(GameActivity.this, ResultActivity.class);
        mGoToResultActivityAndSentResult.putExtra(AVERAGE_RESULT_INTENT, calculateAverageTime(mTimesOfReaction));
        startActivity(mGoToResultActivityAndSentResult);
        finish();
    }
}