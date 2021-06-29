package com.codepath.apps.restclienttemplate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";
    private final int REQUEST_CODE = 20;

    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter adapter;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        client = TwitterApp.getRestClient(this);

        // find the recycler view
        rvTweets = findViewById(R.id.rvTweets);
        //initalize the list of tweets and adapter
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        // configure the recycler view: layout manager and the adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(adapter);
        populateHomeTimeline();

        //logout button functionality (navigate backwards to the login activity, app forgets who is logged in)
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.clearAccessToken();
                finish();
            }
        });
    }

    //code for ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // must return true for menu to be displayed
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // if the compose icon is tapped, navigate to the compose activity
        if (item.getItemId() == R.id.compose) {
            Intent intent = new Intent(this, ComposeActivity.class);
            //use startActivityForResult to pass back the tweet to the timeline from compose activity
            startActivityForResult(intent, REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // add the new tweet to the recycler view on the timeline
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        // RESULT_OK means result has succeeded
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            // get data from the intent (tweet)
            Tweet tweet = Parcels.unwrap(data.getParcelableExtra("tweet"));
            // update the recycler view with this new tweet
            // modify data source of tweets
            tweets.add(0, tweet);
            // update the adapter
            adapter.notifyItemInserted(0);
            // add this so we don't have to scroll up to see the new tweet
            rvTweets.smoothScrollToPosition(0);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweets.addAll(Tweet.fromJSONArray(jsonArray));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "onFailure" + response, throwable);

            }
        });
    }
}