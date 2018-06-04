package inducesmile.com.test_real_time.Helper;

import android.util.Log;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Game.QuizzQuestion;

/**
 * Created by joao on 24/04/2018.
 */

public class QuizQuestionsHandler {
    private int user_score=0;
    private static final QuizQuestionsHandler INSTANCE = new QuizQuestionsHandler();
    private int totalNumberOfQuestions =0;
    ArrayList<QuizzQuestion> questions = new ArrayList<>();
    int currentQuestion =0;
    private QuizQuestionsHandler(){

    }
    public static QuizQuestionsHandler getInstance(){
        return INSTANCE;
    }

    public void newGame(){
        user_score=0;
        currentQuestion=0;
        totalNumberOfQuestions=0;
        questions.clear();
    }

    public void addQuestion(QuizzQuestion q){
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

    public String getCurrentQuestionAnswer(){
        return questions.get(currentQuestion).getAnswer();
    }

    public QuizzQuestion getCurrentQuestion(){
        return questions.get(currentQuestion);
    }

    public ArrayList<String> getAllAnswers(){
        return (questions.get(currentQuestion).getAnswers());
    }


    public String getWrongAnswers(int i){
        return (questions.get(currentQuestion).getWrong1(i));
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
