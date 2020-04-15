package edu.msu.team17.chess.Cloud;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import edu.msu.team17.chess.Cloud.Models.LoadResult;
import edu.msu.team17.chess.Cloud.Models.SaveResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static edu.msu.team17.chess.Cloud.Cloud.SAVE_PATH;
import static edu.msu.team17.chess.Cloud.Cloud.LOAD_PATH;


public interface ChessService {

    @GET(LOAD_PATH)
    Call<LoadResult> loadChess(
            @Query("piece_id") String piece_Id,
            @Query("square_id") String square_id,
            @Query("player") String player,
            @Query("x") String x,
            @Query("y") String y,
            @Query("type") String type
    );

    @FormUrlEncoded
    @POST(SAVE_PATH)
    Call<SaveResult> saveChess(@Field("xml") String xmlData);
}

