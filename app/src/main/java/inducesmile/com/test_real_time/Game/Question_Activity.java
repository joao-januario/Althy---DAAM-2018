package inducesmile.com.test_real_time.Game;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import inducesmile.com.test_real_time.Helper.QuestionsHandler;
import inducesmile.com.test_real_time.R;


public class Question_Activity extends AppCompatActivity {
    CountDownTimer timer;
    private TextView textV_question;
    private TextView textV_answer;
    QuestionsHandler handler = QuestionsHandler.getInstance();
    final int score_modifier=9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_);
        textV_question=findViewById(R.id.question_tv);
        textV_answer=findViewById(R.id.answer_tv);

        textV_question.setText(handler.getCurrentQuestionText());

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
                nextScreen(0,handler.getCurrentQuestionAnswer(),0);
            }
        }.start();

    }

    protected void onDestroy() {

        super.onDestroy();
        timer.cancel();


    }


    public void confirmAnswer(View v){
        int correct_answer = handler.getCurrentQuestionAnswer();
        TextView user_answer_text = findViewById(R.id.answer_tv);
        String user_answer_string = user_answer_text.getText().toString();
        int user_answer = Integer.parseInt(user_answer_string);
        int question_score = calculateScore(correct_answer,user_answer);
        nextScreen(user_answer,correct_answer,question_score);


    }

    public void nextScreen(int user_answer,int correct_answer,int question_score){
        handler.nextQuestion();
        Intent intent = new Intent(this,ScoreActivity.class);
        intent.putExtra("user_answer",user_answer);
        intent.putExtra("correct_answer",correct_answer);
        intent.putExtra("question_score",question_score);
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

}
