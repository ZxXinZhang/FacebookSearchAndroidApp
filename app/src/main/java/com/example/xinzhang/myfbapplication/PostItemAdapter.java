package com.example.xinzhang.myfbapplication;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by xinzhang on 4/17/17.
 */

public class PostItemAdapter extends ArrayAdapter<PostItem> {

    List<PostItem> postItems;
    LayoutInflater mInflater;
    public PostItemAdapter(@NonNull Context context, @NonNull List<PostItem> objects) {
        super(context, R.layout.post_item, objects);
        postItems = objects;
        mInflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.post_item,parent,false);
        }
        Log.i("jj", "getView: ");
        ImageView postPro = (ImageView) convertView.findViewById(R.id.postPro);
        TextView postName = (TextView) convertView.findViewById(R.id.postName);
        TextView postTime = (TextView) convertView.findViewById(R.id.postTime);
        TextView postCon = (TextView) convertView.findViewById(R.id.postCon);
        String proUrl = postItems.get(position).getProUrl();
        Picasso.with(getApplicationContext()).load(proUrl).into(postPro);
        String name = postItems.get(position).getPosterName();
        String time = postItems.get(position).getPostTime();
        String message = postItems.get(position).getPostContent();
        postName.setText(name);
        postTime.setText(time);
        postCon.setText(message);
        return convertView;
    }
}
