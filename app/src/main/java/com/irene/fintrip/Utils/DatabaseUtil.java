package com.irene.fintrip.Utils;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Irene on 2017/3/30.
 */

public class DatabaseUtil {


    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase getDatabase() {
        if (mDatabase == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            mDatabase = FirebaseDatabase.getInstance();
            //mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }


}
