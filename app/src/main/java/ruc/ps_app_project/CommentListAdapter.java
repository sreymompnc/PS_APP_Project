package ruc.ps_app_project;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.List;

import url.constraint;

public class CommentListAdapter extends ArrayAdapter {

    Context context;
    List<String> userComment, dateComment, smsComment, profileComment;

    public CommentListAdapter(Context applicationContext,List<String> userComment,
                              List<String> dateComment,List<String> smsComment, List<String> profileComment) {
        super(applicationContext, R.layout.detail_comment_list);
        this.context = applicationContext;
        this.userComment = userComment;
        this.dateComment = dateComment;
        this.smsComment = smsComment;
        this.profileComment = profileComment;

    }


    @Override
    public int getCount() {
        return userComment.size();
    }

    @Override
    public Object getItem(int i) {
        return 0;
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
            Listview = mInflater.inflate(R.layout.detail_comment_list, parent, false);

            holder = new ViewHolder();

            holder.userCmt = (TextView) Listview.findViewById(R.id.userCmt);
            holder.dateCmt = (TextView) Listview.findViewById(R.id.cmtDate);
            holder.cmtMessage = (TextView) Listview.findViewById(R.id.comments);
            holder.profileCmt = (ImageView) Listview.findViewById(R.id.cmt_image);

            Listview.setTag(holder);
        } else {

            holder = (ViewHolder) Listview.getTag();
        }


        holder.userCmt.setText(userComment.get(position));
        holder.dateCmt.setText(dateComment.get(position));
        holder.cmtMessage.setText(smsComment.get(position));

        // profile
       // final String url = "192.168.1.14:1111/images/users/" + profileComment.get(position);
        //loadImage(url, holder.profileCmt);

        new DownloadImageTask((ImageView) Listview.findViewById(R.id.cmt_image))
                .execute(constraint.url+"images/users/"+profileComment.get(position));

        return Listview;


    }


    public static class ViewHolder {
        TextView userCmt;
        TextView dateCmt;
        ImageView profileCmt;
        TextView cmtMessage;
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

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }






}
