package inducesmile.com.test_real_time;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class JoinSessionActivity extends AppCompatActivity {

    public Button join_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_session);

        join_btn = findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(JoinSessionActivity.this, Question_Activity.class));

            }
        });

    }

}
