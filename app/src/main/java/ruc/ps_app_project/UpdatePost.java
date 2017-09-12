package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class UpdatePost extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    TextInputLayout TextInputProName, TextInputPhoneNumber, TextInputAddress, TextInputDescription;
    TextView updatePost,back;
    ImageView imageViewPost;
    EditText pro_title,pro_phone , pro_address, imgDescription;
    String userLoginID;
    Context context;
    String proId;
    String item;
    final List<String> categories = new ArrayList<String>();
    final List<String> cate_id = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_post);

        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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


        //======================for spinner ====================================
        spinner = (Spinner) findViewById(R.id.category_list);
        spinner.setOnItemSelectedListener(this);


        //================================for validation before when text change===================

        // product name required
        pro_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputProName, pro_title,2);
                }else{
                    showMsgError(TextInputProName, pro_title,"Product name is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Email required
        pro_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputPhoneNumber, pro_phone,2);
                }else{
                    showMsgError(TextInputPhoneNumber, pro_phone,"Phone number is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // password required
        pro_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputAddress, pro_address,2);
                }else{
                    showMsgError(TextInputAddress, pro_address,"Password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Confirm pass required
        imgDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputDescription, imgDescription,2);
                }else{
                    showMsgError(TextInputDescription, imgDescription,"Description is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
//                if(confirmPass.equals(password)){
//                    hideMsgError(TextInputConfirmPass, confirmPass,2);
//                }else {
//                    showMsgError(TextInputConfirmPass, confirmPass,"Password is not match!");
//                }
            }
        });
//
        //================================= for get category of spinner================================
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(constraint.url+"posts/categories", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject objectData = new JSONObject(data);
                        JSONArray array = objectData.getJSONArray("categories");
                        int name = array.length();
                        Log.i("Length", String.valueOf(name));
                        for (int i = 0; i < array.length(); i++){
                            JSONObject object = array.getJSONObject(i);
                            String cat_id = object.getString("id");
                            String name_categories = object.getString("cat_name");
                            Log.i("name_categories",name_categories);
                            categories.add(name_categories);
                            cate_id.add(cat_id);

                            // Create an ArrayAdapter using the string array and a default spinner layout
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(UpdatePost.this, android.R.layout.simple_spinner_item, categories);
                            // Specify the layout to use when the list of choices appears
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            // Apply the adapter to the spinner
                            spinner.setAdapter(dataAdapter);

                        }
                        Log.i("list_categories", String.valueOf(categories));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });


        //===========================Start get data old data of post==========================
        final AsyncHttpClient client = new AsyncHttpClient();
        // client.addHeader("header_key", "header value");

        client.get(constraint.url+"posts/postOldDataUpdate/"+proId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("test","success");
                try {
                    String data = new String(responseBody, "UTF-8");
                    Toast.makeText(UpdatePost.this, data, Toast.LENGTH_LONG).show();
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
                        String cat_ids = obj.getString("categories_id");


                        String text = spinner.getSelectedItem().toString();
                        Toast.makeText(UpdatePost.this, text, Toast.LENGTH_LONG).show();

                        pro_title.setText(product_title);
                        pro_phone.setText(product_phone);
                        pro_address.setText(pro_addresses);
                        imgDescription.setText(imgDescriptions);
                        // post image
                        final String productUrlImg = constraint.url+"images/posts/"+obj.getString("pos_image");
                        loadProductImage(productUrlImg,imageViewPost);



                    } catch (Throwable t) {

                        Toast.makeText(UpdatePost.this, "faild1", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(UpdatePost.this, "faild2", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {;

            }
        });

        updatePost = (TextView)findViewById(R.id.update_post);
        updatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                String pro_id = intent.getStringExtra("pro_id");
                int id = spinner.getSelectedItemPosition();
                String cat_id = cate_id.get(id);
//
                // get text form input
                AsyncHttpClient client = new AsyncHttpClient();
                client.addHeader("apikey", "123");
                RequestParams requestParams = new RequestParams();
                requestParams.put("pos_description", String.valueOf(imgDescription.getText()));
                requestParams.put("pos_telephone", String.valueOf(pro_phone.getText()));
                requestParams.put("pos_title", String.valueOf(pro_title.getText()));
                requestParams.put("pos_address", String.valueOf(pro_address.getText()));
                requestParams.put("posters_id", userLoginID);
                requestParams.put("categories_id", cat_id);
                //For add student

                Toast.makeText(getApplicationContext(),pro_id, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),requestParams.toString(), Toast.LENGTH_LONG).show();
                client.post(constraint.url+"posts/updateInfoPost/"+pro_id, requestParams, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String data = new String(responseBody, "UTF-8");
                            //Log.i("get", data);
                            try {
                                JSONObject obj = new JSONObject(data);
                                String status = obj.getString("status");
                                if(status.equals("success")){
                                    Intent intent = new Intent(UpdatePost.this,PosterProfileActivity.class);
                                    startActivity(intent);
                                }else {

                                }
                                System.out.println(obj);
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });
    }

    //============================Method override of spinner ================Toast.makeText(getApplicationContext(),spinner+a, Toast.LENGTH_SHORT).show();=====================
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinner =  adapterView.getItemAtPosition(i).toString();
        String id = String.valueOf(adapterView.getItemIdAtPosition(i));
        String a = cate_id.get(Integer.parseInt(id));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    //===================To load image of post===========================================
    private void loadProductImage(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(1300,1200)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }

    /*
* Set error message
* */
    public static void showMsgError(TextInputLayout textInputLayout, EditText editText, String errText){
        textInputLayout.setError(errText);
        errorStyle(editText, Color.RED);
    }
    /*
    * Hide error message
    * */
    public static void hideMsgError(TextInputLayout textInputLayout, EditText editText, int color){
        textInputLayout.setError("");
        editText.setTextColor(Color.BLACK);
        errorStyle(editText, color);
    }
    /*Text message error style of Edited text */
    public static void errorStyle(EditText edt, int color){
        Drawable drawable = edt.getBackground(); // get current EditText drawable
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP); // change the drawable color

        if(Build.VERSION.SDK_INT > 16) {
            edt.setBackground(drawable); // set the new drawable to EditText
        }else{
            edt.setBackgroundDrawable(drawable); // use setBackgroundDrawable because setBackground required API 16
        }
    }
}
