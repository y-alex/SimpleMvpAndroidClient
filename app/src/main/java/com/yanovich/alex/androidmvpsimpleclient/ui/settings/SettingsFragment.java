package com.yanovich.alex.androidmvpsimpleclient.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.yanovich.alex.androidmvpsimpleclient.R;

import timber.log.Timber;

/**
 * Created by Alex on 01.05.2016.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener,SharedPreferences.OnSharedPreferenceChangeListener{
    public static final String PREF_EMAIL_KEY = "email_sendDb";

    //PreferenceManager mPrefManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         getPreferenceManager().setSharedPreferencesName("android_simpleclient_pref_file");
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preference);




        bindPreferenceSummaryToValue(findPreference(PREF_EMAIL_KEY));
        bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_splash_time_key)));

    }

    /**
     * Attaches a listener so the summary is always updated with the preference value.
     * Also fires the listener once, to initialize the summary (so it shows up before the value
     * is changed.)
     */
    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);
        Object prefVal = getPreferenceManager().getSharedPreferences()
                .getString(preference.getKey(), "");
        // Set the preference summaries
        Timber.i("bindPref Summary to value , prefKey:" + preference.getKey() + "value:" +prefVal.toString());
        setPreferenceSummary(preference,prefVal);
    }

    private void setPreferenceSummary(Preference preference, Object value) {
        String stringValue = value.toString();
        String key = preference.getKey();

        if (preference instanceof ListPreference) {
            // For list preferences, look up the correct display value in
            // the preference's 'entries' list (since they have separate labels/values).
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        } else if (key.equals(PREF_EMAIL_KEY)) {
                    preference.setSummary(stringValue);
        } else {
            // For other preferences, set the summary to the value's simple string representation.
            preference.setSummary(stringValue);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }



    @Override
    public void onPause() {
        super.onPause();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Timber.i("on shared pref triggered wiht key" + key);

    }

    // This gets called before the preference is changed
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        Timber.i("onPreferenceChange() with value: "+newValue.toString());
        setPreferenceSummary(preference, newValue);
        return true;
    }

}