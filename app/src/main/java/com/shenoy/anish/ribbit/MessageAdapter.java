package com.shenoy.anish.ribbit;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.parse.ParseObject;

import java.util.List;

/**
 * Created by ANISH on 8/9/2015.
 */
public class MessageAdapter extends ArrayAdapter<ParseObject> {

    protected Context mContext;
    protected List<ParseObject> mEvents;

    private static final String TAG = MessageAdapter.class.getSimpleName();

    public MessageAdapter(Context context, List<ParseObject> events){
        super(context, R.layout.message_item, events);
        mContext = context;
        mEvents = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.message_item, null);
            holder = new ViewHolder();
            holder.iconImageView = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.friendName = (TextView) convertView.findViewById(R.id.friendName);
            holder.profilePictureView = (ProfilePictureView) convertView.findViewById(R.id.view);
            holder.message = (TextView) convertView.findViewById(R.id.txtStatusMsg);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.timestamp);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }

        ParseObject event = mEvents.get(position);
        if(event.getBoolean(ParseConstants.KEY_CREATOR_IS_FACEBOOK)) {
            holder.profilePictureView.setProfileId(event.getString(ParseConstants.KEY_CREATOR_FACEBOOK_PROFILE_ID));
            holder.iconImageView.setVisibility(View.INVISIBLE);
        }
        else{
            holder.profilePictureView.setVisibility(View.INVISIBLE);
            holder.iconImageView.setVisibility(View.VISIBLE);
        }

        holder.friendName.setText(event.getString(ParseConstants.KEY_NAME));
        holder.message.setText(event.getString(ParseConstants.KEY_DESCRIPTION));
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                event.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        holder.timeStamp.setText(timeAgo);


        return convertView;
    }

    private static class ViewHolder{
        ImageView iconImageView;
        TextView friendName;
        ProfilePictureView profilePictureView;
        TextView message;
        TextView timeStamp;
    }

    public void refill(List<ParseObject> events){
        mEvents.clear();
        mEvents.addAll(events);
        notifyDataSetChanged();
    }
}
