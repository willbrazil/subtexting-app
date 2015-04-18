package will.subtexting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by william on 4/17/15.
 */
public class UploadContactListArrayAdapter extends ArrayAdapter<Contact> {

    private Context context;
    private ArrayList<Contact> contacts;
    private HashMap<Long, Contact> selected;

    public UploadContactListArrayAdapter(Context context, ArrayList<Contact> contacts, HashMap<Long, Contact> selected) {
        super(context, R.layout.upload_contact_list_item, contacts);
        this.context = context;
        this.contacts = contacts;
        this.selected = selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.upload_contact_list_item, null, false);
        }

        if(selected.containsKey(contacts.get(position).getLocalId())) {
            convertView.setBackgroundColor(getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            convertView.setBackgroundColor(0);
        }

        TextView nameTextView = (TextView) convertView.findViewById(R.id.activity_upload_contacts_list_contact_name);
        nameTextView.setText(contacts.get(position).getName());

        return convertView;
    }
}
