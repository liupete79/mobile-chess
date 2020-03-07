package edu.msu.team17.chess;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.app.AlertDialog;
import android.os.Bundle;

import java.sql.Struct;
import java.util.*;

public class Chess {
    // Collection of puzzle pieces
    public ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();
    public ArrayList<Square> squares = new ArrayList<>();

    //% of display the chess board will take up
    final static float VIEW_SCALE = 0.9f;

    //Size of chess board in pixels
    private int boardSize;
    //Size of square in pixels;
    private int squareSize;

    //Paint for creating the squares of the chess board
    private Paint whiteSpace;
    private Paint blackSpace;
    private Paint outline;

    //Left(X) and Top(Y) margin in pixels for whole board
    private int boardMarginX;
    private int boardMarginY;

    /**
     * The name of the bundle keys to save the chess
     */
    private final static String LOCATIONS = "Chess.locations";
    private final static String IDS = "Chess.ids";

    /**
     * How much we scale the puzzle pieces
     */
    private float scaleFactor;

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private ChessPiece dragging = null;

    /**
     * Most recent relative X touch when dragging
     */
    private float lastRelX;

    /**
     * Most recent relative Y touch when dragging
     */
    private float lastRelY;

    /**
     * Completed chess bitmap
     */
    private Bitmap chessComplete;

    private ChessView chessView;

    private class Square{
        private int coordX, coordY;
        private Rect square = null;
        private ChessPiece piece = null;

        public int getCoordX() {
            return this.coordX;
        }

        public int getCoordY() {
            return this.coordY;
        }

        public Rect getSquare(){
            return this.square;
        }

        public ChessPiece getPiece(){
            return this.piece;
        }

        public void setCoordX(int x) {
            this.coordX = x;
        }

        public void setCoordY(int y) {
            this.coordY = y;
        }

        public void setSquare(Rect square) {
            this.square = square;
        }

        public void setPiece(ChessPiece piece) {
            this.piece = piece;
        }
    }

    public Chess(Context context, ChessView view) {
        whiteSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        outline = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteSpace.setColor(0xffe8e8e8);
        blackSpace.setColor(0xff458c45);
        outline.setColor(0xff000000);
        chessView = view;
        // Load the solved chess image
        chessComplete =
                BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.chess);

