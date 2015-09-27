package com.shenoy.anish.ribbit;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class RecipientsActivity extends ListActivity {

    public static final String TAG = ProfileActivity.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    private String mDescription;
    private Boolean mIsOpen;
    private ArrayList<String> mAttendeesByIds;
    private ArrayList<String> mAttendeesByFirstName;

    protected static MenuItem mSendMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDescription = getIntent().getStringExtra("description");
        mIsOpen = getIntent().getBooleanExtra("isOpen", false);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipients);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    }


    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = mFriendsRelation.getQuery();
        query.addAscendingOrder(ParseConstants.KEY_USERNAME);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> list, ParseException e) {
                if (e == null) {
                    mFriends = list;

                    String[] usernames = new String[mFriends.size()];
                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.get(ParseConstants.KEY_FIRST_NAME).toString();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            getListView().getContext(),
                            android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                } else {
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getListView().getContext());
                    builder.setMessage(e.getMessage());
                    builder.setTitle(R.string.error_title);
                    builder.setPositiveButton("ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recipients, menu);
        mSendMenuItem = menu.getItem(0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_send:
                mAttendeesByIds = getRecipientIds();
                mAttendeesByFirstName = getAttendeesNames();
                ParseObject message = createEvent();
                if (message == null) {

                } else {
                    send(message);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ParseObject createEvent() {
        Event event = new Event(mDescription, mIsOpen);
        event.addAttendees(mAttendeesByIds, mAttendeesByFirstName);
        return event.getEvent();
    }

    private ParseObject createChat(String eventId){
        GroupChat chat = new GroupChat(eventId);
        chat.addAttendees(mAttendeesByIds, mAttendeesByFirstName);
        return chat.getmGroupChat();
    }

    protected ArrayList<String> getRecipientIds() {
        ArrayList<String> recipientIds = new ArrayList<>();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (getListView().isItemChecked(i)) {
                recipientIds.add(mFriends.get(i).getObjectId());
            }
        }
        return recipientIds;
    }

    protected ArrayList<String> getAttendeesNames() {
        ArrayList<String> attendeesNames = new ArrayList<>();
        for (int i = 0; i < getListView().getCount(); i++) {
            if (getListView().isItemChecked(i)) {
                attendeesNames.add(mFriends.get(i).getString(ParseConstants.KEY_FIRST_NAME));
            }
        }
        return attendeesNames;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        mSendMenuItem.setVisible(true);
    }

    protected void send(ParseObject message) {
        try{
            message.save();
            createChat(message.getObjectId()).save();
        }
        catch(ParseException e){
            e.printStackTrace();
        }
    }
}
