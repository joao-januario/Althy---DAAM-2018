package inducesmile.com.test_real_time.Multiplayer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Game.Question_Activity;
import inducesmile.com.test_real_time.R;

public class LoadingScreenMultiplayerActivity extends AppCompatActivity {

    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    byte[] bits = new byte[2000];
    ArrayList<Integer> questions_picked= new ArrayList<Integer>();
    private final int max_questions=5;
    MultiplayerQuestionHandler multiplayerHandler = MultiplayerQuestionHandler.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        roomConfigLocal.current_context=this.getApplicationContext();
        Random r = new Random();
        byte myHostNumber = (byte) Math.abs(r.nextInt());
        roomConfigLocal.setMyNumberHost(myHostNumber);
        bits[0]='H';
        bits[1] = myHostNumber ;
        sendToAllReliably(bits);
        new Thread(new Runnable() {
            @Override
            public void run() {
                    roomConfigLocal.decideHost();
                    sendMessage();
                    roomConfigLocal.setUpQuestions();
                    sendReadyMessage();
                    roomConfigLocal.waitForEveryoneReady();
                    startGame();

            }
        }).start();


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

    public void sendMessage(){
        if (roomConfigLocal.im_host()){
            totalNumberOfFirebaseQuestions();

        }
    }

    private void totalNumberOfFirebaseQuestions(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("perto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int total_questions =  (int ) dataSnapshot.getChildrenCount();

                while(questions_picked.size()<max_questions) {
                    Random r = new Random();
                    int this_question = r.nextInt(total_questions);
                    if (!questions_picked.contains(this_question)) {
                        questions_picked.add(this_question);
                        Collections.sort(questions_picked);
                    }
                }
                for (int i=0;i<questions_picked.size();i++) {
                    BigInteger bi = BigInteger.valueOf(questions_picked.get(i));
                    Log.d("Sending question number" ,""+bi);
                    sendToAllReliably(bi.toByteArray());
                    DataSnapshot question_data = dataSnapshot.child(Integer.toString(questions_picked.get(i)));
                    Question q = question_data.getValue(Question.class);
                    multiplayerHandler.addQuestion(q);

                }

                sendReadyMessage();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        roomConfigLocal.setReady(true);
                        roomConfigLocal.waitForEveryoneReady();
                        startGame();
                    }
                }).start();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    private void sendReadyMessage(){
        Log.d("Sending Ready","Sending Ready Message");
        bits[0]='R';
        sendToAllReliably(bits);
    }

    public void startGame(){
        Intent intent = new Intent(LoadingScreenMultiplayerActivity.this, Question_Activity.class);
        intent.putExtra("Mode",1);
        startActivity(intent);
    }

}
