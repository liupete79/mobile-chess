package edu.msu.team17.chess.Cloud;

import android.util.Log;
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

    //PHP stuff

    private static final String NEW_USER_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/new-user.php";
    private static final String LOGIN_URL = "https://webdev.cse.msu.edu/~yunromi/cse476/project2/login.php";
    private static final String UTF8 = "UTF-8";

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
//    public InputStream openFromCloud(final String id) {
//        // Create a get query
//        String query = LOAD_URL + "?user=" + USER + "&magic=" + MAGIC + "&pw=" + PASSWORD + "&id=" + id;
//
//        try {
//            URL url = new URL(query);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            int responseCode = conn.getResponseCode();
//            if(responseCode != HttpURLConnection.HTTP_OK) {
//                return null;
//            }
//
//            InputStream stream = conn.getInputStream();
//            return stream;
//
//        } catch (MalformedURLException e) {
//            // Should never happen
//            return null;
//        } catch (IOException ex) {
//            return null;
//        }
//    }

    /**
     * Save a game state to the cloud.
     * This should be run in a thread.

     * @return true if successful
     */
    public boolean saveToCloud() {
        /*
        TODO: Figure out how to save game state and send to cloud.
         */
        return true;
    }

    public boolean newUser(String user, String password) {
        String query = NEW_USER_URL + "?user=" + user + "&password=" + password;
        Log.i("test", user);
        Log.i("test", password);
        Log.i("NEW_USER_URL", query);
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


    public boolean login(String user, String password) {
        // Lecture 11 & 12
        String query = LOGIN_URL + "?user=" + user + "&password=" + password;
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
}
