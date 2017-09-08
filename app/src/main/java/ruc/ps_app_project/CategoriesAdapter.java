package ruc.ps_app_project;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import static android.support.v4.view.PagerAdapter.POSITION_NONE;

public class CategoriesAdapter extends ArrayAdapter {

    Context context;
    List<String> postCategoryName;

    public CategoriesAdapter(Context applicationContext, List<String> postCategoryName) {
        super(applicationContext,R.layout.activity_categories_adapter);
        this.context = applicationContext;
        this.postCategoryName = postCategoryName;
    }

    @Override
    public int getCount() {
        return postCategoryName.size();
    }

    @Override
    public Object getItem(int i) {
        return POSITION_NONE;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View Listview = convertView;
        ViewHolder holder;

        if (Listview == null){

            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Listview = mInflater.inflate(R.layout.activity_categories_adapter, parent, false);

            holder = new ViewHolder();

//            holder.id = (TextView)Listview.findViewById(R.id.cate_id) ;
            holder.catageiesName = (TextView) Listview.findViewById(R.id.adapter_cate_name);

            Listview.setTag(holder);
        }else {
            holder = (ViewHolder) Listview.getTag();
        }
//        holder.id.setText(cateId.get(position));
        holder.catageiesName.setText(postCategoryName.get(position));
        return Listview;
    }

    public static class ViewHolder {
        TextView catageiesName;
//        TextView id;

//        return super.getView(position, convertView, parent);
    }

}
