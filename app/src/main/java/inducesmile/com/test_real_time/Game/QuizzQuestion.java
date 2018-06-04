package inducesmile.com.test_real_time.Game;

import java.util.ArrayList;

/**
 * Created by claudio on 1/06/2018.
 */

public class QuizzQuestion {
    private String answer;
    private String question;
    private String question_id;
    public String w1;
    public String w2;
    public String w3;
    private  ArrayList<String> answers;

    public QuizzQuestion(){

    }
    //{answer=, question=, w1=, w2=, w3=, question_id=}
    public QuizzQuestion(String answer,String question, String w1, String w2, String w3, String questionID ){

        this.answer =answer;
        this.question = question;
        this.question_id = questionID;
        this.w1 =w1;
        this.w2 =w2;
        this.w3 =w3;
        answers.add(answer);
        answers.add(w1);
        answers.add(w2);
        answers.add(w3);



    }
    public String getWrong1(int i){

            return w1;

    }



    public String getQuestion() {
        return question;
    }

    public String getAnswer(){
        return answer;
    }

    public ArrayList<String> getAnswers() {

        return answers;
    }



}