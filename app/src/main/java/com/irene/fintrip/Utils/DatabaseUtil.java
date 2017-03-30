package com.irene.fintrip.Utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Irene on 2017/3/30.
 */

public class DatabaseUtil {


    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }


}
