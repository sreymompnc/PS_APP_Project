package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import url.constraint;

//import android.support.v4.widget.SwipeRefreshLayout;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ListView simpleList;
    private Spinner spinner;
    List<String> users;
    List<String> productID,userPostId, postDesc,postPro,postImage,dateAndTime,numeLike,numCmt,numFav,userSaved,userLiked;
    ListView homeListView;
    String roleUser,userLoginID;
    EditText searchValue;
    ImageView image_profile;
    Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView registerAction,loginAction, back, user_name, search,cancelSearch;
    private HomeAdapter homeList,searchList;

//    private SwipeRefreshLayout swipeRefreshLayout;
    Button loadMore;
    int rangePage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = HomeActivity.this;

        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

        SharedPreferences prefUserLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefUserLogin.getString("userId","");
        // -------------------------List view--------------------------
        homeListView = (ListView)findViewById(R.id.simpleListView);
        //Event on ListView



        //-----------drawer bar----------------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //---------------------------Register---------------------------------
        View headerview = navigationView.getHeaderView(0);

        registerAction = (TextView) headerview.findViewById(R.id.action_register);

        registerAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent regIntent = new Intent(HomeActivity.this,Register.class);
                startActivity(regIntent);
            }
        });

        //===========================get sharedPreference====================================
        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userId = preferLogin.getString("userId","");
        final String userName = preferLogin.getString("userName","");
        //=============================for profile===================================
        View header=navigationView.getHeaderView(0);
        user_name = (TextView)header.findViewById(R.id.user_name);
        image_profile = (ImageView)header.findViewById(R.id.image_profile);
        user_name.setText(userName);


        //===============================for image ==================================
        // profile poster
//        final String posterUrlImg = constraint.url+"images/users/"+profiles;
//        loadProfile(posterUrlImg,profile);






        //--------------------------------Login--------------------------------------
        loginAction = (TextView) headerview.findViewById(R.id.action_login);
        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this,"log",Toast.LENGTH_LONG).show();
                Intent regIntent = new Intent(HomeActivity.this,Login.class);
                startActivity(regIntent);
            }
        });





        users = new ArrayList<String>();
        postDesc = new ArrayList<String>();
        postPro = new ArrayList<String>();
        postImage = new ArrayList<String>();
        dateAndTime = new ArrayList<String>();
        numeLike = new ArrayList<String>();
        numCmt = new ArrayList<String>();
        numFav = new ArrayList<String>();
        productID = new ArrayList<String>();
        userPostId = new ArrayList<String>();
        userSaved = new ArrayList<String>();
        userLiked = new ArrayList<String>();
        //------------------------Start get data all of post----------------------
        userPostId = new ArrayList<String>();


// Reguest more data when user click on button load more-----------------------------------
        rangePage = 1;
        loadMore = (Button)findViewById(R.id.buttonLoadMore);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestData(rangePage);
                Toast.makeText(HomeActivity.this,"Load More",Toast.LENGTH_SHORT).show();
            }
        });



        //============================search=======================
        cancelSearch = (TextView)findViewById(R.id.cancelsearch);
        cancelSearch.setVisibility(View.INVISIBLE);


        searchValue = (EditText)findViewById(R.id.search_product);

        searchValue.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                cancelSearch.setVisibility(View.INVISIBLE);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                cancelSearch.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                cancelSearch.setVisibility(View.VISIBLE);

            }
        });

        //---------------------------cancel search-------------------------
        cancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchValue.getText().clear();
                Toast.makeText(HomeActivity.this,"cancel search",Toast.LENGTH_SHORT).show();


                cancelSearch.setVisibility(View.INVISIBLE);
                loadMore.setVisibility(View.VISIBLE);
                
                new HttpAsyncTask().execute(constraint.url+"posts/viewAllPost/"+ rangePage);

            }
        });


        search = (TextView)findViewById(R.id.action_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = searchValue.getText().toString();
                // call AsynTask to perform network operation on separate thread
                new HttpAsyncTaskOfSearch().execute(constraint.url+"posts/search/"+productName);
                Toast.makeText(HomeActivity.this,productName,Toast.LENGTH_SHORT).show();

            }
        });

        //============================End search=======================
