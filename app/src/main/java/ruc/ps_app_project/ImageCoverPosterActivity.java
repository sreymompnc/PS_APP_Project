package ruc.ps_app_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import url.constraint;

/**
 * Created by ACER on 9/5/2017.
 */

public class ImageCoverPosterActivity extends AppCompatActivity {
    Context context;
    ImageView cover;
    TextView back;
    String coverName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_profile);
        context = ImageCoverPosterActivity.this;
        cover = (ImageView) findViewById(R.id.imageView_pro);
        coverName = getIntent().getStringExtra("viewImageCover");

        // profile poster
        final String posterUrlImg = constraint.url+"images/posters/" + coverName;
        loadCover(posterUrlImg, cover);
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    //============================ To load image of profile==============================================
    private void loadCover(String url,ImageView imgView){
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
