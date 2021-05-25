package com.harish.prosafe.ui.customincidents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.AddressValueChangeListener;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.ui.myposts.MyPostsActivity;
import com.harish.prosafe.ui.settings.AppCompatPreferenceActivity;
import com.harish.prosafe.ui.settings.SettingsActivity;
import com.harish.prosafe.ui.updatelocation.UpdateCurrentLocationActivity;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.IBackendProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.harish.prosafe.util.Constants.LOCATION_ADDRESS;

public class CustomIncidentsActivity extends AppCompatPreferenceActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();
    IBackendProvider backendProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.neighbor_settings);
        backendProvider = IBackendProvider.getBackendProvider();

        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {


        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.root_preferences);
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}