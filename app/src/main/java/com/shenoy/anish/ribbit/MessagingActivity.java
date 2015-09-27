package com.shenoy.anish.ribbit;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.messaging.Message;
import com.sinch.android.rtc.messaging.MessageClient;
import com.sinch.android.rtc.messaging.MessageClientListener;
import com.sinch.android.rtc.messaging.MessageDeliveryInfo;
import com.sinch.android.rtc.messaging.MessageFailureInfo;
import com.sinch.android.rtc.messaging.WritableMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    private static final String TAG = MessagingActivity.class.getSimpleName();

    private ParseObject mGroupChat;
    private ArrayList<String> attendees;

    private EditText messageBodyField;
    private String messageBody;
    private String mChatId;
    private MessageService.MessageServiceInterface messageService;
    private String currentUserId;
    private ServiceConnection serviceConnection = new MyServiceConnection();
    private MyMessageClientListener messageClientListener;
    private ListView messagesList;
    private ChatMessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging);

        mChatId = getIntent().getStringExtra("objectId");

        attendees = new ArrayList<>();
        messageClientListener = new MyMessageClientListener();

        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);
        currentUserId = ParseUser.getCurrentUser().getObjectId();

        messagesList = (ListView) findViewById(R.id.listMessages);
        adapter = new ChatMessageAdapter(this);
        messagesList.setAdapter(adapter);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(ParseConstants.CLASS_GROUP_CHAT);
        query.whereEqualTo("objectId", mChatId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                mGroupChat = list.get(0);
                attendees.addAll((ArrayList<String>) mGroupChat.get(ParseConstants.KEY_ATTENDEES));
            }
        });

        messageBodyField = (EditText) findViewById(R.id.messageBodyField);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageBody = messageBodyField.getText().toString();
                if (messageBody.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a message", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.e(TAG, attendees.size() + "");
                messageService.sendMessage(attendees, messageBody+mChatId.toString());
                messageBodyField.setText("");

            }
        });


        ParseQuery<ParseObject> messageQuery = ParseQuery.getQuery("ParseMessage");
        messageQuery.whereEqualTo("chatId", mChatId);
        messageQuery.orderByAscending("createdAt");
        messageQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < messageList.size(); i++) {
                        WritableMessage message = new WritableMessage((ArrayList<String>) messageList.get(i).get("recipientIds"), messageList.get(i).getString("messageText"));
                        if (messageList.get(i).get("senderId").toString().equals(currentUserId)) {

                            adapter.addMessage(message, ChatMessageAdapter.DIRECTION_OUTGOING);
                        } else {
                            adapter.addMessage(message, ChatMessageAdapter.DIRECTION_INCOMING);
                        }
                    }
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        unbindService(serviceConnection);
        messageService.removeMessageClientListener(messageClientListener);
        super.onDestroy();
    }

    private class MyServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.addMessageClientListener(messageClientListener);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
        }
    }

    private class MyMessageClientListener implements MessageClientListener {

        //Notify the user if their message failed to send
        @Override
        public void onMessageFailed(MessageClient client, Message message,
                                    MessageFailureInfo failureInfo) {
            Toast.makeText(MessagingActivity.this, "Message failed to send.", Toast.LENGTH_LONG).show();
            Log.e(TAG, failureInfo.getSinchError().getMessage());
        }


        @Override
        public void onIncomingMessage(MessageClient client, Message message) {
            if (message.getTextBody().toLowerCase().contains(mChatId.toString())) {
                WritableMessage writableMessage = new WritableMessage(message.getRecipientIds(), message.getTextBody().replace(mChatId.toString(), ""));
                adapter.addMessage(writableMessage, ChatMessageAdapter.DIRECTION_INCOMING);
            }
            else{
                Log.e(TAG, "Didn't work");
            }
        }

        @Override
        public void onMessageSent(MessageClient client, Message message, String recipientId) {
            Log.e(TAG, "sent");
            //Display the message that was just sent
            //Later, I'll show you how to store the.        //message in Parse, so you can retrieve and
            //display them every time the conversation is opened
            final WritableMessage writableMessage = new WritableMessage(message.getRecipientIds().get(0), message.getTextBody().replace(mChatId.toString(), ""));
            //only add message to parse database if it doesn't already exist there
            ParseQuery<ParseObject> query = ParseQuery.getQuery("ParseMessage");
            query.whereEqualTo("sinchId", message.getMessageId());
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> messageList, com.parse.ParseException e) {
                    if (e == null) {
                        if (messageList.size() == 0) {
                            ParseObject parseMessage = new ParseObject("ParseMessage");
                            parseMessage.put("senderId", currentUserId);
                            parseMessage.put("recipientIds", writableMessage.getRecipientIds());
                            parseMessage.put("messageText", writableMessage.getTextBody());
                            parseMessage.put("sinchId", writableMessage.getMessageId());
                            parseMessage.put("chatId", mChatId);
                            parseMessage.saveInBackground();

                            adapter.addMessage(writableMessage, ChatMessageAdapter.DIRECTION_OUTGOING);
                        }
                    }
                }
            });


        }

        //Do you want to notify your user when the message is delivered?
        @Override
        public void onMessageDelivered(MessageClient client, MessageDeliveryInfo deliveryInfo) {
        }

        //Don't worry about this right now
        @Override
        public void onShouldSendPushData(MessageClient client, Message message, List<PushPair> pushPairs) {
        }
    }


}
