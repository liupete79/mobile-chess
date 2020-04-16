package edu.msu.team17.chess.Cloud;

import android.util.Log;

import edu.msu.team17.chess.ChessPiece;
import edu.msu.team17.chess.ChessView;
import edu.msu.team17.chess.Cloud.Models.LoadResult;
import edu.msu.team17.chess.Cloud.Models.SaveResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

import android.util.Pair;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "MAGIC";
    private static final String USER = "yunromi"; //might delete later
    private static final String PASSWORD = "cse476pw"; //might delete later
    private static final String PIECE_ID = "piece_id";
    private static final String SQUARE_ID = "square_id"; //might delete later
    private static final String PLAYER = "player"; //might delete later
    private static final String X = "x";
    private static final String Y = "y"; //might delete later
    private static final String TYPE = "type"; //might delete later

    private String opponent;

    //PHP stuff

    private static final String NEW_USER_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/new-user.php";
    private static final String LOGIN_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/login.php";
    private static final String MATCHMAKING_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/matchmaking.php";
    private static final String NEW_GAME_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/play-game.php";
    private static final String GAME_OVER_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/game-over.php";
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/";
    public static final String SAVE_PATH = "save-game.php";
    public static final String LOAD_PATH = "load-game.php";
    private static final String UTF8 = "UTF-8";
    public String getOpponent(){
        return opponent;
    }
    /**
     * Skip the XML parser to the end tag for whatever
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml)
            throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG &&
                tag != XmlPullParser.END_DOCUMENT);
    }


    /**
     * Open a connection to a hatting in the cloud.
     * @param id id for the hatting
     * @return reference to an input stream or null if this fails
     */

    /** DOES NOT WORK, THIS IS BASE FRAMEWORK
     * Current setup is to make an array list of pieces we then give to chess
     * Could also make a new chess object if that is easier, not sure
     */
    public ArrayList<ChessPiece> openFromCloud(final String id) {
        ChessService service = retrofit.create(ChessService.class);
        try {
            Response<LoadResult> response = service.loadChess(PIECE_ID, SQUARE_ID, PLAYER, X, Y, TYPE).execute();

            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("OpenFromCloud", "Failed to load hat, response code is = " + response.code());
                return null;
            }

            LoadResult result = response.body();
            if (result.getStatus().equals("yes")) {
                return result.getPieces();
            }

            Log.e("OpenFromCloud", "Failed to load hat, message is = '" + result.getMessage() + "'");
            return null;
        } catch (IOException e) {
            Log.e("OpenFromCloud", "Exception occurred while loading hat!", e);
            return null;
        }
    }

    public boolean gameOver(String user, String winner) {
        String query = "";
        query = GAME_OVER_URL + "?user=" + user + "&winner=" + winner;
        Log.i("test", user);
        Log.i("GAME_OVER_URL", query);
        InputStream stream = null;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "chess");

                String status = xmlR.getAttributeValue(null, "status");
                Log.i("status", status);
                if(status.equals("no")) {
                    Log.i("Inside status return", "False");
                    return false;
                }
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
    }


    public boolean login(String user, String password, String action) {
        String query = "";
        if (action == "new user") {
            query = NEW_USER_URL + "?user=" + user + "&password=" + password;
        } else // log in
        {
            query = LOGIN_URL + "?user=" + user + "&password=" + password;
        }
        Log.i("test", user);
        Log.i("test", password);
        Log.i("LOGIN_URL", query);
        InputStream stream = null;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "chess");

                String status = xmlR.getAttributeValue(null, "status");
                Log.i("status", status);
                if(status.equals("no")) {
                    Log.i("Inside status return", "False");
                    return false;
                }
                    // We are done
                } catch(XmlPullParserException ex) {
                    return false;
                } catch(IOException ex) {
                    return false;
                }

            } catch (MalformedURLException e) {
                return false;
            } catch (IOException ex) {
                return false;
            } finally {
                if(stream != null) {
                    try {
                        stream.close();
                    } catch(IOException ex) {
                        // Fail silently
                    }
                }
            }
            return true;
    }

    public boolean find_opponent(String user) {
        String query = MATCHMAKING_URL + "?user=" + user;
        Log.i("test", user);
        Log.i("MATCHMAKING_URL", query);
        InputStream stream = null;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "chess");
                String opponent = xmlR.getAttributeValue(null, "opponent");
                if (opponent != null) {
                    Log.i("opponent", opponent);
                    this.opponent = opponent;
                }
                else { Log.i("opponent", "No opponent"); }
                String status = xmlR.getAttributeValue(null, "status");
                Log.i("status", status);
                if(status.equals("no")) {
                    return false;
                }
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
    }

    // Run this function when "Play chess" button is pressed
    public boolean new_game(String user1, String user2) {
        String query = NEW_GAME_URL + "?user1=" + user1 + "&user2="+ user2;
        Log.i("user1", user1);
        Log.i("user2", user2);
        Log.i("MATCHMAKING_URL", query);
        InputStream stream = null;

        try {
            URL url = new URL(query);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            }

            stream = conn.getInputStream();

            /**
             * Create an XML parser for the result
             */
            try {
                XmlPullParser xmlR = Xml.newPullParser();
                xmlR.setInput(stream, UTF8);

                xmlR.nextTag();      // Advance to first tag
                xmlR.require(XmlPullParser.START_TAG, null, "chess");

                String status = xmlR.getAttributeValue(null, "status");
                Log.i("status", status);
                if(status.equals("no")) {
                    return false;
                }
                // We are done
            } catch(XmlPullParserException ex) {
                return false;
            } catch(IOException ex) {
                return false;
            }

        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        return true;
    }

    public boolean saveToCloud(String player1, String player2, ChessView view, String currPlayer) {
        player1 = player1.trim();
        if(player1.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "chess");

            xml.attribute(null, "user1", player1);
            xml.attribute(null, "user2", player2);
            xml.attribute(null, "currPlayer", currPlayer);

            view.saveXml(player1, player2, xml);


            xml.endTag(null, "chess");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        ChessService service = retrofit.create(ChessService.class);
        final String xmlStr = writer.toString();
        Log.i("XML", xmlStr);
        try {
            SaveResult result = service.saveChess(xmlStr).execute().body();
            if (result.getStatus() != null && result.getStatus().equals("yes")) {
                return true;
            }
            Log.e("SaveToCloud", "Failed to save, message = '" + result.getMessage() + "'");
            return false;
        } catch (IOException e) {
            Log.e("SaveToCloud", "Exception occurred while trying to save hat!", e);
            return false;
        }

        // Don't forget to remove the "return true" from the bottom of this function.
    }

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

}
