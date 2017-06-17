package khaliliyoussef.copyvideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
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


import khaliliyoussef.copyvideo.adapter.MoviesAdapter;
import khaliliyoussef.copyvideo.api.ApiClient;
import khaliliyoussef.copyvideo.api.ApiInterface;
import khaliliyoussef.copyvideo.model.Movie;
import khaliliyoussef.copyvideo.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static khaliliyoussef.copyvideo.data.MoviesContract.CONTENT_URI;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_MOVIE_ID;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_OVERVIEW;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_POSTER_PATH;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_RATING;
import static khaliliyoussef.copyvideo.data.MoviesContract.FavouriteMoviesEntry.COLUMN_TITLE;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private RecyclerView recyclerView;
    ProgressDialog pd;
    private SwipeRefreshLayout swipeContainer;
    Toolbar toolbar;
    public static final String LOG_TAG = MoviesAdapter.class.getSimpleName();
    private final static String API_KEY = BuildConfig.API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.main_content);
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

    private void iniViews()
    {
        pd = new ProgressDialog(this);
        pd.setMessage("Loading Movies");
        pd.setCancelable(false);
        pd.show();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        movieList = new ArrayList<>();
//        adapter = new MoviesAdapter(this, movieList);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(adapter);
        // adapter.notifyDataChanged();

        if (API_KEY.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY ", Toast.LENGTH_LONG).show();
            return;
        }
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String sortOrder=   sp.getString(getString(R.string.pref_sort_order_key),getString(R.string.pref_most_popular));

if(sortOrder.equals(getString(R.string.pref_most_popular)))
{
    Toast.makeText(this,"most popular",Toast.LENGTH_SHORT).show();
    LoadMostPopular();
}
else if(sortOrder.equals(getString(R.string.pref_highest_rated)))
{
    Toast.makeText(this,"highest rating",Toast.LENGTH_SHORT).show();
    LoadTopRated();
}
else if(sortOrder.equals(getString(R.string.pref_favorite)))
{
    Toast.makeText(this,"Favourite ",Toast.LENGTH_SHORT).show();

    loadFavoriteMovies();
}

    }

    private void LoadMostPopular() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getMostPopularMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                Toast.makeText(MainActivity.this, "loading JSON", Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Movie> movies = response.body().getResults();
                Toast.makeText(MainActivity.this, " " + movies.get(0).getOriginalTitle(), Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new MoviesAdapter(getApplicationContext(), movies));
                recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing()) {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });


    }
    private void LoadTopRated() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call= apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response)
            {
                Toast.makeText(MainActivity.this,"loading JSON",Toast.LENGTH_SHORT).show();
                int statusCode = response.code();
                List<Movie>  movies = response.body().getResults();
                Toast.makeText(MainActivity.this," "+movies.get(0).getOriginalTitle(),Toast.LENGTH_SHORT).show();
                recyclerView.setAdapter(new MoviesAdapter( getApplicationContext(),movies));
                recyclerView.smoothScrollToPosition(0);
                if (swipeContainer.isRefreshing())
                {
                    swipeContainer.setRefreshing(false);
                }
                pd.dismiss();
            }


            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                //    Log error here since request failed
                Log.e("Error", t.toString());
                Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void loadFavoriteMovies()
    {

        Cursor cursor=getContentResolver().query(CONTENT_URI,
                null,
                null,
                null,
                null);
//Change the cursor to list<Movie> and pass it to the recycler
        List <Movie> movies=new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++)
                {

                    String title =cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                    String overview =cursor.getString(cursor.getColumnIndex(COLUMN_OVERVIEW));
                    Double rating  = cursor.getDouble(cursor.getColumnIndex(COLUMN_RATING));
                    long movie_id  =  cursor.getLong(cursor.getColumnIndex(COLUMN_MOVIE_ID));
                    String poster_path   = cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH));

                    Movie movie=new Movie();
                    movie.setOriginalTitle(title);
                    movie.setOverview(overview);
                    movie.setVoteAverage(rating);
                    movie.setId(movie_id);
                    movie.setPosterPath(poster_path);
                    movies.add(movie);
                    cursor.moveToNext();
                }
            }
        }

        recyclerView.setAdapter(new MoviesAdapter( getApplicationContext(),movies));
        recyclerView.smoothScrollToPosition(0);
        if (swipeContainer.isRefreshing())
        {
            swipeContainer.setRefreshing(false);
        }

        pd.dismiss();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent =new Intent(getApplicationContext(),SettingsActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(LOG_TAG,"Preferences Changed");
        CheckSortOrder();
    }

    private void CheckSortOrder()
    {

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
       String sortOrder=preferences.getString(getString(R.string.pref_sort_order_key),getString(R.string.pref_most_popular));
        if(sortOrder.equals(R.string.pref_most_popular))
        {
            Log.d(LOG_TAG, "CheckSortOrder: sort by Most Popular");
            LoadMostPopular();
        }
        else if (sortOrder.equals(R.string.pref_highest_rated))
        {
            Log.d(LOG_TAG,"sort by the highest rated");
            LoadTopRated();
        }
        else if(sortOrder.equals(R.string.pref_favorite))
        {
            Log.d(LOG_TAG, "CheckSortOrder: favourite movies");
            loadFavoriteMovies();

        }
    }

}

