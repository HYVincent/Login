package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.vincent.cloud.R;

/**
 * @author Vincent Vincent
 * @version v1.0
 * @name Login
 * @page com.vincent.cloud.ui
 * @class describe
 * @date 2018/11/8 11:35
 */
public class GoogleLoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,GoogleLoginActivity.class);
        activity.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);
        initGoogle();
        tvMsg = findViewById(R.id.tv_login_msg);
        findViewById(R.id.btn_google_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    private TextView tvMsg;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 101;

    private void initGoogle() {
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .build();



        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)/* FragmentActivity *//* OnConnectionFailedListener */
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    private void handleSignInResult(GoogleSignInResult result){
        Log.i("robin", "handleSignInResult:" + result.isSuccess());
        if(result.isSuccess()){
            Log.i("robin", "成功");
            GoogleSignInAccount acct = result.getSignInAccount();
            if(acct!=null){
                Log.i("robin", "用户名是:" + acct.getDisplayName());
                Log.i("robin", "用户email是:" + acct.getEmail());
                Log.i("robin", "用户头像是:" + acct.getPhotoUrl());
                Log.i("robin", "用户Id是:" + acct.getId());//之后就可以更新UI了
                Log.i("robin", "用户IdToken是:" + acct.getIdToken());
                tvMsg.setText("用户名是:" + acct.getDisplayName()+"\n用户email是:" + acct.getEmail()+"\n用户头像是:" + acct.getPhotoUrl()+ "\n用户Id是:" + acct.getId()+"\n用户IdToken是:" + acct.getIdToken());
            }
        }else{
            tvMsg.setText("登录失败");
            Log.i("robin", "没有成功"+result.getStatus());
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("robin","google登录-->onConnected,bundle=="+bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i("robin","google登录-->onConnectionSuspended,i=="+i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("robin","google登录-->onConnectionFailed,connectionResult=="+connectionResult);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("robin", "requestCode==" + requestCode + ",resultCode==" + resultCode + ",data==" + data);
        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


}
