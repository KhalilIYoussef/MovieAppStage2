package khaliliyoussef.khalilmovie;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import khaliliyoussef.khalilmovie.adapter.MovieAdapter;
import khaliliyoussef.khalilmovie.api.ApiClient;
import khaliliyoussef.khalilmovie.api.ApiInterface;
import khaliliyoussef.khalilmovie.model.Movie;
import khaliliyoussef.khalilmovie.model.MovieResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static khaliliyoussef.khalilmovie.data.MovieContract.CONTENT_URI;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_RATING;
import static khaliliyoussef.khalilmovie.data.MovieContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class MainActivity extends AppCompatActivity {


    private int VALUE = 1;
    public static final String NAME = "MyPrefsFile";
    public static final String key = "MyPrefsKey";

    private static final String LIST_STATE_KEY = "LIST_KEY";
    private RecyclerView recyclerView;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    Toolbar toolbar;

    RecyclerView.LayoutManager manager;
    public static final String LOG_TAG =MovieAdapter.class.getSimpleName();
    private final static String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.content_main);
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.background_dark));
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                iniViews();
                Toast.makeText(MainActivity.this, "Movies Refreshed", Toast.LENGTH_SHORT).show();
            }
        });

        iniViews();
    }

    private void iniViews() {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Movies");
        pd.setCancelable(false);
        pd.show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            manager = new GridLayoutManager(this, 2);
            recyclerView.setLayoutManager(manager);

        } else {
            manager = new GridLayoutManager(this, 4);
            recyclerView.setLayoutManager(manager);
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY ", Toast.LENGTH_LONG).show();
            return;
        }
        CheckActivity();

    }

    private void LoadMostPopular() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMovie("popular", API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
            //    Toast.makeText(MainActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();


                recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void LoadTopRated() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getMovie("top_rated", API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                Toast.makeText(MainActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                //Toast.makeText(MainActivity.this, " " + movies.get(0).getOriginalTitle(), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
                //recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("Error", t.toString());
                pd.dismiss();
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void LoadFavoriteMovies() {

        Cursor cursor = getContentResolver().query(CONTENT_URI, null, null, null, null);
        // ToDo: Just for logging, you can remove
        List<Movie> movies = new ArrayList<Movie>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Movie movie = new Movie();
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
            movie.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW)));
            movie.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_MOVIE_ID)));
            movies.add(movie);
        }


        recyclerView.setAdapter(new MovieAdapter(getApplicationContext(), movies));
        if (swipeContainer.isRefreshing()) {
            swipeContainer.setRefreshing(false);
        }

        pd.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_popular:

                VALUE = 1;
                setActivity(VALUE);
                LoadMostPopular();
                break;

            case R.id.menu_top_rated:

                VALUE = 2;
                setActivity(VALUE);
                LoadTopRated();
                break;

            case R.id.menu_favorite:

                VALUE = 3;
                setActivity(VALUE);
                LoadFavoriteMovies();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    protected void onPause() {
        super.onPause();
        SharedPreferences checkForActivity = getSharedPreferences(NAME, 0);
        SharedPreferences.Editor editor = checkForActivity.edit();
        editor.putInt(key, VALUE);
        editor.commit();
    }


    public int CheckActivity() {
        SharedPreferences checkForActivity = getSharedPreferences(NAME, 0);
        VALUE = checkForActivity.getInt(key, 1);
        setActivity(VALUE);
        return VALUE;
    }


    public void setActivity(int x) {
        switch (x) {
            case 1:
                VALUE = 1;
                LoadMostPopular();
                break;
            case 2:
                VALUE = 2;
                LoadTopRated();
                break;
            case 3:
                VALUE = 3;
                LoadFavoriteMovies();
                break;
        }


    }
}


