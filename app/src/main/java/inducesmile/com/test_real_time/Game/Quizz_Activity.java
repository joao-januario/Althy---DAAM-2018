package inducesmile.com.test_real_time.Game;


import android.content.Intent;
import android.content.res.Configuration;
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
               // if(option_a.getText().toString().equals(right_answer)){
               //     option_a.setBackgroundResource((R.drawable.button_selected_yellow_round));
                option_a.setBackgroundResource((R.drawable.button_selected_yellow_round));
                checkAnswer(option_a.getText().toString(), right_answer, option_a);
                    nextScreen( option_a.getText().toString(), right_answer, 0);
               // }
            }
        });
        option_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(option_b.getText().toString().equals(right_answer)){
                    //option_b.setBackgroundResource((R.drawable.button_selected_yellow_round));
                option_b.setBackgroundResource((R.drawable.button_selected_yellow_round));
                checkAnswer(option_a.getText().toString(), right_answer, option_b);
                nextScreen(option_b.getText().toString(), right_answer, 0);
               // }
            }
        });
        option_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                option_c.setBackgroundResource((R.drawable.button_selected_yellow_round));
               // if(option_c.getText().toString().equals(right_answer)){
                //    option_c.setBackgroundResource((R.drawable.button_selected_yellow_round));
               new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {

                        try {
                            sleep(2000);
                            setSwitch_screen(true);
                            notifyAll();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        checkAnswer(option_a.getText().toString(), right_answer, option_c);
                        nextScreen(option_c.getText().toString(), right_answer, 0);
                    }
                }).start();
               // }
            }
        });
        option_d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if(option_d.getText().toString().equals(right_answer)){
                //    option_d.setBackgroundResource((R.drawable.button_selected_yellow_round));
                option_d.setBackgroundResource((R.drawable.button_selected_yellow_round));
                checkAnswer(option_a.getText().toString(), right_answer, option_d);
                nextScreen(option_d.getText().toString(), right_answer, 0);
               // }
            }
        });

                timer = new CountDownTimer(20000,1000){
            TextView timer_tv = findViewById(R.id.timer_tv);
            @Override
            public void onTick(long l) {
                timer_tv.setText(""+l/1000);
            }

            @Override
            public void onFinish() {
                nextScreen("0",handler.getCurrentQuestionAnswer(),0);
            }
        }.start();

        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
        //pausar a musica
        stopService(svc);
        //pausar a musica

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
/*
    public void confirmAnswer(View v){
        int correct_answer;
        if (single_or_multi==1){
            correct_answer = multiplayerHandler.getCurrentQuestionAnswer();}
        else{
            correct_answer = handler.getCurrentQuestionAnswer();
        }
        TextView user_answer_text = findViewById(R.id.answer_tv);
        String user_answer_string = user_answer_text.getText().toString();
        int user_answer = Integer.parseInt(user_answer_string);
        int question_score = calculateScore(correct_answer,user_answer);
        nextScreen(user_answer,correct_answer,question_score);


    }*/

    public synchronized void nextScreen( String user_answer,String correct_answer,int question_score){

       // checkAnswer(user_answer, correct_answer, option_btn);

        if (single_or_multi==1){
            multiplayerHandler.nextQuestion();
        }
        else{
            handler.nextQuestion();}
        Intent intent = new Intent(this,ScoreActivity.class);
       // intent.putExtra("user_answer",user_answer);
        //intent.putExtra("correct_answer",correct_answer);
       // intent.putExtra("question_score",question_score);
        shouldPlay=true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (switch_screen==false){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(Quizz_Activity.this,ScoreActivity.class);
                startActivity(intent);

                finish();
            }
        }).start();

    }

    private void checkAnswer(String user_answer, String correct_answer, Button option_btn) {


        if(user_answer.equals(correct_answer)){
            option_btn.setBackgroundResource((R.drawable.button_correct_green_round));
        }

        if(!user_answer.equals(correct_answer)){
            option_btn.setBackgroundResource((R.drawable.button_wrong_red_round));
        }


    }
/*
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
*/
}