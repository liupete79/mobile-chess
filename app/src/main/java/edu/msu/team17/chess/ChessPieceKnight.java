package edu.msu.team17.chess;

import android.content.Context;

public class ChessPieceKnight extends ChessPiece {
    public ChessPieceKnight(Context context, int id, float initialX, float initialY,  String player, int square_id) {
        super(context, id, initialX, initialY, player, square_id);
    }

    @Override
    public void is_valid_move() {

    }
}
