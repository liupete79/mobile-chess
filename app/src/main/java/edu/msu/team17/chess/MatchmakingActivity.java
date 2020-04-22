package edu.msu.team17.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.FieldClassification;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.msu.team17.chess.Cloud.Cloud;

public class MatchmakingActivity extends AppCompatActivity {

    private boolean cancel = false;
    private String username;
    private TextView txtView;
    private Button chessButton;
    private String opponent;
    public boolean toMatchmake = true;
    public boolean isMade = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        username = getIntent().getStringExtra("username");
        txtView = findViewById(R.id.textView);
        chessButton = findViewById(R.id.buttonName);
        chessButton.setClickable(false);
        final Handler handler = new Handler();
        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if(cancel){
                    return;
                }
                Cloud cloud;
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        Cloud cloud = new Cloud();
                        final boolean ok = cloud.find_opponent(username);
                        if (!ok) {
                            Log.i("find_opponent", "did not work");
                            toMatchmake = false;

                        }
                        else {
                            opponent = cloud.getOpponent();
                            Log.i("find_opponent", "worked");
                            MatchmakingActivity.this.runOnUiThread(new Runnable() {
                                public void run() {
                                    txtView.setText(R.string.oppFound);
                                    chessButton.setClickable(true);
                                    chessButton.setEnabled(true);
                                    if(toMatchmake){
                                        onStartNow();
                                    }
                                }
                            });
                        }
                    }
                }).start();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }
    public void onStartNow(){
        final String player1 = username;
        cancel = true;
        final String player2 = opponent;
        if(toMatchmake) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Cloud cloud = new Cloud();
                    final boolean ok = cloud.new_game(player1, player2);
                    isMade = true;
                    if (!ok) {
                    } else {
                    }
                }
            }).start();
        }
    }
    public void onStartChess(View view) {
        final String player1 = username;
        cancel = true;
        final String player2 = opponent;
        Intent intent = new Intent(this, ChessActivity.class);
        if (toMatchmake) {
            intent.putExtra("player1", player1);
            intent.putExtra("player2", player2);
        } else if (!toMatchmake){
            intent.putExtra("player1", player2);
            intent.putExtra("player2", player1);
        }
        startActivity(intent);
    }

    public void onStartMain(View view) {
        cancel = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
