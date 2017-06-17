package khaliliyoussef.copyvideo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.List;
import khaliliyoussef.copyvideo.DetailsActivity;
import khaliliyoussef.copyvideo.R;
import khaliliyoussef.copyvideo.model.Movie;
/**
 * Created by Khalil on 6/8/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private static List<Movie> movies;

    private static Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView cardTitle;
                TextView cardRating;
        ImageView cardImage;

        public MovieViewHolder(View v)
        {
            super(v);
            cardTitle = (TextView) v.findViewById(R.id.cardTitle);
            cardRating = (TextView) v.findViewById(R.id.cardRating);
            cardImage = (ImageView) v.findViewById(R.id.cardImage);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Movie ClickDataItems = movies.get(position);
                        Intent intent = new Intent(context, DetailsActivity.class);
                        intent.putExtra(Intent.EXTRA_TEXT, movies.get(position));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        Toast.makeText(v.getContext(), "you clicked" + ClickDataItems.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public MoviesAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public MoviesAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        holder.cardTitle.setText(movies.get(position).getOriginalTitle());
        holder.cardRating.setText(String.valueOf(movies.get(position).getVoteAverage()));
        Picasso.with(context).load(movies.get(position).getPosterPath())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.cardImage);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}