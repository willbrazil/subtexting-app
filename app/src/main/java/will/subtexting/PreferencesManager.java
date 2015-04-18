package will.subtexting;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by william on 4/18/15.
 */
public class PreferencesManager {

    private static final String PREF_FILE = "app_pref";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASS = "pass";

    public static void setCreds(Context context, String username, String password) {
        SharedPreferences.Editor editor = getPref(context).edit();
        editor.putString(PREF_USERNAME, username);
        editor.putString(PREF_PASS, password);
        editor.commit();
    }

    public static String getUsername(Context context) {
        return getPref(context).getString(PREF_USERNAME, null);
    }

    public static String getPassword(Context context) {
        return getPref(context).getString(PREF_PASS, null);
    }

    private static SharedPreferences getPref(Context context) {
        return context.getSharedPreferences(PREF_FILE, 0);
    }

}
