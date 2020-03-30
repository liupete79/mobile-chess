package edu.msu.team17.chess;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.team17.chess.Cloud.Cloud;

public class MainActivity extends AppCompatActivity {
    boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onStartChess(View view) {
        EditText p1 = findViewById(R.id.username);
        EditText p2 = findViewById(R.id.password);
        final String username = p1.getText().toString();
        final String userpassword = p2.getText().toString();
        new Thread(new Runnable() {

            @Override
            public synchronized void run() {
                Cloud cloud = new Cloud();
                success = cloud.login(username, userpassword);
            }
        }).start();

        if(success){
            Intent intent = new Intent(this, MatchmakingActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(view.getContext(), R.string.logFailed, Toast.LENGTH_SHORT).show();
        }
    }
    public void onCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

}
