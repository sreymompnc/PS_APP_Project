package ruc.ps_app_project;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

import static ruc.ps_app_project.CreatePost.RESULT_IMAGE;

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
    String imageUpdate,paramUrl,roleUser;
    Context context;

    public static final int RESULT_LOAD_IMAGE = 10;
    String picturePath = "";
    String IdUser,UserName;

    String userPostID,page,posterID;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);
////############################# Get share preference ######################################
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");
////############################# Get share preference ######################################

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
      
//===========================get sharedPreference====================================
        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userId = preferLogin.getString("userId","");
        final String userName = preferLogin.getString("userName","");
//        if(page.equals("menupage")){
//
//
//        }else{
//            userId = posterID;


       // userPostID = getIntent().getStringExtra("userPostId");
       // Log.i("GetExtraId",userPostID);


        //============================data of poster==========================================
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", "123");


//        client.get(constraint.url+"posters/profile/"+userId, new AsyncHttpResponseHandler(){

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

//                        String id = poster_data.getString("id");
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
//================================for all user post=====================================
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
                        final int user_datas = user_data.length();
                        for(int i = 0; i <= user_datas; i++){
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
                        Log.i("Poster_id", String.valueOf(POST_ID));
                    }catch (JSONException e){
                        Toast.makeText(PosterProfileActivity.this, "no",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }

                listViewPosterPost = (ListView)findViewById(R.id.listViewPosterPost);
                Log.i("UserId", String.valueOf(POST_ID));
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
                                imageUpdate = "profile";
                                paramUrl = "image";
                                Gallary();
//                                Toast.makeText(PosterProfileActivity.this,"Clicked!!!",Toast.LENGTH_SHORT).show();
//                                Intent intent = new Intent(PosterProfileActivity.this, PosterProfileActivity.class);
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
                                imageUpdate = "cover";
                                paramUrl = "covers";
                                Gallary();
                                Toast.makeText(PosterProfileActivity.this,"Clicked!!!",Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            Log.i("selectImgae",selectedImage.toString());

            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//            InputStream fileImage = convertBitmapToInputStream(BitmapFactory.decodeFile(picturePath));
            ChangeImageProfile();

        }
    }

    public void Gallary(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

//    public InputStream convertBitmapToInputStream(Bitmap bitmap)
//    {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        InputStream inputStream = new ByteArrayInputStream(byteArray);
//        return inputStream;
//    }


    public void ChangeImageProfile(){
        RequestParams requestParams = new RequestParams();
        Log.i("getPosterID",posterID);
        File file = new File(picturePath);
        try {
            requestParams.put(paramUrl, file, "image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.i("getPosterID",imageUpdate);
        Log.i("getPosterID",posterID);
        Log.i("getPosterID",paramUrl);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(constraint.url+"posters/"+imageUpdate+"/"+posterID, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    JSONObject object = new JSONObject(data);
                    String message = object.getString("status");
                    Log.i("message", message);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String bb = "";
            }
        });
    }
}

