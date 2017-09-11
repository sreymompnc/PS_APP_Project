package ruc.ps_app_project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import url.constraint;

public class FavoriteGridViewAdapter extends ArrayAdapter {
    List<String> FAVORITEID,USERID,POST_ID,POSTTITLE, FAVORITEIMAGE;
    Context context;

    public FavoriteGridViewAdapter(@NonNull Context applicationContext, List<String> favID,List<String> userID,
                                   List<String> postID,List<String> postTitle,List<String> postImage) {
        super(applicationContext,R.layout.display_gridview_favorite);
        this.context = applicationContext;
        this.FAVORITEID = favID;
        this.USERID = userID;
        this.POST_ID = postID;
        this.POSTTITLE = postTitle;
        this.FAVORITEIMAGE = postImage;
    }

    @Override
    public int getCount() {
        return POSTTITLE.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View Gridview = view;
        ViewHolder holder;

        if (Gridview == null){

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Gridview = mInflater.inflate(R.layout.display_gridview_favorite, parent, false);

            holder = new FavoriteGridViewAdapter.ViewHolder();

            holder.fav_image = (ImageView) Gridview.findViewById(R.id.fav_image);
            holder.pos_title = (TextView) Gridview.findViewById(R.id.fav_title);

            Gridview.setTag(holder);
        }else {

            holder = (FavoriteGridViewAdapter.ViewHolder) Gridview.getTag();
        }

        // set favorite data
        holder.pos_title.setText(POSTTITLE.get(i));
        // post image
        final String postImageurl = constraint.url+"images/posts/"+FAVORITEIMAGE.get(i);
        loadImagePost(postImageurl,holder.fav_image);


        return Gridview;
    }


    public static class ViewHolder {
        ImageView fav_image;
        TextView pos_title;
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
