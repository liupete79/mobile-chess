package edu.msu.team17.chess.Cloud.Models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

import edu.msu.team17.chess.ChessPiece;

/**
 * I did not delete anything, but commented out the hat functions and such
 * I am assuming we want to return a pieces array, could also return a chess object if easier
 */
@Root(name = "hatter")
public class LoadResult {
    @Attribute(name = "msg", required = false)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    private ArrayList<ChessPiece> pieces;
    @Attribute
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

///    @Element(name = "hatting", type = Hat.class, required = false)
///    private Hat hat;
    public ArrayList<ChessPiece> getPieces(){ return pieces ;}

 ///   public Hat getHat() {
 ///       return hat;
 ///   }

 ///   public void setHat(Hat hat) {
 ///       this.hat = hat;
 ///   }

    public LoadResult() {}

    public LoadResult(String status, String msg/*, Hat hat*/) {
        this.status = status;
        this.message = msg;
        ///this.hat = hat;
    }
}