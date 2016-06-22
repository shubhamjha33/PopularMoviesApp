package shubhamjha33.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback{

    private static boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(findViewById(R.id.detail_fragment)!=null){
            mTwoPane=true;
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.detail_fragment,new DetailsActivityFragment()).
                    commit();
        }
        else{
            mTwoPane=false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String jsonObject) {
        if(mTwoPane){
            DetailsActivityFragment df= new DetailsActivityFragment();
            Bundle b=new Bundle();
            b.putString("movieDetails",jsonObject);
            df.setArguments(b);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment, df)
                    .addToBackStack(null)
                    .commit();
        }
        else{
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT,jsonObject);
            startActivity(intent);
        }
    }
}
