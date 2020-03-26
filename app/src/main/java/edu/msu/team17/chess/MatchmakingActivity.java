package edu.msu.team17.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MatchmakingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
    }

    public void onStartChess(View view) {
        EditText p1 = findViewById(R.id.textName1);
        EditText p2 = findViewById(R.id.textName2);
        String player1 = p1.getText().toString();
        String player2 = p2.getText().toString();
        Intent intent = new Intent(this, ChessActivity.class);
        intent.putExtra("player1", player1);
        intent.putExtra("player2", player2);
        startActivity(intent);
    }
    public void onStartMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
