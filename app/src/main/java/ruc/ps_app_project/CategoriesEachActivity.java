package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

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

public class CategoriesEachActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    List<String> users;
    List<String> productID,userPostId, postDesc,postPro,postImage,dateAndTime,numeLike,numCmt,numFav;

    ListView categoryListView;
    String Id,roleUser,userLoginID;
    TextView registerAction,loginAction, back;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_each);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

        SharedPreferences prefUserLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefUserLogin.getString("userId","");

        categoryListView = (ListView)findViewById(R.id.category_list_id);

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

        intent = getIntent();
        Id = intent.getStringExtra("CategoryID");

        Log.i("GetIdFromCate",Id);

        new HttpAsyncTask().execute(constraint.url+"posts/viewEachCategories/"+Id);

//------------------------ Start Go to Category ------------------------------------------
        back = (TextView)findViewById(R.id.btnPostBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onBackPressed();
            }
        });
//------------------------ End Go to Category ------------------------------------------
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
                JSONArray jArray = jsonObj.getJSONArray("data");

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
                    Log.i("name",productID.toString());



                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "not data!", Toast.LENGTH_LONG).show();

            }

            CategoriesEachAdapter categoriesList = new CategoriesEachAdapter(getApplicationContext(),roleUser,userLoginID,userPostId,productID,users,dateAndTime,postDesc,postPro,postImage,numeLike,numFav,numCmt);
            categoryListView.setAdapter(categoriesList);
//
//            homeList.notifyDataSetChanged();

        }
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        SharedPreferences preferProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        String userRole = preferProfile.getString("user","");

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_categories) {
            Intent intent = new Intent(CategoriesEachActivity.this,CategoriesActivity.class);
            startActivity(intent);
            startActivity(intent);
        } else if (id == R.id.nav_manage_favorite) {

        } else if (id == R.id.nav_manage_profile) {

            if(userRole.equals("seller")){
                Intent intent= new Intent(CategoriesEachActivity.this, PosterProfileActivity.class);
                startActivity(intent);
            }else if(userRole.equals("buyer")){
                Intent intent= new Intent(CategoriesEachActivity.this, RegisterProfile.class);
//                intent.putExtra("isSeller", true);
                startActivity(intent);
            }else {
                Intent intent= new Intent(CategoriesEachActivity.this, AskConfirmActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_manage_post) {

        } else if (id == R.id.nav_change_password) {
            if(userRole.equals("buyer") || userRole.equals("seller")){
                Intent intent = new Intent(CategoriesEachActivity.this,ConfirmEmailChangePassActivity.class);
                startActivity(intent);
            }else{
                Intent intent= new Intent(CategoriesEachActivity.this, AskConfirmActivity.class);
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
                Toast.makeText(CategoriesEachActivity.this, "Logout Successful.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CategoriesEachActivity.this, Login.class);
                startActivity(intent);
            }else{
                Intent intent= new Intent(CategoriesEachActivity.this, AskConfirmActivity.class);
                startActivity(intent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
