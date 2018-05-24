package inducesmile.com.test_real_time.Helper;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BacgroundMusic extends AppCompatActivity {

    public  Intent svc;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        svc=new Intent(this, BackgroundSoundService.class);
        startService(svc);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(svc);
    }
}
