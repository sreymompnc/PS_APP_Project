package ruc.ps_app_project;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import url.constraint;

/**
 * Created by Chamroeurn on 9/4/2017.
 */

class FavoriteSingleton {
    private static FavoriteSingleton ourInstance = null;
    private Context context;
    private FavoriteSingleton() {}
    static FavoriteSingleton getInstance() {

        if(ourInstance == null){
            ourInstance = new FavoriteSingleton();
        }
        return ourInstance;
    }

    //----------------------------------------Start mehtod save favorite------------------
    public void saveFavorite(String userLoginID, String idOfProduct){

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams requestParams = new RequestParams();
        requestParams.add("users_id",userLoginID);
        requestParams.add("posts_id",idOfProduct);


        client.post(constraint.url+"posts/store", requestParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("my test","success");
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObject = new JSONObject(data);

                        String sms = jsonObject.getString("status");
                        if(sms.equals("success")){
                            Toast.makeText(context,"save success",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context,"save fail",Toast.LENGTH_SHORT).show();

                        }


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



    }

    //============================End favorite ========================


}
