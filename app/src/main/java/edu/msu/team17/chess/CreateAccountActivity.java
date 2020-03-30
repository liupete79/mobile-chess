package edu.msu.team17.chess;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import edu.msu.team17.chess.Cloud.Cloud;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }
    public void onCreate(View view) {
        EditText p1 = findViewById(R.id.username);
        EditText p2 = findViewById(R.id.password);
        EditText p3 = findViewById(R.id.passwordConfirm);
        final String username = p1.getText().toString();
        final String userpassword = p2.getText().toString();
        final String passwordConfirm = p3.getText().toString();
        String test = p2.getText().toString();
        String testConfirm = p3.getText().toString();
        if (test.equals(testConfirm)) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    Cloud cloud = new Cloud();
                    cloud.newUser(username, userpassword);
                }
            }).start();
            Toast.makeText(view.getContext(), R.string.acctCreated, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(view.getContext(), R.string.passwordsNotMatch, Toast.LENGTH_SHORT).show();

        }

    }

    public void onCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
