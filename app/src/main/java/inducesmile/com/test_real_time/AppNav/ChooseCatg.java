package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import inducesmile.com.test_real_time.Helper.LoadingScreenSingleplayerActivity;
import inducesmile.com.test_real_time.R;

public class ChooseCatg extends AppCompatActivity {

    public Button inc_Rounds;
    public Button dec_Rounds;
    public TextView rounds_tv;
    public int rounds_count =0;
    public  final int ROUNDS_MAX = 10;
    public final int ROUNDS_MIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_catg);

        //inc_Rounds = findViewById(R.id.btn_increaseRound);
        //inc_Rounds = findViewById(R.id.btn_decreaseRound);
        rounds_tv = findViewById(R.id.rounds_count_tv);
/*
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
*/
    }

    private void increaseRounds() {
        rounds_count++;
        rounds_tv.setText(rounds_count);
    }
    private void decreaseRounds() {
        rounds_count--;
        rounds_tv.setText(rounds_count);
    }

    private void startGame(View v){
        Intent intent = new Intent(this,LoadingScreenSingleplayerActivity.class);
        intent.putExtra("numberOfQuestions",rounds_count);
        startActivity(intent);
        finish();


    }


}
