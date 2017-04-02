package com.irene.fintrip;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Irene on 2017/4/2.
 */

public class FinTripApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}
