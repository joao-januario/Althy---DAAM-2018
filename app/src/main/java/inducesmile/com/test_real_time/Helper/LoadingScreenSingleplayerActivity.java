package inducesmile.com.test_real_time.Helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Game.Question_Activity;
import inducesmile.com.test_real_time.R;

public class LoadingScreenSingleplayerActivity extends AppCompatActivity {

    int numberOfQuestions;
    QuestionsHandler handler = QuestionsHandler.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen_singleplayer);
        numberOfQuestions = getIntent().getIntExtra("numberOfQuestions",0);
        addQuestions();
        generateQuestions();
    }


    //Este add Questions é so para testarmos as perguntas enquanto nao temos o firebase a funcionar
    private void addQuestions(){
        handler.newGame();
        Question q = new Question(1,5000,"Qual é o numero mais proximo de 5000");
        Question q2 = new Question(2,3000,"Qual é o numero mais proximo de 3000");
        handler.addQuestion(q);
        handler.addQuestion(q2);
    }

    private void generateQuestions(){
        for(int i=0;i<numberOfQuestions;i++){
            //Ir ao Firebase buscar perguntas e por no question Handler
        }
        Intent intent = new Intent(this,Question_Activity.class);
        startActivity(intent);
        finish();


    }

}
