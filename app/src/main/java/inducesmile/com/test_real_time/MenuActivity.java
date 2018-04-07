package inducesmile.com.test_real_time;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    public Button account_btn;
    public Button join_btn;
    public Button createSession_btn;
    public Button help_btn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        join_btn =  findViewById(R.id.join_btn);

        join_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, JoinSessionActivity.class));

            }
        });





    }
}
