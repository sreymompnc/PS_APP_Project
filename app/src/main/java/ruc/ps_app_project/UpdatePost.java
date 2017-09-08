package ruc.ps_app_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class UpdatePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    TextInputLayout TextInputProName, TextInputPhoneNumber, TextInputAddress, TextInputDescription;
    TextView updatePost;
    ImageView imageViewPost;
    EditText pro_title,pro_phone , pro_address, imgDescription;
    String userLoginID;
    Context context;
    String proId;
    String item;
    String port = "http://192.168.1.27:8888/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);
        //==================get by id==============================
        imageViewPost = (ImageView)findViewById(R.id.imageViewPost);

        TextInputProName = (TextInputLayout)findViewById(R.id.input_pro_name);
        TextInputPhoneNumber = (TextInputLayout)findViewById(R.id.input_layout_phone);
        TextInputAddress = (TextInputLayout)findViewById(R.id.input_layout_address);
        TextInputDescription = (TextInputLayout)findViewById(R.id.input_layout_description);

        pro_title = (EditText)findViewById(R.id.pro_title);
        pro_phone = (EditText)findViewById(R.id.pro_phone);
        pro_address = (EditText)findViewById(R.id.pro_address);
        imgDescription = (EditText)findViewById(R.id.imgDescription);
        imageViewPost = (ImageView)findViewById(R.id.imageViewPost);

        proId = getIntent().getStringExtra("pro_id");
        SharedPreferences prefUserLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefUserLogin.getString("userId","");
        //===========================Start get data old data of post==========================
        AsyncHttpClient client = new AsyncHttpClient();
        // client.addHeader("header_key", "header value");

        client.get(port+"posters/postOldDataUpdate/"+proId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("test","success");
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray jArray = jsonObj.getJSONArray("postInfo");
                        JSONObject obj = jArray.getJSONObject(0);

                        String product_title = obj.getString("pos_title");
                        String product_phone = obj.getString("pos_telephone");
                        String pro_addresses = obj.getString("pos_address");
                        String imgDescriptions = obj.getString("pos_description");
                        String imageViewPosts = obj.getString("pos_image");
                        String idSellerInfo = obj.getString("id");

                        pro_title.setText(product_title);
                        pro_phone.setText(product_phone);
                        pro_address.setText(pro_addresses);
                        imgDescription.setText(imgDescriptions);
                        // post image
                        final String productUrlImg = port+"images/posts/"+obj.getString("pos_image");
                        loadProductImage(productUrlImg,imageViewPost);



                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("test data","Fail");
                try {
                    String data = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });


        // Spinner element
        spinner = (Spinner) findViewById(R.id.category_list);
        updatePost = (TextView)findViewById(R.id.savepost);
        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
}
