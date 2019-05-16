package com.example.alua.Data;

import android.provider.BaseColumns;

public class LibraryContract {

    private LibraryContract(){}

    public static final class BookCreate implements BaseColumns {

        public final static String TABLE_NAME = "books";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_AUTHOR = "author";
        public final static String COLUMN_CONTENT = "content";
        public final static String COLUMN_GENRE = "genre";
        public final static String COLUMN_AVATAR = "avatar";

        public static final int GENRE_FAIRYTALES = 1;
        public static final int GENRE_STORIES = 2;
        public static final int GENRE_METHODICAL_INSTRUCTIONS = 3;
        public static final int GENRE_UNKNOWN = 4;
    }
}
