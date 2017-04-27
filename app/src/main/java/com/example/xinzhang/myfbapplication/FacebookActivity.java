package com.example.xinzhang.myfbapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import static android.widget.Toast.LENGTH_SHORT;


public class FacebookActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        Intent intent = getIntent();
        String detinfo = intent.getStringExtra(DetailsActivity.fb_MESSAGE);
        String id = detinfo.split("\\|")[0];
        String name = detinfo.split("\\|")[1];
        String prourl = detinfo.split("\\|")[2];
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(name)
                    .setContentDescription(
                            "FB SEARCH FROM CSCI571...")
                    .setContentUrl(Uri.parse("https://graph.facebook.com/v2.8/"+id+"?fields=id,name,picture.width(700).height(700),albums.limit(5){name,photos.limit(2){name, picture}},posts.limit(5)&access_token=EAADZC4CjNFs8BABvhpgGESth5W156YEYZAAzM5EjIEOi7tq2RqGr4imqwn7gXxByF6KykHmVf66cbkHVgvkkhcXjlq0uE9nM15DlumefLkzkVmY8ZCW3hkEE0SrNQGZAFBMXtmEBAmQj4Vyxo11mPGKC2m6ZBJ1YZD"))
                    .setImageUrl(Uri.parse(prourl))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("Facebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            Log.d("Facebook", String.format("Error: %s",error.toString()));
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            Log.d("HelloFacebook", "Success!");
            Toast.makeText(getApplicationContext(),"Please enter a keyword!",Toast.LENGTH_SHORT).show();
        }
    };


}
