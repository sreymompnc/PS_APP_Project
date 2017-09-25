package ruc.ps_app_project;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.io.File;
import java.io.FileNotFoundException;
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
    ImageView cover, profile, imageChange;
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
    final Context contextDialog = this;

    public static final int RESULT_LOAD_IMAGE = 10;
    String picturePath = "";
    String IdUser,UserName;
    String profiles,covers;
    String userPostID,page,posterID;
    String userId;
    String checkUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);
       //=========================== Get share preference ======================================
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

        //==========================for profile============================================
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
                updateInfoPoster.putExtra("userPostId",userPostID);
                startActivity(updateInfoPoster);
            }
        });

        //ask permission for read image
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }



//===========================get sharedPreference====================================
        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userId = preferLogin.getString("userId","");
        final String userName = preferLogin.getString("userName","");

        //---------------------Check where action from- home or menu to open profile poster----------------------
      //  page =  getIntent().getStringExtra("frompage");
        posterID = getIntent().getStringExtra("userPostId");
        if( getIntent().hasExtra("frompagehome")){
            checkUserID = posterID;
        }else{
            checkUserID = userId;
        }

        //=======================check owner or not and hide button===============
        userPostID = getIntent().getStringExtra("userPostId");
//       if(!userPostID.equals(userId)){
//            Toast.makeText(PosterProfileActivity.this,"homepage",Toast.LENGTH_SHORT).show();
//            create_post.setVisibility(View.INVISIBLE);
//            updatePosterInfo.setVisibility(View.INVISIBLE);
//        }

        if(getIntent().hasExtra("menuProfile")){
            create_post.setVisibility(View.VISIBLE);
            updatePosterInfo.setVisibility(View.VISIBLE);
        }else if(!userPostID.equals(userId)){
            checkUserID = userPostID;
            create_post.setVisibility(View.INVISIBLE);
            updatePosterInfo.setVisibility(View.INVISIBLE);
        }


       // Log.i("GetExtraId",userPostID);
//        else if(!userPostID.equals(userId)){
//            Toast.makeText(PosterProfileActivity.this,"frompagehome",Toast.LENGTH_SHORT).show();
//            create_post.setVisibility(View.INVISIBLE);
//            updatePosterInfo.setVisibility(View.INVISIBLE);
//        }


        //============================data of poster==========================================
        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("apikey", "123");

        client.get(constraint.url+"posters/posterProfile/"+checkUserID, new AsyncHttpResponseHandler(){


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
                         profiles = poster_data.getString("image");
                         covers = poster_data.getString("covers");
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
        clients.get(constraint.url+"posters/viewPosterPost/"+checkUserID, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    //Log.i("data", data);
                    try {
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
                builder1.setCancelable(true);

                if(!userPostID.equals(userId)){

                    final Dialog dialog = new Dialog(contextDialog);
                    dialog.setContentView(R.layout.view_image_dialog);

                    TextView viewProfile = (TextView) dialog.findViewById(R.id.view_profile_poster);
                    // if button is clicked, it will delete this post
                    viewProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PosterProfileActivity.this, ImageProfileActivity.class);
                            intent.putExtra("viewImageProfile", profiles);
                            startActivity(intent);
                        }
                    });


                    dialog.show();



                }else{
                    builder1.setNegativeButton(
                            "Change Profile",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    imageUpdate = "profile";
                                    paramUrl = "image";
                                    imageChange = (ImageView)findViewById(R.id.pro_poster) ;
                                    Gallary();

                                }
                            });

                    builder1.setPositiveButton(
                            "View Profile",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(PosterProfileActivity.this, ImageProfileActivity.class);
                                    intent.putExtra("viewImageProfile", profiles);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }



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

                if(!userPostID.equals(userId)){
                    final Dialog dialog = new Dialog(contextDialog);
                    dialog.setContentView(R.layout.cover_poster_image_dialog);

                    TextView viewProfile = (TextView) dialog.findViewById(R.id.view_cover_poster);
                    // if button is clicked, it will delete this post
                    viewProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(PosterProfileActivity.this, ImageCoverPosterActivity.class);
                            intent.putExtra("viewImageCover", covers);
                            startActivity(intent);
                        }
                    });


                    dialog.show();

                }else{
                    builder1.setNegativeButton(
                            "Change Cover",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    imageUpdate = "cover";
                                    paramUrl = "covers";
                                    imageChange = (ImageView) findViewById(R.id.cover_poster);
                                    Gallary();
                                    Toast.makeText(PosterProfileActivity.this,"Clicked!!!",Toast.LENGTH_SHORT).show();
                                }
                            });
                    builder1.setPositiveButton(
                            "View Cover",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(PosterProfileActivity.this, ImageCoverPosterActivity.class);
                                    intent.putExtra("viewImageCover", covers);
                                    startActivity(intent);
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }


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
    //=============================for change image======================================
    public void Gallary(){
        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
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
            imageChange.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            cursor.close();
            ChangeImageProfile();

        }
    }

    public void ChangeImageProfile() {
        RequestParams requestParams = new RequestParams();
        Log.i("getPosterID", userId);
        Toast.makeText(PosterProfileActivity.this, userId, Toast.LENGTH_LONG).show();
        File file = new File(picturePath);
        try {
            requestParams.put(paramUrl, file, "image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(constraint.url + "posters/" + imageUpdate + "/" + userId, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    JSONObject object = new JSONObject(data);
                    String message = object.getString("status");
                    Toast.makeText(PosterProfileActivity.this, "succes", Toast.LENGTH_LONG).show();
                    Log.i("message", message);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(PosterProfileActivity.this, "faild1", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                String bb = "";
                System.out.print("status" + statusCode);


            }
        });


    }
    //===========Back method==========
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }
}

