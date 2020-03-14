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

import static java.lang.Math.abs;

public class Chess {
    // Collection of chess pieces
    public ArrayList<ChessPiece> pieces = new ArrayList<ChessPiece>();

    // Collection of squares
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

    // The name of the bundle keys to save the chess
    private final static String LOCATIONS = "Chess.locations";
    private final static String IDS = "Chess.ids";

    // How much we scale the puzzle pieces
    private float scaleFactor;

    /**
     * This variable is set to a piece we are dragging. If
     * we are not dragging, the variable is null.
     */
    private ChessPiece dragging = null;

    // Most recent relative X touch when dragging
    private float lastRelX;

    // Most recent relative Y touch when dragging
    private float lastRelY;

    // Completed chess bitmap
    private Bitmap chessComplete;

    private ChessView chessView;

    private boolean hasMoved = false;

    private class Square{
        private int coordX, coordY;
        private float x, y; //Relative x, y locations in the range 0-1 for the center of the piece in the center of the square.
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

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
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
        pieces.add(new ChessPieceRook(context, R.drawable.chess_rdt45, 0.0622428f, 0.0622428f, 1, 0));
        pieces.add(new ChessPieceKnight(context, R.drawable.chess_ndt45, 0.18672839f, 0.0622428f, 1, 1));
        pieces.add(new ChessPieceBishop(context, R.drawable.chess_bdt45, 0.311214f, 0.0622428f, 1, 2));
        pieces.add(new ChessPieceQueen(context, R.drawable.chess_qdt45, 0.43569958f, 0.0622428f, 1, 3));
        pieces.add(new ChessPieceKing(context, R.drawable.chess_kdt45, 0.5601852f, 0.0622428f, 1, 4));
        pieces.add(new ChessPieceBishop(context, R.drawable.chess_bdt45, 0.6846708f, 0.0622428f, 1, 5));
        pieces.add(new ChessPieceKnight(context, R.drawable.chess_ndt45, 0.80915636f, 0.0622428f, 1, 6));
        pieces.add(new ChessPieceRook(context, R.drawable.chess_rdt45,  0.93364197f, 0.0622428f, 1, 7));

        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.0622428f, 0.18672839f, 1, 8));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.18672839f, 0.18672839f, 1, 9));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.311214f, 0.18672839f, 1, 10));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.43569958f, 0.18672839f, 1, 11));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.5601852f, 0.18672839f, 1, 12));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.6846708f, 0.18672839f, 1, 13));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.80915636f, 0.18672839f, 1, 14));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_pdt45, 0.93364197f, 0.18672839f, 1,15));

        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.0622428f, 0.80915636f, 2, 48));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.18672839f, 0.80915636f, 2, 49));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.311214f, 0.80915636f, 2, 50));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.43569958f, 0.80915636f, 2, 51));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.5601852f, 0.80915636f, 2, 52));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.6846708f, 0.80915636f, 2, 53));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.80915636f, 0.80915636f, 2, 54));
        pieces.add(new ChessPiecePawn(context, R.drawable.chess_plt45, 0.93364197f, 0.80915636f, 2, 55));

        pieces.add(new ChessPieceRook(context, R.drawable.chess_rlt45, 0.0622428f, 0.93364197f, 2, 56));
        pieces.add(new ChessPieceKnight(context, R.drawable.chess_nlt45, 0.18672839f, 0.93364197f, 2, 57));
        pieces.add(new ChessPieceBishop(context, R.drawable.chess_blt45, 0.311214f, 0.93364197f, 2, 58));
        pieces.add(new ChessPieceQueen(context, R.drawable.chess_qlt45, 0.43569958f, 0.93364197f, 2, 59));
        pieces.add(new ChessPieceKing(context, R.drawable.chess_klt45, 0.5601852f, 0.93364197f, 2, 60));
        pieces.add(new ChessPieceBishop(context, R.drawable.chess_blt45,  0.6846708f, 0.93364197f, 2, 61));
        pieces.add(new ChessPieceKnight(context, R.drawable.chess_nlt45, 0.80915636f, 0.93364197f, 2, 62));
        pieces.add(new ChessPieceRook(context, R.drawable.chess_rlt45, 0.93364197f, 0.93364197f, 2, 63));
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
        int c = 0;
        for(int i = 0; i < 8; i++){
            int sTop = (i * squareSize) + boardMarginY;
            int sBot = (i * squareSize) + squareSize + boardMarginY;
            for(int j = 0; j < 8; j++){
                Square squareToAdd = new Square();
                squareToAdd.setCoordX(j);
                squareToAdd.setCoordY(i);
                squareToAdd.setX(((float)j + (float)squareSize/2 - (float)boardMarginX) / (float)boardSize);
                squareToAdd.setY(((float)i + (float)squareSize/2 - (float)boardMarginX) / (float)boardSize);
                int sLeft = (j * squareSize) + boardMarginX;
                int sRight = (j * squareSize) + squareSize + boardMarginX;
                Rect tempRect = new Rect(sLeft, sTop, sRight, sBot);
                squareToAdd.setSquare(tempRect);
                if (i % 2 == 0 && j % 2 == 0){
                    canvas.drawRect(tempRect, whiteSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop};
                        squareToAdd.setPiece(pieces.get(indx)); indx++;}
                } else if (i % 2 == 0 && j % 2 == 1){
                    canvas.drawRect(tempRect, blackSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop};
                        squareToAdd.setPiece(pieces.get(indx)); indx++;}
                } else if (i % 2 == 1 && j % 2 == 0){
                    canvas.drawRect(tempRect, blackSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop};
                        squareToAdd.setPiece(pieces.get(indx)); indx++;}
                } else if (i % 2 == 1 && j % 2 == 1){
                    canvas.drawRect(tempRect, whiteSpace);
                    if (i < 2 || i > 5) {positions[indx] = new int[]{sLeft, sTop};
                        squareToAdd.setPiece(pieces.get(indx)); indx++;}
                }
                if(squares.size() < 64) {
                    squares.add(squareToAdd);
                }
            }
        }
        float horiz = 1f;
        float vert = 1f;
        for(int i = 0; i < squares.size(); i++){
            if(horiz>16f){
                horiz = 1f;
                vert+=2f;
            }
            squares.get(i).setX(horiz/16f);
            squares.get(i).setY(vert/16f);
            horiz+=2f;
        }
        for (ChessPiece piece : pieces) {
            if (piece.getSquare_id() != -1) {
                piece.draw(canvas, boardSize, boardMarginX, boardMarginY, scaleFactor);
            }
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
            if(pieces.get(p).hit(x, y, boardSize, scaleFactor) && pieces.get(p).getSquare_id() != -1) {
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
            Square toUpdate = squares.get(dragging.getSquare_id());
            if(isValidMove(x, y, dragging, squares.get(dragging.getSquare_id()))) {
                toUpdate.setPiece(null);
                snap(view, x, y);
            } else {
                dragging.setX(squares.get(dragging.getSquare_id()).x);
                dragging.setY(squares.get(dragging.getSquare_id()).y);
                view.invalidate();
            }
            dragging = null;
            return true;
        }

        return false;
    }

    private void snap(View view, float x, float y){
        int snapIndex = 0;
        ArrayList<Integer> possibleSnap = new ArrayList<>();
        float testX = 100;
        float testY = 100;
        int i;
        for(i=0; i<squares.size(); i++){
            if(abs(x-squares.get(i).getX())<=testX){
                testX=abs(x-squares.get(i).getX());
                possibleSnap.add(i);
            }
        }

        for(int j=0; j<possibleSnap.size();j++) {
            if (abs(y - squares.get(possibleSnap.get(j)).getY()) <= testY)
            {
                testY=abs(y - squares.get(possibleSnap.get(j)).getY());
                snapIndex = possibleSnap.get(j);

            }
        }

        dragging.setX(squares.get(snapIndex).getX());
        dragging.setY(squares.get(snapIndex).getY());
        squares.get(snapIndex).setPiece(dragging);
        view.invalidate();
    }

    private boolean isValidMove(float x, float y, ChessPiece piece, Square prevSquare){
        int snapIndex = 0;
        ArrayList<Integer> possibleSnap = new ArrayList<>();
        float testX = 100;
        float testY = 100;
        for(int i=0; i<squares.size(); i++){
            if(abs(x-squares.get(i).getX())<=testX){
                testX=abs(x-squares.get(i).getX());
                possibleSnap.add(i);
            }
        }

        for(int j=0; j<possibleSnap.size();j++) {
            if (abs(y - squares.get(possibleSnap.get(j)).getY()) <= testY)
            {
                testY=abs(y - squares.get(possibleSnap.get(j)).getY());
                snapIndex = possibleSnap.get(j);

            }
        }

        Square moveSquare = squares.get(snapIndex);

        Log.i("moveSquare","(" + moveSquare.getCoordX() + "," + moveSquare.getCoordY() + ")" + " Has Piece:" + moveSquare.getPiece());
        Log.i("prevSquare","(" + prevSquare.getCoordX() + "," + prevSquare.getCoordY() + ")");

        if (piece.getClass() == ChessPiecePawn.class){

            /*
            Rules for pawn:
            Can move two space forwards if it is their first move.
            Otherwise, can only move one space forward if there is no piece in front
            And can move one space diagonally "forward" if an opponent's piece is there.
            White/Black Player Pawns behave differently.
             */

            //White Player
            if(piece.getPlayer() == 2) {
                if (moveSquare.getCoordY() - prevSquare.getCoordY() == -2 && piece.isFirstMove()){
                    Log.i("Square", "(" + squares.get(snapIndex - 8).getCoordX() + "," + squares.get(snapIndex - 8).getCoordY() + ")");
                    if (squares.get(snapIndex - 8).getPiece() == null && moveSquare.getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        return true;
                    }
                } else if (moveSquare.getCoordY() - prevSquare.getCoordY() == -1) {
                    if (moveSquare.getCoordX() == prevSquare.getCoordX() && moveSquare.getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        return true;
                    } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && moveSquare.getPiece() != null) {
                        if (moveSquare.getPiece().getPlayer() == 1) {
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            piece.setFirstMove(false);
                            return true;
                        }
                    }
                }
            }

            //Black Player
            if(piece.getPlayer() == 1) {
                if (moveSquare.getCoordY() - prevSquare.getCoordY() == 2 && piece.isFirstMove()){
                    if (squares.get(snapIndex + 8).getPiece() == null && moveSquare.getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        return true;
                    }
                } else if (moveSquare.getCoordY() - prevSquare.getCoordY() == 1) {
                    if (moveSquare.getCoordX() == prevSquare.getCoordX() && moveSquare.getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        return true;
                    } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && moveSquare.getPiece() != null ) {
                        if (moveSquare.getPiece().getPlayer() == 2) {
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            piece.setFirstMove(false);
                            return true;
                        }
                    }
                }
            }

            return false;

        } else if (piece.getClass() == ChessPieceKnight.class){

            if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && abs(moveSquare.getCoordY() - prevSquare.getCoordY()) == 2){
                if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                    Captured(moveSquare.getPiece());
                    piece.setSquare_id(snapIndex);
                    return true;
                } else if (moveSquare.getPiece() == null) {
                    piece.setSquare_id(snapIndex);
                    return true;
                }
            } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 2 && abs(moveSquare.getCoordY() - prevSquare.getCoordY()) == 1){
                if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                    Captured(moveSquare.getPiece());
                    piece.setSquare_id(snapIndex);
                    return true;
                } else if (moveSquare.getPiece() == null) {
                    piece.setSquare_id(snapIndex);
                    return true;
                }
            }

        } else if (piece.getClass() == ChessPieceBishop.class){

            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            if (abs(x_check) ==  abs(y_check)){
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    }
                }
            }

        } else if (piece.getClass() == ChessPieceRook.class){

            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            if (moveSquare.getCoordX() == prevSquare.getCoordX()){
                if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                    for(int i = 1; i < abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                    for(int i = 1; i < abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (-8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                }
            } else if (moveSquare.getCoordY() == prevSquare.getCoordY()) {
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    for(int i = 1; i < abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    for(int i = 1; i < abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (-i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                }
            }

        } else if (piece.getClass() == ChessPieceQueen.class){

            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            if (abs(x_check) ==  abs(y_check)){
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i < abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            return true;
                        } else if (moveSquare.getPiece() == null){
                            piece.setSquare_id(snapIndex);
                            return true;
                        }
                    }
                }
            } else if (moveSquare.getCoordX() == prevSquare.getCoordX()){
                if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                    for(int i = 1; i < abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                    for(int i = 1; i < abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (-8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                }
            } else if (moveSquare.getCoordY() == prevSquare.getCoordY()) {
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    for(int i = 1; i < abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    for(int i = 1; i < abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (-i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                }
            }

        } else if (piece.getClass() == ChessPieceKing.class){

            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            if(abs(x_check) <= 1 && abs(y_check) <= 1){
                if(x_check == 0 && y_check == -1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == 1 && y_check == -1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == 1 && y_check == 0){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == 1 && y_check == 1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == 0 && y_check == 1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == -1 && y_check == 1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == -1 && y_check == 0){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                } else if(x_check == -1 && y_check == -1){
                    if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        Captured(moveSquare.getPiece());
                        piece.setSquare_id(snapIndex);
                        return true;
                    } else if (moveSquare.getPiece() == null){
                        piece.setSquare_id(snapIndex);
                        return true;
                    }
                }

            }

        }

        return false;
    }

    private void Captured(ChessPiece piece){
        squares.get(piece.getSquare_id()).setPiece(null);
        piece.setSquare_id(-1);
    }

}
