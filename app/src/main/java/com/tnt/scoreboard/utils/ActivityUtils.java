package com.tnt.scoreboard.utils;

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

    public static class Screen {

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
    }
}
