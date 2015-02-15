package com.tnt.scoreboard;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.tnt.scoreboard.adapters.ColorAdapter;
import com.tnt.scoreboard.utils.ColorUtils;

public class ColorPickerDialog extends DialogFragment {

    private int currentColor;
    private ColorAdapter.IOnColorPickListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        ColorAdapter colorAdapter = new ColorAdapter(
                ColorUtils.ColorMap(getActivity()), currentColor);
        colorAdapter.setListener(new ColorAdapter.IOnColorPickListener() {
            @Override
            public void onColorPick(int chooseColor) {
                dismiss();
                listener.onColorPick(chooseColor);
            }
        });
        builder.setAdapter(colorAdapter, null);
        return builder.create();
    }

    public void setCurrentColor(int currentColor) {
        this.currentColor = currentColor;
    }

    public void setListener(ColorAdapter.IOnColorPickListener listener) {
        this.listener = listener;
    }
}
