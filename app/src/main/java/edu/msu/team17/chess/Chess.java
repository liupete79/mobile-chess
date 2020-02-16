package edu.msu.team17.chess;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.util.Log;
import android.app.AlertDialog;
import android.os.Bundle;

import java.util.*;

public class Chess {
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

    public Chess(Context context){
        whiteSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        blackSpace = new Paint(Paint.ANTI_ALIAS_FLAG);
        outline = new Paint(Paint.ANTI_ALIAS_FLAG);
        whiteSpace.setColor(0xffe8e8e8);
        blackSpace.setColor(0xff458c45);
        outline.setColor(0xff000000);
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

        for(int i = 0; i < 8; i++){
            int sTop = (i * squareSize) + boardMarginY;
            int sBot = (i * squareSize) + squareSize + boardMarginY;
            for(int j = 0; j < 8; j++){
                int sLeft = (j * squareSize) + boardMarginX;
                int sRight = (j * squareSize) + squareSize + boardMarginX;
                if (i % 2 == 0 && j % 2 == 0){
                    canvas.drawRect(sLeft, sTop, sRight, sBot, whiteSpace);
                } else if (i % 2 == 0 && j % 2 == 1){
                    canvas.drawRect(sLeft, sTop, sRight, sBot, blackSpace);
                } else if (i % 2 == 1 && j % 2 == 0){
                    canvas.drawRect(sLeft, sTop, sRight, sBot, blackSpace);
                } else if (i % 2 == 1 && j % 2 == 1){
                    canvas.drawRect(sLeft, sTop, sRight, sBot, whiteSpace);
                }
            }
        }

        canvas.save();
        canvas.translate(boardMarginX, boardMarginY);
        canvas.scale(boardSize, boardSize);
        canvas.restore();
    }
}
