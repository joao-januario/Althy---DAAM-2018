package inducesmile.com.test_real_time.Multiplayer;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import inducesmile.com.test_real_time.AppNav.MenuActivity;
import inducesmile.com.test_real_time.R;

public class MultiplayerFinalScoreActivity extends AppCompatActivity {


    RoomConfigLocal roomConfigLocal = RoomConfigLocal.getInstance();
    HashMap<String,Integer> finalScores = roomConfigLocal.getPlayerScore();
    HashMap<String,String> usernames = roomConfigLocal.getAllPlayersIds();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_final_score);
        organizeFinalScores();


    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    private void organizeFinalScores(){
        Object[] a = finalScores.entrySet().toArray();
        Typeface font2 = Typeface.createFromAsset( MultiplayerFinalScoreActivity.this.getAssets(), "fonts/annieuseyourtelescope_file.ttf");

        //transition
        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        Arrays.sort(a, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Map.Entry<String, Integer>) o2).getValue()
                        .compareTo(((Map.Entry<String, Integer>) o1).getValue());
            }
        });
        int i=1;
        for (Object e: a) {

            String playerID = ((Map.Entry<String, Integer>) e).getKey();
            int score = ((Map.Entry<String, Integer>) e).getValue();
            String username = usernames.get(playerID);


            if (i==1){
                TextView tv = findViewById(R.id.winnerName_tv);
                tv.setText(username + "  " + score + "pts");
            }
            else{
                TableLayout table = findViewById(R.id.losers_table);
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));


                TextView posicao = new TextView(this);
                posicao.setTypeface(font2);
                posicao.setText(Integer.toString(i));
                posicao.setTextColor(getResources().getColor(R.color.colorPrimary));
                posicao.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);
                posicao.setGravity(Gravity.CENTER);


                TextView nomeJogador = new TextView(this);
                nomeJogador.setTypeface(font2);
                nomeJogador.setText(username);
                nomeJogador.setTextColor(getResources().getColor(R.color.colorPrimary));
                nomeJogador.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);
                nomeJogador.setGravity(Gravity.LEFT);

                TextView scoreFinal = new TextView(this);
                scoreFinal.setTypeface(font2);
                scoreFinal.setText(Integer.toString(score));
                scoreFinal.setTextColor(getResources().getColor(R.color.colorPrimary));
                scoreFinal.setPadding(dpAsPixels(16),dpAsPixels(5),0,0);
                scoreFinal.setGravity(Gravity.CENTER);

                row.addView(posicao);
                row.addView(nomeJogador);
                row.addView(scoreFinal);

                table.addView(row, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }
            i++;
        }
    }

    private int dpAsPixels(int dp){
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dp*scale + 0.5f);

    }

    public void backToMenu(View v){
        roomConfigLocal.resetRoom();
        Intent i = new Intent(this, MenuActivity.class);
        startActivity(i);
        finish();
    }




}
