package will.subtexting;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
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
        String number = getNumber(context, localId);

        if(number != null) {
            SmsManager.getDefault().sendTextMessage(number, null, intent.getStringExtra("body"), null, null);
            Log.d("test", "message sent...");
        }

    }

    public String getNumber(Context context, String contactId) {

        String number = null;

        ContentResolver mContentResolver = context.getContentResolver();
        Cursor cursor = mContentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone._ID +" = ?",
                new String[] {contactId}, null);

        if (cursor.getCount() > 0){
            cursor.moveToNext();
            number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursor.close();
        return number;

    }

}
