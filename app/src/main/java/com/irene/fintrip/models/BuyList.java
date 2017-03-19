package com.irene.fintrip.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irene on 2017/3/18.
 */

@IgnoreExtraProperties
public class BuyList {
    private String uid;
    private String authorId;
    private String authorName;

    public BuyList() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public BuyList(String uid, String authorId, String authorName) {
        this.uid = uid;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("authorId", authorId);
        result.put("authorName", authorName);

        return result;
    }
}
