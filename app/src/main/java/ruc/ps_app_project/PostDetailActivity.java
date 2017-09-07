package ruc.ps_app_project;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.HttpGet;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

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

public class PostDetailActivity extends AppCompatActivity {
    String userLoginID,roleUser,cmtSms;
    TextView poster,postDate,productName,productPrice,productDis,productDes,phone,email,address;
    Button btnLike,btnFav,btnCmt;
    ImageView posterProfile,postImage;
    TextView detail_back;
    Context context;
    final Context contextDialog = this;
    ListView commentListview;
    LinearLayout hideBtnComment;
    TextView commentPost;
    EditText messages;
    private String  productPostID,userPostID ;
    private CommentListAdapter detailCommentList;
    String port = "http://192.168.1.17:1111/";

    List<String> cmtuser,cmtdate,cmtprofile,cmtsms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        commentListview = (ListView)findViewById(R.id.listCmt);




        // ---------------------------Menu custom--------------------------
        Toolbar mDetailToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mDetailToolBar);
        // --------------------------End-Menu custom--------------------------

        //===================to hide comment ===========
        hideBtnComment = (LinearLayout)findViewById(R.id.btnComment);
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");
//        if(!roleUser.equals("buyer")) {
//            hideBtnComment.setVisibility(View.INVISIBLE);
//        }
        //--------------------------End hide comment-----


        poster = (TextView)findViewById(R.id.posterName);
        postDate = (TextView)findViewById(R.id.postDate);
        productName = (TextView)findViewById(R.id.pName);
        productPrice = (TextView)findViewById(R.id.pPrice);
        productDis = (TextView)findViewById(R.id.pDiscount) ;
        productDes = (TextView)findViewById(R.id.pDes) ;
        phone = (TextView)findViewById(R.id.cPhone);
        email = (TextView)findViewById(R.id.cMail);
        address = (TextView)findViewById(R.id.cAddress);

        btnLike = (Button)findViewById(R.id.btnlike);
        btnFav = (Button)findViewById(R.id.btnfavorite) ;
        btnCmt = (Button)findViewById(R.id.btncommentt);

        posterProfile = (ImageView)findViewById(R.id.detail_circle_image);
        postImage = (ImageView)findViewById(R.id.detailImage);

        // ----------------------get intent ==================================
        productPostID = getIntent().getStringExtra("productId");
        userPostID = getIntent().getStringExtra("userPostId");

        SharedPreferences prefProfile = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefProfile.getString("userId","");
        Toast.makeText(PostDetailActivity.this,productPostID,Toast.LENGTH_LONG).show();
        Toast.makeText(PostDetailActivity.this,userLoginID,Toast.LENGTH_LONG).show();

        //=============================back to home ===========================
        detail_back = (TextView)findViewById(R.id.back_postDetail);
        detail_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        //------------------------Start get data detail of post
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(port+"posts/postDetail/"+productPostID, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("my test","success");
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray jArray = jsonObj.getJSONArray("posts");
                        JSONObject objJson = jArray.getJSONObject(0);

                        poster.setText(objJson.getString("poster"));
                        postDate.setText(objJson.getString("created_at"));

                        productName.setText(objJson.getString("pos_title"));
                        productPrice.setText("$"+objJson.getString("price"));
                        productDis.setText(objJson.getString("discount")+"%");
                        productDes.setText(objJson.getString("pos_description"));
                        phone.setText(objJson.getString("pos_telephone"));
                        email.setText(objJson.getString("postermail"));
                        address.setText(objJson.getString("pos_address"));

                        btnLike.setText(objJson.getString("numlike"));
                        btnFav.setText(objJson.getString("numfavorite"));
                        btnCmt.setText(objJson.getString("numcmt"));

                        // profile poster
                        final String posterUrlImg = port+"images/posters/"+objJson.getString("posterprofile");
                        loadProfile(posterUrlImg,posterProfile);
                        // post image
                        final String productUrlImg = port+"images/posts/"+objJson.getString("pos_image");
                        loadProductImage(productUrlImg,postImage);


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
                try {
                    String data = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });


        //--------------------------End detail post----------------------------------------------------------


        //------------------------------Start comment--------------------------------------------------------

        commentPost = (TextView)findViewById(R.id.sendCmt);
        messages = (EditText)findViewById(R.id.cmtMessage);

        messages.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    commentPost.setAlpha(0.0f);
                    commentPost.setEnabled(false);

                } else {
                    commentPost.setEnabled(true);
                    commentPost.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(PostDetailActivity.this,"kkkk",Toast.LENGTH_SHORT).show();

                            cmtSms = messages.getText().toString();
                            CommentSingleton.getInstance().commentPost(detailCommentList, userLoginID,productPostID,cmtSms);


                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}

        });

        //------------------------------End comment----------------------------------------------------------



        cmtuser = new ArrayList<String>();
        cmtdate = new ArrayList<String>();
        cmtsms = new ArrayList<String>();
        cmtprofile = new ArrayList<String>();

        // call AsynTask to perform network operation on separate thread
        new HttpAsyncTask().execute(port+"posts/listcomment/"+productPostID);


    }

    // To load image of profile
    private void loadProfile(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(800,800)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }

    // To load image of post
    private void loadProductImage(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(1300,1200)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }



    // ---------------- start Overflow menu ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       if(userPostID.equals(userLoginID)){
          MenuInflater mMenuInflater = getMenuInflater();
            mMenuInflater.inflate(R.menu.menu_detail, menu);
           return true;
        }
        return true;
    }


    /**
     * Menu overflow
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.update_post:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                Intent updateIntent = new Intent(PostDetailActivity.this,UpdatePost.class);
                startActivity(updateIntent);
                return true;
            case R.id.delete_post:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                final Dialog dialog = new Dialog(contextDialog);
                dialog.setContentView(R.layout.custom_dialog);

                // btn delete student
                Button deleteStu = (Button) dialog.findViewById(R.id.btnDelete);
                // if button is clicked, it will delete this post
                deleteStu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Action
                    }
                });

                // btn dismiss button
                Button cancel = (Button) dialog.findViewById(R.id.btnCancel);
                // if button is clicked, close the custom dialog
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // ---------------- End Overflow menu ---------------------

    //===============================================start display comment=========================


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
                JSONArray jArray = jsonObj.getJSONArray("posts");
                Log.i("hello","true");

                for(int i=0; i < jArray.length(); i++){
                    JSONObject jsonObject = jArray.getJSONObject(i);

                    String userCmt = jsonObject.getString("commentuser");
                    String cmtDate = jsonObject.getString("cmtdate");
                    String cmt = jsonObject.getString("sms");
                    String usercmtProfile = jsonObject.getString("usercmtprofile");

                    cmtuser.add(userCmt);
                    cmtdate.add(cmtDate);
                    cmtsms.add(cmt);
                    cmtprofile.add(usercmtProfile);
                    Log.i("comment data",cmtprofile.toString());



                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getBaseContext(), "null!", Toast.LENGTH_LONG).show();

            }

             detailCommentList = new CommentListAdapter(getApplicationContext(),
                    cmtuser, cmtdate, cmtsms, cmtprofile);
            commentListview.setAdapter(detailCommentList);
            setListViewHeightBasedOnChildren(commentListview);

        }
    }

    //===========================================End display comment===============================

    //=========================to solve display listview in detail ===========================
    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView  ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, Toolbar.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    //================================to solve display listview in detail ============================


}
