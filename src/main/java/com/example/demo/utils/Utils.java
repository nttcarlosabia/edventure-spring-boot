package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.example.demo.models.Event;
import com.example.demo.models.User;

public class Utils {

    private Utils() {}

    public static String getCurrentDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }

    public static void updateFollowersHistory(Event event, int followersCount) {
        Map<String, Integer> followersHistoryMap = event.getFollowersHistory();
        String currentDateAsString = Utils.getCurrentDateAsString();
        if (followersHistoryMap.containsKey(currentDateAsString)) {
            int existingFollowersCount = followersHistoryMap.get(currentDateAsString);
            int newFollowersCount = followersCount;
            if (existingFollowersCount > followersCount) {
                newFollowersCount = existingFollowersCount - 1;
            }
            followersHistoryMap.put(currentDateAsString, newFollowersCount);
            
        } else {
            followersHistoryMap.put(currentDateAsString, followersCount);
        }

        event.setFollowersHistory(followersHistoryMap);
    }

    public static void updateUserFields(User existingUser, User request) {
        if (request.getNickname() != null) {
            existingUser.setNickname(request.getNickname());
        }
        if (request.getName() != null) {
            existingUser.setName(request.getName());
        }
        if (request.getLastname() != null) {
            existingUser.setLastname(request.getLastname());
        }
        if (request.getShowEmail() != null) {
            existingUser.setShowEmail(request.getShowEmail());
        }
        if (request.getEmail() != null) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getAvatar() != null) {
            existingUser.setAvatar(request.getAvatar());
        }
        if (request.getLoggedDate() != null) {
            existingUser.setLoggedDate(request.getLoggedDate());
        }
    }
    public static void updateEventFields(Event existingEvent, Event request) {
        if (request.getUserOwner() != null) {
            existingEvent.setUserOwner(request.getUserOwner());
        }
        if (request.getName() != null) {
            existingEvent.setName(request.getName());
        }
        if (request.getType() != null) {
            existingEvent.setType(request.getType());
        }
        if (request.getDescription() != null) {
            existingEvent.setDescription(request.getDescription());
        }
        if (request.getImage() != null) {
            existingEvent.setImage(request.getImage());
        }
        if (request.getDate() != null) {
            existingEvent.setDate(request.getDate());
        }
        if (request.getAddress() != null) {
            existingEvent.setAddress(request.getAddress());
        }
        if (request.getPlaceId() != null) {
            existingEvent.setPlaceId(request.getPlaceId());
        }
        if (request.getAssistants() != null) {
            existingEvent.setAssistants(request.getAssistants());
        }
        if (request.getFollowersHistory() != null) {
            existingEvent.setFollowersHistory(request.getFollowersHistory());
        }
    }
}
