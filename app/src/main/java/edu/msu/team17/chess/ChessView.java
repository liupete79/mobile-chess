package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


/**
 * Custom view class for Chess.
 */
public class ChessView extends View {

    //The actual chess board + game
    private Chess chess;

    private String player1;
    private String player2;
    private String currPlayer;

    public ChessView(Context context) {
        super(context);
        init(null, 0);
    }

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ChessView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        chess = new Chess(getContext(), this);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        chess.draw(canvas);
    }

    /**
     * Save the chess to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        chess.saveInstanceState(bundle);
    }

    /**
     * Load the chess from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        chess.loadInstanceState(bundle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return chess.onTouchEvent(this, event);
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getCurrPlayer() {
        return currPlayer;
    }

    public void setCurrPlayer(String currPlayer) {
        this.currPlayer = currPlayer;
    }
}
