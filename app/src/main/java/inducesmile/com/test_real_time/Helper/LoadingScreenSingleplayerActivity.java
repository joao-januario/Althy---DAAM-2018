package inducesmile.com.test_real_time.Helper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Game.Question_Activity;
import inducesmile.com.test_real_time.Game.QuizzQuestion;
import inducesmile.com.test_real_time.Game.Quizz_Activity;
import inducesmile.com.test_real_time.R;

public class LoadingScreenSingleplayerActivity extends AppCompatActivity {

    int numberOfQuestions;
    int categories;
    QuestionsHandler classic_handler = QuestionsHandler.getInstance();
    QuizQuestionsHandler quizz_handler = QuizQuestionsHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen_singleplayer);
        numberOfQuestions = getIntent().getIntExtra("numberOfQuestions",0);
        //se for 0, é misturado, se for 1 é closer winds, se for 2 é quiz, se for 3 ou 4 é shieaaat;
        categories=getIntent().getIntExtra("categories", 4);
        generateQuestions(categories);
    }


    //Este add Questions é so para testarmos as perguntas enquanto nao temos o firebase a funcionar
    /*private void addQuestions(){
        classic_handler.newGame();
        //Question q = new Question(1,5000,"Qual é o numero mais proximo de 5000");
        //Question q2 = new Question(2,3000,"Qual é o numero mais proximo de 3000");
       // classic_handler.addQuestion(q);
        //classic_handler.addQuestion(q2);
    }*/

    private void generateQuestions(int categories) {
        classic_handler.newGame();
        quizz_handler.newGame();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final ArrayList<Integer> question_ids = new ArrayList<>();

        //se for 0, é misturado, se for 1 é closer winds, se for 2 é quiz, se for 3 ou 4 é shieaaat;
        if (categories==2){
            generateQuizQuestions(myRef, question_ids, numberOfQuestions);
        }if (categories==1){
           // generateDistanceQuestions(myRef, question_ids, numberOfQuestions);
        }




    }

    private void generateQuizQuestions(DatabaseReference myRef, final ArrayList<Integer> question_ids, final int numbOfQuest  ) {
        myRef.child("classico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int total_questions = (int) dataSnapshot.getChildrenCount();

                for (int i = 0; i < numbOfQuest; i++) {
                    Random r = new Random();
                    int question_id = r.nextInt(total_questions);
                    while (question_ids.contains(question_id)) {
                        question_id = r.nextInt(total_questions);
                    }
                    question_ids.add(question_id);
                    DataSnapshot question_data = dataSnapshot.child(Integer.toString(question_id));
                    QuizzQuestion qz = question_data.getValue(QuizzQuestion.class);
                    //Log.d("print dos dados : ", qz.getWrong1(1));

                    quizz_handler.addQuestion(qz);
                }
                startQuizz();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void generateDistanceQuestions(DatabaseReference myRef, final ArrayList<Integer> question_ids, final int numbOfQuest  ) {
        myRef.child("perto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int total_questions = (int) dataSnapshot.getChildrenCount();

                for (int i = 0; i < numbOfQuest; i++) {
                    Random r = new Random();
                    int question_id = r.nextInt(total_questions);
                    while (question_ids.contains(question_id)) {
                        question_id = r.nextInt(total_questions);
                    }
                    question_ids.add(question_id);
                    DataSnapshot question_data = dataSnapshot.child(Integer.toString(question_id));
                    Question q = question_data.getValue(Question.class);
                    classic_handler.addQuestion(q);
                }
                startClassic();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void startClassic(){
        Intent intent = new Intent(this,Question_Activity.class);
        intent.putExtra("Mode",0);
        startActivity(intent);
        finish();
    }

    private void startQuizz(){
        Intent intent = new Intent(this,Quizz_Activity.class);
        intent.putExtra("Mode",0);
        startActivity(intent);
        finish();
    }

}
