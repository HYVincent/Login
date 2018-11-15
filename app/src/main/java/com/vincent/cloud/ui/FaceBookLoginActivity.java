package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;
import com.tencent.tauth.Tencent;
import com.vincent.cloud.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name Login
 * @page com.vincent.cloud.ui
 * @class describe
 * @date 2018/11/7 18:14
 */
public class FaceBookLoginActivity extends AppCompatActivity {

    public static void actionStart(Activity activity) {
        Intent intent = new Intent(activity, FaceBookLoginActivity.class);
        activity.startActivity(intent);
    }

    private TextView tvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        initFaceBook();
        tvInfo = findViewById(R.id.facebook_info);
        findViewById(R.id.btn_facebook_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginForFaceBook();
            }
        });
    }


    // 参考地址:https://firebase.google.com/docs/auth/android/google-signin?authuser=0

    private static final String TAG = "FaceBook登录";
    private CallbackManager callbackManager;
    public ProfileTracker profileTracker;
    private static final String DEFAULT_REQUEST_VALUE = "id,name,link,gender,birthday,email,picture,locale,updated_time,timezone,age_range,first_name,last_name";
    List<String> permissions = Arrays.asList(" public_profile", "email");

    private void initFaceBook() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "onSuccess: FaceBook login successfully.get user info next.");
                        getUserInfo(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "onCancel: 登录取消");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d(TAG, "onError: 登录错误       【" + exception.getMessage() + "】");
                    }
                });
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
                onFacebookLogin();
            }
        };
    }

    /**
     * facebook登录结果回调
     */
    public void onFacebookLogin() {
        Profile currentProfile = Profile.getCurrentProfile();
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (currentProfile != null & accessToken != null) {
            String facebookToken = accessToken.getToken();
            String facebookId = currentProfile.getId();
            String facebookName = currentProfile.getName();
            Log.d(TAG, "onFacebookLogin: ->" + facebookToken + "," + facebookId + "," + facebookName);
        }

    }


    private void getUserInfo(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(org.json.JSONObject object, GraphResponse response) {
                //获取登录成功之后的用户详细信息
                Log.d(TAG, "onCompleted: 用户信息:" + object.toString());
                Log.d(TAG, "onCompleted: " + response.toString());
                tvInfo.setText(object.toString());
            }
        });
        //包入你想要得到的資料 送出request
        Bundle parameters = new Bundle();
        parameters.putString("fields", DEFAULT_REQUEST_VALUE);
        request.setParameters(parameters);
        request.executeAsync();
    }

    /**
     * 第三方登录 Facebook
     */
    private void loginForFaceBook() {
        Log.d(TAG, "loginForFaceBook: facebook login start ..");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if (!isLoggedIn) {
            LoginManager.getInstance().logInWithReadPermissions(this, permissions);
        } else {
            LoginManager.getInstance().logOut();
//            getUserInfo(accessToken);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvInfo.setText("退出登录");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
