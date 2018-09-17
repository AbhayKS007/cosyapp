package co.project.cosy.configuration;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import co.project.cosy.Constant.Constant;


/**
 */
public class Configuration {


    public static void  setSharedPrefrenceValue(Object objCurrentClassReference, String strPrefenceFileName,
                                                String strKey, String strValue) {

        SharedPreferences sharedPreferencesForStoringData = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(strPrefenceFileName, 0);
        SharedPreferences.Editor sharedPrefencesEditor = sharedPreferencesForStoringData
                .edit();

        sharedPrefencesEditor.putString(strKey, strValue);
        sharedPrefencesEditor.commit();


    }


    public static String getSharedPrefrenceValue(Object objCurrentClassReference, String key) {

        SharedPreferences settings = ((ContextWrapper) objCurrentClassReference)
                .getSharedPreferences(
                        Constant.PREFS_NAME, Context.MODE_PRIVATE);//should be 1

        String value=settings.getString(key, null);

        return value;

    }
    public static boolean isInternetConnection(Context context) {
        // TODO Auto-generated method stub

        ConnectivityManager cn = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        boolean statusInternet;

        if(nf != null && nf.isConnected()==true )
        {
            Log.i("Info:", "Network Available.");
            statusInternet=true;
        }
        else
        {
            Log.i("Info:", "Network Not Available.");
            statusInternet=false;

        }
        return statusInternet;
    }
}
