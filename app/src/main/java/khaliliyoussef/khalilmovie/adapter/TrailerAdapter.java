package khaliliyoussef.khalilmovie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import khaliliyoussef.khalilmovie.R;
import khaliliyoussef.khalilmovie.model.Trailer;

/**
 * Created by Khalil on 6/8/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MovieViewHolder> {

    private static List<Trailer> trailers;

    private static Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
                TextView trailerTitle;
                ImageView trailerImage;

        public MovieViewHolder(View v) {
            super(v);
            trailerTitle = (TextView) v.findViewById(R.id.trailer_title);
            trailerImage = (ImageView) v.findViewById(R.id.trailer_image);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Trailer trailer=trailers.get(position);
                        String videoKey=trailer.getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+videoKey));
                        intent.putExtra("VIDEO_ID", videoKey);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.trailers = trailers;
        this.context = context;
    }

    @Override
    public TrailerAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        holder.trailerTitle.setText(trailers.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }
}