package edu.msu.team17.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {
    private String winner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            winner = extras.getString("winner");
            TextView currentPlayer = findViewById(R.id.endText);
            String finalWinner = winner + getString(R.string.winner);
            currentPlayer.setText(finalWinner);
        }
    }
    public void onStartMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
