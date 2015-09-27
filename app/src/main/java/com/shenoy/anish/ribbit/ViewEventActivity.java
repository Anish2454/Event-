package com.shenoy.anish.ribbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private TextView mDescriptionView;
    private Button mJoinButton;
    private ParseObject mEvent;
    private ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        mDescriptionView = (TextView)findViewById(R.id.attendeesList);
        mJoinButton = (Button)findViewById(R.id.joinEventButton);
        mCurrentUser = ParseUser.getCurrentUser();

        String eventId = getIntent().getStringExtra("eventId");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_EVENT);
        query.whereEqualTo("objectId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mEvent = list.get(0);
                mDescriptionView.setText(mEvent.getString(ParseConstants.KEY_DESCRIPTION));
                if(mEvent.getBoolean(ParseConstants.KEY_ISOPEN)) {
                    mJoinButton.setVisibility(View.VISIBLE);
                }
            }
        });
            mJoinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEvent.addUnique(ParseConstants.KEY_ATTENDEES, mCurrentUser.getObjectId());
                    mEvent.addUnique(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME, mCurrentUser.getString(ParseConstants.KEY_FIRST_NAME));
                    mEvent.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            finish();
                        }
                    });
                }
            });
    }


}
