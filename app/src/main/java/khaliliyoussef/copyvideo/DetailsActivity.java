package khaliliyoussef.copyvideo;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import khaliliyoussef.copyvideo.model.Movie;

public class DetailsActivity extends AppCompatActivity {
    Movie movie;
TextView title, overView, rating,releaseDate;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initCollapsingToolbar();
        imageView= (ImageView) findViewById(R.id.thumbnail_image_holder);
        title = (TextView) findViewById(R.id.titleDetails);
        overView = (TextView) findViewById(R.id.plotDetails);
        rating = (TextView) findViewById(R.id.ratingDetails);
        releaseDate= (TextView) findViewById(R.id.releaseDetails);
        Intent intent = getIntent();
        movie=intent.getParcelableExtra(Intent.EXTRA_TEXT);
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

    }

    private void initCollapsingToolbar() {

final CollapsingToolbarLayout collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setEnabled(true);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShown=false;
            int scrollRange=-1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
if(scrollRange==1)
{
    scrollRange=appBarLayout.getTotalScrollRange();

}
if(scrollRange==0)
{
    collapsingToolbarLayout.setTitle(getString(R.string.movie_details));
    isShown=false;
}else if (isShown){

    collapsingToolbarLayout.setTitle("");
    isShown=false;
}

            }
        });
    }
}
