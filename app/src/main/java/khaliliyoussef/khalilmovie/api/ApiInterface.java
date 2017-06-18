package khaliliyoussef.khalilmovie.api;

import khaliliyoussef.khalilmovie.model.MoviesResponse;
import khaliliyoussef.khalilmovie.model.ReviewResponse;
import khaliliyoussef.khalilmovie.model.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Khalil on 6/8/2017.
 */

public interface ApiInterface {
    //define what comes from the query "MovieResponse"
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getMostPopularMovies(@Query("api_key") String apiKey);
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id") long id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") long id, @Query("api_key") String apiKey);

}
