package edu.msu.team17.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.msu.team17.chess.Cloud.Cloud;

public class ChessActivity extends AppCompatActivity {

    private String player1;
    private String player2;
    private String currPlayer;
    private final static String PLAYER1 = "ChessActivity.player1";
    private final static String PLAYER2 = "ChessActivity.player2";
    private final static String CURRENTPLAYER = "ChessActivity.currPlayer";

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chess);

        String p1;
        String p2;
        if(bundle != null) {
            // We have saved state
            getChessView().loadInstanceState(bundle);
            player1 = bundle.getString(PLAYER1);
            player2 = bundle.getString(PLAYER2);
            currPlayer = bundle.getString(CURRENTPLAYER);
        }
        else {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                player1 = extras.getString("player1");
                player2 = extras.getString("player2");
                if (player1.length() == 0) {
                    player1 = "Player 1";
                }
                if (player2.length() == 0) {
                    player2 = "Player 2";
                }
                currPlayer = player1;
                TextView currentPlayer = findViewById(R.id.currentPlayer);
                String finalText = player1 + getString(R.string.turn_title);
                currentPlayer.setText(finalText);

            }
        }
        getChessView().setAllPlayers(currPlayer, player1, player2);
    }

    public void tempOpen(View view){
        String gameId = "i dont know";
        new Thread(() -> {
            Cloud cloud = new Cloud();
            //Gets the chess pieces from Chess then sets it using the setter function.
            ArrayList<ChessPiece> newState = cloud.openFromCloud(player1, getChessView().getChess().getPieces());
            if(newState != null){
                getChessView().getChess().setPieces(newState);
            }
        }).start();
        getChessView().invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chess_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle.putString(PLAYER1, player1);
        bundle.putString(PLAYER2, player2);
        bundle.putString(CURRENTPLAYER, currPlayer);
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

    public void onTurnDone(View view) {

        if(getChessView().getHasMoved()== false){
            Toast.makeText(view.getContext(), R.string.please_move, Toast.LENGTH_SHORT).show();
            return;
        }
        if (currPlayer == player1) {
            currPlayer = player2;
        }
        else {
            currPlayer = player1;
        }

        new Thread(() -> {
            Cloud cloud = new Cloud();
            cloud.saveToCloud(player1, player2, getChessView(), currPlayer);

        }).start();
        TextView currentPlayer = findViewById(R.id.currentPlayer);
        String finalText = currPlayer + getString(R.string.turn_title);
        currentPlayer.setText(finalText);
        TextView currColor = findViewById(R.id.currColor);
        if (currPlayer == player1) {
            currColor.setText(getString(R.string.black));
        }
        else {
            currColor.setText(getString(R.string.white));
        }
        getChessView().onDone();
        getChessView().setAllPlayers(currPlayer, player1, player2);
        getChessView().getChess().setTurn(false);
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("winner", currPlayer);
    }

    public void onResign(View view) {
        String winner;
        if (currPlayer == player1) {
            winner = player2;
        }
        else {
            winner = player1;
        }
        new Thread(() -> {
            Cloud cloud = new Cloud();
            cloud.gameOver(player1, winner);

        }).start();
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("winner", winner);
        startActivity(intent);
    }
}
