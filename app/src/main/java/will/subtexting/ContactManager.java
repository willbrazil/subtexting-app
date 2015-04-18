package will.subtexting;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by william on 4/17/15.
 */
public class ContactManager {

    private static ContactManager instance;

    private ContactManager(){}

    public static ContactManager getInstance(){
        if(instance == null) {
            instance = new ContactManager();
        }

        return instance;
    }

    public Contact getContact(Context context, String number) {

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        Cursor cursor = context.getContentResolver().query(uri, new String[] {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup._ID}, ContactsContract.PhoneLookup.NUMBER + "LIKE ?", new String[] {number.replace("+1", "")}, null, null);

        Contact contact = null;
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            contact = new Contact(cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID)), cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.PhoneLookup.DISPLAY_NAME)));
        }

        cursor.close();
        return contact;
    }

    public List<Contact> getMostLikelyContacts(Context context, int max) {

        ArrayList<Contact> contacts = new ArrayList<>();
        HashSet<Long> unique = new HashSet<>();

        ArrayList<String> numbers = MessageManager.getInstance().getMostLikelyNumbers(context);

        for(String num : numbers) {
            Contact c = getContact(context, num);
            if(c != null) {
                if(!unique.contains(c.getLocalId())) {
                    unique.add(c.getLocalId());
                    contacts.add(c);
                }
            }
        }

        if(contacts.size() <= max) {
            return contacts;
        }
        return contacts.subList(0, max);
    }

}
