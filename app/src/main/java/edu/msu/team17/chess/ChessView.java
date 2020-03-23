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

    // Current player
    public String player;

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

    public void onDone() { chess.onDone(); }
    public boolean getHasMoved() {return chess.hasMoveOccured();}

    public void setAllPlayers(String currplayer, String player1, String player2){
        chess.setCurrPlayer(currplayer);
        chess.setPlayer1(player1);
        chess.setPlayer2(player2);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        performClick();
        return chess.onTouchEvent(this, event); }
    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }
}
