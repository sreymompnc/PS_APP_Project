package ruc.ps_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

public class ConfirmCodeActivity extends AppCompatActivity {
    Button btn_continue_reset;
    EditText verifyCode;
    String port = "http://192.168.1.17:1111/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        verifyCode = (EditText)findViewById(R.id.con_code) ;
        btn_continue_reset = (Button)findViewById(R.id.btn_continue_reset);
        btn_continue_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestParams requestParams = new RequestParams();
                requestParams.add("confirmcode",String.valueOf(verifyCode).toString());
//                if ("confirmcode".equals(verifyCode)){
                    AsyncHttpClient goToResetPass = new AsyncHttpClient();
                    goToResetPass.post(port+"users/resetForgotPass", requestParams, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            Intent intent = new Intent(ConfirmCodeActivity.this, ResetPasswordActivity.class);
//                            startActivity(intent);
                            Toast.makeText(ConfirmCodeActivity.this,"Correct!!",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Toast.makeText(ConfirmCodeActivity.this,"Incorrect!!",Toast.LENGTH_SHORT).show();
                        }
                    });
//                }else{
//                    Toast.makeText(ConfirmCodeActivity.this,"Verify Code Wrong!",Toast.LENGTH_SHORT).show();
//                }




            }
        });
    }
}
