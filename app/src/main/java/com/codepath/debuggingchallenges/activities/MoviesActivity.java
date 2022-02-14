package com.codepath.debuggingchallenges.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.debuggingchallenges.R;
import com.codepath.debuggingchallenges.adapters.MoviesAdapter;
import com.codepath.debuggingchallenges.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import okhttp3.Headers;

public class MoviesActivity extends AppCompatActivity {

    private static final String API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed";
    public static final String TAG = "MoviesActivity";

    RecyclerView rvMovies;
    MoviesAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        //RecyclerView rvMovies;
        //MoviesAdapter adapter;                          //adapter must be known to fetchMovies() function

        rvMovies = findViewById(R.id.rvMovies);

        //issue1: must initialize array before using it
        movies = new ArrayList<>();

        //issue 2: make use of adapter, adapter has already been initialized
        adapter = new MoviesAdapter(movies);        //MoviesAdapter constructor only takes 1 parameter

        //fixed following (issue 2): above
        // Create the adapter to convert the array to views
        //MoviesAdapter adapter = new MoviesAdapter(movies);

        // Attach the adapter to a ListView
        rvMovies.setAdapter(adapter);

        //issue 3: set the LayOutManger
        rvMovies.setLayoutManager(new LinearLayoutManager(this));

        fetchMovies();
    }


    private void fetchMovies() {
        String url = " https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY;        //API_KEY must be part of the URL
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON response) {
                Log.d(TAG, "onSuccess");
                //JSONObject response = json.jsonObject;
                try {
                    JSONArray moviesJson = response.jsonObject.getJSONArray("results");
                    Log.i(TAG, "results " + moviesJson.toString());
                    //movies = Movie.fromJSONArray(moviesJson);

                    movies.addAll(Movie.fromJSONArray(moviesJson));
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "Movies: " + movies.size());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(MoviesActivity.class.getSimpleName(), "Error retrieving movies: ", throwable);
            }
        });
    }
}
