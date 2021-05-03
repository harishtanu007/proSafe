package com.harish.prosafe.util;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class Helper  {
    static Helper helper;
    private Helper() {
    }
    private static final String TAG ="Helper" ;
    public static Helper getHelper() {
        if(helper == null)
            helper= new Helper();
        return helper;
    }
    public boolean isInternetConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }

}

