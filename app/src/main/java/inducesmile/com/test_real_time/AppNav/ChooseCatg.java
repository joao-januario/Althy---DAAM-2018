package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;

import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.LoadingScreenSingleplayerActivity;
import inducesmile.com.test_real_time.R;

public class ChooseCatg extends AppCompatActivity {

    public Button inc_Rounds;
    public Button dec_Rounds;
    public Button playButton;
    public TextView rounds_tv;
    public ToggleButton closer_wins_btn;
    public ToggleButton quizz_btn;
    public int rounds_count =1;
    public  final int ROUNDS_MAX = 15;
    public final int ROUNDS_MIN = 1;
    public ImageView closer_check;
    public ImageView quizz_check;
    public BackgroundSoundService music;
    public Intent svc;
    private boolean shouldPlay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_catg);


        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);


        quizz_btn = (ToggleButton) findViewById(R.id.btn_classicQuizz);
        quizz_btn.setChecked(true);
        quizz_check = findViewById(R.id.check_quizz);
        quizz_check.setVisibility(View.VISIBLE);

        playButton = findViewById(R.id.playButton);


        closer_wins_btn=(ToggleButton) findViewById(R.id.btn_closerWins);
        closer_wins_btn.setChecked(true);
        closer_check = findViewById(R.id.check_Closer);
        closer_check.setVisibility(View.VISIBLE);

        inc_Rounds = (Button) findViewById(R.id.btn_increaseRound);
        dec_Rounds = (Button)findViewById(R.id.btn_decreaseRound);
        rounds_tv = (TextView) findViewById(R.id.rounds_count_tv);

        inc_Rounds.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(rounds_count<ROUNDS_MAX)
                    increaseRounds();
            }
        });
        dec_Rounds.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(rounds_count>ROUNDS_MIN)
                    decreaseRounds();
            }
        });
        closer_wins_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                check_click(closer_check);
                activateButton();
            }
        });
        quizz_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                check_click(quizz_check);
                activateButton();
            }
        });

        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
        startService(svc);

    }

    public void activateButton(){
        if(closer_wins_btn.isChecked() || quizz_btn.isChecked()){
            playButton.setEnabled(true);
        }
        if(!closer_wins_btn.isChecked() && !quizz_btn.isChecked()){
            playButton.setEnabled(false);

        }
    }

    @Override
    public void onPause(){
        super.onPause();
        //Intent svc=new Intent(this, BackgroundSoundService.class);
        if(!shouldPlay)
            stopService(svc);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private void increaseRounds() {
        rounds_count++;
        rounds_tv.setText(""+rounds_count);
    }
    private void decreaseRounds() {
        rounds_count--;
        rounds_tv.setText(""+rounds_count+"");
    }

    public void check_click(ImageView check){
        if(check.getVisibility()==View.VISIBLE){
            check.setVisibility(View.INVISIBLE);
            return;
        }else
            check.setVisibility(View.VISIBLE);
        return;


    }

    public void startPlaying(View v){
        Intent intent = new Intent(this, LoadingScreenSingleplayerActivity.class);
        intent.putExtra("numberOfQuestions",rounds_count);
        int categories = checkCategories();
        intent.putExtra("categories",categories);
        shouldPlay = true;
        startActivity(intent);

        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .unlock(getString(R.string.achievement_noob));

        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .increment(getString(R.string.achievement_principiante), 1);
        finish();
    }

    private int checkCategories() {
        if(closer_wins_btn.isChecked() && quizz_btn.isChecked()){
            return 0;
        }if(closer_wins_btn.isChecked() && !quizz_btn.isChecked()){
            return 1;
        }if(!closer_wins_btn.isChecked() && quizz_btn.isChecked()){
            return 2;
        }
        return 3;
    }

}
