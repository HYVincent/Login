package com.vincent.cloud.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.vincent.cloud.R;
import com.vincent.cloud.auth.BaseUIListener;
import com.vincent.cloud.base.Config;
import com.vincent.cloud.util.DialogUtils;

/**
 * @name Login
 * @class name：com.vincent.cloud.ui
 * @class describe
 * @anthor Vincent QQ:1032006226
 * @time 2017/7/21 10:41
 * @change
 * @chang time
 * @class describe
 */

public class SharedActivity extends AppCompatActivity {

    public static void actionStart(Activity activity){
        Intent intent = new Intent(activity,SharedActivity.class);
        activity.startActivity(intent);
    }

    //WX
    private IWXAPI api;
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private static final int requestCode = 100;
    //QQ
    private Tencent mTencent;
    private BaseUIListener listener;
    /**sina微博*/
    public static final int SHARE_CLIENT = 1;
    private WbShareHandler shareHandler;
    private int mShareType = SHARE_CLIENT;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
        initWx();
        initQQ();
        initSina();
        findViewById(R.id.button4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogUtils.showSharedDialog(SharedActivity.this, "https://www.baidu.com", new DialogUtils.SharedListener() {
                    @Override
                    public void sharedToWXFriend(String content) {
                        sendToWeiXin("微信分享",content,"我是微信分享",BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),0);
                    }

                    @Override
                    public void sharedToWXFriendCircle(String content) {
                        sendToWeiXin("微信分享",content,"我是微信分享",BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),1);
                    }

                    @Override
                    public void sharedToWXCollect(String content) {
                        sendToWeiXin("微信分享",content,"我是微信分享",BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher),2);
                    }

                    @Override
                    public void sharedToQQFriend(String content) {
                        shareToQQ("恒富威停车","描述","https://www.baidu.com",Config.APP_LOGO_URL,2);
                    }

                    @Override
                    public void sharedToQQZone(String content) {
                        shareToQQ("恒富威停车","描述","https://www.baidu.com",Config.APP_LOGO_URL,1);
                    }

                    @Override
                    public void sharedToSina(String content) {
                        shareToSina();
                    }
                });
            }
        });
    }


    private void initSina() {
        shareHandler = new WbShareHandler(SharedActivity.this);
        shareHandler.registerApp();
    }



    private void initQQ() {
        // Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
// 其中APP_ID是分配给第三方应用的appid，类型为String。
        mTencent = Tencent.createInstance(Config.QQ_LOGIN_APP_ID, SharedActivity.this);
// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
// 初始化视图
        listener = new BaseUIListener(SharedActivity.this);
    }

    private void initWx() {
        api = WXAPIFactory.createWXAPI(SharedActivity.this, Config.APP_ID_WX,true);
        // 将该app注册到微信
        api.registerApp(Config.APP_ID_WX);
    }

    /**
     * @param title       分享的标题
     * @param openUrl     点击分享item打开的网页地址url
     * @param description 网页的描述
     * @param icon        分享item的图片
     * @param requestCode 0表示为分享到微信好友  1表示为分享到朋友圈 2表示微信收藏
     */
    public void sendToWeiXin(String title, String openUrl, String description, Bitmap icon, int requestCode) {
        //初始化一个WXWebpageObject对象，填写url
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = openUrl;
        //Y用WXWebpageObject对象初始化一个WXMediaMessage对象，填写标题、描述
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;//网页标题
        msg.description = description;//网页描述
        msg.setThumbImage(icon);
        //构建一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "supplier";
        req.message = msg;
        req.scene = requestCode;
        api.sendReq(req);
    }

    private void shareToSina() {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        weiboMessage.textObject = getTextObj();
        weiboMessage.imageObject = getImageObj();
        weiboMessage.mediaObject = getWebpageObj();
        shareHandler.shareMessage(weiboMessage, mShareType == SHARE_CLIENT);
    }

    /**
     * 创建图片消息对象。
     * @return 图片消息对象。
     */
    private ImageObject getImageObj() {
        ImageObject imageObject = new ImageObject();
        Bitmap  bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        imageObject.setImageObject(bitmap);
        return imageObject;
    }

    /**
     * 创建文本消息对象。
     * @return 文本消息对象。
     */
    private TextObject getTextObj() {
        TextObject textObject = new TextObject();
        textObject.text = getSharedText();
        textObject.title = "xxxx";
        textObject.actionUrl = "http://www.baidu.com";
        return textObject;
    }

    /**
     * 获取分享的文本模板。
     */
    private String getSharedText() {
        String text = "70岁富翁拥有两座城堡 抛弃伴侣想找年轻女子生育继承人#UC头条# http://s4.uczzd.cn/ucnews/news?app=ucnews-iflow&aid=17106278003054546318&cid=100&zzd_from=ucnews-iflow&uc_param_str=dndseiwifrvesvntgipf&rd_type=share&pagetype=share&btifl=100&sdkdeep=2&sdksid=55a8f9d3-223a-d5a7-812c-c3af12bb6430&sdkoriginal=55a8f9d3-223a-d5a7-812c-c3af12bb6430";
        return text;
    }

    /**
     * 创建多媒体（网页）消息对象。
     *
     * @return 多媒体（网页）消息对象。
     */
    private WebpageObject getWebpageObj() {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title ="测试title";
        mediaObject.description = "测试描述";
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = "http://news.sina.com.cn/c/2013-10-22/021928494669.shtml";
        mediaObject.defaultText = "Webpage 默认文案";
        return mediaObject;
    }

    /**
     * 官方参考文档地址：http://wiki.open.qq.com/index.php?title=Android_API%E8%B0%83%E7%94%A8%E8%AF%B4%E6%98%8E&=45038#1.13_.E5.88.86.E4.BA.AB.E6.B6.88.E6.81.AF.E5.88.B0QQ.EF.BC.88.E6.97.A0.E9.9C.80QQ.E7.99.BB.E5.BD.95.EF.BC.89
     *
     * @param title       分享的内容title
     * @param openUrl     点击分享内容打开的地址
     * @param description 分享item的描述信息 分享的消息摘要，最长40个字。
     * @param imgUrl      分享item的图片地址
     * @param shareType   1分享到QQ空间 2分享到QQ好友
     */
    public void shareToQQ(String title, String description, String openUrl, String imgUrl, int shareType) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);//消息类型 图文用默认的
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);//标题
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, description);//描述信息
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, openUrl);//这条分享消息被好友点击后的跳转URL。
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imgUrl);//分享图片的URL或者本地路径
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, shareType);//分享额外选项，两种类型可选（默认是不隐藏分享到QZone按钮且不自动打开分享到QZone的对话框）：QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN，分享时自动打开分享到QZone的对话框。QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE，分享时隐藏分享到QZone按钮
        mTencent.shareToQQ(SharedActivity.this, params, listener);
    }

}
