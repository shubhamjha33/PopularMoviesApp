package shubhamjha33.popularmovies;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment {

    public DetailsActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_details, container, false);
        String jsonString=getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT);
        try {
            JSONObject jsonObject=new JSONObject(jsonString);
            ImageView posterImage= (ImageView) rootView.findViewById(R.id.posterImage);
            Picasso.with(getContext()).load(jsonObject.getString("posterImageUrl")).into(posterImage);
            Toolbar originalTitle= (Toolbar) rootView.findViewById(R.id.originalTitle);
            originalTitle.setTitle(jsonObject.getString("originalTitle"));
            originalTitle.setTitleTextColor(Color.WHITE);
            TextView overview= (TextView) rootView.findViewById(R.id.overview);
            overview.setText(jsonObject.getString("overview"));
            TextView releaseDate= (TextView) rootView.findViewById(R.id.releaseDate);
            releaseDate.setText(jsonObject.getString("releaseDate"));
            TextView userRating= (TextView) rootView.findViewById(R.id.userRating);
            userRating.setText(jsonObject.getString("userRating"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rootView;
    }
}
