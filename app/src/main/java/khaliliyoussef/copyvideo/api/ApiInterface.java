package khaliliyoussef.copyvideo.api;

import khaliliyoussef.copyvideo.adapter.MoviesAdapter;
import khaliliyoussef.copyvideo.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khalil on 6/8/2017.
 */

public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/most_popular")
    Call<MoviesResponse> getMostPopular(@Path("id") int id, @Query("api_key") String apiKey);
}
