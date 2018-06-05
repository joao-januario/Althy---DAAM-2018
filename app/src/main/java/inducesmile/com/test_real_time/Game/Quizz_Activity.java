package inducesmile.com.test_real_time.Game;


import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;
import inducesmile.com.test_real_time.Helper.QuizQuestionsHandler;
import inducesmile.com.test_real_time.Multiplayer.MultiplayerQuestionHandler;
import inducesmile.com.test_real_time.R;

import static java.lang.Thread.sleep;


public class Quizz_Activity extends AppCompatActivity {
    CountDownTimer timer;
    private TextView textV_question;
    private String final_answer;
    private String right_answer;
    QuizQuestionsHandler handler = QuizQuestionsHandler.getInstance();
    MultiplayerQuestionHandler multiplayerHandler = MultiplayerQuestionHandler.getInstance();
    final int score_modifier=9;
    private boolean shouldPlay=false;
    private Intent svc;
    private int single_or_multi;
    private boolean switch_screen=false;
    private Button option_a;
    private Button option_b;
    private Button option_c;
    private Button option_d;
    private  long time;
    private long max_timer=20000;
   // public MediaPlayer tick;
   // public MediaPlayer timer_end;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizz);


//Singleplayer = 0 e Multiplayer = 1
        single_or_multi=getIntent().getIntExtra("Mode",0);
        textV_question=findViewById(R.id.question_tv);
        option_a=findViewById(R.id.option_a_btn);
        option_b=findViewById(R.id.option_b_btn);
        option_c=findViewById(R.id.option_c_btn);
        option_d=findViewById(R.id.option_d_btn);


        if (single_or_multi==1){
            textV_question.setText(multiplayerHandler.getCurrentQuestionText());}
        else{
            textV_question.setText(handler.getCurrentQuestionText());
        }

        //Respostas
        right_answer=handler.getCurrentQuestionAnswer();
        fill_buttons();
        //respostas

        //check answer

        option_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickerCode(option_a);
            }
        });
        option_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               clickerCode(option_b);
            }
        });
        option_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickerCode(option_c);

            }
        });
        option_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              clickerCode(option_d);
            }
        });

                timer = new CountDownTimer(max_timer,1000){
             TextView timer_tv = findViewById(R.id.timer_tv);


                    @Override
            public void onTick(long l) {
                 //playtick(l);
                 time=l;
                 timer_tv.setText(""+l/1000);




            }

            @Override
            public void onFinish() {
                nextScreen(right_answer, final_answer);
            }
        }.start();

        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
        //pausar a musica
        stopService(svc);
        //pausar a musica

    }

    private void playtick(long l) {
        MediaPlayer tick = MediaPlayer.create(Quizz_Activity.this, R.raw.click);
        MediaPlayer timer_end = MediaPlayer.create(Quizz_Activity.this, R.raw.end_timer);
        if(l!=0)
            tick.start();
        else
            timer_end.start();
    }

    private void fill_buttons() {

        ArrayList<String> answers = new ArrayList<>();
        answers.add(handler.getCurrentQuestion().w1);
        answers.add(handler.getCurrentQuestion().w2);
        answers.add(handler.getCurrentQuestion().w3);
        answers.add(handler.getCurrentQuestionAnswer());

        int x = (int)(Math.random() * answers.size());
        option_a.setText(answers.remove(x));
        x = (int)(Math.random() * answers.size());
        option_b.setText(answers.remove(x));
        x = (int)(Math.random() * answers.size());
        option_c.setText(answers.remove(x));
        x = (int)(Math.random() * answers.size());
        option_d.setText(answers.remove(x));



    }

    private synchronized void setSwitch_screen(boolean v){
        switch_screen=v;
        notifyAll();
    }

    protected void onDestroy() {

        super.onDestroy();
        timer.cancel();


    }



    public synchronized void nextScreen(final String correct_answer, final String user_answer){

        if (single_or_multi==1){
            multiplayerHandler.nextQuestion();
        }
        else{
            handler.nextQuestion();}
        shouldPlay=true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                    verifyScreen();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                Intent intent = new Intent(Quizz_Activity.this,QuizScoreActivity.class);



                intent.putExtra("question_score",calculateScore(correct_answer, user_answer));
                intent.putExtra("question_time",((int)max_timer/1000)-(int)time/1000);

                startActivity(intent);

                finish();
            }
        }).start();

    }

    private void checkAnswer(final String user_answer, final String correct_answer, final Button option_btn) {

        new CountDownTimer(2000, 1000) {

            @Override
            public void onTick(long arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                if(user_answer.equals(correct_answer)){
                    option_btn.setBackgroundResource((R.drawable.button_correct_green_round));
                    MediaPlayer mp = MediaPlayer.create(Quizz_Activity.this, R.raw.correct_3);
                    mp.start();

                }

                if(!user_answer.equals(correct_answer)){
                    MediaPlayer mp = MediaPlayer.create(Quizz_Activity.this, R.raw.wrong_1);
                    mp.start();
                    option_btn.setBackgroundResource((R.drawable.button_wrong_red_round));
                    if(option_a.getText().toString().equals(correct_answer)){
                        option_a.setBackgroundResource((R.drawable.button_correct_green_round));
                    }if(option_b.getText().toString().equals(correct_answer)){
                        option_b.setBackgroundResource((R.drawable.button_correct_green_round));
                    }if(option_c.getText().toString().equals(correct_answer)){
                        option_c.setBackgroundResource((R.drawable.button_correct_green_round));
                    }if(option_d.getText().toString().equals(correct_answer)){
                        option_d.setBackgroundResource((R.drawable.button_correct_green_round));
                    }
                }
            }
        }.start();




    }

    public synchronized void verifyScreen(){
        while (switch_screen==false){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clickerCode(Button button){
        MediaPlayer mp = MediaPlayer.create(this, R.raw.selection_1);
        mp.start();
        final_answer=button.getText().toString();
        timer.cancel();
        button.setBackgroundResource((R.drawable.button_selected_yellow_round));
        // if(option_c.getText().toString().equals(right_answer)){
        //    option_c.setBackgroundResource((R.drawable.button_selected_yellow_round));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    setSwitch_screen(true);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        checkAnswer(button.getText().toString(), right_answer, button);
        nextScreen(right_answer, final_answer);
    }

    private int calculateScore(String correct_answer,String user_answer){
        if (!correct_answer.equals(user_answer)){
            return 0;
        }
        else{
            double s = (double)(((double)time)/((double)max_timer));
            Log.d("TAGI", Double.toString(s));
            return (int)(s * 10);


        }
    }

}