package com.example.simpleproductivity4;

import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.simpleproductivity4.databinding.FragmentTimerBinding;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Locale;


public class TimerFragment extends Fragment  {
    FragmentTimerBinding mBinding;

    private static long START_TIME_IN_MILLIS = 600000;
    private TextView mTextViewCountDown;
    private ImageView mPlay, mPause, mReset;
    private Button timeButton;
    private CountDownTimer mCountDownTimer;
    private NumberPicker numPickerMin, numPickerSec;
    private boolean mTimerRunning;
    private String[] timerActivities;
    private ArrayAdapter arrayAdapter;
    private ProgressBar progressBar;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void startTimer() {
        mPlay.setImageResource(R.drawable.play_red);
        mPause.setImageResource(R.drawable.ic_baseline_pause_24);
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                progressBar.setProgress((int) millisUntilFinished / 1000);

            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                progressBar.setVisibility(View.GONE);

            }
        }.start();
        mTimerRunning = true;

    }

    private void pauseTimer() {
        if(mTimerRunning) {
            mCountDownTimer.cancel();
            mTimerRunning = false;
            mPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        }


    }

    private void resetTimer() {
        pauseTimer();
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

    }
    private void updateCountDownText() {
        int minutes = (int) (mTimeLeftInMillis / 1000 / 60);
        int seconds = (int) (mTimeLeftInMillis / 1000 % 60);

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mBinding = FragmentTimerBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        timerActivities = getResources().getStringArray(R.array.activities);
        arrayAdapter = new ArrayAdapter(requireContext(), R.layout.dropdown_item, timerActivities);
        mBinding.autoCompleteTextView.setAdapter(arrayAdapter);

    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextViewCountDown = mBinding.textView2;
        progressBar = mBinding.barTimer;
        progressBar.setVisibility(View.GONE);
        numPickerMin = mBinding.numPickerMin;
        numPickerMin.setMinValue(0);
        numPickerMin.setMaxValue(59);

        numPickerSec = mBinding.numPickerSec;
        numPickerSec.setMinValue(0);
        numPickerSec.setMaxValue(60);


        mPlay = mBinding.imageView;
        mPlay.setOnClickListener(v -> {
            startTimer();
            progressBar.setVisibility(View.VISIBLE);
        });
        mPause = mBinding.imageView2;
        mPause.setOnClickListener(v -> {
            if (mTimerRunning) {
                mPause.setImageResource(R.drawable.pause_red);
            }
            pauseTimer();

        });

        timeButton = mBinding.timeButton;
        timeButton.setOnClickListener(v -> {
            mPause.setImageResource(R.drawable.ic_baseline_pause_24);
            mPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            setTime();
        });





        updateCountDownText();
    }

    private void setTime() {
        long mins = numPickerMin.getValue();
        long secs =  numPickerSec.getValue();
        progressBar.setVisibility(View.GONE);



        START_TIME_IN_MILLIS = mins * 60 * 1000 + secs * 1000;
        resetTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding=null;
    }

}
