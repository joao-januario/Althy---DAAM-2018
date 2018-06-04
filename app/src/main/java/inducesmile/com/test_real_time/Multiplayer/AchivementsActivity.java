package inducesmile.com.test_real_time.Multiplayer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import inducesmile.com.test_real_time.AppNav.MenuActivity;
import inducesmile.com.test_real_time.R;

public class AchivementsActivity extends AppCompatActivity {
int open_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_with_friends);
        open_intent=getIntent().getIntExtra("Mode",0);
        if (open_intent==1){
        showAchievements();}
        else{
            backToMenu();
        }
    }

    private static final int RC_ACHIEVEMENT_UI = 9003;

    private void showAchievements() {
        Games.getAchievementsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .getAchievementsIntent()
                .addOnSuccessListener(new OnSuccessListener<Intent>() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, RC_ACHIEVEMENT_UI);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent i = new Intent(AchivementsActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void backToMenu(){
        Intent i = new Intent(AchivementsActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    public void onPause() {

        super.onPause();
        open_intent=0;
    }

    public void onResume() {

        super.onResume();
        if (open_intent==1){
            showAchievements();}
        else{
            backToMenu();
        }
    }
}
