package ruc.ps_app_project;

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
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class ResetPasswordActivity extends AppCompatActivity {
    EditText resetPass,conFirmPass,verifyCode;
    Intent intent;
    String email,newPass,conPass, url;
    Button resetPassword;
    TextInputLayout TextInputVerifyCode, TextInputNewPassword, extInputConfirmPass;
    String port = "http://192.168.1.17:1111/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPass = (EditText)findViewById(R.id.reset_new_pass);
        conFirmPass= (EditText)findViewById(R.id.reset_con_pass);
        verifyCode = (EditText)findViewById(R.id.con_code);
        TextInputVerifyCode = (TextInputLayout)findViewById(R.id.input_layout_confirm_code);
        TextInputNewPassword = (TextInputLayout)findViewById(R.id.input_layout_new_password);
        extInputConfirmPass = (TextInputLayout)findViewById(R.id.input_layout_confirm_new_password);

        // password required
        resetPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputNewPassword, resetPass,2);
                }else{
                    showMsgError(TextInputNewPassword, resetPass,"Password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Confirm pass required
        conFirmPass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(extInputConfirmPass, conFirmPass,2);
                }else{
                    showMsgError(extInputConfirmPass, conFirmPass,"Confirm password is required!");
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
        ///verify code
        verifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputVerifyCode, verifyCode,2);
                }else{
                    showMsgError(TextInputVerifyCode, verifyCode,"Verify code is required!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        resetPassword = (Button)findViewById(R.id.btn_reset_pass);
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newPass = resetPass.getText().toString();
                conPass = conFirmPass.getText().toString();
//
//                Log.i("first",newPass);
//                Log.i("second",conPass);

//                resetPass = (EditText)findViewById(R.id.reset_new_pass);
//                conFirmPass= (EditText)findViewById(R.id.reset_con_pass);
//                verifyCode = (EditTe


                //Check validation
                Boolean checkData = false;
                if(resetPass.length()== 0){
                    showMsgError(TextInputNewPassword, resetPass, "New password is required!");
                }else{
                    hideMsgError(TextInputNewPassword, resetPass,2);
                }
                if(conFirmPass.length()==0){
                    showMsgError(extInputConfirmPass, conFirmPass,"Confirm password is required!");
                    checkData = true;
                }else {
                    hideMsgError(extInputConfirmPass, conFirmPass,2);
                }
                if(verifyCode.length()==0){
                    showMsgError(TextInputVerifyCode, verifyCode,"Verify code is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputVerifyCode, verifyCode,2);
                }
                Log.i("new password",resetPass.toString());
                Log.i("confirm password",conFirmPass.toString());
//                if (!resetPass.equals(conFirmPass)){
//                    showMsgError(extInputConfirmPass, conFirmPass,"Password is not match!");
//                    checkData = true;
//                }else {
//                    hideMsgError(extInputConfirmPass, conFirmPass,2);
//                }

                if(checkData.equals(false)){
                    if(newPass.equals(conPass)){
                        intent = getIntent();
                        email = intent.getStringExtra("extraEmail");
                        url = intent.getStringExtra("url");
                        String getUrl = "";
                        Log.i("url",url);
                        Log.i("email",email);
                        if (url.equals(port+"posters/sendMail")){
                            getUrl = port+"posters/resetForgotPass";
                        }else{
                            getUrl = port+"users/resetForgotPass" ;
                        }
                        RequestParams requestParams = new RequestParams();
                        requestParams.add("email",String.valueOf(email));
                        requestParams.add("confirmcode",String.valueOf(verifyCode.getText().toString()));
                        requestParams.add("password",String.valueOf(conFirmPass.getText().toString()));
                        Log.i("getUrl",getUrl);
                        AsyncHttpClient resetPass = new AsyncHttpClient();
                        resetPass.post(getUrl, requestParams, new AsyncHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    String data = new String(responseBody, "UTF-8");
                                    Log.i("data",data);
                                    try {
                                        JSONObject jsonObject = new JSONObject(data);
                                        String sms = jsonObject.getString("status");
                                        Log.i("message", sms);
                                        if (sms.equals("success")){
                                            Intent intent = new Intent(ResetPasswordActivity.this, Login.class);
                                            startActivity(intent);
                                            Toast.makeText(ResetPasswordActivity.this,"changed!",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(ResetPasswordActivity.this,"not change!!",Toast.LENGTH_SHORT).show();
                                        }

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
                        Toast.makeText(ResetPasswordActivity.this,"Match!!",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ResetPasswordActivity.this,"Password Not Match!",Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }
    /* Set error message
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
