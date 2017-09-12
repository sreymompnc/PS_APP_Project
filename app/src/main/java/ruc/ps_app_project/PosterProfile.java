package ruc.ps_app_project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class PosterProfile extends AppCompatActivity {
    Button btnPost;
    ListView simpleList;
    int flags[] = {R.drawable.flaga, R.drawable.flaga};
    String countryList[] = {"Camboida","India"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poster_profile);

        simpleList = (ListView) findViewById(R.id.simpleListView);
        PosterAdapter customAdapter = new PosterAdapter(getApplicationContext(), countryList, flags);
        simpleList.setAdapter(customAdapter);
    }
}
