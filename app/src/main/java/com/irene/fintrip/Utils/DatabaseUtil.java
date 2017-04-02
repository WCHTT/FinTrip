package com.irene.fintrip.Utils;

import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Irene on 2017/3/30.
 */

public class DatabaseUtil {


    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            Log.e("DEBUG", "new DB instance");
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance();
        }
        return mDatabase;
    }


}
