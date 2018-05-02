package inducesmile.com.test_real_time.Helper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.R;

public class LoadingScreenSingleplayerActivity extends AppCompatActivity {

    int numberOfQuestions;
    QuestionsHandler handler = QuestionsHandler.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen_singleplayer);
        numberOfQuestions =Integer.parseInt(getIntent().getStringExtra("numberOfQuestions"));
        addQuestions();
    }

    private void addQuestions(){
        handler.clearQuestions();
        Question q = new Question(1,5000,"Qual é o numero mais proximo de 5000");
        Question q2 = new Question(2,3000,"Qual é o numero mais proximo de 3000");
        handler.addQuestion(q);
        handler.addQuestion(q2);
    }

    private void generateQuestions(){
        for(int i=0;i<numberOfQuestions;i++){
            //Ir ao Firebase buscar perguntas e por no question Handler
        }


    }

}
