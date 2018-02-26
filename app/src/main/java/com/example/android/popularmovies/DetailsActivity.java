package com.example.android.popularmovies;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.MovieRelated.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 20.02.2018.
 */

public class DetailsActivity extends AppCompatActivity {

    // Base URLs as string for backdrop and poster images
    private static final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    // Binding the views with Butter Knife
    @BindView(R.id.tv_title) TextView movieTitle;
    @BindView(R.id.iv_backdrop) ImageView movieBackdrop;
    @BindView(R.id.iv_poster_details) ImageView moviePoster;
    @BindView(R.id.tv_release_date) TextView movieDate;
    @BindView(R.id.tv_rate) TextView movieRate;
    @BindView(R.id.tv_overview) TextView movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        // Get the data from the intent
        Bundle data = getIntent().getExtras();
        Movie movie = data.getParcelable("movie details");

        // Set the movie title on the view
        movieTitle.setText(movie.getMovieTitle());

        // Set the backdrop image on the view
        String finalBackdropUrl = getImageUrl(BASE_BACKDROP_URL, movie.getMovieImageBackDropPath());
        Picasso.with(this)
                .load(finalBackdropUrl)
                // Designed by starline / Freepik
                .placeholder(R.drawable.empty_placeholder)
                // Designed by Freepik
                .error(R.drawable.error_placeholder)
                .into(movieBackdrop);

        // Set the poster image on the view
        String finalPosterUrl = getImageUrl(BASE_POSTER_URL, movie.getMovieImagePosterPath());
        Picasso.with(this)
                .load(finalPosterUrl)
                // Designed by starline / Freepik
                .placeholder(R.drawable.empty_placeholder)
                // Designed by Freepik
                .error(R.drawable.error_placeholder)
                .into(moviePoster);

        // Set the release date on the view
        movieDate.setText(movie.getMovieDate());

        // Set the movie rate on the view
        movieRate.setText(String.valueOf(movie.getMovieRate()));

        // Set the movie overview on the view
        movieOverview.setText(movie.getMovieOverview());
    }

    // Helper method to get the full image URL as a string
    private String getImageUrl(String baseUrl, String posterPath) {
        Uri baseUri = Uri.parse(baseUrl);
        Uri.Builder posterUri = baseUri.buildUpon();
        posterUri.appendEncodedPath(posterPath);
        String posterUrlString = posterUri.toString();

        return posterUrlString;
    }
}
