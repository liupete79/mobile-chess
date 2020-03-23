package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;

public abstract class ChessPiece {
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

    private boolean isWhite; ///true if on the white team

    private float prevX;

    private float prevY;

    private int player;

    private int square_id = -1;

    private boolean firstMove = true;

    private boolean hasMoved = false;


    public ChessPiece(Context context, int id, float initialX,  float initialY, int player, int square_id) {
    piece = BitmapFactory.decodeResource(context.getResources(), id);
        this.id = id;
        this.x = initialX;
        this.y = initialY;
        this.player = player;
        this.square_id = square_id;
    }

    /**
     * Draw the chess piece
     * @param canvas Canvas we are drawing on
     * @param boardSize Size we draw the chess in pixels
     */
    public void draw(Canvas canvas, int boardSize,
    int marginX, int marginY, float scaleFactor){

        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + x * boardSize, marginY + y * boardSize);

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

    public int getPlayer() {return this.player;}

    public boolean isFirstMove() { return firstMove; }

    public void setFirstMove(boolean firstMove) { this.firstMove = firstMove; }

    public int getSquare_id(){return this.square_id;}

    public void setSquare_id(int square_id){this.square_id = square_id;}

    public float getWidth() {
        return piece.getWidth();
    }

    public float getHeight() {
        return piece.getHeight();
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

        // Make relative to the location and size to the piece size
        int pX = (int)((testX - x) * chessSize / scaleFactor) +
                piece.getWidth() / 2;
        int pY = (int)((testY - y) * chessSize / scaleFactor) +
                piece.getHeight() / 2;        //Log.i("pX", String.valueOf(pX));

        if(pX < 0 || pX >= piece.getWidth() ||
                pY < 0 || pY >= piece.getHeight()) {
            return false;
        }

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

    public abstract void is_valid_move();

    public boolean getHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
}

