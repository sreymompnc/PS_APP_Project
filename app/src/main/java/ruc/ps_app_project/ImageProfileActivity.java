package ruc.ps_app_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class ImageProfileActivity extends AppCompatActivity {
    Context context;
    ImageView profile;
    TextView back;
    String profileName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_profile);
        context = ImageProfileActivity.this;
        profile = (ImageView)findViewById(R.id.imageView_pro);

        profileName = getIntent().getStringExtra("viewImageProfile");

        final String posterUrlImg = constraint.url+"images/posters/"+profileName;
        loadProfile(posterUrlImg,profile);



        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    //============================ To load image of profile==============================================
    private void loadProfile(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(800,800)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);
    }
    //============================ for
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

}
