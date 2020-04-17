package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.Serializable;


/**
 * Custom view class for Chess.
 */
public class ChessView extends View {

    //The actual chess board + game
    private Chess chess;

    // Current player
    public String player;

    /**
     * The bitmap to draw the hat
     */
    private Bitmap chessPieceBitmap = null;

    /**
     * Current program state
     */
    private static class Parameters implements Serializable {
        /**
         * Serialization ID value
         */
        private static final long serialVersionUID = -6692441979811271612L;

        /**
         * X location of hat relative to the image
         */
        public float chessX = 0;

        /**
         * Y location of hat relative to the image
         */
        public float chessY = 0;

        /**
         * Hat scale, also relative to the image
         */
        public int square_id = -1;

        /**
         * Hat rotation angle
         */
        public int player = 1;

        /**
         * Do we draw a feather?
         */
        public String type = "pawn";
    }

    /**
     * The current parameters
     */
    private Parameters params = new Parameters();

    public Chess getChess() {return chess;}

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

    public void loadPiece(ChessPiece piece) {
        // Create a new set of parameters
        final Parameters newParams = new Parameters();

        // Load into it
        newParams.chessX = piece.getX();
        newParams.chessY = piece.getY();
        newParams.player = piece.getPlayer();
        newParams.square_id = piece.getSquare_id();
        newParams.type = piece.getType();

        post(new Runnable() {

            @Override
            public void run() {
                params = newParams;

                // Ensure the options are all set
//                setColor(params.color);
//                setImageUri(params.imageUri);
//                setHat(params.hat);
//                setFeather(params.feather);

            }

        });

    }
}
