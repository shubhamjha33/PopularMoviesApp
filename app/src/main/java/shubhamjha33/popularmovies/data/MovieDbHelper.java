package shubhamjha33.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shubham on 06/21/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME="weather.db";
    static final int DATABASE_VERSION=1;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE_FAVORITE="CREATE TABLE "+ MovieContract.FavoriteEntry.TABLE_NAME+" ( "+
                MovieContract.FavoriteEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieContract.FavoriteEntry.COLUMN_ORIGINAL_TITLE+ " TEXT NOT NULL,"+
                MovieContract.FavoriteEntry.COLUMN_OVERVIEW+" TEXT,"+
                MovieContract.FavoriteEntry.COLUMN_POSTER_URL+" TEXT,"+
                MovieContract.FavoriteEntry.COLUMN_RATING+" REAL,"+
                MovieContract.FavoriteEntry.COLUMN_RELEASE_DATE+" TEXT,"+
                MovieContract.FavoriteEntry.COLUMN_TMDB_ID+" TEXT NOT NULL)";
        db.execSQL(SQL_CREATE_TABLE_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MovieContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
