package inducesmile.com.test_real_time.Multiplayer;

import android.util.Log;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;

public class MultiplayerQuestionHandler {

    private int user_score=0;
    private static final MultiplayerQuestionHandler INSTANCE = new MultiplayerQuestionHandler();
    private int totalNumberOfQuestions =0;
    ArrayList<Question> questions = new ArrayList<>();
    int currentQuestion =0;

    private MultiplayerQuestionHandler(){

    }

    public static MultiplayerQuestionHandler getInstance(){
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
        return questions.get(currentQuestion).getQuestion();
    }

    public int getCurrentQuestionAnswer(){
        return questions.get(currentQuestion).getAnswer();
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



