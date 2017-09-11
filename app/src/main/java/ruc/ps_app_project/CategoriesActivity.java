package ruc.ps_app_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

public class CategoriesActivity extends AppCompatActivity {
    List<String> postCategoryName,categoriesId;
    ListView categoriesListView;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        postCategoryName = new ArrayList<String>();
        categoriesId = new ArrayList<String>();


//        categoriesListView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(constraint.url+"posts/categories", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject jsonObj = new JSONObject(data);
                        JSONArray jArray = jsonObj.getJSONArray("categories");
                        int length = jArray.length();
                        Log.i("arrayObject", String.valueOf(length));
                        for (int i = 0; i < jArray.length(); i++){
                            JSONObject object = jArray.getJSONObject(i);
                            String cateName = object.getString("cat_name");
                            String id = object.getString("id");
//                            Log.i("cateName",cateName);
                            categoriesId.add(id);
                            postCategoryName.add(cateName);
                        }

//                        Log.i("postCategoryName",postCategoryName.toString());
                        CategoriesAdapter categories = new CategoriesAdapter(getApplicationContext(),postCategoryName);
                        categoriesListView.setAdapter(categories);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        categoriesListView = (ListView)findViewById(R.id.category_list_id);
        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String categoryId = categoriesId.get(i);
                Toast.makeText(CategoriesActivity.this,categoryId,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CategoriesActivity.this,CategoriesEachActivity.class);
                intent.putExtra("CategoryID",categoryId);
                startActivity(intent);
            }
        });


        //------------------------ Start Go to Category ------------------------------------------
        back = (TextView)findViewById(R.id.btnPostBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CategoriesActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
//------------------------ End Go to Category ------------------------------------------
    }
    // To get API url
//    public static String GET(String url) {
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
//
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // convert inputstream to string
//            if (inputStream != null)
//                result = convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        return result;
//    }
//
//    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//        String line = "";
//        String result = "";
//        while ((line = bufferedReader.readLine()) != null)
//            result += line;
//
//        inputStream.close();
//        return result;
//    }
//
//    class HttpAsyncTask extends AsyncTask<String, Void, String>{
//
//        @Override
//        protected String doInBackground(String... urls) {
//            return GET(urls[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            try {
//                JSONObject jsonObj = new JSONObject(result);
//                JSONArray jArray = jsonObj.getJSONArray("categories");
//                int i;
//
////                HomeActivity.postCategoryName.clear();
//                for(i=0; i < jArray.length(); i++){
//                    JSONObject jsonObject = jArray.getJSONObject(i);
////                    String idCate = jsonObject.getString("id");
//                    String nameCate = jsonObject.getString("cat_name");
//                    postCategoryName.add(nameCate);
////                    postCategoryId.add(idCate);
//                    Log.i("nameCate",nameCate.toString());
//                }
////                Log.i("nameResult",postCategoryName.toString());
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Toast.makeText(getBaseContext(), "not data!", Toast.LENGTH_SHORT).show();
//
//            }
//
//            CategoriesAdapter categories = new CategoriesAdapter(getApplicationContext(),postCategoryName);
//            categoriesListView.setAdapter(categories);
////            super.onPostExecute(s);
//        }
//
//    }
}
