package vuchris.tacoma.uw.edu.homepagev3;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
    }

    //when user clicks the button, signin or register dialog pops up
    public void launch(View v) {
        DialogFragment fragment = null;
        if (v.getId() == R.id.instructions) {
            fragment = new InstructionsFragment();
        } else if (v.getId() == R.id.game) {
            Intent gameIntent = new Intent(this, GameActivity.class);
            startActivity(gameIntent);
        }

        //show
        if (fragment != null) {
            fragment.show(getSupportFragmentManager(), "launch");
        }
    }
}
