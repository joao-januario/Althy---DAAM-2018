package inducesmile.com.test_real_time.Multiplayer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import inducesmile.com.test_real_time.Game.Question_Activity;
import inducesmile.com.test_real_time.R;

public class MultiplayerScoreActivity extends AppCompatActivity {

    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    ArrayList<String> mparticipants = roomConfigLocal.getmParticipants();
    HashMap<String,Integer> answers =new HashMap<>();
    HashMap<String,String> usernames = RoomConfigLocal.getInstance().getAllPlayersIds();
    HashMap<String,Integer> question_score_helper = new HashMap<>();
    HashMap<String,Integer> question_score = new HashMap<>();
    MultiplayerQuestionHandler mhandler = MultiplayerQuestionHandler.getInstance();

    int correct_answer = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_score);
        answers = roomConfigLocal.getAllAnswersToCurrentQuestion();
        correct_answer = getIntent().getIntExtra("correct answer",0);
        calculateScores();
        updateTotalScores();
        waitForEveryoneReadyForNextRound();
        TextView correctAnswer = findViewById(R.id.correct_answer_mp_tv);
        correctAnswer.setText(Integer.toString(correct_answer));
        correctAnswer.setTextColor(Color.WHITE);


    }

    private void calculateScores(){

        Log.d("Correct Answer",Integer.toString(correct_answer));
        for(String m: mparticipants) {
            int answer = answers.get(m);
            Log.d("all answers received",Integer.toString(answer));
            int score_helper = Math.abs(correct_answer- answer);
            question_score_helper.put(m,score_helper);

        }

        Object[] a = question_score_helper.entrySet().toArray();

        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o1).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o2).getValue());
            }
        });
        int last_value=-1;
        int last_score = -1;
        int next_score=10;

        for (Object e: a) {

            String key = ((Map.Entry<String, Integer>) e).getKey();
            int value = ((Map.Entry<String, Integer>) e).getValue();

            Log.d("username",usernames.get(key));

            Log.d("all answers sorted",Integer.toString(value));
            if ( value==0 ){

                Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                        .unlock(getString(R.string.achievement_adivinho));
                question_score.put(key,20);
                next_score=5;
            }else{
                if ( value == last_value){
                    question_score.put(key,last_score);
                }
                else{
                    Log.d("Calculating","calculating");
                    question_score.put(key,next_score);
                    last_value=value;
                    if (next_score==10){
                        last_score=10;
                        next_score=5;
                    }else{
                        if (next_score==5){
                            last_score=5;
                            next_score=2;
                        }else{
                            if(next_score==2){
                                last_score=2;
                                next_score=0;
                            }
                        }
                    }

                }
            }


        }

        }


    private void updateTotalScores(){
        //To do

        TableLayout tl = findViewById(R.id.score_table);

        Object[] a = question_score.entrySet().toArray();
        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });

        for (Object e: a){
            String playerID =((Map.Entry<String, Integer>) e).getKey();
            int playerScore = ((Map.Entry<String, Integer>) e).getValue();
            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView nomeJogador = new TextView(this);


            nomeJogador.setTextColor(getResources().getColor(R.color.colorPrimary));
            nomeJogador.setText(usernames.get(playerID));
            nomeJogador.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);


            TextView scoreJogador = new TextView(this);
            scoreJogador.setText(Integer.toString(playerScore));
            scoreJogador.setTextColor(getResources().getColor(R.color.colorPrimary));
            scoreJogador.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);

            TextView respostaJogador = new TextView(this);
            respostaJogador.setText(Integer.toString(answers.get(playerID)));
            respostaJogador.setTextColor(getResources().getColor(R.color.colorPrimary));
            respostaJogador.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);

            row.addView(nomeJogador);
            row.addView(scoreJogador);
            row.addView(respostaJogador);
            tl.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));


            roomConfigLocal.addPlayerScore(playerID,playerScore);


        }
    }

    public void waitForEveryoneReadyForNextRound(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                roomConfigLocal.waitForEveryoneReadyForNextRound();
                newRound();
                if(mhandler.moreQuestions()) {
                    mhandler.nextQuestion();
                    Intent i = new Intent(MultiplayerScoreActivity.this, Question_Activity.class);
                    i.putExtra("Mode", 1);
                    startActivity(i);
                    finish();
                }else{
                    Intent i = new Intent(MultiplayerScoreActivity.this,MultiplayerFinalScoreActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }).start();
    }

    public void readyForNextQuestion(View V){
        Button readyButton = findViewById(R.id.readyButton);
        //readyButton.setEnabled(false);
        //readyButton.setVisibility(View.INVISIBLE);
        TextView text = findViewById(R.id.waiting_for_players_tv);
        text.setTextColor(Color.WHITE);
        text.setVisibility(View.VISIBLE);
        roomConfigLocal.setReadyForNextRound(true);
        byte[] message = new byte[2000];
        message[0]='N';
        sendToAllReliably(message);

    }

    private int dpAsPixels(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp*scale + 0.5f);

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

                }
            };



    private void newRound(){
        roomConfigLocal.setReadyForNextRound(false);
        roomConfigLocal.clearEveryoneReadyForNextRound();
        roomConfigLocal.clearAllAnswersToCurrentQuestion();
    }
}
