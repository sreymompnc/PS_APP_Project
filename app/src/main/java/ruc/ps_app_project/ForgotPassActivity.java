package ruc.ps_app_project;

import android.content.Intent;
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

public class ForgotPassActivity extends AppCompatActivity {
    Button btn_continue;
    EditText email;
    String extraEmail, user, url;
    TextInputLayout TextInputEmail;
    TextView goToLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        RadioButton simpleRadioButton = (RadioButton) findViewById(R.id.radio_seller); // initiate a radio button
        Boolean RadioButtonState = simpleRadioButton.isChecked(); // check current state of a radio button (true or false).
        if (RadioButtonState == true){
            user = "seller";
        }

        TextInputEmail = (TextInputLayout)findViewById(R.id.TextInputEmail);
        email = (EditText)findViewById(R.id.for_email);
        Toast.makeText(ForgotPassActivity.this,"selected",Toast.LENGTH_SHORT).show();

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
            public void afterTextChanged(Editable editable) {

            }
        });
        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                RequestParams requestParams = new RequestParams();
//                requestParams.add("email",String.valueOf(email.getText().toString()));
                extraEmail = email.getText().toString();
                //Check validation
                Boolean checkData = false;
                if(extraEmail.length()==0){
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

                    if(checkData.equals(false)){

                        if (user.equals("seller")) {
                            // get text form input
                            AsyncHttpClient client = new AsyncHttpClient();
//                            client.addHeader("apikey", "123");
                            RequestParams requestParams = new RequestParams();
                            requestParams.add("email", String.valueOf(email.getText()));
//                            requestParams.add("password", String.valueOf(password.getText()));
//                            requestParams.add("username", String.valueOf(username.getText()));
//                            requestParams.add("confirmPass", String.valueOf(confirmPass.getText()));
                            //For add student
                            url = "http://192.168.1.22:2222/posters/sendMail";
                            Log.i("url",url);
                            client.post(url, requestParams, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String data = new String(responseBody, "UTF-8");
                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String status = obj.getString("status");
                                            if (status.equals("success")) {
                                                Intent intent = new Intent(ForgotPassActivity.this, ResetPasswordActivity.class);
                                                intent.putExtra("extraEmail",extraEmail);
                                                intent.putExtra("url",url);
                                                startActivity(intent);
                                            }else{
                                                Toast.makeText(ForgotPassActivity.this,"Wrong Email or Position!",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ForgotPassActivity.this, "register failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else if (user.equals("buyer")) {
                            // get text form input
                            AsyncHttpClient client = new AsyncHttpClient();
//                            client.addHeader("apikey", "123");
                            RequestParams requestParams = new RequestParams();
                            requestParams.add("email", String.valueOf(email.getText()));
//                            requestParams.add("password", String.valueOf(password.getText()));
//                            requestParams.add("username", String.valueOf(username.getText()));
//                            requestParams.add("confirmPass", String.valueOf(confirmPass.getText()));
                            //For add student
                            url = "http://192.168.1.22:2222/users/sendMail";
                            Log.i("url",url);
                            client.post(url, requestParams, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String data = new String(responseBody, "UTF-8");
                                        try {
                                            JSONObject obj = new JSONObject(data);
                                            String status = obj.getString("status");
                                            if (status.equals("success")) {
                                                Intent intent = new Intent(ForgotPassActivity.this, ResetPasswordActivity.class);
                                                intent.putExtra("extraEmail",extraEmail);
                                                intent.putExtra("url",url);
                                                startActivity(intent);
                                            } else{
                                                Toast.makeText(ForgotPassActivity.this,"Wrong Email or Position!",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ForgotPassActivity.this, "register failed", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            Toast.makeText(ForgotPassActivity.this, "Failed", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ForgotPassActivity.this, Register.class);
                            startActivity(intent);
                        }

                    }else {
                        Toast.makeText(ForgotPassActivity.this, "Failed", Toast.LENGTH_LONG).show();
                    }
            }
        });

        goToLogin = (TextView)findViewById(R.id.btnPostBack);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassActivity.this,Login.class);
                startActivity(intent);
            }
        });
    }

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
