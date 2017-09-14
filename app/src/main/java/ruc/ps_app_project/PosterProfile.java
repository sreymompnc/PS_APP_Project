package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;
import url.constraint;

public class PosterProfile extends AppCompatActivity {
    Button btnPost;
    CircleImageView imageViewProfile;
    ListView simpleList;
    int flags[] = {R.drawable.flaga, R.drawable.flaga};
    String countryList[] = {"Camboida","India"};
    public static final int RESULT_LOAD_IMAGE = 10;
    String picturePath = "";
    String IdUser,roleUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);

        SharedPreferences pref = getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        IdUser = pref.getString("userId","");
        Log.i("Iduser",IdUser);
        SharedPreferences preProfile = getSharedPreferences("userRole", Context.MODE_PRIVATE);
        roleUser = preProfile.getString("user","");

//############################ Start Change image profile #############################
//        imageViewProfile = (CircleImageView)findViewById(R.id.pro_poster) ;
//        imageViewProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Gallary();
//                createPost();
//            }
//        });
//############################ End Change image profile ###############################

        simpleList = (ListView) findViewById(R.id.simpleListView);
        PosterAdapter customAdapter = new PosterAdapter(getApplicationContext(), countryList, flags);
        simpleList.setAdapter(customAdapter);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            Log.i("selectImgae",selectedImage.toString());
//
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            InputStream fileImage = convertBitmapToInputStream(BitmapFactory.decodeFile(picturePath));
//        }
//    }
//
//    public void Gallary(){
//        Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        startActivityForResult(i, RESULT_LOAD_IMAGE);
//    }
//
//    public InputStream convertBitmapToInputStream(Bitmap bitmap)
//    {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        InputStream inputStream = new ByteArrayInputStream(byteArray);
//        return inputStream;
//    }
//
//
//    public void createPost(){
//        RequestParams requestParams = new RequestParams();
//        File file = new File(picturePath);
//        try {
//            requestParams.put("photo", file, "image/jpeg");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        client.post(constraint.url+"users/profile/"+IdUser, requestParams, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    String data = new String(responseBody, "UTF-8");
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                String bb = "";
//            }
//        });
//    }
}
