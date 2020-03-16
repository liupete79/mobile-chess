package edu.msu.team17.chess;

import android.content.Context;

public class ChessPieceQueen extends ChessPiece {
    public ChessPieceQueen(Context context, int id, float initialX, float initialY,  int player, int square_id, boolean white) {
        super(context, id, initialX, initialY, player, square_id, white);
    }

    @Override
    public void is_valid_move() {

    }
}
