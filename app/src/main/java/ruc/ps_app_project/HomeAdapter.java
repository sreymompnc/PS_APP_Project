package ruc.ps_app_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import url.constraint;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class HomeAdapter extends ArrayAdapter {

    Context context;
    List<String> userPostId, productID, username, dateAndTime, description, profile, allPostImage, numLikes, numFav, numCmt;
    List<String> userSaved, userLiked,productTitle;
    String roleUser,userLoginID;

    public HomeAdapter(Context applicationContext, String roleUser,String userLoginID, List<String> userPostId, List<String> productID,
                       List<String> username, List<String> dateAndTime,
                       List<String> description, List<String> profile, List<String> allPostImage,
                       List<String> numLikes, List<String> numFav, List<String> numCmt,
                       List<String> userSaved,  List<String> userLiked,List<String> productTitle) {
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
        this.userSaved = userSaved;
        this.userLiked = userLiked;
        this.productTitle = productTitle;

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
            holder.proTitle = (TextView)Listview.findViewById(R.id.productTitle) ;
            holder.posterProfile = (ImageView) Listview.findViewById(R.id.circle_image);
            holder.postImages = (ImageView) Listview.findViewById(R.id.displayImage);

            holder.btnLike = (Button) Listview.findViewById(R.id.hbtnlike);
            holder.btnFav = (Button) Listview.findViewById(R.id.hbtnfavorite);
            holder.bntCmt = (Button) Listview.findViewById(R.id.hbtncmt);

            Listview.setTag(holder);
        } else {

            holder = (ViewHolder) Listview.getTag();
        }

        //===========================Check to set color=======================
        String userLikedID = userLiked.get(position);
        if(userLoginID.equals(userLikedID)){
            //holder.btnLike.setBackgroundColor(Color.GREEN);
            Drawable img = getContext().getResources().getDrawable( R.drawable.ic_liked );
            img.setBounds( 0, 0, 82, 82 );
            holder.btnLike.setCompoundDrawables( img, null, null, null );
           // holder.btnLike.setBackgroundDrawable
        }else{
            Drawable img = getContext().getResources().getDrawable( R.drawable.ic_like );
            img.setBounds( 0, 0, 82, 82 );
            holder.btnLike.setCompoundDrawables( img, null, null, null );
        }


        //==============================End color=============================
        //=================================Check saved ==================
        String userSavedID = userSaved.get(position);
        if(userLoginID.equals(userSavedID)){
            Drawable img = getContext().getResources().getDrawable( R.drawable.ic_heart_red );
            img.setBounds( 0, 0, 82, 82 );
            holder.btnFav.setCompoundDrawables( img, null, null, null );
            // holder.btnLike.setBackgroundDrawable
        }else{
            Drawable img = getContext().getResources().getDrawable( R.drawable.ic_heart );
            img.setBounds( 0, 0, 82, 82 );
            holder.btnFav.setCompoundDrawables( img, null, null, null );
        }
        //============================End saved==========================



        // Go to detail activity of click product image
        holder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roleUser.equals("buyer")) {
                    String idOfProduct = productID.get(position);
                    like(idOfProduct);

                }else{
                    Intent intent= new Intent(context, AskConfirmActivity.class);
                    context.startActivity(intent);
                }
            }

        });

        holder.bntCmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(roleUser.equals("buyer")){
                    Intent detailIntent = new Intent(context, PostDetailActivity.class);
                    detailIntent.putExtra("productId", productID.get(position).toString());
                    detailIntent.putExtra("userPostId", userPostId.get(position).toString());
                    detailIntent.putExtra("page","homepage");
                    context.startActivity(detailIntent);
                }else{
                    Intent intent= new Intent(context, AskConfirmActivity.class);
                    context.startActivity(intent);
                }

            }

        });

        holder.postImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detailIntent = new Intent(context, PostDetailActivity.class);
                detailIntent.putExtra("productId", productID.get(position).toString());
                detailIntent.putExtra("userPostId", userPostId.get(position).toString());
                detailIntent.putExtra("page","homepage");
                context.startActivity(detailIntent);
            }

        });

        // ----- Start Go to profile page---------------

        holder.posterProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLoginID.equals(userPostId.get(position))){
                    Intent profileIntent = new Intent(context, PosterProfileActivity.class);
                    profileIntent.putExtra("userPostId", userPostId.get(position).toString());
                    profileIntent.putExtra("poster","owner");
                    //  profileIntent.putExtra("frompage",true);
                    context.startActivity(profileIntent);
                }else {
                    Intent profileIntent = new Intent(context, PosterProfileActivity.class);
                    profileIntent.putExtra("userPostId", userPostId.get(position).toString());
                    profileIntent.putExtra("frompage",true);
                    profileIntent.putExtra("frompagehome",true);
                    profileIntent.putExtra("poster","other");
                    context.startActivity(profileIntent);

                }


            }

        });
        holder.usernames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userLoginID.equals(userPostId)){
                    Intent profileIntent = new Intent(context, PosterProfileActivity.class);
                    profileIntent.putExtra("userPostId", userPostId.get(position).toString());
                    //profileIntent.putExtra("frompagehome",true);
                    profileIntent.putExtra("poster","owner");
                    context.startActivity(profileIntent);
                }else {
                    Intent profileIntent = new Intent(context, PosterProfileActivity.class);
                    profileIntent.putExtra("userPostId", userPostId.get(position).toString());
                    profileIntent.putExtra("poster","other");
                    profileIntent.putExtra("frompagehome",true);
                    context.startActivity(profileIntent);

                }

            }

        });

        // ----- End Go to profile page-----------------
        //==================Button save favorite=============
        holder.btnFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(roleUser.equals("buyer")){
                    String userLikedID = userSaved.get(position);
                    String idOfProduct = productID.get(position);
                    saveFavorite(userLoginID,idOfProduct);

                }else{
                    Intent intent= new Intent(context, AskConfirmActivity.class);
                    context.startActivity(intent);
                }
            }

        });

        holder.usernames.setText(username.get(position));
        holder.createDate.setText(dateAndTime.get(position));
        holder.desc.setText(description.get(position));
        holder.proTitle.setText(productTitle.get(position));

        if(numLikes.get(position).toString().equals("null")){
            holder.btnLike.setText("0");
        }else{
            holder.btnLike.setText(numLikes.get(position));

        }
//        Toast.makeText(context,numLikes.get(position),Toast.LENGTH_SHORT).show();

        if(numFav.get(position).toString().equals("null")){
            holder.btnFav.setText("0");
        }else{
             holder.btnFav.setText(numFav.get(position));
        }
        if(numCmt.get(position).toString().equals("null")){
            holder.bntCmt.setText("0");
        }else{
            holder.bntCmt.setText(numCmt.get(position));

        }
        // profile
        final String url = constraint.url+"images/posters/" + profile.get(position);
        loadImage(url, holder.posterProfile);
        // post image
        final String postImageurl = constraint.url+"images/posts/" + allPostImage.get(position);
        loadImagePost(postImageurl, holder.postImages);

        return Listview;


    }


    public static class ViewHolder {
        TextView usernames,proTitle;
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
    private void like(String productID){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(constraint.url + "posts/checkLike/"+userLoginID+"/"+productID, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    Log.i("userLoginID",userLoginID);
//                    Log.i("userLoginID", String.valueOf(productID));
                    String data = new String(responseBody, "UTF-8");
                    try {
                        JSONObject object = new JSONObject(data);
                        String message = object.getString("status");
                        String name = "";
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

