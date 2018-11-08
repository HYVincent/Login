package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.vincent.cloud.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name Login
 * @page com.vincent.cloud.ui
 * @class describe
 * @date 2018/11/7 18:14
 */
public class FaceBookLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,FaceBookLoginActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
    }
}
