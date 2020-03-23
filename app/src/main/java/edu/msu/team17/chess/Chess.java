package edu.msu.team17.chess;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.startActivity;
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
    private final static String SQUAREIDS = "Chess.squareIds";
    private final static String FIRSTMOVES = "Chess.firstMoves";
    private final static String HASMOVED = "Chess.hasMoved";

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

    // Chess view
    private ChessView chessView;

    private String currPlayer;

    private boolean kingCaptured = false;

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
        int [] squareIds = new int[pieces.size()];
        boolean [] firstMoves = new boolean[pieces.size()];
        boolean [] hasMoved = new boolean[pieces.size()];

        for(int i=0;  i<pieces.size(); i++) {
            ChessPiece piece = pieces.get(i);
            locations[i*2] = piece.getX();
            locations[i*2+1] = piece.getY();
            ids[i] = piece.getId();
            squareIds[i] = piece.getSquare_id();
            firstMoves[i] = piece.isFirstMove();
            hasMoved[i] = piece.getHasMoved();
        }
        bundle.putBooleanArray(HASMOVED, hasMoved);
        bundle.putBooleanArray(FIRSTMOVES, firstMoves);
        bundle.putIntArray(SQUAREIDS, squareIds);
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
        int [] squareIds = bundle.getIntArray(SQUAREIDS);
        boolean [] firstMoves = bundle.getBooleanArray(FIRSTMOVES);
        boolean [] hasMoved = bundle.getBooleanArray(HASMOVED);

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
            piece.setHasMoved(hasMoved[i]);
            piece.setX(locations[i*2]);
            piece.setY(locations[i*2+1]);
            piece.setSquare_id(squareIds[i]);
            piece.setFirstMove(firstMoves[i]);
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
                int i = 0;
                for (i = 0; i < pieces.size(); i++) {
                    if(pieces.get(i).getHasMoved()== true){
                        return false;
                    }
                }
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
                for (ChessPiece piece : pieces) {
                    if (piece instanceof ChessPieceKing) {
                        if (piece.getSquare_id() == -1) {
                            Intent intent = new Intent(view.getContext(), EndActivity.class);
                            view.getContext().startActivity(intent);
                            break;
                        }
                    }
                }
            } else {
                dragging.setX(squares.get(dragging.getSquare_id()).x);
                dragging.setY(squares.get(dragging.getSquare_id()).y);
                Toast.makeText(view.getContext(), R.string.invalid, Toast.LENGTH_SHORT).show();
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
        for(i=0; i<squares.size(); i++){///get an array list of possible snap locations using x variable
            if(abs(x-squares.get(i).getX())<=testX){
                testX=abs(x-squares.get(i).getX());
                possibleSnap.add(i);
            }
        }

        for(int j=0; j<possibleSnap.size();j++) {///use y values to see which square is closest
            if (abs(y - squares.get(possibleSnap.get(j)).getY()) <= testY)
            {
                testY=abs(y - squares.get(possibleSnap.get(j)).getY());
                snapIndex = possibleSnap.get(j);///get an index of the square with the closest coordinates

            }
        }

        dragging.setX(squares.get(snapIndex).getX());
        dragging.setY(squares.get(snapIndex).getY());///snap piece to the square
        squares.get(snapIndex).setPiece(dragging);
        view.invalidate();
    }

    private boolean isValidMove(float x, float y, ChessPiece piece, Square prevSquare){
        for (int i = 0; i < pieces.size(); i++) {

            if(pieces.get(i).getHasMoved()==true){
                return false;
            }
        }
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
            Rules for Pawns:
            Can move two space forwards if it is their first move.
            Otherwise, can only move one space forward if there is no piece in front
            And can move one space diagonally "forward" if an opponent's piece is there.
            White/Black Player Pawns behave differently.
             */

            //White Player
            if(piece.getPlayer() == 2) {
                //Checks for if we can move two spaces, only if it's the piece's first move.
                if (moveSquare.getCoordY() - prevSquare.getCoordY() == -2 && piece.isFirstMove() && moveSquare.getCoordX() == prevSquare.getCoordX()){
                    Log.i("Square", "(" + squares.get(snapIndex - 8).getCoordX() + "," + squares.get(snapIndex - 8).getCoordY() + ")");
                    if (squares.get(snapIndex).getPiece() == null && moveSquare.getPiece() == null && squares.get(piece.getSquare_id() - 8).getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        piece.setHasMoved(true);
                        return true;
                    }
                //Otherwise, checks to see if the pawn moves one space up
                } else if (moveSquare.getCoordY() - prevSquare.getCoordY() == -1) {
                    //If it does and the x coordinate is the same, check if space is empty and move
                    if (moveSquare.getCoordX() == prevSquare.getCoordX() && moveSquare.getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        promotePawn(piece);
                        piece.setHasMoved(true);
                        return true;
                    //If the x coordinate changed by 1, check if there is an enemy piece there and capture.
                    } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && moveSquare.getPiece() != null) {
                        if (moveSquare.getPiece().getPlayer() == 1) {
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            piece.setFirstMove(false);
                            promotePawn(piece);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                }
            }

            //Black Player, same if statements, just in the other direction.
            if(piece.getPlayer() == 1) {
                if (moveSquare.getCoordY() - prevSquare.getCoordY() == 2 && piece.isFirstMove()){
                    if (squares.get(snapIndex).getPiece() == null && moveSquare.getPiece() == null && moveSquare.getCoordX() == prevSquare.getCoordX()) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        piece.setHasMoved(true);
                        return true;
                    }
                } else if (moveSquare.getCoordY() - prevSquare.getCoordY() == 1) {
                    if (moveSquare.getCoordX() == prevSquare.getCoordX() && moveSquare.getPiece() == null && squares.get(piece.getSquare_id() + 8).getPiece() == null) {
                        piece.setSquare_id(snapIndex);
                        piece.setFirstMove(false);
                        promotePawn(piece);
                        piece.setHasMoved(true);
                        return true;
                    } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && moveSquare.getPiece() != null ) {
                        if (moveSquare.getPiece().getPlayer() == 2) {
                            Captured(moveSquare.getPiece());
                            piece.setSquare_id(snapIndex);
                            piece.setFirstMove(false);
                            promotePawn(piece);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                }
            }

            //Return false as the pawn can't move anywhere otherwise.
            return false;

        } else if (piece.getClass() == ChessPieceKnight.class){

            /*
            Rules for Knights:
            Knights move 2 spaces in the x/y, then move 1 space in the y/x coordinate.
            Can hop over other pieces
            Captures enemy pieces in intended destination
             */
            //Checks for moving 1 space in the x direction, then 2 spaces in the y direction
            if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 1 && abs(moveSquare.getCoordY() - prevSquare.getCoordY()) == 2){
                //Checks for piece capture
                if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                    Captured(moveSquare.getPiece());
                    piece.setSquare_id(snapIndex);
                    piece.setHasMoved(true);
                    return true;
                } else if (moveSquare.getPiece() == null) {
                    piece.setSquare_id(snapIndex);
                    piece.setHasMoved(true);
                    return true;
                }
            //Checks for moving 1 space in the y direction, then 2 spaces in the x direction
            } else if (abs(moveSquare.getCoordX() - prevSquare.getCoordX()) == 2 && abs(moveSquare.getCoordY() - prevSquare.getCoordY()) == 1){
                //Checks for piece capture
                if (moveSquare.getPiece() != null && moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                    Captured(moveSquare.getPiece());
                    piece.setSquare_id(snapIndex);
                    piece.setHasMoved(true);
                    return true;
                } else if (moveSquare.getPiece() == null) {
                    piece.setSquare_id(snapIndex);
                    piece.setHasMoved(true);
                    return true;
                }
            }

            return false;

        } else if (piece.getClass() == ChessPieceBishop.class){

            /*
            Rules for Bishops:
            Can move in any diagonal direction
            Can get blocked by pieces on the way
            Captures enemy pieces in intended destination
             */

            //Gets the difference between x and y coordinates
            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            //If the difference is the same, that means we are moving diagonally
            if (abs(x_check) ==  abs(y_check)){
                //Moving to the right side of board (+X)
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    //Moving down the board (+Y)
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        //Checks to make sure there is no piece in the way
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        //Moving up the board (-Y)
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                //Moving to the left side of the board (-X)
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    //Moving down the board (+Y)
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    //Moving up the board (-Y)
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                }
            }

            return false;

        } else if (piece.getClass() == ChessPieceRook.class){

            /*
            Rules for Rooks:
            Can move in any horizontal or vertical direction
            Can get blocked by pieces on the way
            Captures enemy pieces in intended destination
             */

            //Difference between X & Y coordinates
            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            //If the X coord is the same, moving vertically
            if (moveSquare.getCoordX() == prevSquare.getCoordX()){
                //Moving down the board (+Y)
                if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                    //Checks for pieces on the way
                    for(int i = 1; i != abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                //Moving up the board (-Y)
                } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                    for(int i = 1; i != abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (-8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                }
            //If the Y coord is the same, moving horizontally
            } else if (moveSquare.getCoordY() == prevSquare.getCoordY()) {
                //Moving to the right (+X)
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    for(int i = 1; i != abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                //Moving to the left (-X)
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    for(int i = 1; i != abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (-i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                }
            }

            return false;

        } else if (piece.getClass() == ChessPieceQueen.class){

            /*
            Rules for Queens:
            Can move in any cardinal direction
            Can get blocked by any piece on the way
            Captures enemy pieces in intended destination
            Slay the opposition of the King
             */

            //Gets the difference between x and y coordinates
            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            //This is just the Rook + Bishop massive nested if statements combined
            if (abs(x_check) ==  abs(y_check)){
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (7 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                        for(int i = 1; i != abs(x_check); i ++){
                            int sq_index = piece.getSquare_id() + (-9 * i);
                            if (squares.get(sq_index).getPiece() != null){
                                return false;
                            }
                        }
                        if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                            completeMovement(piece, moveSquare);
                            piece.setHasMoved(true);
                            return true;
                        }
                    }
                }
            } else if (moveSquare.getCoordX() == prevSquare.getCoordX()){
                if (moveSquare.getCoordY() > prevSquare.getCoordY()){
                    for(int i = 1; i != abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                } else if (moveSquare.getCoordY() < prevSquare.getCoordY()) {
                    for(int i = 1; i != abs(y_check); i ++){
                        int sq_index = piece.getSquare_id() + (-8 * i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                }
            } else if (moveSquare.getCoordY() == prevSquare.getCoordY()) {
                if (moveSquare.getCoordX() > prevSquare.getCoordX()){
                    for(int i = 1; i != abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                } else if (moveSquare.getCoordX() < prevSquare.getCoordX()) {
                    for(int i = 1; i != abs(x_check); i ++){
                        int sq_index = piece.getSquare_id() + (-i);
                        if (squares.get(sq_index).getPiece() != null){
                            return false;
                        }
                    }
                    if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()){
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                }
            }

            return false;

        } else if (piece.getClass() == ChessPieceKing.class){

            /*
            Rules for Kings:
            Can move only 1 space in any cardinal direction
            Captures enemy pieces in intended destination
            Checks are not being checked for. (Eh? Eh?)
            Rule the chess board.
             */

            //Gets the difference between x and y coordinates
            int x_check = moveSquare.getCoordX() - prevSquare.getCoordX();
            int y_check = moveSquare.getCoordY() - prevSquare.getCoordY();

            //Checks to make sure king is only moving 1 space in any cardinal direction
            if(abs(x_check) <= 1 && abs(y_check) <= 1) {
                if (moveSquare.getPiece() == null || moveSquare.getPiece().getPlayer() != piece.getPlayer()) {
                    //Moving North
                    if (x_check == 0 && y_check == -1) {
                        //Checks for piece capture
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving NorthEast
                    } else if (x_check == 1 && y_check == -1) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving East
                    } else if (x_check == 1 && y_check == 0) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving SouthEast
                    } else if (x_check == 1 && y_check == 1) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving South
                    } else if (x_check == 0 && y_check == 1) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving SouthWest
                    } else if (x_check == -1 && y_check == 1) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving West
                    } else if (x_check == -1 && y_check == 0) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                        //Moving NorthWest
                    } else if (x_check == -1 && y_check == -1) {
                        completeMovement(piece, moveSquare);
                        piece.setHasMoved(true);
                        return true;
                    }
                }
            }
            return false;

        }

        //Return False just in case.
        return false;
    }

    private void completeMovement (ChessPiece pieceToMove, Square destinationSquare){
        if (destinationSquare.getPiece() != null && destinationSquare.getPiece().getPlayer() != pieceToMove.getPlayer()){
            Captured(destinationSquare.getPiece());
            pieceToMove.setSquare_id(squares.indexOf(destinationSquare));
        } else if (destinationSquare.getPiece() == null){
            pieceToMove.setSquare_id(squares.indexOf(destinationSquare));
        }
    }

    //Capture to get the captured pieces off the board
    private void Captured(ChessPiece piece){
        squares.get(piece.getSquare_id()).setPiece(null);
        piece.setSquare_id(-1);
    }

    private boolean promotePawn(final ChessPiece pieceToPromote) {
        if(pieceToPromote.getPlayer()==1) {
            if(squares.get(pieceToPromote.getSquare_id()).coordY == 7) {
                AlertDialog.Builder builder = new AlertDialog.Builder(chessView.getContext());
                builder.setTitle(R.string.promotion_title)
                        .setItems(R.array.promotion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {///Queen Selected
                                    pieces.add(new ChessPieceQueen(chessView.getContext(), R.drawable.chess_qdt45, pieceToPromote.getX(), pieceToPromote.getY(), 1, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==1) {///Bishop Selected
                                    pieces.add(new ChessPieceBishop(chessView.getContext(), R.drawable.chess_bdt45, pieceToPromote.getX(), pieceToPromote.getY(), 1, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==2) {///Rook selected
                                    pieces.add(new ChessPieceRook(chessView.getContext(), R.drawable.chess_rdt45, pieceToPromote.getX(), pieceToPromote.getY(), 1, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==3) {///Knight selected
                                    pieces.add(new ChessPieceKnight(chessView.getContext(), R.drawable.chess_ndt45, pieceToPromote.getX(), pieceToPromote.getY(), 1, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                            }
                        });
                AlertDialog box = builder.create();
                box.show();
            }
        }
        if(pieceToPromote.getPlayer()==2) {
            if(squares.get(pieceToPromote.getSquare_id()).coordY == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(chessView.getContext());
                builder.setTitle(R.string.promotion_title)
                        .setItems(R.array.promotion, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0) {///Queen Selected
                                    pieces.add(new ChessPieceQueen(chessView.getContext(), R.drawable.chess_qlt45, pieceToPromote.getX(), pieceToPromote.getY(), 2, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==1) {///Bishop Selected
                                    pieces.add(new ChessPieceBishop(chessView.getContext(), R.drawable.chess_blt45, pieceToPromote.getX(), pieceToPromote.getY(), 2, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==2) {///Rook selected
                                    pieces.add(new ChessPieceRook(chessView.getContext(), R.drawable.chess_rlt45, pieceToPromote.getX(), pieceToPromote.getY(), 2, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                                if(which==3) {///Knight selected
                                    pieces.add(new ChessPieceKnight(chessView.getContext(), R.drawable.chess_nlt45, pieceToPromote.getX(), pieceToPromote.getY(), 2, pieceToPromote.getSquare_id()));
                                    pieceToPromote.setSquare_id(-1);
                                    chessView.invalidate();
                                }
                            }
                        });
                AlertDialog box = builder.create();
                box.show();
            }
        }

        return false;
    }

    public void onDone () {
        for (int i = 0; i < pieces.size(); i++) {

            pieces.get(i).setHasMoved(false);
        }
    }

}
