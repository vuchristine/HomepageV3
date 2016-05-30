package vuchris.tacoma.uw.edu.homepagev3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

public class InviteActivity extends AppCompatActivity {

    private static final
    String INVITATION_TITLE = "Call your friends",
            INVITATION_MESSAGE = "Hey! Want to invite your friends for this \"awesome\" app? :)",
            INVITATION_CALL_TO_ACTION = "Share";
    
    Button button;

    public static final int REQUEST_INVITE = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //create button
        button = (Button) findViewById(R.id.invite);
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

        //the following methods is for deep linking
        //create a new intent to send message
        Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE)
                .setMessage(INVITATION_MESSAGE)
                .setDeepLink(Uri.parse("tutsplus://code.coupon/50"))
                .setCallToActionText(INVITATION_CALL_TO_ACTION)
                .build();

        //this receives and parse custom URL
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

        //get invitation
        AppInvite.AppInviteApi.getInvitation(googleApiClient, this, true)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {}
                        });
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
