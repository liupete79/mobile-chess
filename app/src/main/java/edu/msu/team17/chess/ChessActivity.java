package edu.msu.team17.chess;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

public class ChessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_chess);

        if(bundle != null) {
            // We have saved state
            getChessView().loadInstanceState(bundle);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chess_menu, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);

        getChessView().saveInstanceState(bundle);
    }

    /**
     * Get the chess view
     * @return ChessView reference
     */
    private ChessView getChessView() {
        return (ChessView)this.findViewById(R.id.chessView);
    }
}
