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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class ConfirmEmailChangePassActivity extends AppCompatActivity {
    TextView backConfirmMail,confirmEmail;
    String roleUser;
    String uri,userLoginID;
    EditText inputEmail;
    TextInputLayout TextInputEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_email_change_pass);

        inputEmail = (EditText)findViewById(R.id.current_mail);
        TextInputEmail = (TextInputLayout)findViewById(R.id.validate_comfirmEmail);



        //=====================user role======================
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

        //============================user login id=======================
        SharedPreferences prefProfile = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefProfile.getString("userId","");


        backConfirmMail = (TextView)findViewById(R.id.back_confirm_email_changePass);
        backConfirmMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //----------------------required----------
        // Email required
        inputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputEmail, inputEmail,2);
                }else{
                    showMsgError(TextInputEmail, inputEmail,"Email is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        //----------------------end required--------

        confirmEmail = (TextView)findViewById(R.id.confirm_email_changePass);
        confirmEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmmail = inputEmail.getText().toString();

                if(roleUser.equals("buyer")){
                    uri = "users/confirmUserEmail/";
                }else if(roleUser.equals("seller")){
                    uri = "posters/confirmPosterEmail/";
                }else{
                    Toast.makeText(ConfirmEmailChangePassActivity.this,"End user",Toast.LENGTH_LONG).show();
                }

                Boolean checkData = false;
                // Email validation
                //Email
                if(confirmmail.length()==0){
                    showMsgError(TextInputEmail, inputEmail,"Email is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, inputEmail,2);
                }
                // invalid email
                if(!emailValidator(confirmmail)){
                    showMsgError(TextInputEmail, inputEmail,"Email is invalid!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, inputEmail,2);
                }

                //---------------------------------start confirm email--------------------

                if(checkData.equals(false)){

                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("email", confirmmail);

                    client.post(constraint.url+uri+userLoginID, requestParams,new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.i("my test","success");
                            try {
                                String data = new String(responseBody, "UTF-8");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    String sms = jsonObject.getString("status");
                                    if(sms.equals("success")){
                                        Intent changePasswordIntent = new Intent(ConfirmEmailChangePassActivity.this,ChangePasswordActivity.class);
                                        startActivity(changePasswordIntent);
                                    }else if(sms.equals("fail")){
                                        showMsgError(TextInputEmail, inputEmail,"Email doses not exist!");
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
                }


                //----------------------------------End confirm email---------------------







            }// end confirm click
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
