package com.hfad.exploreshopping;

import android.util.Log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Cache {

    public static final String TAG = "Cache";
    public final static long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
    public static HashMap<String, List<ClickedItem>> viewedItemsMap = new HashMap<>();
    public static HashMap<String, Date> userIdToDate = new HashMap<>();

    public static void updateViewedItemsMap(String userId, List<ClickedItem> clickedItems){
        Log.d(TAG,"the updateViewedItemsMap method is being called");
        viewedItemsMap.put(userId,clickedItems);
    }

    public static List<ClickedItem> getUserItemsViewed(String userId){

        Log.d(TAG,"The getUserItemsViewed method is being called");
        if (viewedItemsMap.containsKey(userId) && userIdToDate.containsKey(userId)){
            Date twentyFourHoursAgo = new Date(System.currentTimeMillis() - (MILLIS_PER_DAY));

            if (userIdToDate.get(userId).compareTo(twentyFourHoursAgo) < 0){
                Log.d(TAG,"The time the user last requested something from the cache is past twenty-four hours");
                return null;
            }

            Log.d(TAG,"The method to fetch data from the Cache is being called bro");
            return viewedItemsMap.get(userId);

        }
        Log.d(TAG,"The user does not exist in the map or the user Id does not exist in the map for the date and time");
        return null;
    }

    public static void updateUserToIdMap(String userId){
        userIdToDate.put(userId,new Date(System.currentTimeMillis()));
    }

}
