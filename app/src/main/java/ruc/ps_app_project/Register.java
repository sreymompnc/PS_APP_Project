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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class Register extends AppCompatActivity {
    Button btn_register , btn_login;
    EditText username, email, password, confirmPass;
    String user = "";
    TextView back,goToLogin;
    TextInputLayout TextInputConfirmPass, TextInputPassword, TextInputUsername, TextInputEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RadioButton simpleRadioButton = (RadioButton) findViewById(R.id.radio_seller); // initiate a radio button
        Boolean RadioButtonState = simpleRadioButton.isChecked(); // check current state of a radio button (true or false).
        if (RadioButtonState == true){
            //==================Sharepreference user role=============================
            SharedPreferences userPref = getSharedPreferences("userRole", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userPref.edit();
            editor.putString("user","seller");
            editor.commit();
            user = "seller";

        }
        back = (TextView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        goToLogin = (TextView)findViewById(R.id.btnToRegister);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(Register.this,Login.class);
                startActivity(toLoginIntent);
            }
        });

        TextInputConfirmPass = (TextInputLayout)findViewById(R.id.TextInputConfirmPass);
        TextInputPassword = (TextInputLayout)findViewById(R.id.TextInputPass);
        TextInputUsername = (TextInputLayout)findViewById(R.id.TextInputUserName);
        TextInputEmail = (TextInputLayout)findViewById(R.id.TextInputEmail);
        email = (EditText) findViewById(R.id.reg_email);
        password = (EditText) findViewById(R.id.reg_password);
        username = (EditText) findViewById(R.id.reg_name);
        confirmPass = (EditText) findViewById(R.id.reg_confirmpassword);

        // username required
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputUsername, username,2);
                }else{
                    showMsgError(TextInputUsername, username,"User name is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Email required
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputEmail, email,2);
                }else{
                    showMsgError(TextInputEmail, email,"Email is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // password required
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0)
                {
                    hideMsgError(TextInputPassword, password,2);
                }else{
                    showMsgError(TextInputPassword, password,"Password is required!");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        // Confirm pass required
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
            public void afterTextChanged(Editable editable) {
//                if(confirmPass.equals(password)){
//                    hideMsgError(TextInputConfirmPass, confirmPass,2);
//                }else {
//                    showMsgError(TextInputConfirmPass, confirmPass,"Password is not match!");
//                }
            }
        });

        btn_register = (Button)findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputConfirmPass = (TextInputLayout)findViewById(R.id.TextInputConfirmPass);
                TextInputPassword = (TextInputLayout)findViewById(R.id.TextInputPass);
                TextInputUsername = (TextInputLayout)findViewById(R.id.TextInputUserName);
                TextInputEmail = (TextInputLayout)findViewById(R.id.TextInputEmail);
                email = (EditText) findViewById(R.id.reg_email);
                password = (EditText) findViewById(R.id.reg_password);
                username = (EditText) findViewById(R.id.reg_name);
                confirmPass = (EditText) findViewById(R.id.reg_confirmpassword);

                //For validation
                String mail = email.getText().toString();
                String name = username.getText().toString();
                String pass = password.getText().toString();
                String con_Pass = confirmPass.getText().toString();
                //Check validation
                Boolean checkData = false;
                if(name.length()== 0){
                    showMsgError(TextInputUsername, username, "User name is required!");
                    checkData = true;
                }else{
                    hideMsgError(TextInputUsername, username,2);
                }
                if(mail.length()==0){
                    showMsgError(TextInputEmail, email,"Email is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, email,2);
                }
                if(!emailValidator(email.getText().toString())){
                    showMsgError(TextInputEmail, email,"Email is invalid!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputEmail, email,2);
                }
                if(pass.length()==0){
                    showMsgError(TextInputPassword, password,"Password is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputPassword, password,2);
                }
                if(con_Pass.length()==0){
                    showMsgError(TextInputConfirmPass, confirmPass,"Confirm password is required!");
                    checkData = true;
                }else {
                    hideMsgError(TextInputConfirmPass, confirmPass,2);
                }
                if(con_Pass.equals(pass)){
                    hideMsgError(TextInputConfirmPass, confirmPass,2);
                }else {
                    showMsgError(TextInputConfirmPass, confirmPass,"Password is not match!");
                    checkData = true;
                }

                if(checkData.equals(false)){
                    if (user.equals("seller")) {
                        //==================Sharepreference user role=============================
                        SharedPreferences userPref = getSharedPreferences("userRole", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("user","seller");
                        editor.commit();

                        // get text form input
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.addHeader("apikey", "123");
                        RequestParams requestParams = new RequestParams();
                        requestParams.add("email", String.valueOf(email.getText()));
                        requestParams.add("password", String.valueOf(password.getText()));
                        requestParams.add("username", String.valueOf(username.getText()));
                        requestParams.add("confirmPass", String.valueOf(confirmPass.getText()));
                        //For add student
                        client.post(constraint.url+"posters/register", requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    String data = new String(responseBody, "UTF-8");
                                    try {
                                        JSONObject obj = new JSONObject(data);
                                        String status = obj.getString("status");
                                        if (status.equals("success")) {
                                            JSONObject poster_data= obj.getJSONObject("posters");
                                            String username = poster_data.getString("username");
                                            String id = poster_data.getString("id");

                                            SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("userId",id);
                                            editor.putString("userName",username);
                                            editor.commit();

                                            Intent intent = new Intent(Register.this, HomeActivity.class);
                                            startActivity(intent);
                                        } else if (status.equals("fail")){
                                            showMsgError(TextInputEmail, email,"Email is already used!");
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
                    } else if (user.equals("buyer")) {
                        //==================Sharepreference user role=============================
                        SharedPreferences userPref = getSharedPreferences("userRole", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userPref.edit();
                        editor.putString("user","buyer");
                        editor.commit();


                        // get text form input
                        AsyncHttpClient client = new AsyncHttpClient();
                        client.addHeader("apikey", "123");
                        RequestParams requestParams = new RequestParams();
                        requestParams.add("email", String.valueOf(email.getText()));
                        requestParams.add("password", String.valueOf(password.getText()));
                        requestParams.add("username", String.valueOf(username.getText()));
                        requestParams.add("confirmPass", String.valueOf(confirmPass.getText()));
                        //For add student
                        client.post(constraint.url+"users/register", requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                try {
                                    String data = new String(responseBody, "UTF-8");
                                    try {
                                        JSONObject obj = new JSONObject(data);
                                        String status = obj.getString("status");

                                        if (status.equals("success")) {
                                            JSONObject poster_data= obj.getJSONObject("users");
                                            String username = poster_data.getString("username");
                                            String id = poster_data.getString("id");
                                            SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("userId",id);
                                            editor.putString("userName",username);
                                            editor.commit();
                                            Intent intent = new Intent(Register.this, HomeActivity.class);
                                            startActivity(intent);
                                        } else if(status.equals("fail")) {
                                            showMsgError(TextInputEmail, email,"Email is already used!");
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
                                Toast.makeText(Register.this, "register failed", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(Register.this, "Failed", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(Register.this, Register.class);
                        startActivity(intent);
                    }

                }else {
                    Toast.makeText(Register.this, "Failed", Toast.LENGTH_LONG).show();

                }
                }



        });

        // Menu custom
        Toolbar mDetailToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mDetailToolBar);
    }

    // ---------------- start Overflow menu ---------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.menu_login, menu);
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
            case R.id.toLogin:
                if (item.isChecked()) item.setChecked(false);
                else item.setChecked(true);

                Intent updateIntent = new Intent(Register.this,Login.class);
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
