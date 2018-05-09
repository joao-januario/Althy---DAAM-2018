package inducesmile.com.test_real_time.Multiplayer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import inducesmile.com.test_real_time.R;

public class LoadingScreenMultiplayerActivity extends AppCompatActivity {

    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    byte[] bits = new byte[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        bits[0]=(byte )2;
        new Thread(new Runnable() {
            @Override
            public void run() {

                       final int message = roomConfigLocal.getMessage();

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        TextView text = findViewById(R.id.testReceive);
                        text.setText(Integer.toString(message));
                        // Stuff that updates the UI

                    }
                });
            }
        }).start();

    }


    void changeText(int text2){

    }

    void sendToAllReliably(byte[] message) {
        for (String participantId : roomConfigLocal.getParticipants()) {
            if (!participantId.equals(roomConfigLocal.getMyId())) {
                Task<Integer> task = Games.
                        getRealTimeMultiplayerClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .sendReliableMessage(message, roomConfigLocal.getRoomID(), participantId,
                                handleMessageSentCallback).addOnCompleteListener(new OnCompleteListener<Integer>() {
                            @Override
                            public void onComplete(@NonNull Task<Integer> task) {
                                // Keep track of which messages are sent, if desired.

                            }
                        });
            }
        }
    }

    private RealTimeMultiplayerClient.ReliableMessageSentCallback handleMessageSentCallback =
            new RealTimeMultiplayerClient.ReliableMessageSentCallback() {
                @Override
                public void onRealTimeMessageSent(int statusCode, int tokenId, String recipientId) {
                    TextView text_tv = findViewById(R.id.testeSend);
                    text_tv.setText(recipientId);
                }
            };

    public void sendMessage(View v){
        sendToAllReliably(bits);
    }


}
