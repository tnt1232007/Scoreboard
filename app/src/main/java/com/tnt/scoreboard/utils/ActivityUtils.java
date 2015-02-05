package com.tnt.scoreboard.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import com.tnt.scoreboard.R;
import com.tnt.scoreboard.models.Game;

public final class ActivityUtils {

    public static final int GAMES = 0;
    public static final int ARCHIVE = 1;
    public static final int TRASH = 2;
    public static final int SETTINGS = 4;
    public static final int HELP = 5;

    public static Screen[] SCREENS = new Screen[]{
            new Screen(Game.State.NORMAL, View.VISIBLE,
                    R.string.title_activity_games, R.menu.action_game_list,
                    R.color.blue, R.color.blueDark, R.color.blueBackground,
                    R.drawable.ic_empty_games,
                    R.string.empty_header_games, R.string.empty_text_games),
            new Screen(Game.State.ARCHIVE, View.GONE,
                    R.string.title_activity_archive, R.menu.action_archive_list,
                    R.color.orange, R.color.orangeDark, R.color.orangeBackground,
                    R.drawable.ic_empty_archive,
                    R.string.empty_header_archive, R.string.empty_text_archive),
            new Screen(Game.State.DELETE, View.GONE,
                    R.string.title_activity_trash, R.menu.action_trash_list,
                    R.color.teal, R.color.tealDark, R.color.tealBackground,
                    R.drawable.ic_empty_trash,
                    R.string.empty_header_trash, R.string.empty_text_trash)
    };

    public static class Screen implements Parcelable {

        public static final Parcelable.Creator<Screen> CREATOR = new Parcelable.Creator<Screen>() {
            public Screen createFromParcel(Parcel in) {
                return new Screen(in);
            }

            public Screen[] newArray(int size) {
                return new Screen[size];
            }
        };
        public Game.State STATE;
        public int TITLE;
        public int FAB_VISIBLE;
        public int MENU_LAYOUT;
        public int COLOR_PRIMARY;
        public int COLOR_PRIMARY_DARK;
        public int COLOR_ACCENT;
        public int EMPTY_BACKGROUND;
        public int EMPTY_HEADER;
        public int EMPTY_TEXT;

        public Screen(Game.State state, int fabVisible, int title, int menuLayout,
                      int colorPrimary, int colorPrimaryDark, int colorAccent,
                      int emptyBackground, int emptyHeader, int emptyText) {
            this.STATE = state;
            this.TITLE = title;
            this.FAB_VISIBLE = fabVisible;
            this.MENU_LAYOUT = menuLayout;
            this.COLOR_PRIMARY = colorPrimary;
            this.COLOR_PRIMARY_DARK = colorPrimaryDark;
            this.COLOR_ACCENT = colorAccent;
            this.EMPTY_BACKGROUND = emptyBackground;
            this.EMPTY_HEADER = emptyHeader;
            this.EMPTY_TEXT = emptyText;
        }

        public Screen(Parcel in) {
            this.STATE = Game.State.values()[in.readInt()];
            this.TITLE = in.readInt();
            this.FAB_VISIBLE = in.readInt();
            this.MENU_LAYOUT = in.readInt();
            this.COLOR_PRIMARY = in.readInt();
            this.COLOR_PRIMARY_DARK = in.readInt();
            this.COLOR_ACCENT = in.readInt();
            this.EMPTY_BACKGROUND = in.readInt();
            this.EMPTY_HEADER = in.readInt();
            this.EMPTY_TEXT = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(STATE.ordinal());
            dest.writeInt(FAB_VISIBLE);
            dest.writeInt(MENU_LAYOUT);
            dest.writeInt(COLOR_PRIMARY);
            dest.writeInt(COLOR_PRIMARY_DARK);
            dest.writeInt(COLOR_ACCENT);
            dest.writeInt(EMPTY_BACKGROUND);
            dest.writeInt(EMPTY_HEADER);
            dest.writeInt(EMPTY_TEXT);
        }
    }
}
