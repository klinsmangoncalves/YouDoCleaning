package br.com.kmg.youdocleaning.ui;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import br.com.kmg.youdocleaning.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_cleaning_app, rootKey);
    }
}
