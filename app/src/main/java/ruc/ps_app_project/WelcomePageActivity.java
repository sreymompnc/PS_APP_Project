package ruc.ps_app_project;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class WelcomePageActivity extends AppCompatActivity {
    Button welcome_page;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        Drawable loginActivityBackground = findViewById(R.id.activity_forgot_pass).getBackground();
        //loginActivityBackground.setAlpha(100);
        welcome_page = (Button)findViewById(R.id.welcome_page);
        welcome_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomePageActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}
