package com.tnt.scoreboard.libs;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tnt.scoreboard.R;

public class NumberSeekerPreference extends DialogPreference {

    private static final String SUMMARY = "Value: %d";
    private String mInitialDialogSummary = SUMMARY;
    private static final int VALUE = 0;
    private static final int MAX_VALUE = 100;
    private int mMaxValue;
    private int mCurrentValue;
    private String mInitialSummary;
    private SeekBar mNumberSeeker;

    public NumberSeekerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NumberSeekerPreference);
        mMaxValue = a.getInt(R.styleable.NumberSeekerPreference_maxValue, MAX_VALUE);
        mInitialDialogSummary = a.getString(R.styleable.NumberSeekerPreference_dialogSummary);
        mInitialSummary = getSummary().toString();
        a.recycle();

        setDialogLayoutResource(R.layout.pref_number_seeker);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        final TextView summary = (TextView) view.findViewById(R.id.valueSummary);
        summary.setText(String.format(mInitialDialogSummary, mCurrentValue));

        mNumberSeeker = (SeekBar) view.findViewById(R.id.numberSeeker);
        mNumberSeeker.setMax(mMaxValue);
        mNumberSeeker.setProgress(mCurrentValue);
        mNumberSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                summary.setText(String.format(mInitialDialogSummary, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mCurrentValue = restoreValue ? getPersistedInt(VALUE) : (Integer) defaultValue;
        persistInt(mCurrentValue);
        setSummary(null);
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInteger(index, VALUE);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            mCurrentValue = mNumberSeeker.getProgress();
            persistInt(mCurrentValue);
            setSummary(null);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(String.format(mCurrentValue == 1
                ? mInitialSummary.replace("(es)", "").replace("(s)", "")
                : mInitialSummary.replace("(es)", "es").replace("(s)", "s")
                , mCurrentValue));
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
    }

    public String getInitialSummary() {
        return mInitialSummary;
    }

    public void setInitialSummary(String initialSummary) {
        mInitialSummary = initialSummary;
    }

    public String getInitialDialogSummary() {
        return mInitialDialogSummary;
    }

    public void setInitialDialogSummary(String initialDialogSummary) {
        mInitialDialogSummary = initialDialogSummary;
    }
}