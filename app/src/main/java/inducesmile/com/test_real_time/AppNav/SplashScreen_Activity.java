package inducesmile.com.test_real_time.AppNav;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import inducesmile.com.test_real_time.AppNav.ChooseCatg;
import inducesmile.com.test_real_time.R;

public class SplashScreen_Activity extends AppCompatActivity {
    VideoView videoHolder;
    ConstraintLayout layout;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_);

        try {
          // VideoView videoHolder = new VideoView(this);
            videoHolder=findViewById(R.id.video);
            //setContentView(videoHolder);
            Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.splash);
            videoHolder.setVideoURI(video);

            videoHolder.setBackgroundColor(Color.TRANSPARENT);
            videoHolder.setDrawingCacheBackgroundColor(Color.TRANSPARENT);

            videoHolder.requestFocus();
            videoHolder.start();


            videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                   jump();
                }
            });
            videoHolder.start();

        } catch (Exception ex) {
            jump();
        }

    }

    private void jump()
    {

        if(isFinishing()){
            return;}
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        startActivity(new Intent(this, ChooseCatg.class));
        finish();
    }
}
