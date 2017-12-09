package khaliliyoussef.khalilmovie.adapter;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import khaliliyoussef.khalilmovie.R;

import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

/**
 * Created by Khalil on 6/19/2017.
 */

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.GuestViewHolder>
{

    private Cursor mCursor;
    private Context mContext;

    public FavoriteAdapter(Context context, Cursor cursor)
    {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.favorite_card, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        String title = mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE));
        Long movie_id = mCursor.getLong(mCursor.getColumnIndex(COLUMN_MOVIE_ID));
        // TODO (6) Retrieve the id from the cursor and
       // Long id =mCursor.getLong(mCursor.getColumnIndex(MoviesContract.FavouriteMoviesEntry._ID));
        // Display the guest name
        holder.title.setText(title);
        // Display the party count
        holder.movieId.setText(String.valueOf(movie_id));
        // TODO (7) Set the tag of the itemview in the holder to the id it won't display on the screen
       // holder.itemView.setTag(id);
    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }


    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }


    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView title;
        // Will display the party size number
        TextView movieId;


         GuestViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.favorite_title);
            movieId = itemView.findViewById(R.id.favorite_id);
        }

    }
}