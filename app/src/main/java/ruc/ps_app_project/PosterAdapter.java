package ruc.ps_app_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PosterAdapter extends ArrayAdapter {

    Context context;
    List<String> USERNAME,DATETIME,DESCRIPTION,PROFILE, POSTIMAGE,NUMLIKE,NUMFAV,NUMCMT;
    String port = "http://192.168.1.17:1111/";
    public PosterAdapter(Context context, List<String> username,List<String> dateAndTime,
                       List<String> description,List<String> profile, List<String> allPostImage,
                       List<String> numLikes,List<String> numFav,List<String> numCmt) {
        super(context,R.layout.listview_post);
        this.context = context;
        this.USERNAME = username;
        this.DATETIME = dateAndTime;
        this.DESCRIPTION = description;
        this.PROFILE = profile;
        this.POSTIMAGE = allPostImage;
        this.NUMLIKE = numLikes;
        this.NUMFAV = numFav;
        this.NUMCMT = numCmt;
    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {

        Log.i("getView","Getview success");
        ViewHolder holder;
        View vi = view;
//        CustomAdapterHome.ViewHolder holder = null;
        if(vi == null) {

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = mInflater.inflate(R.layout.listview_post, parent, false);

            holder = new ViewHolder();
            holder.pos_image = (ImageView)vi.findViewById(R.id.pos_image);
            holder.pro_image = (ImageView)vi.findViewById(R.id.poster_profile);
            holder.username = (TextView)vi.findViewById(R.id.poster_name);
            holder.pos_description = (TextView)vi.findViewById(R.id.pos_description);
            holder.bntCmt = (Button)vi.findViewById(R.id.btncmt);
            holder.btnFav = (Button)vi.findViewById(R.id.btnfavorite);
            holder.btnLike = (Button)vi.findViewById(R.id.btnlike);
            holder.createDate = (TextView)vi.findViewById(R.id.pos_datetime);

            vi.setTag(holder);

        } else {

            holder = (PosterAdapter.ViewHolder) vi.getTag();
        }
        holder.pos_description.setText(DESCRIPTION.get(i));
        holder.createDate.setText(DATETIME.get(i));
        holder.username.setText(USERNAME.get(i));

        holder.btnLike.setText(NUMLIKE.get(i));
        holder.btnFav.setText(NUMFAV.get(i));
        holder.bntCmt.setText(NUMCMT.get(i));

        // profile
        final String url = port+"images/posters/"+PROFILE.get(i);
        loadImage(url,holder.pro_image );
        // post image
        final String postImageurl = port+"images/posts/"+POSTIMAGE.get(i);
        loadImagePost(postImageurl,holder.pos_image);

        return vi;
    }
    @Override
    public int getCount() {
        return (DESCRIPTION.size());
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        ImageView pos_image;
        ImageView pro_image;
        TextView username;
        TextView createDate;
        TextView pos_description;
        Button btnLike,bntCmt,btnFav;

    }

    // To load image of profile
    private void loadImage(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(800,800)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);

    }

    // To load image of post
    private void loadImagePost(String url,ImageView imgView){
        Picasso.with(context)
                .load(url)
                .resize(1300,1200)
                .centerInside()// to zoom img
                //.centerCrop()
                .into(imgView);

    }

}
