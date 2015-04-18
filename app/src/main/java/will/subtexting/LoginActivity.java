package will.subtexting;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class LoginActivity extends Activity {

    private EditText usernameEditText;
    EditText keyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.activity_login_username);
        keyEditText = (EditText) findViewById(R.id.activity_login_key);
    }

    public void login(View view) {

        String username = usernameEditText.getText().toString();
        String key = keyEditText.getText().toString();

        GCMSetup.register(getApplicationContext(), username, key);

        finish();
    }

}
