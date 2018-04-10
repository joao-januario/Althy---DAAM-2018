package inducesmile.com.test_real_time;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class Question_Activity extends AppCompatActivity {

    private List<String> questions;

    private TextView textV_question;
    private TextView textV_answer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_);
        textV_question=findViewById(R.id.question_textView);
        textV_answer=findViewById(R.id.answer_tv);

        questions=new LinkedList<>();
        questions.addAll(Arrays.asList("Distância daqui até ali?", "Quantos queres?", "Vai ou não vai?",
                "1, 2 ou 3", "achas que sim?", "responde acertadamente!"));

        Collections.shuffle(questions);


        textV_question.setText(questions.get(1));

        textV_answer.setFocusable(true);
        textV_answer.setEnabled(true);
        textV_answer.setClickable(true);
        textV_answer.setFocusableInTouchMode(true);
        textV_answer.setRawInputType(Configuration.KEYBOARD_12KEY);
        //textV_answer.setCursorVisible(true);
        //textV_answer.requestFocus();
        textV_answer.setInputType(InputType.TYPE_CLASS_NUMBER);

    }



}
