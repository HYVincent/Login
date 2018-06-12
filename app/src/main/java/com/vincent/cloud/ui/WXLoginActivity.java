package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.vincent.cloud.R;
import com.vincent.cloud.base.App;
import com.vincent.cloud.base.Config;
import com.vise.log.ViseLog;

/**
 * @name Login
 * @class name：com.vincent.cloud
 * @class describe
 * @anthor Vincent
 * @time 2017/7/19 10:34
 * @change
 * @chang time
 * @class describe
 */

public class WXLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,WXLoginActivity.class);
        activity.startActivity(intent);
    }

    private ImageView ivHead;

    /**
     * 微信登录相关
     */
    private IWXAPI api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_login);
        ivHead = (ImageView)findViewById(R.id.iv_wx_head);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, Config.APP_ID_WX,true);
        //将应用的appid注册到微信
        api.registerApp(Config.APP_ID_WX);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
                req.state = "wechat_sdk_微信登录";
                api.sendReq(req);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(WXLoginActivity.this).load(App.getShared().getString("headUrl","")).into(ivHead);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 0){
            String headUrl = data.getStringExtra("headUrl");
            ViseLog.d("url:"+headUrl);
            Glide.with(WXLoginActivity.this).load(headUrl).into(ivHead);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
