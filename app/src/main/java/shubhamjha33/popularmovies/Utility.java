package shubhamjha33.popularmovies;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Shubham on 06/16/2016.
 */
public class Utility {

    public static String getJSONStringFromUrl(URL url){
        HttpURLConnection urlConnection= null;
        StringBuilder stringBuilder=new StringBuilder("");
        try {
            Log.v("Utility", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream is = urlConnection.getInputStream();
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            stringBuilder=new StringBuilder("");
            String str;
            while((str=br.readLine())!=null){
                stringBuilder.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
