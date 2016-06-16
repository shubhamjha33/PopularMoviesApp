package shubhamjha33.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shubham on 11-04-2016.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<MovieDetails> movieList;

    public ImageAdapter(Context c){
        context=c;
        movieList=new ArrayList<>();
    }

    public ImageAdapter(Context c,List<MovieDetails> imgList){
        context=c;
        movieList=imgList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        movieList.clear();
        movieList=new ArrayList<>();
    }

    public void add(MovieDetails movie){
        movieList.add(movie);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView=null;
        if(convertView==null){
            imageView=new ImageView(context);
        }
        else{
            imageView=(ImageView)convertView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        Picasso.with(context).load(movieList.get(position).getPosterImageUrl()).into(imageView);
        return imageView;
    }
}
