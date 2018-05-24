package inducesmile.com.test_real_time.Game;

/**
 * Created by joao on 24/04/2018.
 */

public class Question {
    private String answer;
    private String question;
    private String question_id;
    

    public Question(){

    }

    public Question(String answer,String question,String questionID){
        this.answer =answer;
        this.question = question;
        this.question_id = questionID;

    }


    public String getQuestion() {
        return question;
    }

    public int getAnswer(){
        return Integer.parseInt(answer);
    }
}
