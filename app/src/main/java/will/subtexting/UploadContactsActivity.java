package will.subtexting;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UploadContactsActivity extends Activity {

    ProgressBar progressBar;

    ListView selectContactList;
    ArrayList<Contact> contacts;
    HashMap<Long, Contact> selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_contacts);

        progressBar = (ProgressBar) findViewById(R.id.activity_upload_contacts_progress_bar);

        contacts = new ArrayList<>();
        selected = new HashMap<>();

        selectContactList = (ListView) findViewById(R.id.activity_upload_contacts_list);

        final ArrayAdapter<Contact> adapter = new UploadContactListArrayAdapter(getApplicationContext(), contacts, selected);
        selectContactList.setAdapter(adapter);

        selectContactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(!selected.containsKey(contacts.get(position).getLocalId())) {
                    selected.put(contacts.get(position).getLocalId(), contacts.get(position));
                    Log.d("upload_list", "Added " + contacts.get(position).getName() + " - " + contacts.get(position).getLocalId());
                } else {
                    selected.remove(contacts.get(position).getLocalId());
                    Log.d("upload_list", "Removed " + contacts.get(position).getName());
                }
                adapter.notifyDataSetChanged();
            }
        });

        new AsyncTask<Void, Void, List<Contact>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<Contact> doInBackground(Void... params) {
                return ContactManager.getInstance().getMostLikelyContacts(getApplicationContext(), 40);
            }

            @Override
            protected void onPostExecute(List<Contact> list) {
                progressBar.setVisibility(View.GONE);
                contacts.addAll(list);
                adapter.notifyDataSetChanged();
                super.onPostExecute(contacts);
            }
        }.execute();
    }




}
