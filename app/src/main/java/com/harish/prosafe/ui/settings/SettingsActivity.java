package com.harish.prosafe.ui.settings;

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
import android.os.Handler;
import android.os.ResultReceiver;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.harish.prosafe.R;
import com.harish.prosafe.data.adapters.AddressValueChangeListener;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.ui.incidentlocation.FetchAddressService;
import com.harish.prosafe.ui.myposts.MyPostsActivity;
import com.harish.prosafe.ui.updatelocation.UpdateCurrentLocationActivity;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.IBackendProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.harish.prosafe.util.Constants.LOCATION_ADDRESS;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    IBackendProvider backendProvider;
    private ResultReceiver resultReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Neighbors Settings");
        backendProvider = IBackendProvider.getBackendProvider();
        resultReceiver = new AddressResultReceiver(new Handler());
        // load settings fragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainPreferenceFragment()).commit();
    }

    public static class MainPreferenceFragment extends PreferenceFragment {
        IBackendProvider backendProvider;
        private Preference currentAddress;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_main);
            backendProvider = IBackendProvider.getBackendProvider();

            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(preference -> {
                sendFeedback(getActivity());
                return true;
            });

            Preference version = findPreference("version");
            try {
                version.setSummary(getString(R.string.app_version, appVersion(getActivity()), appBuild(getActivity())));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            currentAddress= findPreference(getString(R.string.location_title));
            currentAddress.setTitle("Current Address");
            currentAddress.setSelectable(false);
            backendProvider.getAddressValueEventListener(new AddressValueChangeListener() {
                @Override
                public void onSuccess(Coordinates coordinates) {
                    Location location = new Location("providerNA");
                    location.setLatitude(coordinates.getLatitude());
                    location.setLongitude(coordinates.getLongitude());
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    String errorMessage = "";
                    List<Address> addresses = null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    }catch (Exception e){
                        errorMessage = e.getMessage();
                    }
                    if(addresses==null || addresses.isEmpty()){
                        Toast.makeText(getActivity(),"Unable to get current location",Toast.LENGTH_SHORT).show();
                    }else {
                        Address address = addresses.get(0);
                        ArrayList<String> addressFragments = new ArrayList<>();
                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            addressFragments.add(address.getAddressLine(i));
                        }
                        currentAddress.setSummary(TextUtils.join(Objects.requireNonNull(System.getProperty("line.separator")),addressFragments));
                    }

                }

                @Override
                public void onFailed() {

                }
            });


            Preference myPosts = findPreference(getString(R.string.my_posts));
            myPosts.setOnPreferenceClickListener(preference -> {
                openMyPostsActivity(getActivity());
                return false;
            });

            Preference changeAddress = findPreference(getString(R.string.change_address));
            changeAddress.setOnPreferenceClickListener(preference -> {
                openChangeAddressActivity(getActivity());
                return true;
            });

        }

        private void openChangeAddressActivity(Activity activity) {
            startActivityForResult(new Intent(activity, UpdateCurrentLocationActivity.class), Constants.LOCATION_ACTIVITY);
        }

        private void openMyPostsActivity(Activity activity) {
            startActivity(new Intent(activity, MyPostsActivity.class));
        }

    }
    private void fetchAddressfromLatLong(Location location) {
        Intent intent = new Intent(this, FetchAddressService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getApplicationContext(), resultCode, Toast.LENGTH_SHORT).show();
        String address;
        Toast.makeText(getApplicationContext(), "Test", Toast.LENGTH_SHORT).show();
        if (requestCode == Constants.LOCATION_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                address = data.getStringExtra(LOCATION_ADDRESS);
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result

            }
        }
    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        String address;
//        if (requestCode == LOCATION_ACTIVITY) {
//            if(resultCode == Activity.RESULT_OK){
//                address=data.getStringExtra(LOCATION_ADDRESS);
//                Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();
//                backendProvider.addAddressValueEventListener(address).setEventListener(new EventListener() {
//                    @Override
//                    public void onSuccess() {
//
//                    }
//
//                    @Override
//                    public void onFailed() {
//
//                    }
//                });
//            }
//            if (resultCode == Activity.RESULT_CANCELED) {
//                //Write your code if there's no result
//
//            }
//        }
//    }

    public static void sendFeedback(Context context) {
        String body = null;
        int code;
        try {
            body = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            code = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "." + code + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"harishtanu007@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from Ping Me app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, context.getString(R.string.choose_email_client)));
    }


    public static String appVersion(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        String version = pInfo.versionName;
        return version;
    }

    public static int appBuild(Context context) throws PackageManager.NameNotFoundException {
        PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        int build = pInfo.versionCode;
        return build;
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
    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.RESULT_SUCCESS) {
                String address = resultData.getString(Constants.RESULT_DATA_KEY);


            } else {
                Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
