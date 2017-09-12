package ruc.ps_app_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class PosterProfileActivity extends AppCompatActivity {
    Button btnPost, btn_view_pro, create_post,updatePosterInfo;

    private TextView poster_name,back;
    ListView listViewPosterPost;
    ImageView cover, profile;
    List<String> POST_ID = new ArrayList<>();
    List<String> POSTER_ID = new ArrayList<>();
    List<String> PROFILE = new ArrayList<>();
    List<String> POSTIMAGE = new ArrayList<>();
    List<String> USERNAME = new ArrayList<>();
    List<String> NUMLIKE = new ArrayList<>();
    List<String> NUMCMT = new ArrayList<>();
    List<String> NUMFAV = new ArrayList<>();
    List<String> DESCRIPTION = new ArrayList<>();
    List<String> DATETIME = new ArrayList<>();
    Context context;
    String userPostID,page,posterID;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);
        //==========================for profile==============================================
        poster_name = (TextView)findViewById(R.id.postername);
        cover = (ImageView)findViewById(R.id.cover_poster);
        profile = (ImageView)findViewById(R.id.pro_poster);
        context = PosterProfileActivity.this;

        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        create_post = (Button)findViewById(R.id.create_post);
        updatePosterInfo = (Button)findViewById(R.id.update_info_poster);
        updatePosterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updateInfoPoster = new Intent(PosterProfileActivity.this,EditPosterInfoActivity.class);
                startActivity(updateInfoPoster);
            }
        });



        //---------------------Check where action from- home or menu to open profile poster----------------------
        page =  getIntent().getStringExtra("frompage");
        posterID = getIntent().getStringExtra("userPostId");

        if(page.equals("menupage")){
            //===========================get sharedPreference====================================
            SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
            userId = preferLogin.getString("userId","");
            final String userName = preferLogin.getString("userName","");

        }else{
            userId = posterID;
        }

//        Toast.makeText(PosterProfile.this, userName, Toast.LENGTH_LONG).show();

//        userPostID = getIntent().getStringExtra("userPostId");

//        if(!userId.equals(userPostID)){
//            updatePosterInfo.setVisibility(View.INVISIBLE);
//            create_post.setVisibility(View.INVISIBLE);
//        }

        //============================data of poster==========================================
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", "123");
        client.get(constraint.url+"posters/posterProfile/"+userId, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    //Log.i("data", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        //String status = obj.getString("status");
                        JSONObject poster_data= obj.getJSONObject("posterProfile");

                        String username = poster_data.getString("username");
                        String profiles = poster_data.getString("image");
                        String covers = poster_data.getString("covers");
                        //set text for profile
                        poster_name.setText(username);

//                        Log.i("pstername",poster_name.toString());

//                        // profile poster
                        final String posterUrlImg = constraint.url+"images/posters/"+profiles;
                        loadProfile(posterUrlImg,profile);
                        // post image
                        final String productUrlImg = constraint.url+"images/posters/"+covers;
                        loadProductImage(productUrlImg,cover);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
//====================================
//
//
//
//
//
// ==========for all user post=====================================
        final AsyncHttpClient clients = new AsyncHttpClient();
        clients.get(constraint.url+"posters/viewPosterPost/"+userId, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    //Log.i("data", data);
                    try {
                        Toast.makeText(PosterProfileActivity.this, "yes",Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject(data);
                        Log.i("data_all_post",jsonObj.toString());

                        JSONArray user_data = jsonObj.getJSONArray("posterpost");
                        //Loop all info
                        for(int i = 0; i <= user_data.length(); i++){
                            JSONObject poster_data= user_data.getJSONObject(i);
                            String post_id = poster_data.getString("id");
                            String poster_id = poster_data.getString("posters_id");
                            String image_pro = poster_data.getString("image");
                            String image_pos = poster_data.getString("pos_image");
                            String username = poster_data.getString("username");
                            String description = poster_data.getString("pos_description");
                            String dateTime = poster_data.getString("created_at");
                            String likes = poster_data.getString("numlike");
                            String cmts = poster_data.getString("numcmt");
                            String favs = poster_data.getString("numfavorite");
                            Log.i("hello",favs);
                            //add  each info in to list array
                            POST_ID.add(post_id);
                            POSTER_ID.add(poster_id);
                            POSTIMAGE.add(image_pos);
                            PROFILE.add(image_pro);
                            DESCRIPTION.add(description);
                            USERNAME.add(username);
                            DATETIME.add(dateTime);
                            NUMCMT.add(cmts);
                            NUMFAV.add(favs);
                            NUMLIKE.add(likes);
                        }
                    }catch (JSONException e){
                        Toast.makeText(PosterProfileActivity.this, "no",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                listViewPosterPost = (ListView)findViewById(R.id.listViewPosterPost);
                Log.i("Username", String.valueOf(USERNAME.size()));
                Log.i("DATETIME", String.valueOf(DATETIME.size()));
                Log.i("DESCRIPTION", String.valueOf(DESCRIPTION.size()));
                PosterAdapter customAdapter = new PosterAdapter(getApplicationContext(),POST_ID,POSTER_ID, USERNAME,DATETIME ,DESCRIPTION,PROFILE, POSTIMAGE ,NUMLIKE,NUMFAV,NUMCMT);
                listViewPosterPost.setAdapter(customAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
        //================================ For create post ===========================================


        create_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PosterProfileActivity.this, CreatePost.class);
                startActivity(intent);
            }
        });

        //=================================for view profile =========================================
        profile = (ImageView)findViewById(R.id.pro_poster);
        profile.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to");
                builder1.setCancelable(true);


                builder1.setNegativeButton(
                        "Change Profile",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                Intent intent = new Intent(PosterProfile.this, ChangeProfile.class);
//                                startActivity(intent);
                            }
                        });
                builder1.setPositiveButton(
                        "View Profile",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(PosterProfileActivity.this, ImageProfileActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
        //=================================for view profile =========================================
        cover = (ImageView)findViewById(R.id.cover_poster);
        cover.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Do you want to");
                builder1.setCancelable(true);


                builder1.setNegativeButton(
                        "Change Cover",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                Intent intent = new Intent(PosterProfile.this, ChangeProfile.class);
//                                startActivity(intent);
                            }
                        });
                builder1.setPositiveButton(
                        "View Cover",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(PosterProfileActivity.this, ImageCoverPosterActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });


    }


    //============================ To load image of profile==============================================
    private void loadProfile(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(800,800)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }

    //============================== To load image of post================================================
    private void loadProductImage(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(2000,2000)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }


}

