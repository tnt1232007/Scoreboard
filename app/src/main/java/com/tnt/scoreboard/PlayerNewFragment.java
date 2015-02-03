package com.tnt.scoreboard;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.adapters.ColorViewHolder;


public class PlayerNewFragment extends Fragment {

    private static final String PLAYER_NAME = "playerName";
    private static final String COLOR = "color";
    private static final String REMOVABLE = "removable";
    private static final String COLOR_PICKER = "colorPicker";

    private EditText editText;
    private IOnRemoveListener onRemoveListener;
    private String playerName;
    private TextDrawable.IBuilder drawableBuilder = TextDrawable.builder().round();
    private int color;
    private boolean removable;

    public PlayerNewFragment() {
    }

    public static PlayerNewFragment newInstance(String playerName, int color, boolean removable) {
        PlayerNewFragment fragment = new PlayerNewFragment();
        Bundle args = new Bundle();
        args.putString(PLAYER_NAME, playerName);
        args.putInt(COLOR, color);
        args.putBoolean(REMOVABLE, removable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            playerName = getArguments().getString(PLAYER_NAME);
            color = getArguments().getInt(COLOR);
            removable = getArguments().getBoolean(REMOVABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_new, container, false);

        final ImageView colorIcon = (ImageView) v.findViewById(R.id.colorIcon);
        colorIcon.setImageDrawable(drawableBuilder.build(" ", color));
        colorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPicker = new ColorPickerDialog();
                colorPicker.setCurrentColor(color);
                colorPicker.setListener(new ColorViewHolder.IOnColorPickListener() {
                    @Override
                    public void onColorPick(int chooseColor) {
                        PlayerNewFragment.this.color = chooseColor;
                        colorIcon.setImageDrawable(drawableBuilder.build(" ", chooseColor));
                    }
                });
                colorPicker.show(getFragmentManager(), COLOR_PICKER);
            }
        });

        ImageButton removeButton = (ImageButton) v.findViewById(R.id.removeButton);
        removeButton.setVisibility(removable ? View.VISIBLE : View.GONE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRemoveListener != null) onRemoveListener.onRemove();
            }
        });

        editText = ((EditText) v.findViewById(R.id.playerName));
        editText.setHint(playerName);
        return v;
    }

    //<editor-fold desc="Getter Setter">
    public String getPlayerName() {
        playerName = editText.getText().toString();
        return playerName.isEmpty() ? (String) editText.getHint() : playerName;
    }

    public int getColor() {
        return color;
    }

    public void setOnRemoveListener(IOnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public void requestFocus() {
        editText.requestFocus();
    }
    //</editor-fold>

    public interface IOnRemoveListener {

        public void onRemove();
    }
}
