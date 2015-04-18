package will.subtexting;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

/**
 * Created by william on 4/4/15.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {

    private static final String BR_LOG = "BR_LOG";

    @Override
    public void onReceive(Context context, Intent intent) {


        String localId = intent.getStringExtra("local_id");
        Log.d(BR_LOG, "Received. Local Id: " + localId);
        String number = ContactManager.getInstance().getNumber(context, localId);

        Log.d("test", "number: " + number);

        if(number != null) {
            SmsManager.getDefault().sendTextMessage(number, null, intent.getStringExtra("body"), null, null);
            Log.d("test", "message sent...");
        }

    }
}
