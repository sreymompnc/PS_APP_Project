package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import cz.msebera.android.httpclient.Header;

public class ChangePasswordActivity extends AppCompatActivity {
    TextView backChangePass,changePassword;
    String port = "http://192.168.1.17:1111/";
    String uri,userLoginID,roleUser;
    EditText currentPass,newPass,confirmPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPass = (EditText)findViewById(R.id.current_password);
        newPass = (EditText)findViewById(R.id.new_pass) ;
        confirmPass = (EditText)findViewById(R.id.confirm_pass) ;

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


                //==========================start change ========================
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams requestParams = new RequestParams();
                requestParams.add("currentpass",crrentpw );
                requestParams.add("password",newpw);

                Toast.makeText(ChangePasswordActivity.this,crrentpw,Toast.LENGTH_LONG).show();
                Toast.makeText(ChangePasswordActivity.this,newpw,Toast.LENGTH_LONG).show();
                Toast.makeText(ChangePasswordActivity.this,userLoginID,Toast.LENGTH_LONG).show();


                client.post(port+uri+userLoginID, requestParams,new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("my test","success");
                        try {
                            String data = new String(responseBody, "UTF-8");
                            try {
                                JSONObject jsonObject = new JSONObject(data);
                                String sms = jsonObject.getString("status");
                                Toast.makeText(ChangePasswordActivity.this,sms,Toast.LENGTH_LONG).show();

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


                //============================End change ========================




            }
        });


    }

    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }

}
