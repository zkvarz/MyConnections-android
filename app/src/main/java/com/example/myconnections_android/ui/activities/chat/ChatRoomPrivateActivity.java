package com.example.myconnections_android.ui.activities.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.models.ChatRoom;
import com.example.myconnections_android.api.models.MessageSend;
import com.example.myconnections_android.api.requests.GetChatRoomMessagesRequest;
import com.example.myconnections_android.api.requests.GetPrivateChatRoomRequest;
import com.example.myconnections_android.api.requests.SendMessageRequest;
import com.example.myconnections_android.api.responses.LoginResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.gcm.Config;
import com.example.myconnections_android.gcm.NotificationUtils;
import com.example.myconnections_android.model.Message;
import com.example.myconnections_android.preferences.AppPreference;
import com.example.myconnections_android.ui.adapter.ChatRoomThreadAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ChatRoomPrivateActivity extends AppCompatActivity {

    private String chatRoomId;
    private String chatUserId;
    private RecyclerView recyclerView;
    private ChatRoomThreadAdapter mAdapter;
    private ArrayList<Message> messageArrayList;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private EditText inputMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputMessage = (EditText) findViewById(R.id.message);
        Button btnSend = (Button) findViewById(R.id.btn_send);

        Intent intent = getIntent();
        chatUserId = intent.getStringExtra("user_id");
        String title = intent.getStringExtra("phone");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setTitle(title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Logger.debug(getClass(), "chatRoomId " + chatRoomId);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        messageArrayList = new ArrayList<>();

        // self user id is to identify the message owner
        String selfUserId = AppPreference.getInstance().getLoginResponse().getId();

        mAdapter = new ChatRoomThreadAdapter(this, messageArrayList, selfUserId);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        registerBroadcastReceiver();

        getPrivateChatRoomRequest();
    }

    private void registerBroadcastReceiver() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push message is received
                    handlePushNotification(intent);
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // registering the receiver for new notification
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Handling new push message, will add the message to
     * recycler view and scroll it to bottom
     */
    private void handlePushNotification(Intent intent) {
        Logger.debug(getClass(), "handlePushNotification");
        Message message = intent.getParcelableExtra("message");
        String chatRoomId = intent.getStringExtra("chat_room_id");

        Logger.debug(getClass(), "handlePushNotification chat_room_id" + chatRoomId);

        if (message != null && chatRoomId != null) {
            messageArrayList.add(message);
            mAdapter.notifyDataSetChanged();
            if (mAdapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, mAdapter.getItemCount() - 1);
            }
        }
    }

    /**
     * Posting a new message in chat room
     * will make an http call to our server. Our server again sends the message
     * to all the devices as push notification
     */
    private void sendMessage() {
        final String message = this.inputMessage.getText().toString().trim();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(getApplicationContext(), "Enter a message", Toast.LENGTH_SHORT).show();
            return;
        }

        this.inputMessage.setText("");

        if (chatRoomId != null) {
            MessageSend messageSend = new MessageSend(AppPreference.getInstance().getLoginResponse().getToken(), message, chatRoomId);
            messageSend.setToUserId(chatUserId);
            Logger.debug(getClass(), "JSON MessageSend: "  + new Gson().toJson(messageSend));
            new SendMessageRequest(messageSend, new ICallback<Message>() {
                @Override
                public void onSuccess(Message message) {
                    Logger.debug(getClass(), "MESSAGE SENT!");

                    Logger.debug(getClass(), "SendMessageRequest message JSON " + new Gson().toJson(message));
                    messageArrayList.add(message);
                    mAdapter.notifyDataSetChanged();
                    if (mAdapter.getItemCount() > 1) {
                        // scrolling to bottom of the recycler view
                        recyclerView.getLayoutManager().scrollToPosition(mAdapter.getItemCount() - 1);
                    }
                }

                @Override
                public void onError(IError error) {
                    Logger.debug(getClass(), "MESSAGE SEND ERROR " + error.getErrorMessage());
                    Toast.makeText(getApplicationContext(), "Unable to send message to our sever. " /*+ obj.getJSONObject("error").getString("message")*/, Toast.LENGTH_LONG).show();
                }
            }).execute();
        }


    }

    /**
     * Fetching all the messages of a single chat room
     */
    private void fetchPrivateChatThread() {

        ChatRoom chatRoom = new ChatRoom(AppPreference.getInstance().getLoginResponse().getToken(), chatRoomId);
        Logger.debug(getClass(), "JSON SEND: " + new Gson().toJson(chatRoom));

        new GetChatRoomMessagesRequest(chatRoom, new ICallback<ArrayList<Message>>() {
            @Override
            public void onSuccess(ArrayList<Message> messagesArray) {
                Logger.debug(getClass(), "GetChatRoomMessagesRequest RESPONSE ");

                for (int i = 0; i < messagesArray.size(); i++) {
                    messageArrayList.add(messagesArray.get(i));

                }

                mAdapter.notifyDataSetChanged();
                if (mAdapter.getItemCount() > 1) {
                    recyclerView.getLayoutManager().scrollToPosition(mAdapter.getItemCount() - 1);
                }

            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "GetChatRoomMessagesRequest ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "ERROR!" + error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();

    }

    private void getPrivateChatRoomRequest() {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken(AppPreference.getInstance().getLoginResponse().getToken());
        loginResponse.setId(chatUserId);

        Logger.debug(getClass(), "GetPrivateChatRoomRequest JSON SEND: " + new Gson().toJson(loginResponse));
        new GetPrivateChatRoomRequest(loginResponse, new ICallback<ChatRoom>() {
            @Override
            public void onSuccess(ChatRoom chatRoom) {
                Logger.debug(getClass(), "GetChatRoomMessagesRequest RESPONSE ");
                Logger.debug(getClass(), "chatRoom id  " + chatRoom.getChatRoomId());
                chatRoomId = chatRoom.getChatRoomId();

                fetchPrivateChatThread();
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "GetChatRoomMessagesRequest ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "ERROR!" + error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }

}