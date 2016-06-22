package shubhamjha33.popularmovies.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Shubham on 06/21/2016.
 */
public class MovieProvider extends ContentProvider{

    private static final UriMatcher sUriMatcher=buildUriMatcher();

    private static final int FAVORITE=100;

    private MovieDbHelper mOpenHelper;


    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.CONTENT_AUTHORITY, MovieContract.PATH_FAVORITE,FAVORITE);
        return uriMatcher;
    }


    @Override
    public String getType(Uri uri){
        final int match=sUriMatcher.match(uri);
        switch(match){
            case FAVORITE:
                return MovieContract.FavoriteEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case FAVORITE:retCursor=mOpenHelper.getReadableDatabase().query(MovieContract.FavoriteEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Cursor retCursor;
        long _id;
        Uri returnUri=null;
        switch(sUriMatcher.match(uri)){
            case FAVORITE:_id=
                mOpenHelper.getWritableDatabase().insert(MovieContract.FavoriteEntry.TABLE_NAME, null, values);
                if(_id>0)
                    returnUri=MovieContract.FavoriteEntry.buildFavoriteUri(_id);
                else
                    throw new SQLException("Failed to insert row into "+uri);
                break;
            default:throw new UnsupportedOperationException("Unknown Uri "+uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleteCount=0;
        final int match=sUriMatcher.match(uri);
        int _id;
        switch (match){
            case FAVORITE:
                _id=mOpenHelper.getWritableDatabase().delete(MovieContract.FavoriteEntry.TABLE_NAME, selection, selectionArgs);
                if(_id>0)
                    deleteCount=1;
                else
                    throw new SQLException("Failed to delete row into "+uri);
                break;
            default:throw new UnsupportedOperationException("Unknown uri: "+uri);
        }
        if(deleteCount!=0)
            getContext().getContentResolver().notifyChange(uri,null);
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db=mOpenHelper.getWritableDatabase();
        final int match=sUriMatcher.match(uri);
        int updateCount=0;
        switch (match){
            case FAVORITE:{
                updateCount=db.update(MovieContract.FavoriteEntry.TABLE_NAME,values,selection,selectionArgs);
                break;
            }
        }
        if(updateCount>0)
            getContext().getContentResolver().notifyChange(uri,null);
        return updateCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
