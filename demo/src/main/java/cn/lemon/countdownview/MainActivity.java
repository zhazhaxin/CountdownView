package cn.lemon.countdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cn.lemon.view.Action;
import cn.lemon.view.CountdownView;

public class MainActivity extends AppCompatActivity {

    private CountdownView mCountdownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCountdownView = (CountdownView) findViewById(R.id.countdown);
        mCountdownView.start();
        mCountdownView.setOnFinishAction(new Action() {
            @Override
            public void onAction() {
                Toast.makeText(MainActivity.this,"结束",Toast.LENGTH_SHORT).show();
            }
        });
        mCountdownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"点击",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
