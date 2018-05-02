package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import android.widget.TextView;

import inducesmile.com.test_real_time.Helper.LoadingScreenSingleplayerActivity;
import inducesmile.com.test_real_time.R;

public class ChooseCatg extends AppCompatActivity {

    public Button inc_Rounds;
    public Button dec_Rounds;
    public TextView rounds_tv;
    public int rounds_count =0;
    public ToggleButton closer_wins_btn;
    public ToggleButton quizz_btn;
    public  final int ROUNDS_MAX = 10;
    public final int ROUNDS_MIN = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_catg);

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