        // Load the puzzle pieces
        pieces.add(new ChessPiece(context, R.drawable.chess_rdt45, 0.0622428f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_ndt45, 0.18672839f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_bdt45, 0.311214f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_qdt45, 0.43569958f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_kdt45, 0.5601852f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_bdt45, 0.6846708f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_ndt45, 0.80915636f, 0.0622428f));
        pieces.add(new ChessPiece(context, R.drawable.chess_rdt45,  0.93364197f, 0.0622428f));

        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.0622428f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.18672839f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.311214f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.43569958f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.5601852f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.6846708f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.80915636f, 0.18672839f));
        pieces.add(new ChessPiece(context, R.drawable.chess_pdt45, 0.93364197f, 0.18672839f));

        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.0622428f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.18672839f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.311214f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.43569958f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.5601852f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.6846708f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.80915636f, 0.80915636f));
        pieces.add(new ChessPiece(context, R.drawable.chess_plt45, 0.93364197f, 0.80915636f));

        pieces.add(new ChessPiece(context, R.drawable.chess_rlt45, 0.0622428f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_nlt45, 0.18672839f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_blt45, 0.311214f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_qlt45, 0.43569958f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_klt45, 0.5601852f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_blt45,  0.6846708f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_nlt45, 0.80915636f, 0.93364197f));
        pieces.add(new ChessPiece(context, R.drawable.chess_rlt45, 0.93364197f, 0.93364197f));
    }

    public void draw(Canvas canvas){
        int wid = canvas.getWidth();
        int hit = canvas.getHeight();

        // Determine the minimum of the two dimensions
        int minDim = wid < hit ? wid : hit;

        boardSize = (int)(minDim * VIEW_SCALE);
        squareSize = boardSize/8;

        // Compute the margins so we center the board
        boardMarginX = (wid - boardSize) / 2;
        boardMarginY = (hit - boardSize) / 2;

        // Draw the outline of the board
        canvas.drawRect(boardMarginX - 3, boardMarginY - 3, boardMarginX + boardSize + 3,
                boardMarginY + boardSize + 3, outline);

        scaleFactor = (float)boardSize/(float)(6*chessComplete.getWidth());

        int[][] positions = new int[32][];
        int indx = 0;
        for(int i = 0; i < 8; i++){
            int sTop = (i * squareSize) + boardMarginY;
            int sBot = (i * squareSize) + squareSize + boardMarginY;
            for(int j = 0; j < 8; j++){
                Square squareToAdd = new Square();
                squareToAdd.setCoordX(j);
                squareToAdd.setCoordY(i);
                int sLeft = (j * squareSize) + boardMarginX;
                int sRight = (j * squareSize) + squareSize + boardMarginX;
                Rect tempRect = new Rect(sLeft, sTop, sRight, sBot);
                squareToAdd.setSquare(tempRect);
                if (i % 2 == 0 && j % 2 == 0){
                    canvas.drawRect(tempRect, whiteSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop}; indx++;}
                } else if (i % 2 == 0 && j % 2 == 1){
                    canvas.drawRect(tempRect, blackSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop}; indx++;}
                } else if (i % 2 == 1 && j % 2 == 0){
                    canvas.drawRect(tempRect, blackSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop}; indx++;}
                } else if (i % 2 == 1 && j % 2 == 1){
                    canvas.drawRect(tempRect, whiteSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop}; indx++;}
                }
                squares.add(squareToAdd);
            }
        }

        for (ChessPiece piece : pieces) {
            piece.draw(canvas, boardSize, boardMarginX, boardMarginY, scaleFactor);
        }

        canvas.save();
        canvas.translate(boardMarginX, boardMarginY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.restore();
    }

    /**
     * Save the chess to a bundle
     * @param bundle The bundle we save to
     */
    public void saveInstanceState(Bundle bundle) {
        float [] locations = new float[pieces.size() * 2];
        int [] ids = new int[pieces.size()];

        for(int i=0;  i<pieces.size(); i++) {
            ChessPiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
        }

        bundle.putFloatArray(LOCATIONS, locations);
        bundle.putIntArray(IDS,  ids);
    }

    /**
     * Read the chess from a bundle
     * @param bundle The bundle we save to
     */
    public void loadInstanceState(Bundle bundle) {
        float [] locations = bundle.getFloatArray(LOCATIONS);
        int [] ids = bundle.getIntArray(IDS);

        for(int i=0; i<ids.length-1; i++) {

            // Find the corresponding piece
            // We don't have to test if the piece is at i already,
            // since the loop below will fall out without it moving anything
            for (int j = i + 1; j < ids.length; j++) {
                if (ids[i] == pieces.get(j).getId()) {
                    // We found it
                    // Yah...
                    // Swap the pieces
                    ChessPiece t = pieces.get(i);
                    pieces.set(i, pieces.get(j));
                    pieces.set(j, t);
                }
            }
        }

        for(int i=0;  i<pieces.size(); i++) {
            ChessPiece piece = pieces.get(i);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
        }
    }

    /**
     * Handle a touch event from the view.
     * @param view The view that is the source of the touch
     * @param event The motion event describing the touch
     * @return true if the touch is handled.
     */
    public boolean onTouchEvent(View view, MotionEvent event) {
        //
        // Convert an x,y location to a relative location in the
        // puzzle.
        //
        float relX = (event.getX() - boardMarginX) / boardSize;
        float relY = (event.getY() - boardMarginY) / boardSize;

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                return onTouched(relX, relY);

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                return onReleased(view, relX, relY);

            case MotionEvent.ACTION_MOVE:
                // If we are dragging, move the piece and force a redraw
                if(dragging != null) {
                    dragging.move(relX - lastRelX, relY - lastRelY);
                    lastRelX = relX;
                    lastRelY = relY;
                    view.invalidate();
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Handle a touch message. This is when we get an initial touch
     * @param x x location for the touch, relative to the puzzle - 0 to 1 over the board
     * @param y y location for the touch, relative to the puzzle - 0 to 1 over the board
     * @return true if the touch is handled
     */
    private boolean onTouched(float x, float y) {

        // Check each piece to see if it has been hit
        // We do this in reverse order so we find the pieces in front
        for(int p=pieces.size()-1; p>=0;  p--) {
            if(pieces.get(p).hit(x, y, boardSize, scaleFactor)) {
                // We hit a piece!
                dragging = pieces.get(p);
                pieces.remove(p);
                pieces.add(dragging);
                lastRelX = x;
                lastRelY = y;
                return true;
            }
        }

        return false;
    }

    /**
     * Handle a release of a touch message.
     * @param x x location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @param y y location for the touch release, relative to the puzzle - 0 to 1 over the puzzle
     * @return true if the touch is handled
     */
    private boolean onReleased(View view, float x, float y) {

        if(dragging != null) {
            dragging = null;
            return true;
        }

        return false;
    }
}
