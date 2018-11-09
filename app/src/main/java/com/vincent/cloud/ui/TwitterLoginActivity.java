package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vincent.cloud.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name Login
 * @page com.vincent.cloud.ui
 * @class describe
 * @date 2018/11/9 14:35
 */
public class TwitterLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, TwitterLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_login);
        initTwitter();
        findViewById(R.id.btn_twitter_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                twitterAuthClient.authorize(TwitterLoginActivity.this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        TwitterAuthToken authToken = result.data.getAuthToken();
                        Toast.makeText(TwitterLoginActivity.this, "Twitter 登录成功-----" + authToken, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Toast.makeText(TwitterLoginActivity.this, "Twitter 登录失败-----", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private TwitterAuthClient twitterAuthClient;
    private static final String TWITTER_KEY = "";//Twitter平台的key
    private static final String TWITTER_SECRET = "";//Twitter平台的secret


    private void initTwitter() {
        // 初始化Twitter
        TwitterConfig config = new TwitterConfig.Builder(TwitterLoginActivity.this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);
        // 获取twitter 的客户端
        twitterAuthClient = new TwitterAuthClient();
    }

    //最后通过 twitterAuthClient 将登录结果返回。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TwitterAuthConfig.DEFAULT_AUTH_REQUEST_CODE) {
            twitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

}
