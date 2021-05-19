package com.harish.prosafe.util;



import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.harish.prosafe.data.model.Coordinates;

public class Helper  {
    static Helper helper;private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private Helper() {
    }
    private static final String TAG ="Helper" ;
    public static Helper getHelper() {
        if(helper == null)
            helper= new Helper();
        return helper;
    }

    public static double distance(Coordinates coordinates1, Coordinates coordinates2) {
        double lat1=coordinates1.getLatitude();
        double lat2 =coordinates2.getLatitude();
        double lon1=coordinates1.getLongitude();
        double lon2=coordinates2.getLongitude();
        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles and 6371 for Kms
        double r = 3956;
        // calculate the result
        return(c * r);
    }

    public static String getDistanceUnit() {
        return "mi";
    }

    public boolean isInternetConnected(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //we are connected to a network
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis() / 1000L;
        if (now < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            now *= 1000;
        }
        if (time > now || time <= 0) {
            return "error";
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }

}

