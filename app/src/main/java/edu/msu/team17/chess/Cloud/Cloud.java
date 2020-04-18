package edu.msu.team17.chess.Cloud;

import android.util.Log;

import edu.msu.team17.chess.ChessPiece;
import edu.msu.team17.chess.ChessView;
import edu.msu.team17.chess.Cloud.Models.SaveResult;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressWarnings("deprecation")
public class Cloud {
    private static final String MAGIC = "MAGIC";
    private static final String USER = "yunromi"; //might delete later
    private static final String PASSWORD = "cse476pw"; //might delete later

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
    public ArrayList<ChessPiece> openFromCloud(final String player1, ArrayList<ChessPiece> oldState) {
            String query = BASE_URL + LOAD_PATH + "?user=" + player1;
            Log.i("test", player1);
            Log.i("LOAD_URL", query);
            InputStream stream = null;
            int index = 0;

            try {
                URL url = new URL(query);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                int responseCode = conn.getResponseCode();
                if(responseCode != HttpURLConnection.HTTP_OK) {
                    return null;
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


                    while (xmlR.next() != XmlPullParser.END_TAG) {
                        if (xmlR.getEventType() != XmlPullParser.START_TAG) {
                            continue;
                        }
                        String name = xmlR.getName();
                        // Starts by looking for the entry tag
                        if (name.equals("chessgame")) {
                            xmlR.require(XmlPullParser.START_TAG, null, "chessgame");
                            int square_id = Integer.parseInt(xmlR.getAttributeValue(null, "square_id"));
                            int piece_id = Integer.parseInt(xmlR.getAttributeValue(null, "piece_id"));
                            int player = Integer.parseInt(xmlR.getAttributeValue(null, "player"));
                            float x = Float.parseFloat(xmlR.getAttributeValue(null, "x"));
                            float y = Float.parseFloat(xmlR.getAttributeValue(null, "y"));
                            String type = xmlR.getAttributeValue(null, "type");
                            oldState.get(index).setSquare_id(square_id);
                            oldState.get(index).setX(x);
                            oldState.get(index).setY(y);
                            Log.i("square_id", String.valueOf(square_id));
                            Log.i("piece_id", String.valueOf(piece_id));
                            Log.i("player", String.valueOf(player));
                            Log.i("x", String.valueOf(x));
                            Log.i("y", String.valueOf(y));
                            Log.i("type", type);
                            xmlR.nextTag();      // Advance to first tag
                            index++;
                        } else {
                        }
                    }


                    if(status.equals("no")) {
                        Log.i("Inside status return", "False");
                        return null;
                    }


                    // We are done
                } catch(XmlPullParserException ex) {
                    return null;
                } catch(IOException ex) {
                    return null;
                }

            } catch (MalformedURLException e) {
                return null;
            } catch (IOException ex) {
                return null;
            } finally {
                if(stream != null) {
                    try {
                        stream.close();
                    } catch(IOException ex) {
                        // Fail silently
                    }
                }
            }
            return oldState;
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
