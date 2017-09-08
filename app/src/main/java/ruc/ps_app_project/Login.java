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
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class Login extends AppCompatActivity {
    String user = "";
    Button btnLogin;
    EditText logEmail, logPassword;
    TextView forgetPassword,register,back;
    TextInputLayout TextInputEmail,TextInputPassword;
    String port = "http://192.168.1.27:8888/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextInputEmail = (TextInputLayout)findViewById(R.id.input_layout_name);
        TextInputPassword = (TextInputLayout)findViewById(R.id.input_layout_password) ;
        logEmail = (EditText)findViewById(R.id.log_email);
        logPassword = (EditText)findViewById(R.id.log_password);
        RadioButton simpleRadioButton = (RadioButton) findViewById(R.id.radio_seller); // initiate a radio button
        Boolean RadioButtonState = simpleRadioButton.isChecked(); // check current state of a radio button (true or false).
        if (RadioButtonState == true){
            user = "seller";
        }

            // Email required
        logEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputEmail, logEmail,2);
                }else{
                    showMsgError(TextInputEmail, logEmail,"Email is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // password required
        logPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputPassword, logPassword,2);
                }else{
                    showMsgError(TextInputPassword, logPassword,"Password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Menu custom
        Toolbar mDetailToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mDetailToolBar);

        //Start login process

        btnLogin = (Button) findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                

            Log.i("userStatus", user);
                //For validation
            String mail = logPassword.getText().toString();
            String pass = logPassword.getText().toString();

                Boolean checkData = false;
                if(mail.length()==0){
                    showMsgError(TextInputEmail, logEmail,"Email is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, logEmail,2);
                }
                if(!emailValidator(logEmail.getText().toString())){
                    showMsgError(TextInputEmail, logEmail,"Email is invalid!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, logEmail,2);
                }
                if(pass.length()==0){
                    showMsgError(TextInputPassword, logPassword,"Password is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputPassword, logPassword,2);
                }
                if(checkData.equals(false)) {
                    if (user.equals("seller")) {
                        //==================Sharepreference user role=============================
                        SharedPreferences userPref = getSharedPreferences("userRole", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("user","seller");
                        editor.commit();


                        RequestParams requestParams = new RequestParams();
                        requestParams.add("email",String.valueOf(logEmail.getText().toString()));
                        requestParams.add("password",String.valueOf(logPassword.getText().toString()));
                        Log.i("input",requestParams.toString());
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post(port+"posters/login", requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                                try {
                                    String data = new String(responseBody, "UTF-8");
                                    Log.i("Data", data);
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        String checkStatus = object.getString("status");
                                        Log.i("checkStatus",checkStatus);
                                        String aa = "";

                                        if (checkStatus.equals("success")){
                                            JSONArray objectUser = object.getJSONArray("data");
                                            Log.i("object", objectUser.toString());
                                            JSONObject objUser = objectUser.getJSONObject(0);
                                            String username = objUser.getString("username");
                                            String id = objUser.getString("id");

                                            SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("userId", id);
                                            editor.putString("userName", username);
                                            editor.commit();

                                            Intent goHome = new Intent(Login.this, HomeActivity.class);
                                            startActivity(goHome);
                                            Toast.makeText(Login.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(Login.this, "Email or Password is wrong!", Toast.LENGTH_SHORT).show();
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                                try {
                                    String data = new String(responseBody, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(Login.this, "Login failse!!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (user.equals("buyer")) {
                        //==================Sharepreference user role=============================
                        SharedPreferences userPref = getSharedPreferences("userRole", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("user","buyer");
                        editor.commit();

                        RequestParams requestParams = new RequestParams();
                        requestParams.add("email",String.valueOf(logEmail.getText().toString()));
                        requestParams.add("password",String.valueOf(logPassword.getText().toString()));
                        Log.i("input",requestParams.toString());
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.post(port+"users/login", requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                                try {
                                    String data = new String(responseBody, "UTF-8");
                                    Log.i("Data", data);
                                    try {
                                        JSONObject object = new JSONObject(data);
                                        String checkStatus = object.getString("status");
                                        Log.i("checkStatus",checkStatus);
                                        String aa = "";

                                        if (checkStatus.equals("success")){
                                            JSONArray objectUser = object.getJSONArray("data");
                                            Log.i("object", objectUser.toString());
                                            JSONObject objUser = objectUser.getJSONObject(0);
                                            String username = objUser.getString("username");
                                            String id = objUser.getString("id");

                                            SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("userId", id);
                                            editor.putString("userName", username);
                                            editor.commit();

                                            Intent goHome = new Intent(Login.this, HomeActivity.class);
                                            startActivity(goHome);
                                            Toast.makeText(Login.this, "Login Success!!", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(Login.this, "Email or Password is wrong!", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    Toast.makeText(Login.this, "Error", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

                                try {
                                    String data = new String(responseBody, "UTF-8");
//                                Toast.makeText(MainActivity.this,"Add fale", Toast.LENGTH_SHORT).show();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(Login.this, "Login failse!!", Toast.LENGTH_SHORT).show();
                            }


                        });
                    }else{
                        Toast.makeText(Login.this, "Please check your position!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
//--------------------------- Start Go To Forget Password ----------------------------------
        forgetPassword = (TextView)findViewById(R.id.action_forgotpw);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgetpassword = new Intent(Login.this,ForgotPassActivity.class);
                startActivity(forgetpassword);
                Toast.makeText(Login.this,"Clicked!!",Toast.LENGTH_SHORT).show();
            }
        });
//--------------------------- End Go To Forget Password ----------------------------------
//--------------------------- Start Go To Register Form ----------------------------------
        register = (TextView)findViewById(R.id.btnToRegister);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgetpassword = new Intent(Login.this,Register.class);
                startActivity(forgetpassword);
            }
        });
//--------------------------- End Go To Register Form ----------------------------------
//--------------------------- Start Go To Home ----------------------------------
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, HomeActivity.class);
                startActivity(intent);
            }
        });
//--------------------------- End Go To Home ----------------------------------
    }


    // ---------------- start Overflow menu ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_register, menu);
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
            case R.id.toRegiter:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                Intent updateIntent = new Intent(Login.this,Register.class);
                startActivity(updateIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // ---------------- End Overflow menu ---------------------

    // ---------Checkbox-----------
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

//        if(radiobtn.setChecked(true))
//        {
//            Toast.makeText(ForgotPassActivity.this,"selected",Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(ForgotPassActivity.this,"Not selected",Toast.LENGTH_SHORT).show();
//        }

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_seller:
                if (checked)
                    user = "seller";
                break;
            case R.id.radio_buyer:
                if (checked)
                    user = "buyer";
                break;
        }
    }
    //-------------End checkbox----

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
    //Is valid email
    public  boolean emailValidator(final String mailAddress) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();

    }

}
