package com.shenoy.anish.ribbit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANISH on 8/7/2015.
 */
public class InboxFragment extends ListFragment {

    private List<ParseObject> mEvents;
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    private List<ParseUser> mFriends;
    private ParseUser mCurrentUser;

    private static final String TAG = InboxFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);

        mEvents = new ArrayList<>();

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrieveEvents();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        retrieveEvents();
    }

    private void retrieveEvents() {
        mCurrentUser = ParseUser.getCurrentUser();
        ParseRelation<ParseUser> friendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATION);
        ParseQuery<ParseUser> query = friendsRelation.getQuery();
        ParseQuery<ParseObject> eventQuery = new ParseQuery<>(ParseConstants.CLASS_EVENT);
        eventQuery.whereMatchesKeyInQuery(ParseConstants.KEY_CREATOR_ID, "objectId", query);
        eventQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
                if (e == null) {
                    mEvents = list;
                    if (getListView().getAdapter() == null) {
                        MessageAdapter adapter = new MessageAdapter(
                                getListView().getContext(),
                                mEvents);
                        setListAdapter(adapter);
                    } else {
                        ((MessageAdapter) getListView().getAdapter()).refill(mEvents);
                    }
                }
            }
        });

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ParseObject event = mEvents.get(position);
        Intent intent = new Intent(getActivity(), ViewEventActivity.class);
        intent.putExtra("eventId", event.getObjectId());
        startActivity(intent);
    }
}
