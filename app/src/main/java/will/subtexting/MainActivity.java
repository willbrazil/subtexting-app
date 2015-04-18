package will.subtexting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(!GCMSetup.isRegisteredOnServer(getApplicationContext())) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
/*
        ArrayList<String> numbers = MessageManager.getInstance().getMostLikelyNumbers(getApplicationContext());

        final StringBuilder sb = new StringBuilder();
        sb.append("{");

        for(String num : numbers) {
            Contact c = ContactManager.getInstance().getContact(getApplicationContext(), num);

            if(c != null) {
                Log.d("contacts", c.getLocalId() + ": " + c.getName());
                sb.append("\""+ c.getLocalId() +"\":\""+ c.getName() +"\", ");
            }
        }
        sb.deleteCharAt(sb.length()-2);
        sb.append("}");

        Log.d("req", sb.toString());

        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {

                Map<String, String> data = new HashMap<String, String>();
                data.put("contact_list", sb.toString());

                HttpRequest req = HttpRequest.post("http://52.11.152.202:80/contacts").basic("will", "7z92bx1a").form(data);



                int code = req.code();
                Log.d("req", "req: " + req.body());
                Log.d("req", "code: " + req.code());

                return code;
            }

            @Override
            protected void onPostExecute(Integer integer) {

                if(integer == 200) {
                    Toast.makeText(getApplicationContext(), "Worked!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed: " + integer, Toast.LENGTH_SHORT).show();
                }

                super.onPostExecute(integer);
            }
        }.execute();
        */
    }

    public void selectContacts(View view) {
        startActivity(new Intent(getApplicationContext(), UploadContactsActivity.class));
    }

}
