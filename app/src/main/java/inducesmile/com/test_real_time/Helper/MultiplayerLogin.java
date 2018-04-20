package inducesmile.com.test_real_time.Helper;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by joao on 19/04/2018.
 */

public class MultiplayerLogin {
    private static final MultiplayerLogin INSTANCE = new MultiplayerLogin();
GoogleSignInAccount signInClient;

private MultiplayerLogin(){

}
public static MultiplayerLogin getInstance(){
    return INSTANCE;
}

public void setSignInAccount(GoogleSignInAccount signInClient){
    Log.d("setting","Saving sign in account");
    this.signInClient=signInClient;
    }

    public GoogleSignInAccount getSignedInAccount(){
        return signInClient;
    }

}
