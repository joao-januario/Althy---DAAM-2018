package inducesmile.com.test_real_time.AppNav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.TextView;

import inducesmile.com.test_real_time.R;

public class ChooseCatg extends AppCompatActivity {

    public Button inc_Rounds;
    public Button dec_Rounds;
    public TextView rounds_tv;
    public ToggleButton closer_wins_btn;
    public ToggleButton quizz_btn;
    public int rounds_count =1;
    public  final int ROUNDS_MAX = 10;
    public final int ROUNDS_MIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_catg);

        quizz_btn = (ToggleButton) findViewById(R.id.btn_classicQuizz);
        quizz_btn.setChecked(true);
        closer_wins_btn=(ToggleButton) findViewById(R.id.btn_closerWins);
        closer_wins_btn.setChecked(true);

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

    }

    private void increaseRounds() {
        rounds_count++;
        rounds_tv.setText(""+rounds_count);
    }
    private void decreaseRounds() {
        rounds_count--;
        rounds_tv.setText(""+rounds_count+"");
    }


}
