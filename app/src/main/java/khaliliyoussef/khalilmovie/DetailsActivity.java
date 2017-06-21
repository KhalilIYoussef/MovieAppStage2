package khaliliyoussef.khalilmovie;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;
import java.util.List;
import khaliliyoussef.khalilmovie.adapter.ReviewAdapter;
import khaliliyoussef.khalilmovie.adapter.TrailerAdapter;
import khaliliyoussef.khalilmovie.api.ApiClient;
import khaliliyoussef.khalilmovie.api.ApiInterface;
import khaliliyoussef.khalilmovie.model.Movie;
import khaliliyoussef.khalilmovie.model.Review;
import khaliliyoussef.khalilmovie.model.ReviewResponse;
import khaliliyoussef.khalilmovie.model.Trailer;
import khaliliyoussef.khalilmovie.model.TrailerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static khaliliyoussef.khalilmovie.data.MoviesContract.CONTENT_URI;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static khaliliyoussef.khalilmovie.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
TextView title, overView, rating,releaseDate;
    ImageView imageView;
    ToggleButton toggleButton;
    RecyclerView recyclerView;
    RecyclerView reviewsRecyclers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        imageView= (ImageView) findViewById(R.id.details_image);
        title = (TextView) findViewById(R.id.details_title);
        overView = (TextView) findViewById(R.id.details_overview);
        rating = (TextView) findViewById(R.id.details_vote_average);
        releaseDate= (TextView) findViewById(R.id.details_relase_date);
        recyclerView= (RecyclerView) findViewById(R.id.details_trailer);
        reviewsRecyclers= (RecyclerView) findViewById(R.id.details_review);
        toggleButton = (ToggleButton) findViewById(R.id.ib_favorite);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(getApplicationContext());
        reviewsRecyclers.setLayoutManager(manager);
        //get the movie from the MainActivity
        Intent intent = getIntent();
        movie=intent.getParcelableExtra(Intent.EXTRA_TEXT);

if(CheckFavorite())
{
    //it's favorite
    toggleButton.setChecked(true);

    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            toggleButton.setChecked(false);
            getContentResolver().delete(CONTENT_URI,
                    COLUMN_MOVIE_ID+"=?",
                    new String[]{String.valueOf(movie.getId())}
            );
        }
    });
}
else
{
//it's not favorite
    toggleButton.setChecked(false);

    toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            toggleButton.setChecked(true);
            ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE, movie.getOriginalTitle());
                values.put(COLUMN_OVERVIEW, movie.getOverview());
                values.put(COLUMN_RATING, movie.getVoteAverage());
                values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
                values.put(COLUMN_MOVIE_ID, movie.getId());
                getContentResolver().insert(CONTENT_URI, values);

        }
    });
}

////
//   //     Cursor c=getContentResolver().query(CONTENT_URI, new String[]{COLUMN_MOVIE_ID},COLUMN_MOVIE_ID+"=?",new String[]{String.valueOf(movie.getId())},null);
//    //    long s= c.getLong(c.getColumnIndex(COLUMN_MOVIE_ID));
//        if(toggleButton.isChecked())
//        { //toggleButton.(R.drawable.ic_star_black_24dp);
//            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
//            {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
//                {
//                  //  toggleButton.setBackground(R.drawable.ic_star_border_black_24dp);
//                    getContentResolver().delete(CONTENT_URI, COLUMN_MOVIE_ID + "=?", new String[]{String.valueOf(movie.getId())});
//
//                }
//        });
//        }
//        else
//            {
//                //toggleButton.setBackground(R.drawable.ic_star_black_24dp);
//                ContentValues values = new ContentValues();
//                values.put(COLUMN_TITLE, movie.getOriginalTitle());
//                values.put(COLUMN_OVERVIEW, movie.getOverview());
//                values.put(COLUMN_RATING, movie.getVoteAverage());
//                values.put(COLUMN_POSTER_PATH, movie.getPosterPath());
//                values.put(COLUMN_MOVIE_ID, movie.getId());
//                getContentResolver().insert(CONTENT_URI, values);
//            }
        if(movie!=null)
        {

            title.setText(movie.getOriginalTitle());
            overView.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());
            rating.setText(String.valueOf(movie.getVoteAverage() + "/10"));

            Picasso.with(this)
                    .load( movie.getPosterPath())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(imageView);

        }
        else
            {

                Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
            }
            long movie_id=movie.getId();
             LoadTrailerJSON(movie_id);
             LoadReviewJSON(movie_id);




    }

    private boolean CheckFavorite()
    {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        if ((cursor != null) && (cursor.getCount() > 0))
        {
            Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
        return true;
        }
        else
        {
           return false;
        }

    }


    private void LoadTrailerJSON(long id)
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TrailerResponse> call = apiService.getMovieTrailers(id,BuildConfig.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Trailer> trailers = response.body().getResults();
                recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                recyclerView.smoothScrollToPosition(0);

            }


            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });



    }
    private void LoadReviewJSON(long id)
    {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewResponse> call = apiService.getMovieReviews(id,BuildConfig.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response)
            {
                Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Review> trailers = response.body().getResults();
                reviewsRecyclers.setAdapter(new ReviewAdapter(getApplicationContext(), trailers));
                reviewsRecyclers.smoothScrollToPosition(0);

            }


            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });



    }


}
