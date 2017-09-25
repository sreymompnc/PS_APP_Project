package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class EditUserActivity extends AppCompatActivity {
    EditText user_name,user_mail,user_phone,user_add;
    String userLoginID;
    TextView btnUpdate,back_update;
    TextInputLayout TextInputConfirmPhone, TextInputAdd, TextInputUsername, TextInputEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        user_name = (EditText)findViewById(R.id.user_name);
        user_mail = (EditText)findViewById(R.id.user_email);
        user_phone = (EditText)findViewById(R.id.user_phone);
        user_add = (EditText)findViewById(R.id.user_add);


        TextInputConfirmPhone = (TextInputLayout)findViewById(R.id.TextInputPhoneU);
        TextInputAdd = (TextInputLayout)findViewById(R.id.TextInputAddU);
        TextInputUsername = (TextInputLayout)findViewById(R.id.TextInputUserNameU);
        TextInputEmail = (TextInputLayout)findViewById(R.id.TextInputEmailU);

        //-------------------Back--------------------------
        back_update = (TextView)findViewById(R.id.udpate_user_back);
        back_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        SharedPreferences prefUserLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefUserLogin.getString("userId","");



        //------------------------Start get data old data of user
        AsyncHttpClient client = new AsyncHttpClient();
        // client.addHeader("header_key", "header value");

        client.get(constraint.url+"users/userProfile/"+userLoginID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("test","success");
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray jArray = jsonObj.getJSONArray("posterProfile");
                        JSONObject obj = jArray.getJSONObject(0);

                        String userName = obj.getString("username");
                        String usereEmail = obj.getString("email");
                        String userPhone = obj.getString("phone");
                        String useradd = obj.getString("address");
                        String idInfo = obj.getString("id");

                        Log.i("ddd",idInfo);

                        user_name.setText(userName);
                        user_mail.setText(usereEmail);
                        user_phone.setText(userPhone);
                        user_add.setText(useradd);




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

        //--------------------------End get old user ------------------------------


        //=====================================Require to check validation -========================

        // username required
        user_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputUsername, user_name,2);
                }else{
                    showMsgError(TextInputUsername, user_name,"User name is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Email required
        user_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputEmail, user_mail,2);
                }else{
                    showMsgError(TextInputEmail, user_mail,"Email is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        // phone required
        user_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputConfirmPhone, user_phone,2);
                }else{
                    showMsgError(TextInputConfirmPhone, user_phone,"Phone is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // address required
        user_add.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputAdd, user_add,2);
                }else{
                    showMsgError(TextInputAdd, user_add,"Address is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        //---------------------------start update user-----------------------------
        btnUpdate = (TextView) findViewById(R.id.btnUpdateUser);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AsyncHttpClient client = new AsyncHttpClient();

                String newUserName = user_name.getText().toString();
                String newEmail = user_mail.getText().toString();
                String newPhone = user_phone.getText().toString();
                String newAdd = user_add.getText().toString();

                Boolean checkData = false;
                //======================Start check validation of input===================
                if(newUserName.length()== 0){
                    checkData = true;
                    showMsgError(TextInputUsername,user_name, "User name is required!");
                }else{
                    hideMsgError(TextInputUsername, user_name,2);
                }
                //Email
                if(newEmail.length()==0){
                    showMsgError(TextInputEmail, user_mail,"Email is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, user_mail,2);
                }

                if(!emailValidator(newEmail)){
                    showMsgError(TextInputEmail, user_mail,"Email is invalid!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, user_mail,2);

                }

                //Phone
                if(newPhone.length()==0){
                    showMsgError(TextInputConfirmPhone, user_phone,"Phone is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputConfirmPhone, user_phone,2);
                }

                //Phone
                if(newAdd.length()==0){
                    showMsgError(TextInputAdd, user_add,"Address is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputAdd, user_add,2);

                }

                //===========================End check validation of input==========



                if(checkData.equals(false)){
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("username", newUserName);
                    requestParams.add("email",newEmail);
                    requestParams.add("phone",newPhone);
                    requestParams.add("address",newAdd);

                    client.post(constraint.url+"users/updateUserInfo/"+userLoginID, requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String data = new String(responseBody, "UTF-8");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    String sms = jsonObject.getString("status");
                                    if(sms.equals("success")){
                                        Toast.makeText(EditUserActivity.this,"success",Toast.LENGTH_LONG).show();
                                        Intent backProfileUser = new Intent(EditUserActivity.this,RegisterProfile.class);
                                        startActivity(backProfileUser);
                                    }
                                    else if(sms.equals("fail")){
                                        //
                                        Toast.makeText(EditUserActivity.this,"No data change",Toast.LENGTH_LONG).show();
                                        Intent backProfileUser = new Intent(EditUserActivity.this,RegisterProfile.class);
                                        startActivity(backProfileUser);
                                    }else{
                                        showMsgError(TextInputEmail, user_mail,"This email is already used!");
                                    }


                                } catch (Throwable t) {
                                    t.printStackTrace();
                                }
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            try {
                                String data = new String(responseBody, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                    });



            }else {
                Toast.makeText(EditUserActivity.this, "Update is fail", Toast.LENGTH_LONG).show();

            }
            }
        });



        //---------------------------End update user-----------------------------


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
    //Is valid email
    public  boolean emailValidator(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }


    //===========Back method==========
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }




}
