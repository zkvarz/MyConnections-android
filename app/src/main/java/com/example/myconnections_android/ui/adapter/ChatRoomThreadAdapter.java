package com.example.myconnections_android.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.myconnections_android.R;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.model.Message;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kvarivoda on 06.04.2016.
 */
public class ChatRoomThreadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String userId;
    private int SELF = 100;
    private static String today;

    private Context mContext;
    private ArrayList<Message> messageArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView message, timestamp;

        public ViewHolder(View view) {
            super(view);
            message = (TextView) itemView.findViewById(R.id.message);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }
    }


    public ChatRoomThreadAdapter(Context mContext, ArrayList<Message> messageArrayList, String userId) {
        this.mContext = mContext;
        this.messageArrayList = messageArrayList;
        this.userId = userId;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        Logger.debug(getClass(), "onCreateViewHolder");
        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_self, parent, false);
        } else {
            // others message
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.chat_item_other, parent, false);
        }


        return new ViewHolder(itemView);
    }


    @Override
    public int getItemViewType(int position) {
        Message message = messageArrayList.get(position);

        Logger.debug(getClass(), "messageArrayList.size " + messageArrayList.size());

        if (message.getLoginResponse() != null && message.getLoginResponse().getId() != null) {
            if (message.getLoginResponse().getId().equals(userId)) {
                Logger.debug(getClass(), "SELF ");
                return SELF;
            }
        } else {
            Logger.debug(getClass(), "IMPOSSIBRU!11 loginReponse ID NULL");
            Logger.debug(getClass(), "message.getLoginResponse().getChatRoomId() " + message.getLoginResponse().getId());
        }
        Logger.debug(getClass(), "Not SELF ");
        return position;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Logger.debug(getClass(), "onBindViewHolder");

        Message message = messageArrayList.get(position);
        ((ViewHolder) holder).message.setText("DSgsd fgsdf gsg sdgsdg sdg sdgsdgsdggf fsdgsdg sd");
        ((ViewHolder) holder).message.setText(message.getMessage());

        Logger.debug(getClass(), "message JSON " + new Gson().toJson(message));

        String timestamp = getTimeStamp(message.getTimestamp());

        if (message.getLoginResponse().getPhone() != null)
            timestamp = message.getLoginResponse().getPhone() + ", " + timestamp;


        ((ViewHolder) holder).timestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public static String getTimeStamp(String dateStr) {
        String timestamp = "";
        Logger.debug(ChatRoomThreadAdapter.class.getClass(), "getTimeStamp ");
        if (dateStr != null && !dateStr.isEmpty()) {
            Logger.debug(ChatRoomThreadAdapter.class.getClass(), "dateStr " + dateStr);
            Date date = new Date();
            date.setTime(Long.valueOf(dateStr));
            String formattedDate = new SimpleDateFormat("MMM d, yyyy").format(date);
            timestamp = formattedDate;
        }

        return timestamp;
    }
}