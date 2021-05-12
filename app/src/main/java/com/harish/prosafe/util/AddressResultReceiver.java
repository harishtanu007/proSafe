package com.harish.prosafe.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

public class AddressResultReceiver extends ResultReceiver {

    AddressResultReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (resultCode == Constants.RESULT_SUCCESS) {
            String address = resultData.getString(Constants.RESULT_DATA_KEY);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(Constants.LOCATION_ADDRESS,address);
//            returnIntent.putExtra(Constants.LOCATION_LATITUDE,latitude);
//            returnIntent.putExtra(Constants.LOCATION_LONGITUDE,longitude);
//            setResult(Activity.RESULT_OK,returnIntent);
//            finish();
        } else {
//            Toast.makeText(getApplicationContext(), resultData.getString(Constants.RESULT_DATA_KEY), Toast.LENGTH_SHORT).show();
        }
    }
}
