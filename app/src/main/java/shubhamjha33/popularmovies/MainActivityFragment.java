package shubhamjha33.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shubhamjha33.popularmovies.data.MovieContract;
import shubhamjha33.popularmovies.data.MovieDbHelper;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String LOG_CLASS_NAME="MainActivFrag";
    MovieAdapter movieAdapter;
    private static int LOADER_ID=101;

    @Override
    public void onStart(){
        super.onStart();
        fetchMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter=new MovieAdapter(getContext(),new ArrayList<MovieDetails>());
        GridView gridView=(GridView)rootView.findViewById(R.id.gridView);
        gridView.setAdapter(movieAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, movieAdapter.getItem(position).getJSONString());
                startActivity(intent);
            }
        });
        return rootView;
    }

    public void fetchMovies(){
        Log.v(LOG_CLASS_NAME, "Fetch Movies Executed");
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(getContext());
        if(sharedPreferences.getString(getString(R.string.pref_sort),getString(R.string.pref_sort_default)).equals(getString(R.string.pref_sort_favorite)))
            getLoaderManager().initLoader(LOADER_ID,null,this);
        else
            new FetchAsyncTaskExecuter().execute();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieContract.FavoriteEntry.CONTENT_URI,MovieContract.FAVORITE_COLUMNS,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data!=null&&data.moveToFirst()){
            MovieDetails movieDetails;
            movieAdapter.clear();
            do{
                movieDetails=new MovieDetails();
                movieDetails.convertCursorToData(data);
                movieAdapter.add(movieDetails);
            }while(data.moveToNext());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.clear();
    }

    public class FetchAsyncTaskExecuter extends AsyncTask<Void,Void,List<MovieDetails> >{

        protected List<MovieDetails> parseJsonStringTMDB(String jsonData){
            List<MovieDetails> movieDetailsList=new ArrayList();
            MovieDetails movieDetails;
            try {
                Log.v(LOG_CLASS_NAME,jsonData);
                JSONObject jsonObject=new JSONObject(jsonData);
                JSONArray resultArray=jsonObject.getJSONArray("results");
                for(int i=0;i<resultArray.length();i++){
                    JSONObject resultItem=resultArray.getJSONObject(i);
                    movieDetails=new MovieDetails();
                    movieDetails.extractParamsFromJsonObj(resultItem);
                    movieDetailsList.add(movieDetails);
                }
            } catch (JSONException e) {
                Log.e(LOG_CLASS_NAME,"JSONException occured");
                e.printStackTrace();
            }
            return movieDetailsList;
        }

        @Override
        protected List<MovieDetails> doInBackground(Void... params) {
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getContext());
            Uri webUrl=Uri.parse("http://api.themoviedb.org/3/movie/"+sharedPreferences.getString("pref_sort","popular")+"?").buildUpon().appendQueryParameter("api_key", getString(R.string.api_key)).build();
            try{
                URL url=new URL(webUrl.toString());
                Log.v(LOG_CLASS_NAME,webUrl.toString());
                return parseJsonStringTMDB(Utility.getJSONStringFromUrl(url));
            } catch (MalformedURLException e) {
                Log.e(LOG_CLASS_NAME,"MalformedURLException occured");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<MovieDetails> movieDetailsList) {
            if(movieDetailsList!=null&&movieDetailsList.size()>0){
                movieAdapter.clear();
                for(MovieDetails movie:movieDetailsList) {
                    Log.v(LOG_CLASS_NAME,movie.toString());
                    movieAdapter.add(movie);
                }
            }
        }
    }
}