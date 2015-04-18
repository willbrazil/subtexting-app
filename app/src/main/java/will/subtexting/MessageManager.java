package will.subtexting;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by william on 4/17/15.
 */
public class MessageManager {

    private static MessageManager instance;

    private MessageManager() {}

    public static MessageManager getInstance() {
        if(instance == null) {
            instance = new MessageManager();
        }

        return instance;
    }

    protected ArrayList<String> getMostLikelyNumbers(Context context) {

        Set<String> numbers = new HashSet<>();
        ArrayList<String> numbersByRelevance = new ArrayList<>();

        String[] projection = new String[] {"address"};
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), projection, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            if(!numbers.contains(address)) {
                numbers.add(address);
                numbersByRelevance.add(address);
            }

            cursor.moveToNext();
        }
        cursor.close();
        return numbersByRelevance;
    }

}
