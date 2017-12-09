package khaliliyoussef.khalilmovie.api;

import khaliliyoussef.khalilmovie.model.MovieResponse;
import khaliliyoussef.khalilmovie.model.ReviewResponse;
import khaliliyoussef.khalilmovie.model.TrailerResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(@Query("api_key") String apiKey);
    @GET("movie/popular")
    Call<MovieResponse> getMostPopularMovies(@Query("api_key") String apiKey);
    @GET("movie/{sort}")
    Call<MovieResponse> getMovie(@Path("sort") String order, @Query("api_key") String key);
    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getMovieTrailers(@Path("movie_id") long id, @Query("api_key") String apiKey);
    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getMovieReviews(@Path("movie_id") long id, @Query("api_key") String apiKey);


}
