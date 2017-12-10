package khaliliyoussef.khalilmovie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 8;

    private static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        final String SQL_CREATE_FAVOURITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavouriteMoviesEntry.TABLE_NAME + " (" +
                MovieContract.FavouriteMoviesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID+ " LONG NOT NULL UNIQUE, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
              //  MovieContract.FavouriteMoviesEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.FavouriteMoviesEntry.COLUMN_RATING + " REAL NOT NULL);";
                db.execSQL(SQL_CREATE_FAVOURITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.FavouriteMoviesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

}
