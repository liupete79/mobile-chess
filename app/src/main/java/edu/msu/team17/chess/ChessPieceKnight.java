package edu.msu.team17.chess;

import android.content.Context;

public class ChessPieceKnight extends ChessPiece {
    public ChessPieceKnight(Context context, int id, float initialX, float initialY,  int player, int square_id) {
        super(context, id, initialX, initialY, player, square_id);
    }

    @Override
    public String getType() {
        return "Knight";
    }
}
