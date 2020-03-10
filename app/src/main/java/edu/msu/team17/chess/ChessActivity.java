package edu.msu.team17.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class ChessActivity extends AppCompatActivity {

    private String currPlayer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chess);

        String p1;
        String p2;
        if(bundle != null) {
            // We have saved state
            getChessView().loadInstanceState(bundle);
        }
        else {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                p1 = "1";
                p2 = "2";
            } else {
                p1 = extras.getString("player1");
                p2 = extras.getString("player2");
            }
            TextView currentPlayer = findViewById(R.id.currentPlayer);
            currentPlayer.setText(p1 + "'s Turn!");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chess_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        getChessView().saveInstanceState(bundle);
    }

    /**
     * Get the chess view
     * @return ChessView reference
     */
    private ChessView getChessView() {
        return (ChessView)this.findViewById(R.id.chessView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rules:
                // The puzzle is done
                // Instantiate a dialog box builder
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(getChessView().getContext());

                // Parameterize the builder
                builder.setTitle(R.string.rules_title);
                builder.setMessage(R.string.rules);
                builder.setPositiveButton(android.R.string.ok, null);

                // Create the dialog box and show it
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                return true;
            case R.id.simulate_winner:
                Intent intent = new Intent(this, EndActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void onEndChess(View view) {
        Intent intent = new Intent(this, EndActivity.class);
        startActivity(intent);
    }
}
