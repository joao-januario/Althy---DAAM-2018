package inducesmile.com.test_real_time.Game;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;
import inducesmile.com.test_real_time.Multiplayer.MultiplayerQuestionHandler;
import inducesmile.com.test_real_time.Multiplayer.MultiplayerScoreActivity;
import inducesmile.com.test_real_time.Multiplayer.RoomConfigLocal;
import inducesmile.com.test_real_time.R;


public class Question_Activity extends AppCompatActivity {
    CountDownTimer timer;
    private TextView textV_question;
    private TextView textV_answer;
    QuestionsHandler handler = QuestionsHandler.getInstance();
    MultiplayerQuestionHandler multiplayerHandler = MultiplayerQuestionHandler.getInstance();
    final int score_modifier=9;
    private boolean shouldPlay=false;
    private Intent svc;
    private int single_or_multi;
    private boolean answered=false;
    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_);


//Singleplayer = 0 e Multiplayer = 1
        single_or_multi=getIntent().getIntExtra("Mode",0);
        textV_question=findViewById(R.id.question_tv);
        textV_answer=findViewById(R.id.answer_tv);

        if (single_or_multi==1){

        textV_question.setText(multiplayerHandler.getCurrentQuestionText());}
        else{
            textV_question.setText(handler.getCurrentQuestionText());
        }

        textV_answer.setFocusable(true);
        textV_answer.setEnabled(true);
        textV_answer.setClickable(true);
        textV_answer.setFocusableInTouchMode(true);
        textV_answer.setRawInputType(Configuration.KEYBOARD_12KEY);
        //textV_answer.setCursorVisible(true);
        //textV_answer.requestFocus();
        textV_answer.setInputType(InputType.TYPE_CLASS_NUMBER);

        timer = new CountDownTimer(20000,1000){
            TextView timer_tv = findViewById(R.id.timer_tv);
            @Override
            public void onTick(long l) {
                timer_tv.setText(""+l/1000);
            }

            @Override
            public void onFinish() {

                final int correct_answer;
                if(single_or_multi==1){
                    if(!answered){
                    correct_answer = multiplayerHandler.getCurrentQuestionAnswer();
                    int user_answer = 0;
                    roomConfigLocal.addMyAnswer(user_answer);
                    BigInteger bi = BigInteger.valueOf(user_answer);
                    Log.d("Sending My Answer" ,""+bi);
                    sendToAllReliably(bi.toByteArray());
                    Button button = findViewById(R.id.ok_btn2);
                    button.setEnabled(false);
                    button.setVisibility(View.INVISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            roomConfigLocal.waitForEveryoneToAnswer();
                            Intent intent = new Intent(Question_Activity.this,MultiplayerScoreActivity.class);
                            intent.putExtra("correct answer",correct_answer);
                            startActivity(intent);
                            finish();
                        }
                    }).start();}
                }else{
                    nextScreen(0,handler.getCurrentQuestionAnswer(),0);
                }
            }
        }.start();

        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
        //pausar a musica
        stopService(svc);
        //pausar a musica

    }

    protected void onDestroy() {

        super.onDestroy();
        timer.cancel();


    }

  /*  @Override
    protected void onPause() {
        super.onPause();
        if(!shouldPlay)
            stopService(svc);
    }*/

    public void confirmAnswer(View v){
        final int correct_answer;
        if (single_or_multi==1){
            answered=true;
            correct_answer = multiplayerHandler.getCurrentQuestionAnswer();
            TextView user_answer_text = findViewById(R.id.answer_tv);
            String user_answer_string = user_answer_text.getText().toString();
            int user_answer = Integer.parseInt(user_answer_string);
            roomConfigLocal.addMyAnswer(user_answer);
            BigInteger bi = BigInteger.valueOf(user_answer);
            Log.d("Sending My Answer" ,""+bi);
            sendToAllReliably(bi.toByteArray());
            Button button = findViewById(R.id.ok_btn2);
            button.setEnabled(false);
            button.setVisibility(View.INVISIBLE);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    roomConfigLocal.waitForEveryoneToAnswer();
                    Intent intent = new Intent(Question_Activity.this,MultiplayerScoreActivity.class);
                    intent.putExtra("correct answer",correct_answer);
                    startActivity(intent);
                    finish();
                }
            }).start();

        }
        else{
            correct_answer = handler.getCurrentQuestionAnswer();

            TextView user_answer_text = findViewById(R.id.answer_tv);
            String user_answer_string = user_answer_text.getText().toString();
            int user_answer = Integer.parseInt(user_answer_string);
            int question_score = calculateScore(correct_answer,user_answer);
            nextScreen(user_answer,correct_answer,question_score);


        }
    }

    public void nextScreen(int user_answer,int correct_answer,int question_score){
        if (single_or_multi==1){
            multiplayerHandler.nextQuestion();
        }
        else{
        handler.nextQuestion();}
        Intent intent = new Intent(this,ScoreActivity.class);
        intent.putExtra("user_answer",user_answer);
        intent.putExtra("correct_answer",correct_answer);
        intent.putExtra("question_score",question_score);
        shouldPlay=true;
        startActivity(intent);
        finish();
    }

    private int calculateScore(int correct_answer,int user_answer){
        if (correct_answer<0){
            return 0;
        }
        else{
            if (correct_answer/user_answer<1){
                double i = (double) correct_answer/(double ) user_answer;
                return (int) Math.round(i *score_modifier);
            }else{
                double i = (double) correct_answer/(double)user_answer;
                return (int) Math.round((double) score_modifier/(double) i);
            }

        }
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

}
