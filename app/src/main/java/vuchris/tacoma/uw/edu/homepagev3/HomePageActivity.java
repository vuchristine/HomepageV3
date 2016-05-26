package vuchris.tacoma.uw.edu.homepagev3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class HomePageActivity extends AppCompatActivity {

    //strings for invite
    private static final
    String INVITATION_TITLE = "Call your friends and family",
            INVITATION_MESSAGE = "Hey! Want to invite your friends and families to try this awesome app? :)",
            INVITATION_CALL_TO_ACTION = "Share";
    private static final int REQUEST_INVITE = 12;

    //create button
    Button button = (Button) findViewById(R.id.invite);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //following code creates an invite for user to send
        //make invite button work

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something, will you?
                Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE)
                        .setMessage(INVITATION_MESSAGE)
                        .setCallToActionText(INVITATION_CALL_TO_ACTION)
                        .build();
                startActivityForResult(intent, REQUEST_INVITE);
            }
        });


        //build a new intent that sends app invite
        Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE)
                .setMessage(INVITATION_MESSAGE)
                .setDeepLink(Uri.parse("tutsplus://code.coupon/50"))
                .setCallToActionText(INVITATION_CALL_TO_ACTION)
                .build();

        //to receive and parse custom URL
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        Log.d(getString(R.string.app_name), "onConnectionFailed:" + connectionResult);
                        showMessage("Sorry, the connection has failed.");
                    }
                })
                .build();

        //check for invites
        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, true)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {}
                        });

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {

                // You successfully sent the invite,
                // we can dismiss the button.
                button.setVisibility(View.GONE);

                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                StringBuilder sb = new StringBuilder();
                sb.append("Sent ").append(Integer.toString(ids.length)).append(" invitations: ");
                for (String id : ids) sb.append("[").append(id).append("]");
                Log.d(getString(R.string.app_name), sb.toString());

            } else {
                // Sending failed or it was canceled using the back button
                showMessage("Sorry, I wasn't able to send the invites");
            }
        }
    }

    private void showMessage(String s) {
    }

}
