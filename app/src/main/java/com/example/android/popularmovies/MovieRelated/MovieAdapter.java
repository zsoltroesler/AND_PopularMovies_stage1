package com.example.android.popularmovies.MovieRelated;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.DetailsActivity;
import com.example.android.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Zsolt on 20.02.2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movieList;
    private Context context;
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    /**
     * Create a new {@link MovieAdapter} object.
     *
     * @param context   is the current context (i.e. Activity) that the adapter is being created in.
     * @param movieList is the list of {@link Movie}s to be displayed.
     */
    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView posterView;

        // Public constructor, instantiate all of the references to the private variables
        public ViewHolder(View view) {
            super(view);
            this.posterView = view.findViewById(R.id.iv_poster);
        }
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.card_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder, int position) {
        final Movie movie = movieList.get(position);

        String finalPosterUrl = getImageUrl(BASE_POSTER_URL, movie.getMovieImagePosterPath());
        Picasso.with(context)
                .load(finalPosterUrl)
                // Designed by starline / Freepik
                .placeholder(R.drawable.empty_placeholder)
                // Designed by Freepik
                .error(R.drawable.error_placeholder)
                .into(holder.posterView);

        // Attach an OnClickListener to open a current movie for details
        holder.posterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra("movie details", movie);
                context.startActivity(detailsIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (movieList == null || movieList.isEmpty()) {
            return 0;
        } else {
            return this.movieList.size();
        }
    }

    // Helper method to set new movie list or clear the previous one
    public void setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
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
