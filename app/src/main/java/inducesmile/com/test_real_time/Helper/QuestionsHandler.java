package inducesmile.com.test_real_time.Helper;

import android.util.Log;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;

/**
 * Created by joao on 24/04/2018.
 */

public class QuestionsHandler {
    private int user_score=0;
    private static final QuestionsHandler INSTANCE = new QuestionsHandler();
    private int totalNumberOfQuestions =0;
    ArrayList<Question> questions = new ArrayList<>();
    int currentQuestion =0;
    private QuestionsHandler(){

    }
    public static QuestionsHandler getInstance(){
        return INSTANCE;
    }

    public void newGame(){
        user_score=0;
        currentQuestion=0;
        totalNumberOfQuestions=0;
        questions.clear();
    }

    public void addQuestion(Question q){
        questions.add(q);
        totalNumberOfQuestions++;
        Log.d("TotalQuestions",Integer.toString(totalNumberOfQuestions));
    }

    public void nextQuestion(){
        currentQuestion++;
        Log.d("Next Question",Integer.toString(currentQuestion));
    }


    public String getCurrentQuestionText(){
        return questions.get(currentQuestion).getQuestionText();
    }

    public int getCurrentQuestionAnswer(){
        return questions.get(currentQuestion).getCorrectAnswer();
    }

    public void updateUserScore(int questionScore){
        user_score+=questionScore;
    }

    public int getUserScore(){
        return user_score;
    }

    public boolean moreQuestions(){
        if (currentQuestion>=totalNumberOfQuestions){
            return false;
        }
        else {
            return true;
        }
    }



}
