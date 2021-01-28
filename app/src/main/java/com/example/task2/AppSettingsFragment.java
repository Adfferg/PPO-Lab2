package com.example.task2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Layout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;

public class AppSettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_prefs, rootKey);
        Preference theme = findPreference(getString(R.string.pref_dark_theme));
        assert theme != null;
        theme.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                getActivity().recreate();
                return true;
            }
        });
        Preference clear_data = findPreference(getString(R.string.pref_clear_data));
        clear_data.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                SharedPreferences prefs = PreferenceManager
                        .getDefaultSharedPreferences(getContext());
                prefs.edit().clear().apply();
                DbAdapter adapter = new DbAdapter(getContext());
                adapter.Open();
                adapter.ClearDb();
                adapter.Close();
                goToMainActivity();
                return true;

            }
        });

       Preference lang = findPreference(getString(R.string.pref_language));
        lang.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                loadLocale((String)newValue);
                return true;
            }
        });
    }

    public void loadLocale(String language){
        switch (language)
        {
            case "Русский язык":
                setLocale("ru");
                break;
            case "Беларуская мова":
                setLocale("be");
                break;
            case "English":
                setLocale("en");
                break;
            default:
                break;
        }
    }
    public void setLocale(String language)
    {
        Locale myLocale = new Locale(language);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.locale = myLocale;
        resources.updateConfiguration(config, null);
        getActivity().recreate();
    }
    public void goToMainActivity(){
        Intent intent = new Intent(this.getActivity(), MainActivity.class);
        getActivity().finish();
        startActivity(intent);
    }
}
