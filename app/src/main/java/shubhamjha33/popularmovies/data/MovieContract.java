package shubhamjha33.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Shubham on 06/21/2016.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY="shubhamjha33.popularmovies.app";

    public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String PATH_FAVORITE="favorite";

    public static final class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME="favorite";
        public static final String COLUMN_TMDB_ID="tmdb_id";
        public static final String COLUMN_ORIGINAL_TITLE="original_title";
        public static final String COLUMN_POSTER_URL="poster_url";
        public static final String COLUMN_OVERVIEW="overview";
        public static final String COLUMN_RELEASE_DATE="release_date";
        public static final String COLUMN_RATING="rating";
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE).build();
        public static final String CONTENT_TYPE=ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE;;
        public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+CONTENT_AUTHORITY+"/"+PATH_FAVORITE;
        public static Uri buildFavoriteUri(long _id){
            return ContentUris.withAppendedId(CONTENT_URI,_id);
        }
    }

    public static final String[] FAVORITE_COLUMNS={
        FavoriteEntry.COLUMN_TMDB_ID,
        FavoriteEntry.COLUMN_ORIGINAL_TITLE,
        FavoriteEntry.COLUMN_OVERVIEW,
        FavoriteEntry.COLUMN_RELEASE_DATE,
        FavoriteEntry.COLUMN_RATING,
        FavoriteEntry.COLUMN_POSTER_URL
    };

    public static final int COL_TMDB_ID=0;
    public static final int COL_ORIGINAL_TITLE=1;
    public static final int COL_OVERVIEW=2;
    public static final int COL_RELEASE_DATE=3;
    public static final int COL_RATING=4;
    public static final int COL_POSTER_URL=5;

}
