package de.goddchen.android.rxfiredroid.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.goddchen.android.rxfiredroid.database.RxFireDroidDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RxFireDroidDatabase.setValue("test", "value")
                .doOnComplete(() ->
                        RxFireDroidDatabase.getValues("test")
                                .subscribe(dataSnapshot ->
                                        Log.d("Test", dataSnapshot.getValue(String.class))))
                .subscribe();
    }
}
