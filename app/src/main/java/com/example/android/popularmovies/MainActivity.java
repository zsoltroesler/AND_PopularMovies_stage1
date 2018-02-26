package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.MovieRelated.Movie;
import com.example.android.popularmovies.MovieRelated.MovieAdapter;
import com.example.android.popularmovies.MovieRelated.MovieLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>{

    /** Tag for the log messages */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /** API key to be able to run query on themoviedb.org data set */
    private static final String API_KEY = BuildConfig.API_KEY;

    /** base URL for movie list data from the themoviedb.org data set */
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private static final int MOVIE_LOADER_ID = 1;
    private List<Movie> movieList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView emptyStateTextView;
    private MovieAdapter movieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "TEST: MainActivity onCreate() called...");

        //Assignments
        recyclerView = findViewById(R.id.rv_movie_list);
        progressBar = findViewById(R.id.pb_loading_indicator);
        emptyStateTextView = findViewById(R.id.tv_empty_state);
        progressBar.setVisibility(View.VISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(this, movieList);
        recyclerView.setAdapter(movieAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, establish connection
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, this);

        } else {
            // First, hide loading indicator so error message will be visible
            progressBar.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(getString(R.string.no_internet_connection));
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: MainActivity onCreateLoader() called...");

        // Get the "order by" preference in order to list the movies according to popularity
        // or to top rated
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.pref_key_order),
                getString(R.string.pref_default_order));

        // Create the query URL and pass it into MovieLoader as String
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(orderBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);

        return new MovieLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "TEST: MainActivity onLoadFinished() called...");

        // Hide loading indicator because the data has been loaded
        progressBar.setVisibility(View.GONE);

        // Clear the movieAdapter from the previous data
        movieAdapter.setMovieList(null);

        // If there is a valid list of {@link Movie}, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (data != null && !data.isEmpty()) {
            movieList = data;
            movieAdapter.setMovieList(data);
            movieAdapter.notifyDataSetChanged();
        } else {
            emptyStateTextView.setText(getString(R.string.no_movies));
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(LOG_TAG, "TEST: MainActivity onLoadReset() called...");
        movieAdapter.setMovieList(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to calculate dynamically to number of columns of gridlayout
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }
}
