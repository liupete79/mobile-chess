package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;


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

    public void saveXml(String player1, String player2, XmlSerializer xml) throws IOException {
        int number = 0;
        for(ChessPiece piece :chess.pieces){
            xml.startTag(null, "chessgame");
            xml.attribute(null, "piece_id", Integer.toString(number));
            if (piece.getPlayer() == 1){
                xml.attribute(null, "player", player1);
            }
            else{
                xml.attribute(null, "player", player2);
            }
            xml.attribute(null, "square_id", Integer.toString(piece.getSquare_id()));
            xml.attribute(null, "x", Float.toString(piece.getX()));
            xml.attribute(null, "y", Float.toString(piece.getY()));

            if (piece instanceof ChessPieceBishop) {
                xml.attribute(null, "type", "bishop");
            }
            else if (piece instanceof ChessPieceKing) {
                xml.attribute(null, "type", "king");
            }
            else if (piece instanceof ChessPieceKnight) {
                xml.attribute(null, "type", "knight");
            }
            else if (piece instanceof ChessPiecePawn) {
                xml.attribute(null, "type", "pawn");
            }
            else if (piece instanceof ChessPieceQueen) {
                xml.attribute(null, "type", "queen");
            }
            else {
                xml.attribute(null, "type", "rook");
            }

            xml.endTag(null,  "chessgame");
            number ++;
        }
    }
}
