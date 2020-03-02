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

    private class Square{
        private int x, y;
        private Rect square = null;
        private ChessPiece piece = null;

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public Rect getSquare(){
            return this.square;
        }

        public ChessPiece getPiece(){
            return this.piece;
        }

        public void setX(int x) {
            this.x = x;
        }

        public void setY(int y) {
            this.y = y;
        }

        public void setSquare(Rect square) {
            this.square = square;
        }

        public void setPiece(ChessPiece piece) {
            this.piece = piece;
        }
    }

    public Chess(Context context) {
        whiteSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        outline = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteSpace.setColor(0xffe8e8e8);
        blackSpace.setColor(0xff458c45);
        outline.setColor(0xff000000);
        // Load the puzzle pieces
        pieces.add(new ChessPiece(context, R.drawable.chess_rdt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_ndt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_bdt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_qdt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_kdt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_bdt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_ndt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_rdt45));
        for (int x = 0; x < 8; x++) {
            pieces.add(new ChessPiece(context, R.drawable.chess_pdt45));
        }

//        for (int x = 0; x < 32; x++) {
//            pieces.add(null);
//        }

        for (int x = 0; x < 8; x++) {
            pieces.add(new ChessPiece(context, R.drawable.chess_plt45));
        }
        pieces.add(new ChessPiece(context, R.drawable.chess_rlt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_nlt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_blt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_qlt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_klt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_blt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_nlt45));
        pieces.add(new ChessPiece(context, R.drawable.chess_rlt45));
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

        //Testing with 1 square
        /*canvas.drawRect(boardMarginX,
                boardMarginY,
                squareSize + boardMarginX,
                squareSize + boardMarginY,
                whiteSpace);
        */
        scaleFactor = (float) (((float)squareSize *1.25) / (float)(boardSize));

        int[][] positions = new int[32][];
        int indx = 0;
        for(int i = 0; i < 8; i++){
            int sTop = (i * squareSize) + boardMarginY;
            int sBot = (i * squareSize) + squareSize + boardMarginY;
            for(int j = 0; j < 8; j++){
                int sLeft = (j * squareSize) + boardMarginX;
                int sRight = (j * squareSize) + squareSize + boardMarginX;
                Rect tempRect = new Rect(sLeft, sTop, sRight, sBot);
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
            }
        }

        canvas.save();
        canvas.translate(boardMarginX, boardMarginY);
        canvas.scale(scaleFactor, scaleFactor);
        canvas.restore();

        indx = 0;
        for (ChessPiece piece : pieces) {
            int locX = positions[indx][0];
            int locY = positions[indx][1];
            piece.draw(canvas, boardMarginX, boardMarginY, boardSize, locX, locY, scaleFactor);
            indx++;
        }

        canvas.save();
        canvas.translate(boardMarginX, boardMarginY);
        canvas.scale(boardSize, boardSize);
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
}
