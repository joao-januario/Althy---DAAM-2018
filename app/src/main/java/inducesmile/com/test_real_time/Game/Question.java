package inducesmile.com.test_real_time.Game;

import com.google.android.gms.games.quest.Quest;

/**
 * Created by joao on 24/04/2018.
 */

public class Question {
    private int questionID;
    private int correctAnswer;
    private String questionText;

    public Question(int questionID, int correctAnswer,String questionText){
        this.questionID = questionID;
        this.correctAnswer=correctAnswer;
        this.questionText = questionText;
    }


    public String getQuestionText() {
        return questionText;
    }

    public int getCorrectAnswer(){
        return correctAnswer;
    }
}
