package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.vincent.cloud.R;
import com.vincent.cloud.base.Config;
import com.vincent.cloud.entity.QQUser;
import com.vise.log.ViseLog;

import org.json.JSONException;


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

public class QQLoginActivity extends AppCompatActivity {

    private Tencent mTencent;

    private UserInfo userInfo;

    private BaseUiListener listener = new BaseUiListener();

    private String QQ_uid;//qq_openid

    private ImageView ivHead;

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,QQLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qq_login);
        ivHead = (ImageView)findViewById(R.id.iv);
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
        // 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, this.getApplicationContext());
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViseLog.d("开始QQ登录..");
                if (!mTencent.isSessionValid())
                {
                    //注销登录 mTencent.logout(this);
                    mTencent.login(QQLoginActivity.this, "all", listener);
                }
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = JSONObject.parseObject(String.valueOf(msg.obj));
                ViseLog.d("UserInfo:"+ JSON.toJSONString(response));
                QQUser user=JSONObject.parseObject(response.toJSONString(),QQUser.class);
                if (user!=null) {
                    ViseLog.d("userInfo:昵称："+user.getNickname()+"  性别:"+user.getGender()+"  地址："+user.getProvince()+user.getCity());
                    ViseLog.d("头像路径："+user.getFigureurl_qq_2());
                    Glide.with(QQLoginActivity.this).load(user.getFigureurl_qq_2()).into(ivHead);
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode,resultCode,data,new BaseUiListener());
    }


    class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            ViseLog.d("授权:"+o.toString());
            try {
                org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                initOpenidAndToken(jsonObject);
                updateUserInfo();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            ViseLog.d("onError:code:" + e.errorCode + ", msg:"
                    + e.errorMessage + ", detail:" + e.errorDetail);
        }
        @Override
        public void onCancel() {
            ViseLog.d("onCancel");
        }
    }

    /**
     * 获取登录QQ腾讯平台的权限信息(用于访问QQ用户信息)
     * @param jsonObject
     */
    public void initOpenidAndToken(org.json.JSONObject jsonObject) {
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
                QQ_uid = openId;
            }
        } catch(Exception e) {
        }
    }

    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {
                @Override
                public void onError(UiError e) {
                }
                @Override
                public void onComplete(final Object response) {
                    Message msg = new Message();
                    msg.obj = response;
                    ViseLog.e("................"+response.toString());
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                }
                @Override
                public void onCancel() {
                    ViseLog.d("登录取消..");
                }
            };
            userInfo = new UserInfo(this, mTencent.getQQToken());
            userInfo.getUserInfo(listener);
        }
    }

}
