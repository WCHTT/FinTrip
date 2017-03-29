package com.irene.fintrip.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Irene on 2017/3/18.
 */

@IgnoreExtraProperties
public class Trip {
    private String buyListId;
    private String authorId;
    private String authorName;
    private String createdTime;
    private Long createdTimeStampOrder;
    private String listName;

    public Trip() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Trip(String buyListId, String authorId, String authorName, String createdTime, Long createdTimeStampOrder, String listName) {
        this.buyListId = buyListId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.createdTime = createdTime;
        this.createdTimeStampOrder = createdTimeStampOrder;
        this.listName = listName;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("buyListId", buyListId);
        result.put("authorId", authorId);
        result.put("authorName", authorName);
        result.put("createdTime",createdTime);
        result.put("createdTimeStampOrder", createdTimeStampOrder);
        result.put("listName", listName);

        return result;
    }

    public String getBuyListId() {
        return buyListId;
    }

    public void setBuyListId(String buyListId) {
        this.buyListId = buyListId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public Long getCreatedTimeStampOrder() {
        return createdTimeStampOrder;
    }

    public void setCreatedTimeStampOrder(Long createdTimeStampOrder) {
        this.createdTimeStampOrder = createdTimeStampOrder;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
