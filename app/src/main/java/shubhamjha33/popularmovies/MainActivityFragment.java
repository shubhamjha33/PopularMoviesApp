package shubhamjha33.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static final String LOG_CLASS_NAME="MainActivFrag";
    ImageAdapter movieAdapter;

    @Override
    public void onStart(){
        super.onStart();
        fetchMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter=new ImageAdapter(getContext(),new ArrayList<MovieDetails>());
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
        Log.v(LOG_CLASS_NAME,"Fetch Movies Executed");
        new FetchAsyncTaskExecuter().execute();
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
            try {
                URL url=new URL(webUrl.toString());
                Log.v(LOG_CLASS_NAME,webUrl.toString());
                HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();
                InputStream is=urlConnection.getInputStream();
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                StringBuilder stringBuilder=new StringBuilder("");
                String str;
                while((str=br.readLine())!=null){
                    stringBuilder.append(str);
                }
                return parseJsonStringTMDB(stringBuilder.toString());
            } catch (MalformedURLException e) {
                Log.e(LOG_CLASS_NAME,"MalformedURLException occured");
                e.printStackTrace();
            } catch (IOException e) {
                Log.e(LOG_CLASS_NAME,"IOException occured");
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
