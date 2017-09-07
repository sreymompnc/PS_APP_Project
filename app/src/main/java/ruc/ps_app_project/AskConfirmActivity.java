package ruc.ps_app_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AskConfirmActivity extends AppCompatActivity {

    Button toLogin,toRegister;
    TextView goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_confirm);

        goBack = (TextView)findViewById(R.id.back_to_home);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toLogin = (Button)findViewById(R.id.goToLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toLoginIntent = new Intent(AskConfirmActivity.this,Login.class);
                startActivity(toLoginIntent);
            }
        });

        toRegister = (Button)findViewById(R.id.goToRegister);
        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toRegisterIntent = new Intent(AskConfirmActivity.this,Register.class);
                startActivity(toRegisterIntent);
            }
        });


    }
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }
}
