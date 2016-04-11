package com.example.myconnections_android.ui.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.models.Session;
import com.example.myconnections_android.api.requests.GetUsersRequest;
import com.example.myconnections_android.api.responses.UsersResponse;
import com.example.myconnections_android.core.structure.helpers.Logger;
import com.example.myconnections_android.core.structure.models.error.IError;
import com.example.myconnections_android.core.structure.requests.mock.ICallback;
import com.example.myconnections_android.preferences.AppPreference;
import com.example.myconnections_android.ui.adapter.UserListAdapter;

import java.util.ArrayList;

public class UsersActivity extends AppCompatActivity {

    private UserListAdapter adapter;
    private static ArrayList<UsersResponse> usersResponseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        ListView usersListView = (ListView) findViewById(R.id.usersListView);
        adapter = new UserListAdapter(this, usersResponseList);
        usersListView.setAdapter(adapter);
        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long l) {
                UsersResponse usersResponse = usersResponseList.get(position);
                Toast.makeText(getApplicationContext(), "yaa " + position + " " + usersResponse.getPhone(), Toast.LENGTH_LONG).show();

                Logger.debug(getClass(), "clicked id: " + usersResponseList.get(position).get_id());

                Intent intent = new Intent(UsersActivity.this, ChatRoomPrivateActivity.class);
                intent.putExtra("user_id", usersResponse.get_id());
                intent.putExtra("phone", usersResponse.getPhone());
                startActivity(intent);
            }
        });

        getUsersRequest();
    }

    private void getUsersRequest() {
        new GetUsersRequest(new Session(AppPreference.getInstance().getLoginResponse().getToken(), null), new ICallback<ArrayList<UsersResponse>>() {
            @Override
            public void onSuccess(ArrayList<UsersResponse> usersResponse) {
                Logger.debug(getClass(), "GetUsersRequest RESPONSE ");
                usersResponseList.clear();
                usersResponseList.addAll(usersResponse);
            }

            @Override
            public void onError(IError error) {
                Logger.debug(getClass(), "GetUsersRequest ERROR " + error.getErrorMessage());
                Toast.makeText(getApplicationContext(), "ERROR!" + error.getErrorMessage(), Toast.LENGTH_LONG).show();
            }
        }).execute();
    }
}
