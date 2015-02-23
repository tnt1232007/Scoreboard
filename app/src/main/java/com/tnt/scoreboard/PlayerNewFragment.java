/*
 * Copyright 2015 TNT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tnt.scoreboard;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.tnt.scoreboard.adapters.ColorAdapter;

import java.util.ArrayList;

public class PlayerNewFragment extends Fragment {

    private static final String PLAYER_NAME = "playerName";
    private static final String COLOR = "color";
    private static final String REMOVABLE = "removable";
    private static final String COLOR_PICKER = "colorPicker";

    private final TextDrawable.IBuilder mBuilder = TextDrawable.builder().round();
    private AutoCompleteTextView mTextView;
    private IOnRemoveListener onRemoveListener;
    private String mPlayerName;
    private int mColor;
    private boolean mRemovable;

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
            mPlayerName = getArguments().getString(PLAYER_NAME);
            mColor = getArguments().getInt(COLOR);
            mRemovable = getArguments().getBoolean(REMOVABLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_new, container, false);

        final ImageView colorIcon = (ImageView) v.findViewById(R.id.colorIcon);
        colorIcon.setImageDrawable(mBuilder.build(" ", mColor));
        colorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorPickerDialog colorPicker = new ColorPickerDialog();
                colorPicker.setCurrentColor(mColor);
                colorPicker.setListener(new ColorAdapter.IOnColorPickListener() {
                    @Override
                    public void onColorPick(int color) {
                        PlayerNewFragment.this.mColor = color;
                        colorIcon.setImageDrawable(mBuilder.build(" ", color));
                    }
                });
                colorPicker.show(getFragmentManager(), COLOR_PICKER);
            }
        });

        ImageButton removeButton = (ImageButton) v.findViewById(R.id.removeButton);
        removeButton.setVisibility(mRemovable ? View.VISIBLE : View.GONE);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRemoveListener != null) onRemoveListener.onRemove();
            }
        });

        mTextView = ((AutoCompleteTextView) v.findViewById(R.id.playerName));
        mTextView.setHint(mPlayerName);
        mTextView.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, getNameList()));
        return v;
    }

    public String getPlayerName() {
        mPlayerName = mTextView.getText().toString();
        return mPlayerName.isEmpty() ? (String) mTextView.getHint() : mPlayerName;
    }

    public int getColor() {
        return mColor;
    }

    public void setOnRemoveListener(IOnRemoveListener onRemoveListener) {
        this.onRemoveListener = onRemoveListener;
    }

    public void requestFocus() {
        mTextView.requestFocus();
    }

    private String[] getNameList() {
        ArrayList<String> contactsList = new ArrayList<>();
        Cursor cursor = getActivity().getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
            contactsList.add(cursor.getString(cursor.getColumnIndex(
                    ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
        cursor.close();
        return contactsList.toArray(new String[contactsList.size()]);
    }

    public interface IOnRemoveListener {

        public void onRemove();
    }
}
