package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.tauth.Tencent;
import com.vincent.cloud.R;
import com.vise.log.ViseLog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * @name Login
 * @class name：com.vincent.cloud
 * @class describe
 * @anthor Vincent QQ:1032006226
 * @time 2017/7/19 10:34
 * @change
 * @chang time
 * @class describe
 */

public class SinaLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,SinaLoginActivity.class);
        activity.startActivity(intent);
    }

    private ImageView ivHead;

    /**
     * 新浪微博
     */
    private SsoHandler mSsoHandler;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sina_login);
        ivHead = (ImageView)findViewById(R.id.iv_head_url_sina);
        //新浪微博
        mSsoHandler = new SsoHandler(SinaLoginActivity.this);
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //授权方式有三种，第一种对客户端授权 第二种对Web短授权，第三种结合前两中方式
                mSsoHandler.authorize(new SelfWbAuthListener());
            }
        });
    }

    private class SelfWbAuthListener implements com.sina.weibo.sdk.auth.WbAuthListener{
        @Override
        public void onSuccess(final Oauth2AccessToken token) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAccessToken = token;
                    if (mAccessToken.isSessionValid()) {
                        // 显示 Token
//                        updateTokenView(false);
                        // 保存 Token 到 SharedPreferences
//                        AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                        ViseLog.d("授权成功...sina。。。");
                        //获取个人资料
                        //https://api.weibo.com/2/users/show.json
                        OkHttpUtils.get()
                                .url("https://api.weibo.com/2/users/show.json")
                                .addParams("access_token",mAccessToken.getToken())
                                .addParams("uid",mAccessToken.getUid())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        ViseLog.d("获取失败："+e.getMessage());
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        ViseLog.d("response:"+response);
                                        JSONObject jsonObject = JSON.parseObject(response);
                                        String headUrl = jsonObject.getString("profile_image_url");
                                        Glide.with(SinaLoginActivity.this).load(headUrl).into(ivHead);
                                    }
                                });

                    }
                }
            });
        }

        @Override
        public void cancel() {
            ViseLog.d("取消授权---sinal---");
        }

        @Override
        public void onFailure(WbConnectErrorMessage errorMessage) {
            Toast.makeText(SinaLoginActivity.this, errorMessage.getErrorMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //sina login
        if(mSsoHandler!=null){
            mSsoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }

}
