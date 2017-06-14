package khaliliyoussef.copyvideo.adapter;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import khaliliyoussef.copyvideo.R;
import khaliliyoussef.copyvideo.data.MoviesContract;


/**
 * This CustomCursorAdapter creates and binds ViewHolders, that hold the description and priority of a task,
 * to a RecyclerView to efficiently display data.
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MovieViewHolder> {

    // Class variables for the Cursor that holds task data and the Context
    private Cursor mCursor;
    private Context mContext;


    /**
     * Constructor for the CustomCursorAdapter that initializes the Context.
     *
     * @param mContext the current Context
     */
    public FavoriteAdapter(Context mContext,Cursor cursor)
    {
        this.mCursor=cursor;
        this.mContext = mContext;
    }


    /**
     * Called when ViewHolders are created to fill a RecyclerView.
     *
     * @return A new MovieViewHolder that holds the view for each task
     */
    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // Inflate the task_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_card, parent, false);

        return new MovieViewHolder(view);
    }


    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        // Indices for the _id, description, and priority columns
        int idIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry._ID);
        int titleIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE);
        int movieIdIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID);
        int overviewIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW);
        int ratingIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry.COLUMN_RATING);
        int posterPathIndex = mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH);


        mCursor.moveToPosition(position); // get to the right location in the cursor

        // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        final Long movie_id = mCursor.getLong(idIndex);
        final String title = mCursor.getString(idIndex);
        final String overview = mCursor.getString(idIndex);
        final Double rating = mCursor.getDouble(ratingIndex);
        final String poster_path = mCursor.getString(posterPathIndex);


        //Set values
        holder.itemView.setTag(id);
        holder.cardTitle.setText(title);
        holder.cardRating.setText(String.valueOf(rating));
        Picasso.with(mContext).load(poster_path).into(holder.cardImage);

    }




    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }


    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }


    // Inner class for creating ViewHolders
    class MovieViewHolder extends RecyclerView.ViewHolder {

        // Class variables for the task description and priority TextViews
        TextView cardTitle;
        TextView cardRating;
        ImageView cardImage;
        /**
         * Constructor for the TaskViewHolders.
         *
         * @param v The view inflated in onCreateViewHolder
         */
        public MovieViewHolder(View v) {
            super(v);

            cardTitle = (TextView) v.findViewById(R.id.cardTitle);
            cardRating = (TextView) v.findViewById(R.id.cardRating);
            cardImage = (ImageView) v.findViewById(R.id.cardImage);
        }
    }
}