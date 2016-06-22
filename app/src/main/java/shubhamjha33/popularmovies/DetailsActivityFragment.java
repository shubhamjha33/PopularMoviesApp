package shubhamjha33.popularmovies;

import android.content.ContentUris;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shubhamjha33.popularmovies.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private boolean mIsFavorite;
    private MovieDetails movieDetails;
    private int LOADER_ID=101;
    private ViewHolder mViewHolder;

    private class ViewHolder{
        private LinearLayout reviewLinearLayout,trailerLinearLayout;
        private ImageButton favorite;
        private ImageView posterImage;
        private Toolbar originalTitle;
        private TextView overview,releaseDate,userRating;
        public ViewHolder(View rootView){
            reviewLinearLayout =(LinearLayout)rootView.findViewById(R.id.reviewsLinearLayout);
            trailerLinearLayout=(LinearLayout)rootView.findViewById(R.id.trailerLinearLayout);
            posterImage= (ImageView) rootView.findViewById(R.id.posterImage);
            originalTitle= (Toolbar) rootView.findViewById(R.id.originalTitle);
            overview= (TextView) rootView.findViewById(R.id.overview);
            releaseDate= (TextView) rootView.findViewById(R.id.releaseDate);
            userRating= (TextView) rootView.findViewById(R.id.userRating);
            favorite=(ImageButton)rootView.findViewById(R.id.favorite);
        }
    }

    private void updateView(String jsonString){
        JSONObject jsonObject= null;
        try {
            jsonObject = new JSONObject(jsonString);
            movieDetails=new MovieDetails();
            movieDetails.extractParamsFromJsonObj(jsonObject);
            mViewHolder.originalTitle.setTitle(movieDetails.getOriginalTitle());
            mViewHolder.originalTitle.setTitleTextColor(Color.WHITE);
            mViewHolder.overview.setText(movieDetails.getOverview());
            Picasso.with(getContext()).load(movieDetails.getPosterImageUrl()).into(mViewHolder.posterImage);
            mViewHolder.releaseDate.setText(movieDetails.getReleaseDate());
            mViewHolder.userRating.setText(Double.valueOf(movieDetails.getUserRating()).toString());
            new FetchReviewsAsyncTask().execute(jsonObject.getString("id"));
            new FetchTrailerAsyncTask().execute(jsonObject.getString("id"));
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_details, container, false);
        mViewHolder=new ViewHolder(rootView);
        Intent intent=getActivity().getIntent();
        Bundle b=getArguments();
        if(intent!=null&&intent.getStringExtra(Intent.EXTRA_TEXT)!=null) {
            String jsonString = getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
            updateView(jsonString);
        }
        else if(b!=null&&b.containsKey("movieDetails")){
            String jsonString=getArguments().getString("movieDetails");
            updateView(jsonString);
        }
        mIsFavorite = false;
        mViewHolder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
        mViewHolder.favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsFavorite) {
                    int deleteCount = getContext().getContentResolver().delete(MovieContract.FavoriteEntry.CONTENT_URI, MovieContract.FavoriteEntry.COLUMN_TMDB_ID + " = ?", new String[]{movieDetails.getId()});
                    if (deleteCount > 0)
                        mViewHolder.favorite.setImageResource(android.R.drawable.btn_star_big_off);
                } else {
                    long _id = ContentUris.parseId(getContext().getContentResolver().insert(MovieContract.FavoriteEntry.CONTENT_URI, movieDetails.getContentValues()));
                    if (_id > 0) {
                        mViewHolder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
                        }
                    }
                    mIsFavorite = !mIsFavorite;
                }
            });
        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.FavoriteEntry.CONTENT_URI, MovieContract.FAVORITE_COLUMNS, MovieContract.FavoriteEntry.COLUMN_TMDB_ID+" = ?",new String[]{movieDetails.getId()},null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null&&data.moveToFirst()){
            mIsFavorite=true;
            mViewHolder.favorite.setImageResource(android.R.drawable.btn_star_big_on);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    class Trailer{

        String trailerName;
        String trailerKey;

        public String getTrailerName() {
            return trailerName;
        }

        public void setTrailerName(String trailerName) {
            this.trailerName = trailerName;
        }

        public String getTrailerKey() {
            return trailerKey;
        }

        public void setTrailerKey(String trailerKey) {
            this.trailerKey = trailerKey;
        }

    }

    public class FetchTrailerAsyncTask extends AsyncTask<String,Void,List<Trailer>>{

        @Override
        protected List<Trailer> doInBackground(String... params) {
            Uri webUrl=Uri.parse("http://api.themoviedb.org/3/movie/"+params[0]+"/videos").buildUpon().appendQueryParameter("api_key", getString(R.string.api_key)).build();
            try {
                URL url=new URL(webUrl.toString());
                return getTrailersFromJSON(Utility.getJSONStringFromUrl(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private List<Trailer> getTrailersFromJSON(String jsonStringFromUrl) {
            List<Trailer> trailerList=new ArrayList<>();
            try {
                Trailer trailer;
                JSONObject jsonObject= new JSONObject(jsonStringFromUrl);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject trailerItem=jsonArray.getJSONObject(i);
                    trailer=new Trailer();
                    trailer.setTrailerName(trailerItem.getString("name"));
                    trailer.setTrailerKey(trailerItem.getString("key"));
                    trailerList.add(trailer);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return trailerList;
        }

        @Override
        protected void onPostExecute(List<Trailer> trailerList) {
            if(trailerList!=null){
                for(Trailer trailer:trailerList){
                    LinearLayout ll=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_item_trailer, null);
                    TextView trailerName=(TextView)ll.findViewById(R.id.trailer_item_name);
                    trailerName.setText(trailer.getTrailerName());
                    ll.setOnClickListener(new TrailerClickListener(trailer));
                    mViewHolder.trailerLinearLayout.addView(ll);
                }
            }
        }

    }

    class TrailerClickListener implements View.OnClickListener{

        Trailer trailer;

        public TrailerClickListener(Trailer t){
            trailer=t;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://youtube.com/watch?v=" + trailer.getTrailerKey()));
            startActivity(intent);
        }
    }

    class Reviews{
        String itemAuthor,itemText;

        public void setItemAuthor(String itemAuthor) {
            this.itemAuthor = itemAuthor;
        }

        public void setItemText(String itemText) {
            this.itemText = itemText;
        }

        public String getItemAuthor() {
            return itemAuthor;
        }

        public String getItemText() {
            return itemText;
        }

    }

    public class FetchReviewsAsyncTask extends AsyncTask<String,Void,List<Reviews>>{

        public List<Reviews> getReviewsFromJSON(String jsonString){
            List<Reviews> reviewsList=new ArrayList<>();
            Log.v("DetailActivFrag",jsonString);
            Reviews review;
            try {
                JSONObject jsonObject= new JSONObject(jsonString);
                JSONArray jsonArray=jsonObject.getJSONArray("results");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject reviewItem=jsonArray.getJSONObject(i);
                    review=new Reviews();
                    review.setItemAuthor(reviewItem.getString("author"));
                    review.setItemText(reviewItem.getString("content"));
                    reviewsList.add(review);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return reviewsList;
        }

        @Override
        protected List<Reviews> doInBackground(String... params) {
            Uri webUrl=Uri.parse("http://api.themoviedb.org/3/movie/"+params[0]+"/reviews").buildUpon().appendQueryParameter("api_key", getString(R.string.api_key)).build();
            try {
                URL url=new URL(webUrl.toString());
                return getReviewsFromJSON(Utility.getJSONStringFromUrl(url));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Reviews> reviewsList) {
            if(reviewsList!=null){
                for(Reviews reviews:reviewsList){
                    LinearLayout ll=(LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.list_item_review, null);
                    TextView author=(TextView)ll.findViewById(R.id.review_item_author);
                    author.setText(reviews.getItemAuthor());
                    TextView content=(TextView)ll.findViewById(R.id.review_item_text);
                    content.setText(reviews.getItemText());
                    mViewHolder.reviewLinearLayout.addView(ll);
                }
            }
        }
    }
}
