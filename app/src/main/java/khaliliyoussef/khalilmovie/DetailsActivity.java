package khaliliyoussef.khalilmovie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import khaliliyoussef.khalilmovie.adapter.ReviewsAdapter;
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

import static khaliliyoussef.khalilmovie.data.MovieContract.CONTENT_URI;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_RATING;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
    TextView overView, rating, releaseDate;
    ImageView imageView;
    FloatingActionButton imageButton;
    RecyclerView recyclerView;
    RecyclerView reviewsRecyclers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar title = getSupportActionBar();
        title.setDisplayHomeAsUpEnabled(true);


        imageView = (ImageView) findViewById(R.id.image_details);
        overView = (TextView) findViewById(R.id.overview_details);
        rating = (TextView) findViewById(R.id.vote_average);
        releaseDate = (TextView) findViewById(R.id.release_date);
        recyclerView = (RecyclerView) findViewById(R.id.trailer_details);
        reviewsRecyclers = (RecyclerView) findViewById(R.id.review_details);
        imageButton = (FloatingActionButton) findViewById(R.id.favorite);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getApplicationContext());
        reviewsRecyclers.setLayoutManager(manager);
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(Intent.EXTRA_TEXT);


        if (CheckFavorite()) {
            //it's favorite
            imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    getContentResolver().delete(CONTENT_URI,
                            COLUMN_MOVIE_ID + "=?",
                            new String[]{String.valueOf(movie.getId())}
                    );

                }
            });

        } else {
            //it's not favorit
            imageButton.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                    ContentValues values = new ContentValues();
                    String Title = movie.getOriginalTitle();
                    String PosterPath = movie.getPosterPath();
                    values.put(COLUMN_TITLE, Title);
                    values.put(COLUMN_OVERVIEW, movie.getOverview());
                    values.put(COLUMN_RATING, movie.getVoteAverage());
                    values.put(COLUMN_POSTER_PATH, PosterPath);
                    values.put(COLUMN_MOVIE_ID, movie.getId());


                    getContentResolver().insert(CONTENT_URI, values);
                }
            });

        }


        if (movie != null) {
            String Title = movie.getOriginalTitle();
            String PosterPath = movie.getPosterPath();
            //Toast.makeText(this, "" + movie.getOriginalTitle(), Toast.LENGTH_SHORT).show();
            title.setTitle(String.valueOf(Title));
            overView.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());
            rating.setText(String.valueOf(movie.getVoteAverage() + "/10"));
            Picasso.with(this)
                    .load("http://image.tmdb.org/t/p/w185//" + PosterPath)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(imageView);

        } else {

            Toast.makeText(this, "No API Data", Toast.LENGTH_SHORT).show();
        }
        long movie_id = movie.getId();
        LoadTrailerJSON(movie_id);
        LoadReviewJSON(movie_id);


    }


    private synchronized boolean CheckFavorite() {
        Cursor cursor = getContentResolver().query(CONTENT_URI,
                new String[]{COLUMN_MOVIE_ID},
                COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movie.getId())},
                null);

        //  Toast.makeText(this, "cursor=" + cursor.toString(), Toast.LENGTH_SHORT).show();
        return (cursor != null) && (cursor.getCount() > 0);

    }


    private void LoadTrailerJSON(long id) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<TrailerResponse> call = apiService.getMovieTrailers(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                // Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Trailer> trailers = response.body().getResults();
                recyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), trailers));
                // recyclerView.smoothScrollToPosition(0);

            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                Toast.makeText(DetailsActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void LoadReviewJSON(long id) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewResponse> call = apiService.getMovieReviews(id, BuildConfig.API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                // Toast.makeText(DetailsActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Review> reviews = response.body().getResults();
                reviewsRecyclers.setAdapter(new ReviewsAdapter(getApplicationContext(), reviews));
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

    public void intitalizeToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

}


