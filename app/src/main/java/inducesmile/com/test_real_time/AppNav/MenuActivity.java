package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import inducesmile.com.test_real_time.Helper.BackgroundSoundService;
import inducesmile.com.test_real_time.Helper.MultiplayerLogin;
import inducesmile.com.test_real_time.Multiplayer.AchivementsActivity;
import inducesmile.com.test_real_time.Multiplayer.RandomPlayActivity;
import inducesmile.com.test_real_time.R;

public class MenuActivity extends AppCompatActivity {


    public Button multi_btn;
    public Button solo_btn;
    public Button random_btn;
    public Button help_btn;
    public ToggleButton mute_btn;
    GoogleSignInAccount signedInAccount;
    public Intent svc;
    private boolean shouldPlay = false;


    MultiplayerLogin login = MultiplayerLogin.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        signedInAccount = login.getSignedInAccount();
        //Log.d("Teste","testesaaf");
        PlayersClient playersClient = Games.getPlayersClient(this,signedInAccount);

        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        Log.d("playerid",player.getDisplayName());

                    }
                });

        this.overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);

        multi_btn =  findViewById(R.id.btn_Multi);
        solo_btn =  findViewById(R.id.btn_solo);
        random_btn =  findViewById(R.id.btn_random);

        //MUSICA!
        //shouldPlay = true;

        mute_btn=findViewById(R.id.mute_btn);
        svc=new Intent(this, BackgroundSoundService.class);
        startService(svc);

        solo_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                shouldPlay = true;
                startActivity(new Intent(MenuActivity.this, ChooseCatg.class));
                //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        multi_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AchivementsActivity.class);
                i.putExtra("Mode",1);
                startActivity(i);
                finish();
            }
        });

        random_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                shouldPlay = true;
                Intent intent = new Intent(MenuActivity.this, RandomPlayActivity.class);
                startActivity(intent);
            }
        });

        mute_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //shouldPlay = true;
                toggleMute( svc, mute_btn);

            }
        });



    }

    private void toggleMute(Intent svc, ToggleButton button) {

        if(shouldPlay==true){
            mute_btn.setBackgroundResource(R.drawable.sound_off_icon);
            stopService(svc);
            shouldPlay=false;
            return;
        }if(shouldPlay==false){
            mute_btn.setBackgroundResource(R.drawable.sound_on_icon);
            startService(svc);
            shouldPlay=true;
            return;
        }

    }


    @Override
    public void onPause(){
        super.onPause();
        //Intent svc=new Intent(this, BackgroundSoundService.class);
        if(!shouldPlay)
            if(svc!=null){
            stopService(svc);}

    }

    @Override
    public void onBackPressed ()
    {

        super.onBackPressed();
    }

    private void signInSilently() {
        GoogleSignInClient signInClient = GoogleSignIn.getClient(this,
                GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN);
        signInClient.silentSignIn().addOnCompleteListener(this,
                new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        if (task.isSuccessful()) {
                            // The signed in account is stored in the task's result.
                            signedInAccount = task.getResult();
                        } else {

                        }
                    }
                });
    }


    @Override
    protected void onResume() {
        super.onResume();
        //signInSilently();
    }



}
