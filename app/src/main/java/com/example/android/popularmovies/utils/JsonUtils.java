package com.example.android.popularmovies.utils;

/**
 * Created by Zsolt on 20.02.2018.
 */

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmovies.MovieRelated.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility functions to handle JSON data from themoviedb.org API.
 */
public class JsonUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    public static final String KEY_RESULTS = "results";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_DATE = "release_date";
    public static final String KEY_RATE = "vote_average";

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Movie> parseMovieJson(String json) throws JSONException {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        List<Movie> movieList = new ArrayList<>();

        // Try to parse the JSON response.
        try {
            JSONObject rootJsonObject = new JSONObject(json);
            JSONArray results = rootJsonObject.optJSONArray(KEY_RESULTS);

            // For each movie in the results, create a {@link Movie} object
            for (int i = 0; i < results.length(); i++) {

                // Get a single movie at position i within the results array
                JSONObject singleMovie = results.getJSONObject(i);

                // Extract the value for the key called "title"
                String movieTitle = singleMovie.optString(KEY_TITLE);

                // Extract the value for the key called "poster_path"
                String movieImagePoster = singleMovie.optString(KEY_POSTER_PATH);

                // Extract the value for the key called "backdrop_path"
                String movieImageBackdrop = singleMovie.optString(KEY_BACKDROP_PATH);

                // Extract the value for the key called "overview"
                String movieOverview = singleMovie.optString(KEY_OVERVIEW);

                // Extract the value for the key called "release_date"
                String movieDate = singleMovie.optString(KEY_DATE);

                // Extract the value for the key called "vote_average"
                double movieRate = singleMovie.optDouble(KEY_RATE);

                Movie movie = new Movie(movieTitle, movieImagePoster, movieImageBackdrop, movieOverview, movieDate, movieRate);

                // Add the Movie object to the list
                movieList.add(movie);
            }
        }
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON response", e);
        }
        return movieList;
    }
}
