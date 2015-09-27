package com.shenoy.anish.ribbit;

import android.content.Context;
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
 * Created by ANISH on 9/27/2015.
 */
public class GroupChatAdapter extends ArrayAdapter<ParseObject> {

        protected Context mContext;
        protected List<ParseObject> mChats;

        private static final String TAG = MessageAdapter.class.getSimpleName();

        public GroupChatAdapter(Context context, List<ParseObject> chats){
            super(context, R.layout.group_chat_item, chats);
            mContext = context;
            mChats = chats;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.group_chat_item, null);
                holder = new ViewHolder();
                holder.iconImageView = (ImageView) convertView.findViewById(R.id.group_picture_view);
                holder.nameLabel = (TextView) convertView.findViewById(R.id.group_name);
                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }

            ParseObject chat = mChats.get(position);

            holder.nameLabel.setText(chat.getString(ParseConstants.KEY_NAME));


            return convertView;
        }

        private static class ViewHolder{
            ImageView iconImageView;
            TextView nameLabel;
        }

        public void refill(List<ParseObject> events){
            mChats.clear();
            mChats.addAll(events);
            notifyDataSetChanged();
        }
    }

