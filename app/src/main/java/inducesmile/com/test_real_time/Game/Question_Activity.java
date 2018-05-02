package inducesmile.com.test_real_time.Game;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

    private TextView textV_question;
    private TextView textV_answer;
    QuestionsHandler handler = QuestionsHandler.getInstance();
    final int score_modifier=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_);
        textV_question=findViewById(R.id.question_textView);
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

    }



    private void confirmAnswer(){
        int correct_answer = handler.getCurrentQuestionAnswer();
        TextView user_answer_text = findViewById(R.id.answer_tv);
        String user_answer_string = user_answer_text.getText().toString();
        int user_answer = Integer.parseInt(user_answer_string);
        int question_score = calculateScore(correct_answer,user_answer);
        handler.nextQuestion();





    }

    private int calculateScore(int correct_answer,int user_answer){
        if (correct_answer<0){
            return 0;
        }
        else{
            if (correct_answer/user_answer<1){
                return (correct_answer/user_answer)*score_modifier;
            }else{
                return score_modifier/(correct_answer/user_answer);
            }

        }
    }

}
