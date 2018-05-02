package inducesmile.com.test_real_time.Helper;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;

/**
 * Created by joao on 24/04/2018.
 */

public class QuestionsHandler {

    private static final QuestionsHandler INSTANCE = new QuestionsHandler();

    ArrayList<Question> questions = new ArrayList<>();
    int current_question=0;
    private QuestionsHandler(){

    }
    public static QuestionsHandler getInstance(){
        return INSTANCE;
    }

    public void clearQuestions(){
        questions.clear();
    }

    public void addQuestion(Question q){
        questions.add(q);
    }

    public void nextQuestion(){
        current_question++;
    }


    public String getCurrentQuestionText(){
        return questions.get(current_question).getQuestionText();
    }

    public int getCurrentQuestionAnswer(){
        return questions.get(current_question).getCorrectAnswer();
    }

}
