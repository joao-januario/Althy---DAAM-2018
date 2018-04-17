package inducesmile.com.test_real_time;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MenuActivity extends AppCompatActivity {


    public Button multi_btn;
    public Button solo_btn;
    public Button random_btn;
    public Button help_btn;
    GoogleSignInAccount signedInAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        signedInAccount =(GoogleSignInAccount) getIntent().getSerializableExtra("GoogleSignInAccount");

        multi_btn =  findViewById(R.id.btn_Multi);
        solo_btn =  findViewById(R.id.btn_solo);
        random_btn =  findViewById(R.id.btn_random);


        solo_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, ChooseCatg.class));

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
                            Intent menu = new Intent(MenuActivity.this,MultiplayerGoogleLoginActivity.class);
                            menu.putExtra("GoogleSignInAccount",signedInAccount);
                            startActivity(menu);
                            finish();

                        }
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();

        //if(signedInAccount==null)
           // signInSilently();
    }



}
