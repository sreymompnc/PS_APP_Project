package ruc.ps_app_project;

import android.content.ClipData;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Chamroeurn on 9/4/2017.
 */

class CommentSingleton {
    private static CommentSingleton ourInstance = null;
    private CommentSingleton() {}
    private List<ClipData.Item> items;
    private String messages ;
    String port = "http://192.168.1.17:1111/";
    static CommentSingleton getInstance() {
        if(ourInstance == null){
            ourInstance = new CommentSingleton();
        }
        return ourInstance;
    }



    //==============================Start comment product=============================================

    public void commentPost(final CommentListAdapter adapter, String userLoginID, String postID, String cmtSms) {
        messages = cmtSms;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.add("users_id",userLoginID);
        requestParams.add("posts_id",postID);
        requestParams.add("message",cmtSms);


        client.post(port+"posts/comment", requestParams, new AsyncHttpResponseHandler() {
            @Override

            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {

                        JSONObject jsonObject = new JSONObject(data);

                        String sms = jsonObject.getString("status");

                        if(sms.equals("success")){

                            adapter.notifyDataSetChanged();
                        }
                        Log.i("kkk",sms);


                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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

    //===============================End comment product =============================================




}
