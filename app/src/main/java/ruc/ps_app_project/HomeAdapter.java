package ruc.ps_app_project;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nineoldandroids.animation.ValueAnimator;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static android.content.ContentValues.TAG;
import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class HomeAdapter extends ArrayAdapter {

    Context context;
    List<String> userPostId, productID, username, dateAndTime, description, profile, allPostImage, numLikes, numFav, numCmt;
    String roleUser,userLoginID;
    String port = "http://192.168.1.17:1111/";
    public HomeAdapter(Context applicationContext, String roleUser,String userLoginID, List<String> userPostId, List<String> productID,
                       List<String> username, List<String> dateAndTime,
                       List<String> description, List<String> profile, List<String> allPostImage,
                       List<String> numLikes, List<String> numFav, List<String> numCmt) {
        super(applicationContext, R.layout.homelist_item);
        this.context = applicationContext;
        this.roleUser = roleUser;
        this.userLoginID = userLoginID;
        this.userPostId = userPostId;
        this.productID = productID;
        this.username = username;
        this.dateAndTime = dateAndTime;
        this.description = description;
        this.profile = profile;
        this.allPostImage = allPostImage;
        this.numLikes = numLikes;
        this.numFav = numFav;
        this.numCmt = numCmt;
    }

    @Override
    public int getCount() {
        return username.size();
    }

    @Override
    public Object getItem(int i) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View Listview = view;
        ViewHolder holder;
        if (Listview == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Listview = mInflater.inflate(R.layout.homelist_item, parent, false);

            holder = new ViewHolder();

            holder.createDate = (TextView) Listview.findViewById(R.id.datetime);
            holder.usernames = (TextView) Listview.findViewById(R.id.userItem);
            holder.desc = (TextView) Listview.findViewById(R.id.descrip);
            holder.posterProfile = (ImageView) Listview.findViewById(R.id.circle_image);
            holder.postImages = (ImageView) Listview.findViewById(R.id.displayImage);

            holder.btnLike = (Button) Listview.findViewById(R.id.hbtnlike);
            holder.btnFav = (Button) Listview.findViewById(R.id.hbtnfavorite);
            holder.bntCmt = (Button) Listview.findViewById(R.id.hbtncmt);




            Listview.setTag(holder);
        } else {

            holder = (ViewHolder) Listview.getTag();
        }

        // Go to detail activity of click product image
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Toast.makeText(context,"create like",Toast.LENGTH_LONG).show();
            }

        });

        holder.bntCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, PostDetailActivity.class);
                detailIntent.putExtra("productId", productID.get(position).toString());
                detailIntent.putExtra("userPostId", userPostId.get(position).toString());
                context.startActivity(detailIntent);
            }

        });



        holder.postImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, PostDetailActivity.class);
                detailIntent.putExtra("productId", productID.get(position).toString());
                detailIntent.putExtra("userPostId", userPostId.get(position).toString());
                context.startActivity(detailIntent);
            }

        });

        // ----- Start Go to profile page---------------
        holder.posterProfile.setOnClickListener(onProfileClickListener);
        holder.usernames.setOnClickListener(onProfileClickListener);
        // ----- End Go to profile page-----------------
        //==================Button save favorite=============
        holder.btnFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String idOfProduct = productID.get(position);
                    FavoriteSingleton.getInstance().saveFavorite(userLoginID,idOfProduct);

            }

        });


        holder.usernames.setText(username.get(position));
        holder.createDate.setText(dateAndTime.get(position));
        holder.desc.setText(description.get(position));
        holder.btnLike.setText(numLikes.get(position));
        holder.btnFav.setText(numFav.get(position));
        holder.bntCmt.setText(numCmt.get(position));
        // profile
        final String url = port+"images/posters/" + profile.get(position);
        loadImage(url, holder.posterProfile);
        // post image
        final String postImageurl = port+"images/posts/" + allPostImage.get(position);
        loadImagePost(postImageurl, holder.postImages);


        return Listview;


    }


    public static class ViewHolder {
        TextView usernames;
        TextView createDate;
        TextView desc;
        ImageView posterProfile;
        ImageView postImages;
        Button btnLike, bntCmt, btnFav;
        LinearLayout postHead;
    }

    // To load image of profile
    private void loadImage(String url, ImageView imgView) {
        Picasso.with(context)
                .load(url)
                .resize(800, 800)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);

    }

    // To load image of post
    private void loadImagePost(String url, ImageView imgView) {
        Picasso.with(context)
                .load(url)
                .resize(1300, 1200)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);

    }


    //------------------start open profile page
    private View.OnClickListener onProfileClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
                Intent profileIntent = new Intent(context, PosterProfile.class);
                context.startActivity(profileIntent);
        }
    };
    //----------------------end profile page-------------



}

