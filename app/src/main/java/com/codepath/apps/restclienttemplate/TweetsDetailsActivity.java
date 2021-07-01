package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcel;
import org.parceler.Parcels;

public class TweetsDetailsActivity extends AppCompatActivity {
    Tweet tweet;
    TextView tvScreenNameDetails;
    TextView tvUsernameDetails;
    TextView tvTweetContentDetails;
    ImageView ivProfileImageDetails;
    ImageView tweetImageDetails;
    TextView relativeTimestampDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tweets_details_activity);
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra(Tweet.class.getSimpleName()));

        tvScreenNameDetails = (TextView) findViewById(R.id.tvScreenNameDetails);
        tvUsernameDetails = (TextView) findViewById(R.id.tvUsernameDetails);
        tvTweetContentDetails = (TextView) findViewById(R.id.tvTweetContentDetails);
        ivProfileImageDetails = (ImageView) findViewById(R.id.ivProfileImageDetails);
        tweetImageDetails = (ImageView) findViewById(R.id.tweetImageDetails);
        relativeTimestampDetails = (TextView) findViewById(R.id.relativeTimestampDetails);


        // it does not go to the details page when I click on the body of the tweet?!?
        // set all the views to be the corresponding tweet or user content/media
        tvScreenNameDetails.setText(tweet.getUser().getName());
        tvUsernameDetails.setText("@" + tweet.getUser().getScreenName());
        tvTweetContentDetails.setText(tweet.getBody());
        relativeTimestampDetails.setText(tweet.getCreatedAt());
        Glide.with(this).load(tweet.getTweetImage()).into(tweetImageDetails);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImageDetails);




    }
}
