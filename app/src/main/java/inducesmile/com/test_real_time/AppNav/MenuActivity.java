package inducesmile.com.test_real_time.AppNav;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

import inducesmile.com.test_real_time.Helper.MultiplayerLogin;
import inducesmile.com.test_real_time.Multiplayer.RandomPlayActivity;
import inducesmile.com.test_real_time.R;

public class MenuActivity extends AppCompatActivity {


    public Button multi_btn;
    public Button solo_btn;
    public Button random_btn;
    public Button help_btn;
    GoogleSignInAccount signedInAccount;
    MultiplayerLogin login = MultiplayerLogin.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        signedInAccount = login.getSignedInAccount();
        PlayersClient playersClient = Games.getPlayersClient(this,signedInAccount);
        playersClient.getCurrentPlayer()
                .addOnSuccessListener(new OnSuccessListener<Player>() {
                    @Override
                    public void onSuccess(Player player) {
                        Log.d("playerid",player.getDisplayName());

                    }
                });



        multi_btn =  findViewById(R.id.btn_Multi);
        solo_btn =  findViewById(R.id.btn_solo);
        random_btn =  findViewById(R.id.btn_random);
        solo_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ChooseCatg.class));

            }
        });
        random_btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                 Intent intent = new Intent(MenuActivity.this, RandomPlayActivity.class);
                startActivity(intent);
            }
        });
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
