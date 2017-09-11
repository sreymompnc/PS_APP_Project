package ruc.ps_app_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class FavoritePageActivity extends AppCompatActivity {
    TextView favoritePage;
    String userLoginID;
    GridView gridViewFavorites;
    List<String> FAVORITEID ;
    List<String> USERID ;
    List<String> POST_ID;
    List<String> FAVORITEIMAGE ;
    List<String> POSTTITLE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);


        gridViewFavorites = (GridView)findViewById(R.id.displayFavorite);
        //=============================back ===========================
        favoritePage = (TextView)findViewById(R.id.back_favorite);
        favoritePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //===========================get sharedPreference====================================
        SharedPreferences prefProfile = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefProfile.getString("userId","");

        FAVORITEID = new ArrayList<>();
        USERID = new ArrayList<>();
        POST_ID = new ArrayList<>();
        FAVORITEIMAGE = new ArrayList<>();
        POSTTITLE = new ArrayList<>();


        //==================================Start display======================
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(constraint.url+"users/viewUserFavorite/"+userLoginID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray jArray = jsonObj.getJSONArray("users");
                        for(int i=0; i < jArray.length(); i++){
                            JSONObject jsonObject = jArray.getJSONObject(i);
                            String favID = jsonObject.getString("id");
                            String userID  = jsonObject.getString("users_id");
                            String postID = jsonObject.getString("posts_id");
                            String postImage = jsonObject.getString("pos_image");
                            String postTitle = jsonObject.getString("pos_title");

                            FAVORITEID.add(favID);
                            USERID.add(userID);
                            POST_ID.add(postID);
                            FAVORITEIMAGE.add(postImage);
                            POSTTITLE.add(postTitle);

                        }


                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("my test","Fail");
                Toast.makeText(FavoritePageActivity.this,"fail",Toast.LENGTH_LONG).show();
                try {
                    String data = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }


        });

        FavoriteGridViewAdapter customeFavoriteAdapter = new FavoriteGridViewAdapter(getApplicationContext(),FAVORITEID,USERID,POST_ID,POSTTITLE, FAVORITEIMAGE);
        gridViewFavorites.setAdapter(customeFavoriteAdapter);
        //==================================End display========================

    }
}
