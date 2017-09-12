package ruc.ps_app_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

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


public class CreatePost extends Activity implements OnItemSelectedListener{
    private Spinner spinner;
    TextView savePost, back;
    public static final int RESULT_IMAGE = 10;
    Button btn_upload;
    EditText pro_name, phone_number, address, description;
    String picturePath = "";
    String item;
    final List<String> categories = new ArrayList<String>();
    final List<String> cate_id = new ArrayList<String>();
    TextInputLayout TextInputName, TextInputPhone, TextInputAddress, TextInputDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        savePost = (TextView)findViewById(R.id.savepost);
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //======================for spinner ====================================
        spinner = (Spinner) findViewById(R.id.category_list);
        spinner.setOnItemSelectedListener(this);

        TextInputName = (TextInputLayout)findViewById(R.id.TextInputName);
        TextInputPhone = (TextInputLayout)findViewById(R.id.TextInputPhone);
        TextInputAddress = (TextInputLayout)findViewById(R.id.TextInputAddress);
        TextInputDescription = (TextInputLayout)findViewById(R.id.TextInputDes);
        pro_name = (EditText) findViewById(R.id.pro_title);
        phone_number = (EditText) findViewById(R.id.pro_phone);
        address = (EditText) findViewById(R.id.pro_address);
        description = (EditText) findViewById(R.id.imgDescription);
//================================for validation before when text change===================

        // product name required
        pro_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputName, pro_name,2);
                }else{
                    showMsgError(TextInputName, pro_name,"Product name is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Email required
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputPhone, phone_number,2);
                }else{
                    showMsgError(TextInputPhone, phone_number,"Phone number is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // password required
        address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputAddress, address,2);
                }else{
                    showMsgError(TextInputAddress, address,"Password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Confirm pass required
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputDescription, description,2);
                }else{
                    showMsgError(TextInputDescription, description,"Description is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        savePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final String text = spinner.getSelectedItem().toString();
//                Toast.makeText(CreatePost.this,text,Toast.LENGTH_LONG).show();

                //For validation
                String title = pro_name.getText().toString();
                String phone = phone_number.getText().toString();
                String des = description.getText().toString();
                String add = address.getText().toString();
                //Check validation
                Boolean checkData = false;
                if(title.length()== 0){
                    showMsgError(TextInputName, pro_name, "Product name is required!");
                    checkData = true;
                }else{
                    hideMsgError(TextInputName, pro_name,2);
                }
                if(phone.length()==0){
                    showMsgError(TextInputPhone, phone_number,"Phone number is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputPhone, phone_number,2);
                }
                if(des.length()==0){
                    showMsgError(TextInputDescription, description,"Description is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputDescription, description,2);
                }
                if(add.length()==0){
                    showMsgError(TextInputAddress, address,"Address is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputAddress, address,2);
                }
                if(checkData.equals(false)) {
                    createPost();
                }else {
                    Toast.makeText(CreatePost.this, "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });


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
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(CreatePost.this, android.R.layout.simple_spinner_item, categories);
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




        //====================btn upload image =====================================
        btn_upload = (Button)findViewById(R.id.selectImage);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gallary();
            }
        });
    }

    //============================Method override of spinner =====================================
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String spinner =  adapterView.getItemAtPosition(i).toString();
        String id = String.valueOf(adapterView.getItemIdAtPosition(i));
        String a = cate_id.get(Integer.parseInt(id));

//        Intent cat_id = new Intent(this, CreatePost.class);
//        cat_id.putExtra("catId",a);
        Toast.makeText(getApplicationContext(),spinner+a, Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





    //===============================for image ===================================================
    public void Gallary(){
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_IMAGE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//             InputStream fileImage = convertBitmapToInputStream(BitmapFactory.decodeFile(picturePath));

        }
    }

//===================================function for create post =================================

    public void createPost(){
        //Login sharePreference
        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userId = preferLogin.getString("userId","");
        int id = spinner.getSelectedItemPosition();
        String cat_id = cate_id.get(id);
        Toast.makeText(getApplicationContext(),cat_id, Toast.LENGTH_SHORT).show();
        //        description= (MultiAutoCompleteTextView)findViewById(R.id.postDescription);
        description = (EditText) findViewById(R.id.imgDescription);
        phone_number = (EditText)findViewById(R.id.pro_phone);
        pro_name = (EditText)findViewById(R.id.pro_title);
        address = (EditText)findViewById(R.id.pro_address);

        RequestParams requestParams = new RequestParams();
        requestParams.put("pos_description", String.valueOf(description.getText()));
        requestParams.put("pos_telephone", String.valueOf(phone_number.getText()));
        requestParams.put("pos_title", String.valueOf(pro_name.getText()));
        requestParams.put("pos_address", String.valueOf(address.getText()));
        requestParams.put("posters_id", userId);
        requestParams.put("categories_id", cat_id);
        File file = new File(picturePath);
        try {
            requestParams.put("pos_image", file, "image/jpeg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        requestParams.add("image","");
//        requestParams.add("InputStream","");

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(constraint.url+"posts/createPost", requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    JSONObject obj = new JSONObject(data);
                    String status = obj.getString("status");

                        if(status.equals("success")){
                            Intent intent = new Intent(CreatePost.this,PosterProfileActivity.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(CreatePost.this,CreatePost.class);
                            startActivity(intent);
                        }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
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


