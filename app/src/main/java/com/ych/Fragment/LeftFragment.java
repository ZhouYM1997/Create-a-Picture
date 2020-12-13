package com.ych.Fragment;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.ych.Activity.ChatActivity;
import com.ych.Activity.Login_Activity;
import com.ych.Activity.Register_Activity;
import com.ych.R;
import com.ych.unit.AnimUtil;
import com.ych.unit.MyDialog;
import com.ych.unit.Utils;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;


public class LeftFragment extends Fragment implements View.OnClickListener {

    View view1;
    Bitmap bitmaptest;
    private ImageView iv_add;
    private TextView tv_1, tv_2, tv_3, tv_4;
    private PopupWindow mPopupWindow;
    LinearLayout ly1;
    ImageView gallery;
    ImageView camera;
    private AnimUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    ImageView imageView ;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
     //摄像头与相册
     private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView iv_image;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    //-----------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_left, container, false);
        view1 = view;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        ly1=view1.findViewById(R.id.ly_left);

        //这是右上角的
        mPopupWindow = new PopupWindow(view.getContext());
        animUtil = new AnimUtil();
        imageView = view1.findViewById(R.id.iv_image);
        iv_add = view.findViewById(R.id.iv_add);
        iv_add.setOnClickListener(this);
        gallery=view.findViewById(R.id.gallery);
        camera=view.findViewById(R.id.camera);
        gallery.setOnClickListener(this);
        camera.setOnClickListener(this);
        return view;
    }

    //这是右上角的方法
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                showPop();
                toggleBright();
                break;
            case R.id.tv_1:
                mPopupWindow.dismiss();
                if(bitmaptest==null)
                {
                    Toast.makeText(view.getContext(), "还未上传图片识别", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final View viewemp=view;
                    MyDialog.show(this, "确定下载吗?", new MyDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                               String filename=saveImage(bitmaptest);
                                Toast.makeText(viewemp.getContext(), "下载成功", Toast.LENGTH_SHORT).show();}
                        }
                    );
                }
                break;
            case R.id.tv_2:
                mPopupWindow.dismiss();
                Intent intenttest=new Intent();
                intenttest.setClass(getContext(),ChatActivity.class);
                startActivity(intenttest);
                break;
            case R.id.gallery:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra("crop1", "true");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                this.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;
            case R.id.camera:
                // 激活相机
                Intent intent1 = new Intent("android.media.action.IMAGE_CAPTURE");
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    tempFile = new File(Environment.getExternalStorageDirectory(),
                            PHOTO_FILE_NAME);
                    // 从文件中创建uri
                    Uri uri = Uri.fromFile(tempFile);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
                this.startActivityForResult(intent1, PHOTO_REQUEST_CAREMA);
                break;
            default:
                break;
        }
    }

    private void showPop() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(view1.getContext()).inflate(R.layout.pop_add, null));
        // 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置pop透明效果
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));
        // 设置pop出入动画
        mPopupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        mPopupWindow.showAsDropDown(iv_add, -100, 0);
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });

        tv_1 = mPopupWindow.getContentView().findViewById(R.id.tv_1);
        tv_2 = mPopupWindow.getContentView().findViewById(R.id.tv_2);
        tv_1.setOnClickListener(this);
        tv_2.setOnClickListener(this);
    }

    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getActivity().getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
    //上面是右上角的方法
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        this.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    /*
     * 判断sdcard是否被挂载
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {


            // 从相册返回的数据
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                crop(uri);
            }

        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            // 从相机返回的数据
            if (hasSdcard()) {
                crop(Uri.fromFile(tempFile));
            } else {
                Toast.makeText(getActivity(), "未找到存储卡，无法存储照片！", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == PHOTO_REQUEST_CUT)  {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                Module module = null;
                bitmaptest=bitmap;
                String s=Utils.assetFilePath(getActivity(), "netAndroid.pt");
                System.out.println("s 的值为："+s);
                module = Module.load(s);
                // showing image on UI
                final float[] TORCHVISION_NORM_MEAN_RGB = new float[]{0.5f, 0.5f, 0.5f};
                final float[] TORCHVISION_NORM_STD_RGB = new float[]{0.5f, 0.5f, 0.5f};
                final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                        TORCHVISION_NORM_MEAN_RGB, TORCHVISION_NORM_STD_RGB);
                System.out.println(bitmap.getWidth());
                System.out.println(bitmap.getHeight());
                // running the model
                final Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();
                final float[] scores = outputTensor.getDataAsFloatArray();

                Log.i("scores[0]", String.valueOf(scores[0]));
                Log.i("scores[1]", String.valueOf(scores[1]));
                imageView.setImageBitmap(bitmap);
                if((scores[0]+scores[1]<4)&&(Math.abs(scores[0]-scores[1])<3))
                {
                    Toast.makeText(getActivity(), "请输入正确的猫猫和狗狗的图像", Toast.LENGTH_SHORT).show();
                }
                else if (scores[0]>scores[1])
                {
                    MyDialog.show(this, "经识别为猫", new MyDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                        }
                    });
                }
                else
                {
                    MyDialog.show(this, "经识别为狗", new MyDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                        }
                    });
                }




            }
            try {
                // 将临时文件删除
                tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
    public String saveImage(Bitmap bmp) {

        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "title", "description");
        Intent intent;

        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.jpg"))));
        return  null;
    }
}

