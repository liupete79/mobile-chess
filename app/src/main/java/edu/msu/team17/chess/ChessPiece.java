package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public class ChessPiece {
    /**
     * The image for the actual piece.
     */
    private Bitmap piece;

    /**
     * x location.
     * We use relative x locations in the range 0-1 for the center
     * of the puzzle piece.
     */
    private float x = 0;

    /**
     * y location
     */
    private float y = 0;

    /**
     * The chess piece ID
     */
    private int id;

    private boolean isWhite;

    private boolean Captured = false;

    private boolean firstMove = true;


    public ChessPiece(Context context, int id) {
        piece = BitmapFactory.decodeResource(context.getResources(), id);
        this.id = id;
    }

    /**
     * Draw the chess piece
     * @param canvas Canvas we are drawing on
     * @param boardSize Size we draw the chess in pixels
     */
    public void draw(Canvas canvas, int boardSize,
    int locX, int locY, float scaleFactor){
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(locX + boardSize/16, boardSize/16 + locY );

        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);
        canvas.restore();
    }

    public int getId() {
        return id;
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

    public boolean getWhite(){
        return isWhite;
    }

    public void setWhite(boolean white){
        this.isWhite = white;
    }

    /**
     * Test to see if we have touched a chess piece
     * @param testX X location as a normalized coordinate (0 to 1)
     * @param testY Y location as a normalized coordinate (0 to 1)
     * @param chessSize the size of the chess in pixels
     * @param scaleFactor the amount to scale a piece by
     * @return true if we hit the piece
     */
    public boolean hit(float testX, float testY,
                       int chessSize, float scaleFactor) {

        Log.i("scaleFactor", String.valueOf(scaleFactor));
        Log.i("ID", String.valueOf(this.id));
        Log.i("x", String.valueOf(this.x));
        Log.i("y", String.valueOf(this.y));
        Log.i("testX", String.valueOf(testX));
        Log.i("testY", String.valueOf(testY));
        Log.i("width", String.valueOf(piece.getWidth()));
        Log.i("height", String.valueOf(piece.getHeight()));
        // Make relative to the location and size to the piece size
        int pX = (int)(((testX - x) * chessSize * scaleFactor) +
                piece.getWidth() * scaleFactor / 2);
        int pY = (int)(((testY - y) * chessSize * scaleFactor) +
                piece.getHeight() * scaleFactor  / 2);
        Log.i("pX", String.valueOf(pX));
        Log.i("pY", String.valueOf(pY));

        if(pX < 0 || pX >= piece.getWidth()* scaleFactor ||
                pY < 0 || pY >= piece.getHeight()* scaleFactor) {
            Log.i("Hit", "Not hit");
            return false;
        }

        Log.i("Hit", "Hit");
        // We are within the rectangle of the piece.
        // Are we touching actual picture?
        return (piece.getPixel(pX, pY) & 0xff000000) != 0;
    }

    /**
     * Move the puzzle piece by dx, dy
     * @param dx x amount to move
     * @param dy y amount to move
     */
    public void move(float dx, float dy) {
        x += dx;
        y += dy;
    }
}