//        new HttpAsyncTask().execute(constraint.url+"posts/viewAllPost"+ rangePage);
        requestData(rangePage);
    }

    public void requestData(int rangePage){
        new HttpAsyncTask().execute(constraint.url+"posts/viewAllPost/"+ rangePage);
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
//            swipeRefreshLayout.setRefreshing(true);

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jArray = jsonObj.getJSONArray("data");


                if (jArray.length() > 0) {
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jsonObject = jArray.getJSONObject(i);
                        String name = jsonObject.getString("username");
                        String description = jsonObject.getString("pos_description");
                        String postIds = jsonObject.getString("id");
                        String idUserPost = jsonObject.getString("posters_id");
                        String postProfile = jsonObject.getString("image");
                        String postImg = jsonObject.getString("pos_image");
                        String dateTime = jsonObject.getString("created_at");
                        String likes = jsonObject.getString("numlike");
                        String cmts = jsonObject.getString("numcmt");
                        String favs = jsonObject.getString("numfavorite");
                        String user_save_fav_id = jsonObject.getString("user_fav_id");
                        String user_like_id = jsonObject.getString("user_like_id");



                        users.add(name);
                        postDesc.add(description);
                        postPro.add(postProfile);
                        postImage.add(postImg);
                        dateAndTime.add(dateTime);
                        numeLike.add(likes);
                        numCmt.add(cmts);
                        numFav.add(favs);
                        productID.add(postIds);
                        userPostId.add(idUserPost);
                        userSaved.add(user_save_fav_id);
                        userLiked.add(user_like_id);

                        Log.i("name", productID.toString());

                    }
                    rangePage ++;

                }

            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "not data!", Toast.LENGTH_LONG).show();

            }
            homeList = new HomeAdapter(getApplicationContext(),
                    roleUser,userLoginID,userPostId,productID,users,dateAndTime,
                    postDesc,postPro,postImage,numeLike,numFav,numCmt,userSaved,userLiked);
            homeListView.setAdapter(homeList);
        }
    }

    //================================start search=====================================================

    class HttpAsyncTaskOfSearch extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONArray jArray = jsonObj.getJSONArray("posts");
                Toast.makeText(getBaseContext(), "it is true!", Toast.LENGTH_LONG).show();

                users.clear();
                postDesc.clear();
                postPro.clear();
                postImage.clear();
                dateAndTime.clear();
                numeLike.clear();
                numCmt.clear();
                numFav.clear();
                productID.clear();
                userPostId.clear();
                userSaved.clear();
                userLiked.clear();

                for(int i=0; i < jArray.length(); i++){
                    JSONObject jsonObject = jArray.getJSONObject(i);
                    String name = jsonObject.getString("username");
                    String description = jsonObject.getString("pos_description");
                    String postIds = jsonObject.getString("id");
                    String idUserPost = jsonObject.getString("posters_id");
                    String postProfile = jsonObject.getString("image");
                    String postImg = jsonObject.getString("pos_image");
                    String dateTime = jsonObject.getString("created_at");
                    String likes = jsonObject.getString("numlike");
                    String cmts = jsonObject.getString("numcmt");
                    String favs = jsonObject.getString("numfavorite");
                    String user_save_fav_id = jsonObject.getString("user_fav_id");
                    String user_like_id = jsonObject.getString("user_like_id");




                    users.add(name);
                    postDesc.add(description);
                    postPro.add(postProfile);
                    postImage.add(postImg);
                    dateAndTime.add(dateTime);
                    numeLike.add(likes);
                    numCmt.add(cmts);
                    numFav.add(favs);
                    productID.add(postIds);
                    userPostId.add(idUserPost);
                    userSaved.add(user_save_fav_id);
                    userLiked.add(user_like_id);

                    Log.i("name",productID.toString());
                    loadMore.setVisibility(View.INVISIBLE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "Search no data!", Toast.LENGTH_LONG).show();

                users.clear();
                postDesc.clear();
                postPro.clear();
                postImage.clear();
                dateAndTime.clear();
                numeLike.clear();
                numCmt.clear();
                numFav.clear();


                loadMore.setVisibility(View.INVISIBLE);

            }

            searchList = new HomeAdapter(getApplicationContext(),
                    roleUser,userLoginID,userPostId,productID,users,dateAndTime,postDesc,postPro,postImage,numeLike,numFav,numCmt,userSaved,userLiked);
            homeListView.setAdapter(searchList);



        }
    }

    //================================end search=====================================================



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences preferProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        String userRole = preferProfile.getString("user","");

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(homeIntent);
        } else if (id == R.id.nav_categories) {
            Intent intent = new Intent(HomeActivity.this,CategoriesActivity.class);
            startActivity(intent);
            startActivity(intent);
        } else if (id == R.id.nav_manage_favorite) {
            if(roleUser.equals("buyer")){
                Intent goToFavoritePage = new Intent(HomeActivity.this,FavoritePageActivity.class);
                startActivity(goToFavoritePage);
            }else{
                Intent intent= new Intent(HomeActivity.this, AskConfirmActivity.class);
                startActivity(intent);
            }


        } else if (id == R.id.nav_manage_profile) {

            if(userRole.equals("seller")){
                Toast.makeText(HomeActivity.this, userRole, Toast.LENGTH_LONG).show();
                Intent intent= new Intent(HomeActivity.this, PosterProfileActivity.class);
                intent.putExtra("frompage","menupage");
                startActivity(intent);
            }else if(userRole.equals("buyer")){
                Toast.makeText(HomeActivity.this, userRole, Toast.LENGTH_LONG).show();
                Intent intent= new Intent(HomeActivity.this, RegisterProfile.class);
//                intent.putExtra("isSeller", true);
                startActivity(intent);
            }else {
                    Intent intent= new Intent(HomeActivity.this, AskConfirmActivity.class);
                    startActivity(intent);
            }

        } else if (id == R.id.nav_manage_post) {
                if(userRole.equals("seller")){
                    Intent intent= new Intent(HomeActivity.this, PosterProfileActivity.class);
                    intent.putExtra("frompage","menupage");
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this,"You don't have permisson to access",Toast.LENGTH_SHORT).show();
                }
        } else if (id == R.id.nav_change_password) {
            if(userRole.equals("buyer") || userRole.equals("seller")){
                Intent intent = new Intent(HomeActivity.this,ConfirmEmailChangePassActivity.class);
                startActivity(intent);
            }else{
                Intent intent= new Intent(HomeActivity.this, AskConfirmActivity.class);
                startActivity(intent);
            }
        } else if (id == R.id.nav_Logout){
            if(userRole.equals("buyer") || userRole.equals("seller")) {

                SharedPreferences preferLogout = getSharedPreferences("userRole", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferLogout.edit();
                edit.clear();
                edit.commit();
                /// Clear share Preference
                SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();
                Log.i("Clear", editor.toString());
                Toast.makeText(HomeActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);
            }else{
                Intent intent= new Intent(HomeActivity.this, AskConfirmActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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



}