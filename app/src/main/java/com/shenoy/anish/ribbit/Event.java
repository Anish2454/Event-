package com.shenoy.anish.ribbit;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by ANISH on 9/19/2015.
 */
public class Event {
    ParseUser user = ParseUser.getCurrentUser();
    ParseObject mEvent;
    ArrayList<String> mRecipients;
    ArrayList<String> mAttendees;
    String mName;
    public Event(String name, Boolean isOpen){
        mName = name;
        mRecipients = new ArrayList<>();
        mAttendees = new ArrayList<>();
        mEvent = new ParseObject(ParseConstants.CLASS_EVENT);
        //mEvent.put(ParseConstants.KEY_LOCATION, geoPoint);
        mEvent.put(ParseConstants.KEY_CREATOR, user.getUsername());
        mEvent.put(ParseConstants.KEY_NAME, mName);
        mEvent.put(ParseConstants.KEY_ISOPEN, isOpen);
        mEvent.put(ParseConstants.KEY_CREATOR_IS_FACEBOOK, user.getBoolean(ParseConstants.KEY_IS_FACEBOOK));
        if(user.getBoolean(ParseConstants.KEY_IS_FACEBOOK)) {
            mEvent.put(ParseConstants.KEY_CREATOR_FACEBOOK_PROFILE_ID, user.getString(ParseConstants.KEY_FACEBOOK_PROFILE_ID));
        }

        newAttendee(user.getObjectId(), user.getString(ParseConstants.KEY_FIRST_NAME));
    }

    public void newAttendee(String objectId, String firstName){
        mRecipients.add(objectId);
        mEvent.put(ParseConstants.KEY_ATTENDEES, mRecipients);
        mAttendees.add(firstName);
        mEvent.put(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME, mAttendees);
    }

    public void addAttendees(ArrayList<String> attendees, ArrayList<String> attendeesByFirstName){
        mRecipients.addAll(attendees);
        mEvent.put(ParseConstants.KEY_ATTENDEES, mRecipients);
        mAttendees.addAll(attendeesByFirstName);
        mEvent.put(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME, mAttendees);
    }

    public ArrayList<String> getmRecipients() {
        return mRecipients;
    }

    public ParseObject getEvent(){
        return mEvent;
    }


}
