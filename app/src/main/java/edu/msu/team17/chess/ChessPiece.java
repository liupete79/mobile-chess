package edu.msu.team17.chess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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


    public ChessPiece(Context context, int id) {
        piece = BitmapFactory.decodeResource(context.getResources(), id);
    }

    /**
     * Draw the chess piece
     * @param canvas Canvas we are drawing on
     * @param marginX Margin x value in pixels
     * @param marginY Margin y value in pixels
     * @param boardSize Size we draw the chess in pixels
     */
    public void draw(Canvas canvas, int marginX, int marginY, int boardSize,
    int locX, int locY, float scaleFactor){
        canvas.save();

        // Convert x,y to pixels and add the margin, then draw
        canvas.translate(marginX + locX + 5, boardSize/16 + locY );

        canvas.scale(scaleFactor, scaleFactor);

        // This magic code makes the center of the piece at 0, 0
        canvas.translate(-piece.getWidth() / 2f, -piece.getHeight() / 2f);

        // Draw the bitmap
        canvas.drawBitmap(piece, 0, 0, null);
        canvas.restore();
    }
}
