package com.shenoy.anish.ribbit;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.Date;

public class CreateEventActivity extends AppCompatActivity {

    private static final String TAG = CreateEventActivity.class.getSimpleName();

    private EditText mDescription;
    private CheckBox mCheckbox;
    private Button mCreateEvent;
    private String mEventDescription;
    private Event mEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mDescription = (EditText) findViewById(R.id.descriptionEditText);
        mCheckbox = (CheckBox) findViewById(R.id.openEventCheckBox);
        mCreateEvent = (Button) findViewById(R.id.createeventButton);

        mCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEventDescription = mDescription.getText().toString().trim();
                Boolean isOpen = mCheckbox.isChecked();
                if (mCheckbox.isChecked()) {
                    mEvent = new Event(mEventDescription, isOpen);
                    saveEvent(mEvent.getEvent());

                } else {
                    Intent intent = new Intent(CreateEventActivity.this, RecipientsActivity.class);
                    intent.putExtra("description", mEventDescription);
                    intent.putExtra("isOpen", isOpen);
                    startActivity(intent);
                }
            }
        });
    }

    protected void saveEvent(ParseObject message) {
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Toast.makeText(CreateEventActivity.this, "Message Sent!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event, menu);
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
