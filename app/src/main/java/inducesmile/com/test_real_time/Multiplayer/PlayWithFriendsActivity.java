package inducesmile.com.test_real_time.Multiplayer;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.WindowManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.GamesCallbackStatusCodes;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.games.RealTimeMultiplayerClient;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.Multiplayer;
import com.google.android.gms.games.multiplayer.Participant;
import com.google.android.gms.games.multiplayer.realtime.OnRealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateCallback;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import inducesmile.com.test_real_time.AppNav.MenuActivity;
import inducesmile.com.test_real_time.Helper.MultiplayerLogin;
import inducesmile.com.test_real_time.R;

public class PlayWithFriendsActivity extends AppCompatActivity {
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
                Intent i = new Intent(PlayWithFriendsActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void backToMenu(){
        Intent i = new Intent(PlayWithFriendsActivity.this, MenuActivity.class);
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
