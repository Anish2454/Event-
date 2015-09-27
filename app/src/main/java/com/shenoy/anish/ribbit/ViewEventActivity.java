package com.shenoy.anish.ribbit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ViewEventActivity extends AppCompatActivity {

    private TextView attendeesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        attendeesView = (TextView)findViewById(R.id.attendeesList);

        String eventId = getIntent().getStringExtra("eventId");
        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_EVENT);
        query.whereEqualTo("objectId", eventId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                for(ParseObject event : list){
                    String textViewText = "";
                    List<String> names = event.getList(ParseConstants.KEY_ATTENDEES_BY_FIRST_NAME);
                    for(String name : names){
                        name += ", ";
                        textViewText += name;
                        attendeesView.setText(textViewText);
                    }
                }
            }
        });
    }

}
