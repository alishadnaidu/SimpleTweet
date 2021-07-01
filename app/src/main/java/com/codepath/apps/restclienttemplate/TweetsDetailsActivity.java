package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button likeButton;
    TextView tvLikeCount;
    public Integer likes;

    boolean isPlay = false;

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
        final Button likeButton = (Button) findViewById(R.id.likeButton);
        tvLikeCount = (TextView) findViewById(R.id.tvLikeCount);


        // it does not go to the details page when I click on the body of the tweet?!?
        // set all the views to be the corresponding tweet or user content/media
        tvScreenNameDetails.setText(tweet.getUser().getName());
        tvUsernameDetails.setText("@" + tweet.getUser().getScreenName());
        tvTweetContentDetails.setText(tweet.getBody());
        relativeTimestampDetails.setText(tweet.getCreatedAt());
        Glide.with(this).load(tweet.getTweetImage()).into(tweetImageDetails);
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImageDetails);
        likes = tweet.getNumberOfLikes();
        tvLikeCount.setText(""+likes);

        likeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tvLikeCount.setText(""+(tweet.getNumberOfLikes() + 1));
                if(isPlay){
                    v.setBackgroundResource(R.drawable.heartoutlinered);
                    tvLikeCount.setText(""+likes);
                }else{
                    v.setBackgroundResource(R.drawable.heartfilledred);
                }

                isPlay = !isPlay; // reverse
            }
        });
    }
}
