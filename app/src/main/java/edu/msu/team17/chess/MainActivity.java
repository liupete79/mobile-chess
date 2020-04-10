package edu.msu.team17.chess;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.team17.chess.Cloud.Cloud;

public class MainActivity extends AppCompatActivity {
    boolean success = false;
    boolean saveLogin;
    private EditText p1;
    private EditText p2;
    private CheckBox rememberMe;
    private SharedPreferences loginPrefs;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p1 = findViewById(R.id.username);
        p2 = findViewById(R.id.password);
        rememberMe = (CheckBox)findViewById(R.id.rememberBox);
        loginPrefs = getSharedPreferences("logPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPrefs.edit();

        saveLogin = loginPrefs.getBoolean("saveLogin", false);
        if (saveLogin == true){
            p1.setText(loginPrefs.getString("username", ""));
            p2.setText(loginPrefs.getString("userpassword", ""));
            rememberMe.setChecked(true);
        }
    }

    public void onStartChess(View view) {
        //EditText p1 = findViewById(R.id.username);
        //EditText p2 = findViewById(R.id.password);
        final String username = p1.getText().toString();
        final String userpassword = p2.getText().toString();
        final View v = view;

        if (rememberMe.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", username);
            loginPrefsEditor.putString("userpassword", userpassword);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                Cloud cloud = new Cloud();
                final boolean ok = cloud.login(username, userpassword, "login");
                if (!ok) {
                    /*
                     * If we fail to save, display a toast
                     */
                    v.post(new Runnable() {

                        @Override
                        public void run() {

                            Toast.makeText(v.getContext(), R.string.logFailed, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Intent intent = new Intent(v.getContext(), MatchmakingActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                }
            }
        }).start();

    }
    public void onCreateAccount(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

}
