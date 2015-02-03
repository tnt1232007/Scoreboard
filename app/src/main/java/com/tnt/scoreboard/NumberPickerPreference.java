package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

//TODO: Need refined
public class NumberPickerPreference extends DialogPreference {
    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 100;
    private static final boolean WRAP_SELECTOR_WHEEL = true;
    private final int mMinValue;
    private final int mMaxValue;
    private final boolean mWrapSelectorWheel;
    private int mSelectedValue;
    private NumberPicker mNumberPicker;

    public NumberPickerPreference(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        mMinValue = MIN_VALUE;
        mMaxValue = MAX_VALUE;
        mWrapSelectorWheel = WRAP_SELECTOR_WHEEL;
    }

    @Override
    protected void onSetInitialValue(final boolean restoreValue, final Object defaultValue) {
        mSelectedValue = restoreValue ? this.getPersistedInt(0) : (Integer) defaultValue;
        this.updateSummary();
    }

    @Override
    protected Object onGetDefaultValue(final TypedArray a, final int index) {
        return a.getInteger(index, 0);
    }

    @Override
    protected void onPrepareDialogBuilder(final AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        mNumberPicker = new NumberPicker(this.getContext());
        mNumberPicker.setMinValue(mMinValue);
        mNumberPicker.setMaxValue(mMaxValue);
        mNumberPicker.setValue(mSelectedValue);
        mNumberPicker.setWrapSelectorWheel(mWrapSelectorWheel);
        mNumberPicker.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        final LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(mNumberPicker);

        builder.setView(linearLayout);
    }

    @Override
    protected void onDialogClosed(final boolean positiveResult) {
        if (positiveResult && this.shouldPersist()) {
            mSelectedValue = mNumberPicker.getValue();
            this.persistInt(mSelectedValue);
            this.updateSummary();
        }
    }

    private void updateSummary() {
        super.setSummary(String.format("Wait %d second(s) before update score history", mSelectedValue));
    }
}