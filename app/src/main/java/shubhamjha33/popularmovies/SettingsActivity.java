package shubhamjha33.popularmovies;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;


/**
 * Created by Shubham on 4/28/2016.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    public static final String SORT_OPTION_KEY = "pref_sort";
    public static final String LOG_CLASS_NAME = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort)));

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    protected void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        onPreferenceChange(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String val=newValue.toString();
        if(preference instanceof ListPreference){
            ListPreference listPreference=(ListPreference)preference;
            int prefIndex=listPreference.findIndexOfValue(val);
            if(prefIndex>=0){
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        else
            preference.setSummary(val);
        return true;
    }
}
