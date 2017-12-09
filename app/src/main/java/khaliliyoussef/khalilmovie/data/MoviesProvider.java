package khaliliyoussef.khalilmovie.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    static final int CODE_FAVOURITE_MOVIES = 200;
    static final int CODE_FAVOURITE_MOVIES_ID = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private DbHelper mOpenHelper;

    static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = Contract.CONTENT_AUTHORITY;

        matcher.addURI(authority, Contract.PATH_FAVOURITE_MOVIES, CODE_FAVOURITE_MOVIES);
        matcher.addURI(authority, Contract.PATH_FAVOURITE_MOVIES + "/#", CODE_FAVOURITE_MOVIES_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        Context context=getContext();
        mOpenHelper = new DbHelper(context);
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor mCursor = null;
        SQLiteDatabase database = mOpenHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        switch (match) {

            case CODE_FAVOURITE_MOVIES:
                mCursor = database.query(Contract.FavouriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                     break;

            default:
                throw new IllegalStateException("cant Query this URI ! ");

        }

        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        int numRowsDeleted;

        switch (sUriMatcher.match(uri)) {

            case CODE_FAVOURITE_MOVIES:
                selection = Contract.FavouriteMoviesEntry.COLUMN_MOVIE_ID + "=?";
                // selectionArgs = new String[]
                // {String.valueOf(ContentUris.parseId(uri))};
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        Contract.FavouriteMoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new RuntimeException("We are not implementing getType in Movies App.");
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case CODE_FAVOURITE_MOVIES:
                long id = db.insert(Contract.FavouriteMoviesEntry.TABLE_NAME, null, values);
                if ( id > 0 )
                {
                    returnUri = ContentUris.withAppendedId(Contract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default: {
                throw new UnsupportedOperationException("Unknown uri:  " + uri);


            }
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
