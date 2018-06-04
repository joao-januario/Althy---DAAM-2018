package inducesmile.com.test_real_time.Multiplayer;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import inducesmile.com.test_real_time.Game.Question;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;
import inducesmile.com.test_real_time.R;

public class MultiplayerQuestionHandler {

    private static final MultiplayerQuestionHandler INSTANCE = new MultiplayerQuestionHandler();
    private final int  totalNumberOfQuestions =5;
    ArrayList<Question> questions = new ArrayList<>();
    int currentQuestion =1;
    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    private MultiplayerQuestionHandler(){

    }

    public static MultiplayerQuestionHandler getInstance(){
        return INSTANCE;
    }



    public void newGame(){
        currentQuestion=1;
        questions.clear();
    }

    public void addQuestion(Question q){
        questions.add(q);
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


    public boolean moreQuestions(){
        Log.i("More Questions",Integer.toString(currentQuestion));
        if (currentQuestion>=totalNumberOfQuestions-1){
            return false;
        }
        else {
            return true;
        }
    }

}



