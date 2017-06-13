package khaliliyoussef.copyvideo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import khaliliyoussef.copyvideo.R;
import khaliliyoussef.copyvideo.model.Review;

/**
 * Created by Khalil on 6/8/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MovieViewHolder> {

    private static List<Review> reviews;

    private static Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
                TextView reviewDetails;


        public MovieViewHolder(View v) {
            super(v);
            reviewAuthor = (TextView) v.findViewById(R.id.review_author);
            reviewDetails = (TextView) v.findViewById(R.id.review_details);

        }
    }

    public ReviewAdapter(Context context, List<Review> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_card, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        holder.reviewAuthor.setText(reviews.get(position).getAuthor());
        holder.reviewDetails.setText(reviews.get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}