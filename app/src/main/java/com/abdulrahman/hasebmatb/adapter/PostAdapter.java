package com.abdulrahman.hasebmatb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.model.PostModel;

import java.util.List;

/**
 * Created by abdulrahman on 12/28/16.
 */

public class PostAdapter  extends BaseAdapter {
    List<PostModel> postModels;
    Context context;
    LayoutInflater layoutInflater;

    public PostAdapter(List<PostModel> postModels, Context context) {
        this.postModels = postModels;
        this.context = context;
    }
    @Override
    public int getCount() {
        return postModels.size();
    }

    @Override
    public Object getItem(int i) {
        return postModels.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
     layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       view= layoutInflater.inflate(R.layout.post_row,viewGroup,false);
        TextView userNameTxt= (TextView) view.findViewById(R.id.username);
        TextView locationTxt= (TextView) view.findViewById(R.id.location);
        TextView timeAgoTxt= (TextView) view.findViewById(R.id.timeAgo);
        TextView postTxt= (TextView) view.findViewById(R.id.post_textview);
        userNameTxt.setText(postModels.get(i).getUserName());
        locationTxt.setText(postModels.get(i).getLocation());
        timeAgoTxt.setText(postModels.get(i).getPostTime());
        postTxt.setText(postModels.get(i).getPostText());
        return view;
    }
}
