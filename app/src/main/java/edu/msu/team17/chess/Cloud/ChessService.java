package edu.msu.team17.chess.Cloud;

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

    @FormUrlEncoded
    @POST(SAVE_PATH)
    Call<SaveResult> saveChess(@Field("xml") String xmlData);
}

