package de.goddchen.android.rxfiredroid.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.goddchen.android.rxfiredroid.auth.RxFireDroidAuth;
import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxFireDroidAuth.signIn("goddchen+test@gmail.com", "password")
                .subscribe(
                        firebaseUser -> {
                            Log.d("Test", "Logged in");
                        },
                        throwable -> {
                            Log.e("Test", "Error logging in", throwable);
                        });
    }
}
