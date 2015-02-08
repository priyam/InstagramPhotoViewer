package com.pc.instagramclient;

import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PhotosActivity extends ActionBarActivity {

    public static final String CLIENT_ID = "1e606bcce9b2496fa684e60e615d534b";

    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        photos = new ArrayList<>();
        //1. Create the adapter linking it to the resource
        aPhotos = new InstagramPhotosAdapter(this, photos);

        //2. find the listview from the layout
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);

        //3. Set the adapter binding it to the listview
        lvPhotos.setAdapter(aPhotos);

        //Send out API Requests to POPULAR PHOTOS
        fetchPopularPhotos();

        /*TextView tvCaptions = (TextView) findViewById(R.id.tvCaption);
        // Create the TypeFace from the TTF asset
        Typeface font = Typeface.createFromAsset(getAssets(), "customfont.ttf");
        // Assign the typeface to the view
        tvCaptions.setTypeface(font);*/

    }


    //Trigger API request
    public void fetchPopularPhotos()
    {
        /*
        - Popular media: https://api.instagram.com/v1/media/popular?access_token=ACCESS-TOKEN
        - Response

        */

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        //Creating the network client
        AsyncHttpClient client = new AsyncHttpClient();

        //Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler(){
            //onSuccess (worked)

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Expecting a JSONObject
                //- Type: { "data" => [x] => "type" } ("image" or "video")
                //- URL: { "data" => [x] => "images" => "standard_resolution" => "url" }
                //- Caption: { "data" => [x] => "caption" => "text" }
                //- Username: { "data" => [x] => "user" => "username"}

                //Iterate each of photo items and decode the item into java object

                JSONArray photosJSON = null;

                try {
                    photosJSON = response.getJSONArray("data");
                    //iterate the array of posts
                    for (int i=0; i < photosJSON.length(); i++){
                        //get the JSONObject at that position
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        //decode the attributes of the JSON into the data model

                        InstagramPhoto photo = new InstagramPhoto();
                        photo.setUsername(photoJSON.getJSONObject("user").getString("username"));
                        photo.setCaption(photoJSON.getJSONObject("caption").getString("text"));
                        photo.setImageUrl(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url"));
                        photo.setImageHeight(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height"));
                        photo.setImageWidth(photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("width"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setUserPicUrl(photoJSON.getJSONObject("user").getString("profile_picture"));
                        photo.setLikesCount(photoJSON.getJSONObject("likes").getInt("count"));
                        photo.setCreatedTime(photoJSON.getLong("created_time"));
                        photos.add(photo);
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
                //callback
                aPhotos.notifyDataSetChanged();
            }

            //onFailure (failed)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
