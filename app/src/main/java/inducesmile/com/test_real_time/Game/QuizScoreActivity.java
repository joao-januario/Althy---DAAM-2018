package inducesmile.com.test_real_time.Game;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;

import java.util.Random;

import inducesmile.com.test_real_time.AppNav.MenuActivity;
import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.QuestionsHandler;
import inducesmile.com.test_real_time.Helper.QuizQuestionsHandler;
import inducesmile.com.test_real_time.Helper.UserScore;
import inducesmile.com.test_real_time.R;

public class QuizScoreActivity extends AppCompatActivity {

    private int user_score;
    private int user_time;
    private boolean shouldPlay=false;
    private Intent svc;
    private UserScore total_score = UserScore.getInstance();

    QuestionsHandler handler = QuestionsHandler.getInstance();
    QuizQuestionsHandler quiz_handler = QuizQuestionsHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_score);
        //user_answer=getIntent().getIntExtra("user_answer",0);
        //correct_answer=getIntent().getIntExtra("correct_answer",0);
        user_score=getIntent().getIntExtra("question_score",0);
        user_time=getIntent().getIntExtra("question_time",0);
        TextView userScoreTv = findViewById(R.id.userScore_tv);
        userScoreTv.setText(Integer.toString(user_score));

        TextView question_time = findViewById(R.id.question_time);
        question_time.setText(Integer.toString(user_time));


        handler.updateUserScore(user_score);

        TextView userTotalScoreTv = findViewById(R.id.totalScore_tv);
        userTotalScoreTv.setText(Integer.toString(total_score.getTotal_score()));

        //Transition
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    //O more questions verifica se o utilizador está na ultima pergunta ou não, se estiver volta ao menu principal, se n carrega a proxima pergunta
    public void nextQuestion(View v){
        Intent intent;
        Random r = new Random();
        double random = r.nextDouble();

        if(random>0.5){
            if ( handler.moreQuestions() ) {
                intent = new Intent(this, Question_Activity.class);
            }else{
                if ( quiz_handler.moreQuestions() ) {
                    intent = new Intent(this, Quizz_Activity.class);
                }else{
                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .unlock(getString(R.string.achievement_noob));

                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .increment(getString(R.string.achievement_principiante), 1);


                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .increment(getString(R.string.achievement_mestre), 1);
                    //total_score.clear_score();
                    intent = new Intent(this, MenuActivity.class);

                }
            }
        }else{
            if ( quiz_handler.moreQuestions() ) {
                intent = new Intent(this, Quizz_Activity.class);
            }else{
                if ( handler.moreQuestions() ) {
                    intent = new Intent(this, Question_Activity.class);
                }else{
                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .unlock(getString(R.string.achievement_noob));

                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .increment(getString(R.string.achievement_principiante), 1);


                    Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                            .increment(getString(R.string.achievement_mestre), 1);
                    //total_score.clear_score();
                    intent = new Intent(this, MenuActivity.class);

                }
            }
        }
        shouldPlay=true;
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!shouldPlay)
            stopService(svc);
    }





}
