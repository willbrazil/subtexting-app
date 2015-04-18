package will.subtexting;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by william on 4/4/15.
 */
public class GCMSetup {

    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final String TAG = "gcm_setup";
    private static final String SENDER_ID = "212138337191";
    private static final String IS_REGISTRED_ON_SERVER = "is_registered_on_server";

    public static void register(Context context, String username, String key) {

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
        String id = getRegistrationId(context);

        if (id.isEmpty()){
            registerInBackground(context, gcm, username, key);
        } else {
            if(!isRegisteredOnServer(context)){
                sendRegistrationIdToBackend(context, id, username, key);
            }
            Log.d(TAG, "Already registered: " + id);
        }

    }

    private static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion =  getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return context.getSharedPreferences("subtexting_prefs",
                Context.MODE_PRIVATE);
    }

    private static void registerInBackground(final Context context, final GoogleCloudMessaging gcm, final String username, final String key) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    String regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.
                    sendRegistrationIdToBackend(context, regid, username, key);

                    // For this demo: we don't need to send it because the device
                    // will send upstream messages to a server that echo back the
                    // message using the 'from' address in the message.

                    // Persist the registration ID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

        }.execute(null, null, null);
    }

    private static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    private static void sendRegistrationIdToBackend(final Context context, final String id, final String username, final String key) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                Map<String, String> data = new HashMap<String, String>();
                data.put("username", username);
                data.put("password", key);
                data.put("registration_id", id);
                try {
                    int response = HttpRequest.post("http://52.11.152.202:80/registration_id").form(data).code();
                    if(response == 200) {
                        Log.d(TAG, "Added to server.");
                        setRegistered(context);
                    } else {
                        Log.d(TAG, "Response: " + response);
                    }
                } catch (HttpRequest.HttpRequestException e){
                    Log.d(TAG, "Failed: " + e.getMessage());
                }

                return null;
            }
        }.execute();

    }

    private static void setRegistered(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(IS_REGISTRED_ON_SERVER, true);
        edit.commit();
    }

    public static boolean isRegisteredOnServer(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        return prefs.getBoolean(IS_REGISTRED_ON_SERVER, false);
    }

}
