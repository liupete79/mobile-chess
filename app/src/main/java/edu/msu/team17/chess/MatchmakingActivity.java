package edu.msu.team17.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.msu.team17.chess.Cloud.Cloud;

public class MatchmakingActivity extends AppCompatActivity {

    boolean cancel = false;
    String username;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        username = getIntent().getStringExtra("username");
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
                            /*
                             * If we fail to save, display a toast
                             v.post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(v.getContext(), R.string.logFailed, Toast.LENGTH_SHORT).show();
                                }
                            });
                             */
                            Log.i("find_opponent", "did not work");
                        }
                        else {
                            Log.i("find_opponent", "worked");
                            ///Intent intent = new Intent(v.getContext(), MatchmakingActivity.class);
                            ///startActivity(intent);
                        }
                    }
                }).start();
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void onStartChess(View view) {
        String player1 = username;
        cancel = true;
        String player2 = "need to fill";
        Intent intent = new Intent(this, ChessActivity.class);
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);
        startActivity(intent);
    }
    public void onStartMain(View view) {
        cancel = true;
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
