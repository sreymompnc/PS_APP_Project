package ruc.ps_app_project;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
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
    List<String> POSTERID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);


        gridViewFavorites = (GridView)findViewById(R.id.displayFavorite);

        gridViewFavorites.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent toDetail = new Intent(FavoritePageActivity.this,PostDetailActivity.class);
                toDetail.putExtra("userPostId",POSTERID .get(i).toString());
                toDetail.putExtra("productId",POST_ID .get(i).toString());
                toDetail.putExtra("favId",FAVORITEID .get(i).toString());
                toDetail.putExtra("page","favoritepage");

                startActivity(toDetail);

            }
        });
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
        POSTERID = new ArrayList<>();

        // call AsynTask to perform network operation on separate thread
        new FavoritePageActivity.HttpAsyncTask().execute(constraint.url+"users/viewUserFavorite/"+userLoginID);

    }


    // To get API url
    public static String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jArray = jsonObj.getJSONArray("users");
                for(int i=0; i < jArray.length(); i++){
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String favID = jsonObject.getString("id");
                    String userID  = jsonObject.getString("users_id");
                    String postID = jsonObject.getString("posts_id");
                    String postImage = jsonObject.getString("pos_image");
                    String postTitle = jsonObject.getString("pos_title");
                    String posterID = jsonObject.getString("posters_id");

                    FAVORITEID.add(favID);
                    USERID.add(userID);
                    POST_ID.add(postID);
                    FAVORITEIMAGE.add(postImage);
                    POSTTITLE.add(postTitle);
                    POSTERID.add(posterID);

                }


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "No data!", Toast.LENGTH_LONG).show();

            }

            FavoriteGridViewAdapter customeFavoriteAdapter = new FavoriteGridViewAdapter(getApplicationContext(),FAVORITEID,USERID,POST_ID,POSTTITLE, FAVORITEIMAGE);
            gridViewFavorites.setAdapter(customeFavoriteAdapter);


        }
    }



}
