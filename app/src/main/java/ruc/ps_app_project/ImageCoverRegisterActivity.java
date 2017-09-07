package ruc.ps_app_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ACER on 9/5/2017.
 */

public class ImageCoverRegisterActivity extends AppCompatActivity{
    Context context;
    ImageView cover;
    TextView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_profile);
        context = ImageCoverRegisterActivity.this;
        cover = (ImageView) findViewById(R.id.imageView_pro);

        SharedPreferences preferLogin = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String userId = preferLogin.getString("userId", "");
        final String userName = preferLogin.getString("userName", "");

        Toast.makeText(ImageCoverRegisterActivity.this, "profile", Toast.LENGTH_SHORT).show();
        final AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.6:8888/users/userProfile/" + userId, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF8");
                    //Log.i("data", data);
                    try {
                        JSONObject obj = new JSONObject(data);
                        //String status = obj.getString("status");
                        JSONArray jArray = obj.getJSONArray("posterProfile");
                        JSONObject poster_data= jArray.getJSONObject(0);
                        String covers = poster_data.getString("cover");
                        // profile poster
                        final String posterUrlImg = "http://192.168.1.6:8888/images/users/" + covers;
                        loadCover(posterUrlImg, cover);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(ImageCoverRegisterActivity.this, "1",Toast.LENGTH_SHORT).show();
                    }
                } catch (UnsupportedEncodingException e) {
                    Toast.makeText(ImageCoverRegisterActivity.this, "2",Toast.LENGTH_SHORT).show();
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
