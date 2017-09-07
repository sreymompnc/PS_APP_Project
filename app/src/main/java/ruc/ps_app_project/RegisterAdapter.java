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

public class RegisterAdapter extends ArrayAdapter {
    List<String> POSTTITLE,FAVORITEIMAGE;
    String port = "http://192.168.1.17:1111/";
    Context context;

    public RegisterAdapter(@NonNull Context applicationContext, List<String> pos_title, List<String> fav_pos) {
        super(applicationContext,R.layout.activity_gridview_favorite);
        this.context = applicationContext;
        this.POSTTITLE = pos_title;
        this.FAVORITEIMAGE = fav_pos;

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
            Gridview = mInflater.inflate(R.layout.activity_gridview_favorite, parent, false);

            holder = new RegisterAdapter.ViewHolder();

            holder.fav_image = (ImageView) Gridview.findViewById(R.id.fav_pro);
            holder.pos_title = (TextView) Gridview.findViewById(R.id.title_pro);

            Gridview.setTag(holder);
        }else {

            holder = (RegisterAdapter.ViewHolder) Gridview.getTag();
        }

        // post image
        final String postImageurl = port+"images/posts/"+FAVORITEIMAGE.get(i);
        loadImagePost(postImageurl,holder.fav_image);
        holder.pos_title.setText(POSTTITLE.get(i));

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
