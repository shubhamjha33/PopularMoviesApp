package shubhamjha33.popularmovies;

import org.json.JSONException;
import org.json.JSONObject;

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
            jsonObject.put("originalTitle",originalTitle);
            jsonObject.put("posterImageUrl",posterImageUrl);
            jsonObject.put("overview",overview);
            jsonObject.put("releaseDate",releaseDate);
            jsonObject.put("userRating",userRating);
            return jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  "";
    }
}
