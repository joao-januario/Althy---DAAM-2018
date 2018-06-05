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

import java.lang.reflect.Array;
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
    private boolean canStartQuiz=false;
    private boolean canStartClassic=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen_singleplayer);
        numberOfQuestions = getIntent().getIntExtra("numberOfQuestions",0);
        //se for 0, é misturado, se for 1 é closer winds, se for 2 é quiz, se for 3 ou 4 é shieaaat;
        categories=getIntent().getIntExtra("categories", 4);
        generateQuestions(categories);

        new Thread(new Runnable() {
            @Override
            public void run() {
                checkQuestionsReady();
                if(categories==2){
                    startQuizz();
                }else{
                    if(categories==1){
                        startClassic();
                    }else{
                        if(categories==0){
                            Random r = new Random();
                            if(r.nextDouble()>0.5){
                                if(classic_handler.moreQuestions()){
                                    startClassic();
                                }else{
                                    startQuizz();
                                }
                            }else{
                                if(quizz_handler.moreQuestions()){
                                    startQuizz();
                                }else{
                                    startClassic();
                                }
                            }
                        }
                    }
                }
            }
        }).start();
    }


    //Este add Questions é so para testarmos as perguntas enquanto nao temos o firebase a funcionar
    /*private void addQuestions(){
        classic_handler.newGame();
        //Question q = new Question(1,5000,"Qual é o numero mais proximo de 5000");
        //Question q2 = new Question(2,3000,"Qual é o numero mais proximo de 3000");
       // classic_handler.addQuestion(q);
        //classic_handler.addQuestion(q2);
    }*/

    private void generateQuestions(final int categories) {
        classic_handler.newGame();
        quizz_handler.newGame();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        final ArrayList<Integer> question_ids = new ArrayList<>();



        //se for 0, é misturado, se for 1 é closer winds, se for 2 é quiz, se for 3 ou 4 é shieaaat;
        if (categories==2){
            generateQuizQuestions(myRef, numberOfQuestions);
        }if (categories==1){
            notifythreads();
            generateDistanceQuestions(myRef,  numberOfQuestions);
        }if (categories==0){
            notifythreads();
            Random ran = new Random();
            int r = ran.nextInt(numberOfQuestions+1);
            Log.d("Random",Integer.toString(r));
            generateDistanceQuestions(myRef,  r);
                generateQuizQuestions(myRef,  numberOfQuestions-r);
        }







    }

    private void generateQuizQuestions(DatabaseReference myRef, final int numbOfQuest  ) {
        myRef.child("classico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Integer> question_ids = new ArrayList<>();
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
                canStartQuiz=true;
                notifythreads();
                //startQuizz();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    public void generateDistanceQuestions(DatabaseReference myRef, final int numbOfQuest  ) {
        myRef.child("perto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                int total_questions = (int) dataSnapshot.getChildrenCount();
                ArrayList<Integer> question_ids = new ArrayList<>();
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
                canStartClassic=true;
                notifythreads();
                Log.d("notige no distance", "print");
                //startClassic();
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    private void startClassic(){
        Intent intent = new Intent(LoadingScreenSingleplayerActivity.this,Question_Activity.class);
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

    public synchronized void notifythreads(){
        if (canStartClassic){
            Log.d("TAG3","vcvcvvcvc0");
        }
        notifyAll();
    }

    public synchronized void checkQuestionsReady(){
        Log.d("TAG2","asfafsa");


        if (categories==1){
            while(canStartClassic==false){
                try {
                    Log.d("TAG", "print ");
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
    }else{
            if(categories==2){
                while (canStartQuiz==false){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                if (categories==0){
                    while(!canStartQuiz && !canStartClassic){
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


    }
}
