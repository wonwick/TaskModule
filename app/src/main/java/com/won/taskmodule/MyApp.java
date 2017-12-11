package com.won.taskmodule;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Oshan Wickramaratne on 2017-12-11.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}