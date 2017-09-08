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
import android.widget.GridView;
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

public class RegisterProfile extends AppCompatActivity {
    String port = "http://192.168.1.27:8888/";
    Button updateUserInfo;
    GridView gridViewFavorite;
    Button btnPost, btn_cancel,btn_change_pro, btn_view_pro;
    TextView register_name,back;
    ListView listViewPosterPost;
    ImageView cover, profile;
    List<String> ID = new ArrayList<>();
    List<String> USERID = new ArrayList<>();
    List<String> POSTER_ID = new ArrayList<>();
    List<String> FAVORITEIMAGE = new ArrayList<>();
    List<String> POSTTITLE = new ArrayList<>();


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile);
        context = RegisterProfile.this;

        //=========================Go to update user ==============
        updateUserInfo = (Button)findViewById(R.id.update_info_user);
        updateUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent updatePosterInfoIntent = new Intent(RegisterProfile.this,EditUserActivity.class);
                startActivity(updatePosterInfoIntent);
            }
        });

//==========================for profile==============================================
        register_name = (TextView)findViewById(R.id.textView_username);
        cover = (ImageView)findViewById(R.id.imageView_cover);
        profile = (ImageView)findViewById(R.id.imageView_profile);

        //===========================get sharedPreference====================================
        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userId = preferLogin.getString("userId","");
        final String userName = preferLogin.getString("userName","");
        Toast.makeText(RegisterProfile.this, userName, Toast.LENGTH_LONG).show();

        //============================data of poster==========================================
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", "123");
        client.get(port+"users/userProfile/"+userId, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    Log.i("data", data);
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray user_data = jsonObj.getJSONArray("posterProfile");
                        //String status = obj.getString("status");
                        JSONObject poster_data= user_data.getJSONObject(0);
                        String register_names = poster_data.getString("username");
                        String profiles = poster_data.getString("image");
                        String covers = poster_data.getString("covers");
                        //set text
                        register_name.setText(register_names);

                        // profile poster
                        final String posterUrlImg = port+"images/users/"+profiles;
                        loadProfile(posterUrlImg,profile);
                        // post image
                        final String productUrlImg = port+"images/users/"+covers;
                        loadProductImage(productUrlImg,cover);

                    }catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(RegisterProfile.this, "failed 1", Toast.LENGTH_LONG).show();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                    Toast.makeText(RegisterProfile.this, "failed 2", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        //==============================================for all favorite post=====================================

        client.get(port+"users/viewUserFavorite/"+userId, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    Log.i("user_data", data);
                    try {
                        Toast.makeText(RegisterProfile.this, "yes",Toast.LENGTH_SHORT).show();
                        JSONObject jsonObj = new JSONObject(data);
                        Log.i("user_data_obj", jsonObj.toString());
                        JSONArray user_data = jsonObj.getJSONArray("users");
                        //Loop all info
                        for(int i = 0; i <= user_data.length(); i++){
                            JSONObject poster_data= user_data.getJSONObject(i);
                            String post_id = poster_data.getString("id");
                            String user_id = poster_data.getString("users_id");
                            String poster_id = poster_data.getString("posters_id");
                            String pos_title = poster_data.getString("pos_title");
                            String image_pos = poster_data.getString("pos_image");
                            //add  each info in to list array
                            ID.add(post_id);
                            USERID.add(user_id);
                            POSTER_ID.add(poster_id);
                            FAVORITEIMAGE.add(image_pos);
                            POSTTITLE.add(pos_title);

                        }
                    }catch (JSONException e){
                        Toast.makeText(RegisterProfile.this, "no",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
                gridViewFavorite = (GridView)findViewById(R.id.gridViewFavorite);
                RegisterAdapter customAdapter = new RegisterAdapter(getApplicationContext(),POSTTITLE, FAVORITEIMAGE);
                gridViewFavorite.setAdapter(customAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        //=================================for view profile =========================================
        profile = (ImageView)findViewById(R.id.imageView_profile);
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
                                Intent intent = new Intent(RegisterProfile.this, ImageProRegisterActivity.class);
                                startActivity(intent);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        //=================================for view profile =========================================
        cover = (ImageView)findViewById(R.id.imageView_cover);
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
                                Intent intent = new Intent(RegisterProfile.this, ImageCoverRegisterActivity.class);
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
