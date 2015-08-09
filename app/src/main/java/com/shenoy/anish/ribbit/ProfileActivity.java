package com.shenoy.anish.ribbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ProfileActivity extends AppCompatActivity {

    private ParseUser mUser;
    private String objectId;

    protected TextView mFirstNameText;
    protected TextView mLastNameText;
    protected TextView mCityText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mFirstNameText = (TextView) findViewById(R.id.firstNameText);
        mLastNameText = (TextView) findViewById(R.id.lastNameText);
        mCityText = (TextView) findViewById(R.id.cityText);

        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.getInBackground(objectId, new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    mUser = parseUser;

                    mFirstNameText.setText(getResources().getString(R.string.firstNamePlaceholder) + " " + mUser.get(ParseConstants.KEY_FIRST_NAME).toString());
                    mLastNameText.setText(getResources().getString(R.string.lastNamePlaceholder) + " " + mUser.get(ParseConstants.KEY_LAST_NAME).toString());
                    mCityText.setText(getResources().getString(R.string.cityPlaceholder) + " " + mUser.get(ParseConstants.KEY_CITY).toString());
                } else {
                    mFirstNameText.setText("Test");
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
