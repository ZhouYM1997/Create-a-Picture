package com.ych;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.ych.unit.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Tencent mTencent;
    private TextView mTvResult;
    private ImageView mIvIcon;
    private Context mContext;
    Button getmBtn;
    private String nicknameString;
    private String openidString;
    private Bitmap bitmap;
    private String APP_ID = "101918841";
    BaseUiListener BaseUiListener;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        nicknameString = response.getString("nickname");
                        mTvResult.setText("QQ名:" + nicknameString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (msg.what == 1) {
                Bitmap bitmap = (Bitmap) msg.obj;
                mIvIcon.setImageBitmap(bitmap);
            }
        }
    };
    private Button mBtn_login;
    private Button mBtn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getmBtn=findViewById(R.id.login);
        getmBtn.setOnClickListener(this);
        mContext = this;
        initUI();
    }

    /**
     * 初始化UI控件
     */
    private void initUI() {
        mTvResult = (TextView) findViewById(R.id.tv_result);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login:
                mTencent = Tencent.createInstance(APP_ID, mContext);
                mTencent.login(this, "all", BaseUiListener);
                break;
        }
    }


    /**
     * 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法，
     * onComplete  onCancel onError
     * 分别表示第三方登录成功，取消 ，错误。
     */
    private class BaseUiListener implements IUiListener {


        @Override
        public void onComplete(Object response) {
            try {
                //获得的数据是JSON格式的，获得你想获得的内容
                //如果你不知道你能获得什么，看一下下面的LOG
                Log.i("openidString1", "openidString1");
                openidString = ((JSONObject) response).getString("openid");

            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("eeeeee", String.valueOf(e));
            }
            /**到此已经获得OpneID以及其他你想获得的内容了
             QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
             sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
             如何得到这个UserInfo类呢？  */
            QQToken qqToken = mTencent.getQQToken();
            UserInfo info = new UserInfo(mContext, qqToken);
            //这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
            info.getUserInfo(new IUiListener() {
                public void onComplete(final Object response) {
                    mHandler.obtainMessage(0, response).sendToTarget();
                    /**由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接在mHandler里进行操作*/
                    new Thread() {
                        @Override
                        public void run() {

                            JSONObject json = (JSONObject) response;
                            Log.e(TAG, json.toString());
                            try {
                                bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mHandler.obtainMessage(1, bitmap).sendToTarget();
                        }
                    }.start();
                }

                @Override
                public void onError(UiError uiError) {

                }

                public void onCancel() {

                }

                @Override
                public void onWarning(int i) {

                }
            });
        }
        @Override
        public void onError(UiError arg0) {

        }
        @Override
        public void onCancel() {
        }

        @Override
        public void onWarning(int i) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data,BaseUiListener);

    }
}