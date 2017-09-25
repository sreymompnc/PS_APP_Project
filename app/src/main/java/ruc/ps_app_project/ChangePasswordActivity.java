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

public class ChangePasswordActivity extends AppCompatActivity {
    TextView backChangePass,changePassword;
    String uri,userLoginID,roleUser;
    EditText currentPass,newPass,confirmPass;
    TextInputLayout TextInputCurrentPass, TextInputPass, TextInputConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPass = (EditText)findViewById(R.id.current_password);
        newPass = (EditText)findViewById(R.id.new_pass) ;
        confirmPass = (EditText)findViewById(R.id.confirm_pass) ;


        TextInputCurrentPass = (TextInputLayout)findViewById(R.id.input_layout_changepassword_current_password);
        TextInputPass = (TextInputLayout)findViewById(R.id.input_layout_changepassword_new_password);
        TextInputConfirmPass = (TextInputLayout)findViewById(R.id.input_layout_confirm_password);



        backChangePass = (TextView)findViewById(R.id.back_changePass);
        backChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //=====================user role======================
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

        //============================user login id=======================
        SharedPreferences prefProfile = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        userLoginID = prefProfile.getString("userId","");

        //=======================================required field====================
        // current password required
        currentPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputCurrentPass, currentPass,2);
                }else{
                    showMsgError(TextInputCurrentPass, currentPass,"Current password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // new password required
        newPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputPass, newPass,2);
                }else{
                    showMsgError(TextInputPass, newPass,"New password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });


        // confirm password required
        confirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputConfirmPass, confirmPass,2);
                }else{
                    showMsgError(TextInputConfirmPass, confirmPass,"Confirm password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });




        changePassword = (TextView)findViewById(R.id.confirm_email_changePass);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crrentpw = currentPass.getText().toString();
                String newpw = newPass.getText().toString();
                String confirmpw = confirmPass.getText().toString();

                if(roleUser.equals("buyer")){
                    uri = "users/changepassword/";
                }else if(roleUser.equals("seller")){
                    uri = "posters/changepassword/";
                }

                Boolean checkData = false;
                //======================Start check validation of input===================
                // == current pass======
                if(crrentpw.length()== 0){
                    checkData = true;
                    showMsgError(TextInputCurrentPass,currentPass, "Current password is required!");
                }else{
                    hideMsgError(TextInputCurrentPass, currentPass,2);
                }
                //===== new pass =======
                if(newpw.length()== 0){
                    checkData = true;
                    showMsgError(TextInputPass,newPass, "New password is required!");
                }else{
                    hideMsgError(TextInputPass, newPass,2);
                }
                //========confirm pass======
                if(confirmpw.length()== 0){
                    checkData = true;
                    showMsgError(TextInputConfirmPass,confirmPass, "Confirm password is required!");
                }else{
                    hideMsgError(TextInputConfirmPass, confirmPass,2);
                }

                //===Check confirm password================
                if(confirmpw.equals(newpw)){
                    hideMsgError(TextInputConfirmPass, confirmPass,2);
                }else {
                    showMsgError(TextInputConfirmPass, confirmPass,"Password is not match!");
                    checkData = true;
                }



                if(checkData.equals(false)) {

                    //==========================start change ========================
                    AsyncHttpClient client = new AsyncHttpClient();
                    RequestParams requestParams = new RequestParams();
                    requestParams.add("currentpass", crrentpw);
                    requestParams.add("password", newpw);


                    client.post(constraint.url + uri + userLoginID, requestParams, new AsyncHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.i("my test", "success");
                            try {
                                String data = new String(responseBody, "UTF-8");
                                try {
                                    JSONObject jsonObject = new JSONObject(data);
                                    String sms = jsonObject.getString("status");
                                    if(sms.equals("success")){
                                        Toast.makeText(ChangePasswordActivity.this, sms, Toast.LENGTH_LONG).show();
                                        Intent changePassToHome = new Intent(ChangePasswordActivity.this,HomeActivity.class);
                                        startActivity(changePassToHome);
                                    }else{
                                        Toast.makeText(ChangePasswordActivity.this, sms, Toast.LENGTH_LONG).show();
                                        showMsgError(TextInputCurrentPass, currentPass,"This password does not exist!");
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
                            Log.i("my test", "Fail");
                            try {
                                String data = new String(responseBody, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                //============================End change ========================

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
    //Is valid email
    public  boolean emailValidator(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }


    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }

}
