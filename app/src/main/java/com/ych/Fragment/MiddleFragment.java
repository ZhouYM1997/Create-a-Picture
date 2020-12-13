package com.ych.Fragment;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ych.Activity.ChatActivity;
import com.ych.R;
import com.ych.unit.AnimUtil;
import com.ych.unit.MyDialog;
import org.tensorflow.contrib.android.TensorFlowInferenceInterface;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class MiddleFragment extends Fragment implements View.OnClickListener {
    ImageView gallery1;
    ImageView camera1;
    View view1;
    String modelPath=null;
    private ImageView iv_add1;
    private int i=0;
    LinearLayout ly;
    Bitmap rawBitmap1;
    private TextView tv_1, tv_2, tv_3, tv_4;
    private PopupWindow mPopupWindow;
    private AnimUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    private static final long DURATION = 500;
    private static final float START_ALPHA = 0.7f;
    private static final float END_ALPHA = 1f;
    private int[] intValues = new int[65536];
    private float[] floatValues = new float[196608];
    Bitmap bm1;
    //摄像头与相册
    private static final int PHOTO_REQUEST_CAREMA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private ImageView imageView;
    /* 头像名称 */
    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private File tempFile;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_middle, container, false);
        view1=view;
        gallery1 = view.findViewById(R.id.gallery1);
        camera1 = view.findViewById(R.id.camera1);
        imageView = (ImageView) view.findViewById(R.id.style);
        gallery1.setOnClickListener(this);
        camera1.setOnClickListener(this);
        mPopupWindow = new PopupWindow(view.getContext());
        animUtil = new AnimUtil();
        iv_add1 = view.findViewById(R.id.iv_add1);
        iv_add1.setOnClickListener(this);
        ly=view.findViewById(R.id.ly);
        imageView1=view.findViewById(R.id.style1);
        imageView2=view.findViewById(R.id.style2);
        imageView3=view.findViewById(R.id.style3);
        imageView4=view.findViewById(R.id.style4);
        imageView5=view.findViewById(R.id.style5);
        imageView6=view.findViewById(R.id.style6);
        imageView1.setOnClickListener(this);
        imageView2.setOnClickListener(this);
        imageView3.setOnClickListener(this);
        imageView4.setOnClickListener(this);
        imageView5.setOnClickListener(this);
        imageView6.setOnClickListener(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        return view;
    }

    public Bitmap stylizeImage(Bitmap bitmap, String modelPath) {
        bitmap = Bitmap.createScaledBitmap(bitmap, 256, 256, false);
        bitmap.getPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        TensorFlowInferenceInterface tensorFlowInferenceInterface = new TensorFlowInferenceInterface(getContext().getAssets(), modelPath);

        int a;
        for (a = 0; a < this.intValues.length; ++a) {
            int val = this.intValues[a];
            this.floatValues[a * 3 + 0] = (float) (val >> 16 & 255);
            this.floatValues[a * 3 + 1] = (float) (val >> 8 & 255);
            this.floatValues[a * 3 + 2] = (float) (val & 255);
        }

        Log.w("tensor", "Width: " + bitmap.getWidth() + ", Height: " + bitmap.getHeight());
        tensorFlowInferenceInterface.feed("input", this.floatValues, new long[]{1L, (long) bitmap.getWidth(), (long) bitmap.getHeight(), 3L});
        System.out.println("feed over");
        tensorFlowInferenceInterface.run(new String[]{"output"}, false);
        tensorFlowInferenceInterface.fetch("output", this.floatValues);
        System.out.println("run over");
        a = -1111111;
        float b = (float) (a >> 16 & 255);
        float c = (float) (a >> 8 & 255);
        float d = (float) (a & 255);
        System.out.println(b);
        System.out.println(c);
        System.out.println(d);
        a = -16777216 | (int) b << 16 | (int) c << 8 | (int) d;
        System.out.println(a);

        for (int i = 0; i < this.intValues.length; ++i) {
            this.intValues[i] = 0;
            this.intValues[i] = -16777216 | (int) this.floatValues[i * 3] << 16 | (int) this.floatValues[i * 3 + 1] << 8 | (int) this.floatValues[i * 3 + 2];
        }

        bitmap.setPixels(this.intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        System.out.println("all over");
        return bitmap;
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.iv_add1:
                showPop();
                toggleBright();
                break;
            case R.id.tv_1:
                i++;
                mPopupWindow.dismiss();
                if(i!=0)
                {
                    final View viewemp=v;
                    MyDialog.show(this, "确定下载吗?", new MyDialog.OnConfirmListener() {
                        @Override
                        public void onConfirmClick() {
                            if(bm1==null)
                            { String filename=saveImage(rawBitmap1);
                                Toast.makeText(viewemp.getContext(), "原图下载成功", Toast.LENGTH_SHORT).show();}
                                else{
                                String filename=saveImage(bm1);
                                Toast.makeText(viewemp.getContext(), "风格图像下载成功", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
                break;
            case R.id.tv_2:
                mPopupWindow.dismiss();
                Intent intenttest=new Intent();
                intenttest.setClass(getContext(),ChatActivity.class);
                startActivity(intenttest);
                break;
            case R.id.gallery1:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.putExtra("crop1", "true");
                // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
                this.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                break;
            case R.id.camera1:
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
            case R.id.style1:
                Toast.makeText(v.getContext(), "毕加索《缪斯》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow1.pb";
                 bm1= this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
        });
                mTransitionDrawable.setCrossFadeEnabled(true);
                mTransitionDrawable.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable);
                //imageView.setImageBitmap(bm1);
                break;
            case R.id.style2:
                Toast.makeText(v.getContext(), "梵高《星空》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow2.pb";
                bm1 = this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable1 = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
                });
                mTransitionDrawable1.setCrossFadeEnabled(true);
                mTransitionDrawable1.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable1);
                //imageView.setImageBitmap(bm1);
                break;
            case R.id.style3:
                Toast.makeText(v.getContext(), "梵高《梵高在阿尔勒卧室》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow3.pb";
                bm1 = this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable2 = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
                });
                mTransitionDrawable2.setCrossFadeEnabled(true);
                mTransitionDrawable2.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable2);
                //imageView.setImageBitmap(bm1);
                break;
            case R.id.style4:
                Toast.makeText(v.getContext(), "张择端《清明上河图》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow4.pb";
                bm1 = this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable3 = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
                });
                mTransitionDrawable3.setCrossFadeEnabled(true);
                mTransitionDrawable3.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable3);
                //imageView.setImageBitmap(bm1);
                break;
            case R.id.style5:
                Toast.makeText(v.getContext(), "毕加索《戴帽子的女子》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow5.pb";
                bm1 = this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable4 = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
                });
                mTransitionDrawable4.setCrossFadeEnabled(true);
                mTransitionDrawable4.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable4);
                //imageView.setImageBitmap(bm1);
                break;
            case R.id.style6:
                Toast.makeText(v.getContext(), "莫奈《日出印象》", Toast.LENGTH_LONG).show();
                modelPath = "android_tensorflow7.pb";
                bm1 = this.stylizeImage(rawBitmap1, modelPath);
                TransitionDrawable mTransitionDrawable5 = new TransitionDrawable(new Drawable[]{
                        imageView.getDrawable(),
                        new BitmapDrawable(bm1)
                });
                mTransitionDrawable5.setCrossFadeEnabled(true);
                mTransitionDrawable5.startTransition(1000);
                imageView.setImageDrawable(mTransitionDrawable5);
                //imageView.setImageBitmap(bm1);
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
        mPopupWindow.showAsDropDown(iv_add1, -100, 0);
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
        } else if (requestCode == PHOTO_REQUEST_CUT) {
            // 从剪切图片返回的数据
            if (data != null) {
                Bitmap rawBitmap = data.getParcelableExtra("data");
                rawBitmap1=rawBitmap;
                imageView.setImageBitmap(rawBitmap);
                ly.setVisibility(View.VISIBLE );
            }


        }
    }
    //保存图片
    public String saveImage(Bitmap bmp) {

        MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bmp, "title", "description");
        Intent intent;

        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File("/sdcard/Boohee/image.jpg"))));
        return  null;
    }

}