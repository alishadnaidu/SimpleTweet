package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet {

    public String body;
    public String createdAt;
    public User user;
    public String relativeTimestamp;
    public String tweetImage;
    public int numberOfLikes;

    //empty constructor needed by Parceler Library
    public Tweet() {}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        tweet.body = jsonObject.getString("full_text");
        //convert the "created at" date into relative timestamp
        //tweet.createdAt is now the relative timestamp (ex. "3 seconds ago")
        tweet.createdAt = getRelativeTimestamp(jsonObject.getString("created_at"));
        tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.numberOfLikes = jsonObject.getInt("favorite_count");

        // must go into the entities JSONObject to get to media
        JSONObject entities = jsonObject.getJSONObject("entities");
        // only set tweet.tweetImage to the url of the image in the tweet if the tweet contains an image
        // if tweet does not have media (aka image), just set the tweetImage to an empty string
        if (entities.has("media")) {
            tweet.tweetImage = entities.getJSONArray("media").getJSONObject(0).getString("media_url_https");
        }
        else {
            tweet.tweetImage = "";
        }
        return tweet;
    }

    //method to convert raw date from API to relative timestamp
    //source: https://gist.github.com/nesquena/f786232f5ef72f6e10a7
    public static String getRelativeTimestamp(String createdAt) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAt).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length();  i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));

        }
        return tweets;
    }

    // getter methods, used in tweets details activity

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTweetImage() {
        return tweetImage;
    }

    public User getUser() { return user; }

    public int getNumberOfLikes() { return numberOfLikes; }
}
