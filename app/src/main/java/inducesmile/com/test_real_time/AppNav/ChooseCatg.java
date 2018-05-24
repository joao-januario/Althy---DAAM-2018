package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;
import android.widget.TextView;

import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.LoadingScreenSingleplayerActivity;
import inducesmile.com.test_real_time.R;

public class ChooseCatg extends AppCompatActivity {

    public Button inc_Rounds;
    public Button dec_Rounds;
    public TextView rounds_tv;
    public ToggleButton closer_wins_btn;
    public ToggleButton quizz_btn;
    public int rounds_count =1;
    public  final int ROUNDS_MAX = 9;
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

        quizz_btn = (ToggleButton) findViewById(R.id.btn_classicQuizz);
        quizz_btn.setChecked(true);
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
                closerClick();
            }
        });


        //MUSICA!
        svc=new Intent(this, BackgroundSoundService.class);
        startService(svc);

    }

    @Override
    public void onPause(){
        super.onPause();
        //Intent svc=new Intent(this, BackgroundSoundService.class);
        if(!shouldPlay)
            stopService(svc);

    }

    private void increaseRounds() {
        rounds_count++;
        rounds_tv.setText(""+rounds_count);
    }
    private void decreaseRounds() {
        rounds_count--;
        rounds_tv.setText(""+rounds_count+"");
    }

    public void closerClick(){
        if(closer_check.getVisibility()==View.VISIBLE){
            closer_check.setVisibility(View.INVISIBLE);
            return;
        }else
            closer_check.setVisibility(View.VISIBLE);
        return;


    }

    public void startPlaying(View v){
        Intent intent = new Intent(this, LoadingScreenSingleplayerActivity.class);
        intent.putExtra("numberOfQuestions",rounds_count);
        shouldPlay = true;
        startActivity(intent);

        finish();
    }

}
