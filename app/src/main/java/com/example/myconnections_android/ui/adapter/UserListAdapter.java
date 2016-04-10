package com.example.myconnections_android.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myconnections_android.R;
import com.example.myconnections_android.api.responses.UsersResponse;

import java.util.List;

/**
 * Created by kvarivoda on 10.09.2015.
 */
public class UserListAdapter extends ArrayAdapter<UsersResponse> {

    private List<UsersResponse> list;
    private Context context;

    public UserListAdapter(Context context,
                           List<UsersResponse> list) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public UsersResponse getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewAlbum;
        TextView textViewDate;
        ImageView imageViewSong;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.user_item, parent, false);
            holder = new ViewHolder();
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.textViewAuthor = (TextView) convertView.findViewById(R.id.textViewAuthor);
            holder.textViewAlbum = (TextView) convertView.findViewById(R.id.textViewAlbum);
            holder.textViewDate = (TextView) convertView.findViewById(R.id.textViewDate);
            holder.imageViewSong = (ImageView) convertView.findViewById
                    (R.id.imageViewSong);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UsersResponse songItem = getItem(position);

        if (songItem != null) {

            if (!TextUtils.isEmpty(songItem.getPhone())) {
                holder.textViewTitle.setText(songItem.getPhone());
            } else {
                holder.textViewTitle.setText("");
            }

        }
        return convertView;
    }

}