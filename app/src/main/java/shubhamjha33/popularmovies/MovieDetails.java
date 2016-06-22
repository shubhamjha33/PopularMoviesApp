package shubhamjha33.popularmovies;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

import shubhamjha33.popularmovies.data.MovieContract;

/**
 * Created by Shubham on 12-04-2016.
 */
public class MovieDetails {

    protected String id,originalTitle,posterImageUrl,overview,releaseDate;
    protected double userRating;

    public String getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    public void extractParamsFromJsonObj(JSONObject data){
        try {
            id=data.getString("id");
            originalTitle=data.getString("original_title");
            posterImageUrl="http://image.tmdb.org/t/p/w185"+data.getString("poster_path");
            overview=data.getString("overview");
            userRating=data.getDouble("vote_average");
            releaseDate=data.getString("release_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJSONString(){
        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("id",id);
            jsonObject.put("original_title",originalTitle);
            jsonObject.put("poster_path",posterImageUrl);
            jsonObject.put("overview",overview);
            jsonObject.put("release_date",releaseDate);
            jsonObject.put("vote_average",userRating);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void convertCursorToData(Cursor cursor){
        id=cursor.getString(MovieContract.COL_TMDB_ID);
        originalTitle=cursor.getString(MovieContract.COL_ORIGINAL_TITLE);
        userRating=cursor.getDouble(MovieContract.COL_RATING);
        posterImageUrl=cursor.getString(MovieContract.COL_POSTER_URL);
        overview=cursor.getString(MovieContract.COL_OVERVIEW);
        releaseDate=cursor.getString(MovieContract.COL_RELEASE_DATE);
    }

    public ContentValues getContentValues(){
        ContentValues contentValues=new ContentValues();
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_TMDB_ID,id);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE,originalTitle);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_OVERVIEW,overview);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_POSTER_URL,posterImageUrl);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE,releaseDate);
        contentValues.put(MovieContract.FavoriteEntry.COLUMN_RATING,userRating);
        return contentValues;
    }
}
