package com.tnt.scoreboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPickerPreference extends DialogPreference {

    private static final int VALUE = 0;
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10;
    private static final int INCREMENT = 1;
    private static final boolean INFINITE_SCROLL = true;

    private int mMinValue;
    private int mMaxValue;
    private int mIncrement;
    private boolean mInfiniteScroll;

    private int mCurrentValue;
    private int mDisplayedValue;
    private String mInitialSummary;
    private NumberPicker mNumberPicker;

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.NumberPickerPreference);
        mMinValue = a.getInt(R.styleable.NumberPickerPreference_minValue, MIN_VALUE);
        mMaxValue = a.getInt(R.styleable.NumberPickerPreference_maxValue, MAX_VALUE);
        mIncrement = a.getInt(R.styleable.NumberPickerPreference_increment, INCREMENT);
        mInfiniteScroll = a.getBoolean(
                R.styleable.NumberPickerPreference_infiniteScroll, INFINITE_SCROLL);
        mInitialSummary = getSummary() == null ? "%d" : getSummary().toString();
        a.recycle();

        setDialogLayoutResource(R.layout.pref_number_picker);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        mNumberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        if (mIncrement == INCREMENT) {
            mNumberPicker.setMaxValue(mMaxValue);
        } else {
            String[] values = calculateDisplayedValues(mMinValue, mMaxValue, mIncrement);
            mNumberPicker.setDisplayedValues(values);
            mNumberPicker.setMaxValue(values.length - 1);
        }
        mNumberPicker.setValue(mCurrentValue);
        mNumberPicker.setWrapSelectorWheel(mInfiniteScroll);
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
        mDisplayedValue = restoreValue ? getPersistedInt(VALUE) : (Integer) defaultValue;
        mCurrentValue = calculateActualValue(mDisplayedValue);
        persistInt(mDisplayedValue);
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
            mCurrentValue = mNumberPicker.getValue();
            mDisplayedValue = calculateDisplayedValue(mCurrentValue);
            persistInt(mDisplayedValue);
            setSummary(null);
        }
    }

    @Override
    public void setSummary(CharSequence summary) {
        super.setSummary(String.format(mDisplayedValue == 1
                ? mInitialSummary.replace("(es)", "").replace("(s)", "")
                : mInitialSummary.replace("(es)", "es").replace("(s)", "s")
                , mDisplayedValue));
    }

    private String[] calculateDisplayedValues(int minValue, int maxValue, int increment) {
        String[] values = new String[(maxValue - minValue) / increment + 1];
        for (int i = 0; i < values.length; i++) {
            int value = minValue + i * increment;
            if (value > maxValue)
                break;
            values[i] = String.valueOf(value);
        }
        return values;
    }

    private int calculateDisplayedValue(int value) {
        return value * mIncrement + mMinValue;
    }

    private int calculateActualValue(int displayedValue) {
        return (displayedValue - mMinValue) / mIncrement;
    }
}