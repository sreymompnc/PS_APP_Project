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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_profile);
        context = ImageProfileActivity.this;
        profile = (ImageView)findViewById(R.id.imageView_pro);

        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userId = preferLogin.getString("userId","");
        final String userName = preferLogin.getString("userName","");

        Toast.makeText(ImageProfileActivity.this, "profile",Toast.LENGTH_SHORT).show();
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get(constraint.url+"posters/posterProfile/"+userId, new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    //Log.i("data", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        //String status = obj.getString("status");
                        JSONObject poster_data= obj.getJSONObject("posterProfile");
                        String profiles = poster_data.getString("image");
                        // profile poster
                        final String posterUrlImg = constraint.url+"images/posters/"+profiles;
                        loadProfile(posterUrlImg,profile);

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }catch (UnsupportedEncodingException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

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
