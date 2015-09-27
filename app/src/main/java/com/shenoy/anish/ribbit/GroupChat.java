package com.shenoy.anish.ribbit;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by ANISH on 9/26/2015.
 */
public class GroupChat {
    private ArrayList<String> mRecipients;
    private ArrayList<String> mAttendees;
    private ParseObject mGroupChat;
    private String mEventId;
    private ParseUser mCurrentUser;
    private String mName;


    public GroupChat(String eventId, String name){
        mName = name;
        mCurrentUser = ParseUser.getCurrentUser();
        mRecipients = new ArrayList<>();
        mAttendees = new ArrayList<>();
        mEventId = eventId;
        mGroupChat = new ParseObject(ParseConstants.CLASS_GROUP_CHAT);
        mGroupChat.put(ParseConstants.KEY_EVENT_ID, mEventId);
        mGroupChat.put(ParseConstants.KEY_NAME, mName);
        newAttendee(mCurrentUser.getObjectId(), mCurrentUser.getString(ParseConstants.KEY_FIRST_NAME));
    }

    public void newAttendee(String objectId, String firstName){
        mRecipients.add(objectId);
        mGroupChat.put(ParseConstants.KEY_ATTENDEES, mRecipients);
        mAttendees.add(firstName);
        mGroupChat.put(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME, mAttendees);
    }

    public void addAttendees(ArrayList<String> attendees, ArrayList<String> attendeesByFirstName){
        mRecipients.addAll(attendees);
        mGroupChat.put(ParseConstants.KEY_ATTENDEES, mRecipients);
        mAttendees.addAll(attendeesByFirstName);
        mGroupChat.put(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME, mAttendees);
    }

    public ParseObject getmGroupChat(){
        return mGroupChat;
    }
}
