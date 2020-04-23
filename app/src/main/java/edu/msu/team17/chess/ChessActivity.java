package edu.msu.team17.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.msu.team17.chess.Cloud.Cloud;

public class ChessActivity extends AppCompatActivity {

    private String player1;
    private String player2;
    private String currPlayer;
    private String user;
    private final static String PLAYER1 = "ChessActivity.player1";
    private final static String PLAYER2 = "ChessActivity.player2";
    private final static String USER = "ChessActivity.user";
    private final static String CURRENTPLAYER = "ChessActivity.currPlayer";
    private boolean yourTurn;
    private boolean hasMoved = false;
    private boolean cancel = false;

    private static final long DISCONNECT_TIMER = 240000;
    //5 Minutes, 60 * 5 * 1000 = 300 000
    //30 Seconds, 30 * 1000 = 30 000

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
            user = bundle.getString(USER);
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
                user = extras.getString("user");
                TextView currentPlayer = findViewById(R.id.currentPlayer);
                String finalText = player1 + getString(R.string.turn_title);
                currentPlayer.setText(finalText);

            }
        }
        getChessView().getChess().setUser(user);
        getChessView().setAllPlayers(currPlayer, player1, player2);
        if(user.equals(currPlayer)){
            getChessView().getChess().setYourTurn(true);
            yourTurn = true;
        }
        else{
            getChessView().getChess().setYourTurn(false);
            yourTurn = false;
        }

        final Handler handler = new Handler();

        final int delay = 1000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){

                Log.i("turn", "check");
                ArrayList<ChessPiece> pieces = getChessView().getChess().getPieces();
                for (int i = 0; i < pieces.size(); i++) {

                    boolean test = pieces.get(i).getHasMoved();
                    if(test == true){
                        hasMoved = true;
                        break;
                    }
                    else{
                        hasMoved = false;
                    }
                }
                if(cancel){
                    return;
                }
                if(getChessView().getChess().getDragging() == null){
                    if(yourTurn == false) {
                            checkGame();
                            load();
                    }
                }
                if(yourTurn){

                }
                handler.postDelayed(this, delay);
            }
        }, delay);
    }

    private Handler disconnectHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            return true;
        }
    });

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            // Perform any required operation on disconnect
            onResign(getChessView());
        }
    };

    public void resetDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, DISCONNECT_TIMER);
    }

    public void stopDisconnectTimer(){
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction(){
        resetDisconnectTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }

    public boolean comparePieces(ArrayList<ChessPiece> first, ArrayList<ChessPiece> second){
        Integer total1 = 0;
        Integer total2 = 0;
        for(ChessPiece piece :first){
                total1 += piece.getSquare_id();
        }
        for(ChessPiece piece2 :second){
                total2 += piece2.getSquare_id();
        }
        if(total1.intValue() != total2.intValue()){
            return true;
        }
        return false;
    }

    public boolean dumb = false;

    public void checkGame(){
        String gameId = "i dont know";
        new Thread(() -> {
            Cloud cloud = new Cloud();
            cloud.getGameStatus(player1);
            if(cloud.gameDone){
                cancel = true;
                Intent intent = new Intent(this, EndActivity.class);
                intent.putExtra("winner", cloud.winner);
                startActivity(intent);
            }
            String current = cloud.getCurrPlayer();
            if(yourTurn == false){
                if(current.equals(user)){
                    ChessActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            newTurn();
                        }
                    });
                }
            }

        }).start();

    }

    public void newTurn(){
        yourTurn = true;
        if (currPlayer == player1) {
            currPlayer = player2;
        }
        else {
            currPlayer = player1;
        }
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
        getChessView().setAllPlayers(currPlayer, player1, player2);
        getChessView().getChess().setTurn(true);

    }
    public void load(){
        String gameId = "i dont know";
        new Thread(() -> {
            Cloud cloud = new Cloud();
            cloud.setContext(getChessView().getChess().getContex());
            //Gets the chess pieces from Chess then sets it using the setter function.
            ArrayList<ChessPiece> newState = cloud.openFromCloud(player1, getChessView().getChess().getPieces());
            if(newState != null) {
                    getChessView().getChess().setPieces(newState);
                    ChessActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            getChessView().invalidate();
                        }
                    });
                }


            else{
            }
        }).start();
    }

    public void tempOpen(View view){
        String gameId = "i dont know";
        new Thread(() -> {
            Cloud cloud = new Cloud();
            cloud.setContext(getChessView().getChess().getContex());
            //Gets the chess pieces from Chess then sets it using the setter function.
            ArrayList<ChessPiece> newState = cloud.openFromCloud(player1, getChessView().getChess().getPieces());
            if(newState != null){
                getChessView().getChess().setPieces(newState);
                ChessActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        yourTurn = true;
                        getChessView().invalidate();
                    }
                });


            }
            else{
            }
        }).start();
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

    public void onTurnDoneKinda() {

        if (currPlayer == player1) {
            currPlayer = player2;
        }
        else {
            currPlayer = player1;
        }
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
        getChessView().getChess().setTurn(true);
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
            yourTurn = false;

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
        cancel = true;
        Intent intent = new Intent(this, EndActivity.class);
        intent.putExtra("winner", winner);
        startActivity(intent);
    }
}
