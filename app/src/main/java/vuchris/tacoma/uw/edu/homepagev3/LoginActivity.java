package vuchris.tacoma.uw.edu.homepagev3;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    //when user clicks the button
    public void launch(View v) {
        DialogFragment fragment = null;
        if (v.getId() == R.id.signin_button) {
            fragment = new SignInFragment();
        } else if (v.getId() == R.id.register_button) {
            fragment = new RegisterFragment();
        }

        //show
        if (fragment != null) {
            fragment.show(getSupportFragmentManager(), "launch");
        }
    }

}
