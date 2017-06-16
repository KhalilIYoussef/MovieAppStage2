package khaliliyoussef.copyvideo;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.List;
import khaliliyoussef.copyvideo.adapter.ReviewAdapter;
import khaliliyoussef.copyvideo.adapter.TrailerAdapter;
import khaliliyoussef.copyvideo.api.ApiClient;
import khaliliyoussef.copyvideo.api.ApiInterface;
import khaliliyoussef.copyvideo.model.Movie;
import khaliliyoussef.copyvideo.model.Review;
import khaliliyoussef.copyvideo.model.ReviewResponse;
import khaliliyoussef.copyvideo.model.Trailer;
import khaliliyoussef.copyvideo.model.TrailerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static khaliliyoussef.copyvideo.data.MoviesContract.CONTENT_URI;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
TextView title, overView, rating,releaseDate;
    ImageView imageView;
    ImageButton imageButton;
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
        imageButton= (ImageButton) findViewById(R.id.ib_favorite);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager manager= new LinearLayoutManager(getApplicationContext());
        reviewsRecyclers.setLayoutManager(manager);
        //get the movie from the MainActivity
        Intent intent = getIntent();
        movie=intent.getParcelableExtra(Intent.EXTRA_TEXT);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                values.put(COLUMN_TITLE,movie.getOriginalTitle());
                values.put(COLUMN_OVERVIEW,movie.getOverview());
                values.put(COLUMN_RATING,movie.getVoteAverage());
                values.put(COLUMN_POSTER_PATH,movie.getPosterPath());
                values.put(COLUMN_MOVIE_ID,movie.getId());
                getContentResolver().insert(CONTENT_URI,values);
            }
        });
        if(movie!=null)
        {

            title.setText(movie.getOriginalTitle());
            overView.setText(movie.getOverview());
            releaseDate.setText(movie.getReleaseDate());
            rating.setText(String.valueOf(movie.getVoteAverage() + "/10"));
            Picasso.with(this).load( movie.getPosterPath()).into(imageView);

        }
        else
            {

                Toast.makeText(this,"No API Data",Toast.LENGTH_SHORT).show();
            }
            long movie_id=movie.getId();
             LoadTrailerJSON(movie_id);
             LoadReviewJSON(movie_id);




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
