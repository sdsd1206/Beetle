package com.example.jack.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.livertmppushsdk.FdkAacEncode;
import com.alex.livertmppushsdk.RtmpSessionManager;
import com.alex.livertmppushsdk.SWVideoEncoder;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class sell_live_bidding extends AppCompatActivity {
    private final static int ID_RTMP_PUSH_START = 100;
    private final static int ID_RTMP_PUSH_EXIT = 101;
    private final int WIDTH_DEF = 480;
    private final int HEIGHT_DEF = 640;
    private final int FRAMERATE_DEF = 20;
    private final int BITRATE_DEF = 800 * 1000;

    private final int SAMPLE_RATE_DEF = 22050;
    private final int CHANNEL_NUMBER_DEF = 2;

    private final String LOG_TAG = "sell_live_bidding";
    private final boolean DEBUG_ENABLE = false; //音视频是否保存在本地

    private String _rtmpUrl = "rtmp://**.***.***.**:2000/live";

    PowerManager.WakeLock _wakeLock;
    private DataOutputStream _outputStream = null;

    private AudioRecord _AudioRecorder = null;
    private byte[] _RecorderBuffer = null;
    private FdkAacEncode _fdkaacEnc = null;
    private int _fdkaacHandle = 0;

    public SurfaceView _mSurfaceView = null;
    private Camera _mCamera = null;
    private boolean _bIsFront = true;
    private SWVideoEncoder _swEncH264 = null;
    private int _iDegrees = 0;

    private int _iRecorderBufferSize = 0;

    private Button _SwitchCameraBtn = null;

    private boolean _bStartFlag = false;

    private int _iCameraCodecType = android.graphics.ImageFormat.NV21;

    private byte[] _yuvNV21 = new byte[WIDTH_DEF * HEIGHT_DEF * 3 / 2];
    private byte[] _yuvEdit = new byte[WIDTH_DEF * HEIGHT_DEF * 3 / 2];

    private RtmpSessionManager _rtmpSessionMgr = null;

    private Queue<byte[]> _YUVQueue = new LinkedList<byte[]>();
    private Lock _yuvQueueLock = new ReentrantLock();

    private Thread _h264EncoderThread = null;
    private Runnable _h264Runnable = new Runnable() {
        @Override
        public void run() {
            while (!_h264EncoderThread.interrupted() && _bStartFlag) {
                int iSize = _YUVQueue.size();
                if (iSize > 0) {
                    _yuvQueueLock.lock();
                    byte[] yuvData = _YUVQueue.poll();
                    if (iSize > 9) {
                        Log.i(LOG_TAG, "###YUV Queue len=" + _YUVQueue.size() + ", YUV length=" + yuvData.length);
                    }

                    _yuvQueueLock.unlock();
                    if (yuvData == null) {
                        continue;
                    }

                    if (_bIsFront) {
                        _yuvEdit = _swEncH264.YUV420pRotate270(yuvData, HEIGHT_DEF, WIDTH_DEF);
                    } else {
                        _yuvEdit = _swEncH264.YUV420pRotate90(yuvData, HEIGHT_DEF, WIDTH_DEF);
                    }
                    byte[] h264Data = _swEncH264.EncoderH264(_yuvEdit);
                    if (h264Data != null) {
                        _rtmpSessionMgr.InsertVideoData(h264Data);
                        if (DEBUG_ENABLE) {
                            try {
                                _outputStream.write(h264Data);
                                int iH264Len = h264Data.length;
                                //Log.i(LOG_TAG, "Encode H264 len="+iH264Len);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            _YUVQueue.clear();
        }
    };


    private Runnable _aacEncoderRunnable = new Runnable() {
        @Override
        public void run() {
            DataOutputStream outputStream = null;
            if (DEBUG_ENABLE) {
                File saveDir = Environment.getExternalStorageDirectory();
                String strFilename = saveDir + "/buy.aac";
                try {
                    if (!new File(strFilename).exists()) {
                        new File(strFilename).createNewFile();
                    }
                    outputStream = new DataOutputStream(new FileOutputStream(strFilename));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            long lSleepTime = SAMPLE_RATE_DEF * 16 * 2 / _RecorderBuffer.length;

            while (!_AacEncoderThread.interrupted() && _bStartFlag) {
                int iPCMLen = _AudioRecorder.read(_RecorderBuffer, 0, _RecorderBuffer.length); // Fill buffer
                if ((iPCMLen != _AudioRecorder.ERROR_BAD_VALUE) && (iPCMLen != 0)) {
                    if (_fdkaacHandle != 0) {
                        byte[] aacBuffer = _fdkaacEnc.FdkAacEncode(_fdkaacHandle, _RecorderBuffer);
                        if (aacBuffer != null) {
                            long lLen = aacBuffer.length;

                            _rtmpSessionMgr.InsertAudioData(aacBuffer);
                            //Log.i(LOG_TAG, "fdk aac length="+lLen+" from pcm="+iPCMLen);
                            if (DEBUG_ENABLE) {
                                try {
                                    outputStream.write(aacBuffer);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } else {
                    Log.i(LOG_TAG, "######fail to get PCM data");
                }
                try {
                    Thread.sleep(lSleepTime / 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i(LOG_TAG, "AAC Encoder Thread ended ......");
        }
    };
    private Thread _AacEncoderThread = null;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private int getDispalyRotation() {
        int i = getWindowManager().getDefaultDisplay().getRotation();
        switch (i) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    private int getDisplayOritation(int degrees, int cameraId) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        int result = 0;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private Camera.PreviewCallback _previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] YUV, Camera currentCamera) {
            if (!_bStartFlag) {
                return;
            }

            boolean bBackCameraFlag = true;

            byte[] yuv420 = null;

            if (_iCameraCodecType == android.graphics.ImageFormat.YV12) {
                yuv420 = new byte[YUV.length];
                _swEncH264.swapYV12toI420_Ex(YUV, yuv420, HEIGHT_DEF, WIDTH_DEF);
            } else if (_iCameraCodecType == android.graphics.ImageFormat.NV21) {
                yuv420 = _swEncH264.swapNV21toI420(YUV, HEIGHT_DEF, WIDTH_DEF);
            }

            if (yuv420 == null) {
                return;
            }
            if (!_bStartFlag) {
                return;
            }
            _yuvQueueLock.lock();
            if (_YUVQueue.size() > 1) {
                _YUVQueue.clear();
            }
            _YUVQueue.offer(yuv420);
            _yuvQueueLock.unlock();
        }
    };

    public void InitCamera() {
        Camera.Parameters p = _mCamera.getParameters();

        Size prevewSize = p.getPreviewSize();
        showlog("Original Width:" + prevewSize.width + ", height:" + prevewSize.height);

        List<Size> PreviewSizeList = p.getSupportedPreviewSizes();
        List<Integer> PreviewFormats = p.getSupportedPreviewFormats();
        showlog("Listing all supported preview sizes");
        for (Camera.Size size : PreviewSizeList) {
            showlog("  w: " + size.width + ", h: " + size.height);
        }

        showlog("Listing all supported preview formats");
        Integer iNV21Flag = 0;
        Integer iYV12Flag = 0;
        for (Integer yuvFormat : PreviewFormats) {
            showlog("preview formats:" + yuvFormat);
            if (yuvFormat == android.graphics.ImageFormat.YV12) {
                iYV12Flag = android.graphics.ImageFormat.YV12;
            }
            if (yuvFormat == android.graphics.ImageFormat.NV21) {
                iNV21Flag = android.graphics.ImageFormat.NV21;
            }
        }

        if (iNV21Flag != 0) {
            _iCameraCodecType = iNV21Flag;
        } else if (iYV12Flag != 0) {
            _iCameraCodecType = iYV12Flag;
        }
        p.setPreviewSize(HEIGHT_DEF, WIDTH_DEF);
        p.setPreviewFormat(_iCameraCodecType);
        p.setPreviewFrameRate(FRAMERATE_DEF);

        showlog("_iDegrees=" + _iDegrees);
        _mCamera.setDisplayOrientation(_iDegrees);
        p.setRotation(_iDegrees);
        _mCamera.setPreviewCallback(_previewCallback);
        _mCamera.setParameters(p);
        try {
            _mCamera.setPreviewDisplay(_mSurfaceView.getHolder());
        } catch (Exception e) {
            return;
        }
        _mCamera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
        _mCamera.startPreview();
    }

    private final class SurceCallBack implements SurfaceHolder.Callback {
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            _mCamera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {
                    if (success) {
                        InitCamera();
                        camera.cancelAutoFocus();//只有加上了这一句，才会自动对焦。
                    }
                }
            });
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            _iDegrees = getDisplayOritation(getDispalyRotation(), 0);
            if (_mCamera != null) {
                InitCamera();
                return;
            }
            //华为i7前后共用摄像头
            if (Camera.getNumberOfCameras() == 1) {
                _bIsFront = false;
                _mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                _mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            InitCamera();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    }

    private void Start() {
        if (DEBUG_ENABLE) {
            File saveDir = Environment.getExternalStorageDirectory();
            String strFilename = saveDir + "/buy.h264";
            try {
                if (!new File(strFilename).exists()) {
                    new File(strFilename).createNewFile();
                }
                _outputStream = new DataOutputStream(new FileOutputStream(strFilename));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //_rtmpSessionMgr.Start("rtmp://192.168.0.110/buy_live_bidding_live/12345678");
        _rtmpSessionMgr = new RtmpSessionManager();
        _rtmpSessionMgr.Start(_rtmpUrl);

        int iFormat = _iCameraCodecType;
        _swEncH264 = new SWVideoEncoder(WIDTH_DEF, HEIGHT_DEF, FRAMERATE_DEF, BITRATE_DEF);
        _swEncH264.start(iFormat);

        _bStartFlag = true;

        _h264EncoderThread = new Thread(_h264Runnable);
        _h264EncoderThread.setPriority(Thread.MAX_PRIORITY);
        _h264EncoderThread.start();

        _AudioRecorder.startRecording();
        _AacEncoderThread = new Thread(_aacEncoderRunnable);
        _AacEncoderThread.setPriority(Thread.MAX_PRIORITY);
        _AacEncoderThread.start();
    }

    private void Stop() {
        _bStartFlag = false;

        _AacEncoderThread.interrupt();
        _h264EncoderThread.interrupt();

        _AudioRecorder.stop();
        _swEncH264.stop();

        _rtmpSessionMgr.Stop();

        _yuvQueueLock.lock();
        _YUVQueue.clear();
        _yuvQueueLock.unlock();

        if (DEBUG_ENABLE) {
            if (_outputStream != null) {
                try {
                    _outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private OnClickListener _switchCameraOnClickedEvent = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            //华为i7前后共用摄像头
            if (Camera.getNumberOfCameras() == 1) {
                Toast.makeText(sell_live_bidding.this, "沒有前鏡頭喔！", Toast.LENGTH_SHORT).show();
                return;
            }

            if (_mCamera == null) {
                return;
            }
            _mCamera.setPreviewCallback(null);
            _mCamera.stopPreview();
            _mCamera.release();
            _mCamera = null;

            if (_bIsFront) {
                _mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            } else {
                _mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            }
            _bIsFront = !_bIsFront;
            InitCamera();
        }
    };

    private void InitAudioRecord() {
        _iRecorderBufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE_DEF,
                AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT);
        _AudioRecorder = new AudioRecord(AudioSource.MIC,
                SAMPLE_RATE_DEF, AudioFormat.CHANNEL_CONFIGURATION_STEREO,
                AudioFormat.ENCODING_PCM_16BIT, _iRecorderBufferSize);
        _RecorderBuffer = new byte[_iRecorderBufferSize];

        _fdkaacEnc = new FdkAacEncode();
        _fdkaacHandle = _fdkaacEnc.FdkAacInit(SAMPLE_RATE_DEF, CHANNEL_NUMBER_DEF);
    }

    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            Bundle b = msg.getData();
            int ret;
            switch (msg.what) {
                case ID_RTMP_PUSH_START: {
                    Start();
                    break;
                }
            }
        }
    };

    private void RtmpStartMessage() {
        Message msg = new Message();
        msg.what = ID_RTMP_PUSH_START;
        Bundle b = new Bundle();
        b.putInt("ret", 0);
        msg.setData(b);
        mHandler.sendMessage(msg);
    }

    private void InitAll() {
        WindowManager wm = this.getWindowManager();

        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        int iNewWidth = (int) (height * 3.0 / 4.0);

        RelativeLayout rCameraLayout = (RelativeLayout) findViewById(R.id.cameraRelative);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        int iPos = width - iNewWidth;
        layoutParams.setMargins(iPos, 0, 0, 0);

        _mSurfaceView = (SurfaceView) this.findViewById(R.id.surfaceViewEx);
        _mSurfaceView.getHolder().setFixedSize(HEIGHT_DEF, WIDTH_DEF);
        _mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        _mSurfaceView.getHolder().setKeepScreenOn(true);
        _mSurfaceView.getHolder().addCallback(new SurceCallBack());
        _mSurfaceView.setLayoutParams(layoutParams);

        InitAudioRecord();

        _SwitchCameraBtn = (Button) findViewById(R.id.SwitchCamerabutton);
        _SwitchCameraBtn.setOnClickListener(_switchCameraOnClickedEvent);

        RtmpStartMessage();//開始推流
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sell_live_bidding);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        _rtmpUrl = intent.getStringExtra(StartActivity.RTMPURL_MESSAGE);

        InitAll();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        _wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "My Tag");

        //----
        init();
        sell();

        chat();
        money();

        database();

//        chat.start();
//        chat2.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "sell_live_bidding onDestroy...");
    }

    protected void onResume() {
        super.onResume();
        _wakeLock.acquire();
    }

    protected void onPause() {
        super.onPause();
        _wakeLock.release();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系統提示");
            // 设置对话框消息
            isExit.setMessage("確定要退出嗎");
            // 添加选择按钮并注册监听
            isExit.setButton("確定", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();
        }
        return false;
    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE: {// "确认"按钮退出程序
                    if (_mCamera != null) {
                        try {
                            _mCamera.setPreviewCallback(null);
                            _mCamera.setPreviewDisplay(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        _mCamera.stopPreview();
                        _mCamera.release();
                        _mCamera = null;
                    }
                    if (_bStartFlag) {
                        Stop();
                    }
                    sell_live_bidding.this.finish();
                    break;
                }
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    public void showlog(String info) {
        System.out.print("Watson " + info + "\n");
    }

    private String suid;
    private Button clock;
    private int max = 0;
    private DatabaseReference ref;
    private FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<live_room> rooms = new ArrayList<>();
    private ArrayList<Chat> mchat = new ArrayList<>();
    private ArrayList<String> gData = new ArrayList<>();
    private ArrayList<ArrayList<order>> iData = new ArrayList<>();
    private ArrayList<order> lData = new ArrayList<>();
    private ArrayList<user> muser = new ArrayList<>();
    private MyBaseExpandableListAdapter adapter;
    private ExpandableListView exlist;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private CountDownTimer mCountDownTimer;
    private EditText edittext;
    private TextView tf_second, textview1, textview2, textview3, shop;
    private Button check, money;
    private ImageView shopimg;
    String tmp1, tmp2; // 暫存文字訊息
    Socket clientSocket1, clientSocket2;

    View item;

    public void init() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("請輸入商品名稱");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        exlist = (ExpandableListView) findViewById(R.id.exlist);
        adapter = new MyBaseExpandableListAdapter(this);
        exlist.setAdapter(adapter);
        exlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        exlist.setGroupIndicator(null);
        int groupCount = exlist.getCount();
        for (int i = 0; i < groupCount; i++) {
            exlist.expandGroup(i);
        }

        edittext = (EditText) findViewById(R.id.editText2);
        textview1 = (TextView) findViewById(R.id.textView6);
        textview1.setMovementMethod(ScrollingMovementMethod.getInstance());
        textview2 = (TextView) findViewById(R.id.textView7);
        textview2.setMovementMethod(ScrollingMovementMethod.getInstance());
        textview3 = (TextView) findViewById(R.id.top);
        shop = (TextView) findViewById(R.id.shop);
        shopimg = (ImageView) findViewById(R.id.shopimg);
        check = (Button) findViewById(R.id.button2);
        check.setOnClickListener(checklistener);
        money = (Button) findViewById(R.id.money);
        money.setOnClickListener(moneylistener);
        tf_second = (TextView) findViewById(R.id.tf_second);

        clock = (Button) findViewById(R.id.clock);
//        賣家倒數設置
        clock.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                item = LayoutInflater.from(sell_live_bidding.this).inflate(R.layout.clock, null);
                new AlertDialog.Builder(sell_live_bidding.this)
                        .setTitle("輸入倒數秒數")
                        .setView(item)
                        .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText editText = (EditText) item.findViewById(R.id.count);
                                EditText editText2 = (EditText) item.findViewById(R.id.count2);
                                String min = editText.getText().toString();
                                if (min.equals("")) {
                                    min = "0";
                                }
                                String sec = editText2.getText().toString();
                                int second = Integer.parseInt(min) * 60 + Integer.parseInt(sec);
                                ref = FirebaseDatabase.getInstance().getReference("clock").child(fuser.getUid()).child("sec");
                                ref.setValue("" + second);
                                tf_second.setText("" + second);
                                mCountDownTimer = new CountDownTimer(second * 1000, 1000) {

                                    //開始倒數
                                    public void onTick(long millisUntilFinished) {
                                        ref.setValue(tf_second.getText().toString());
                                        tf_second.setText("" + (millisUntilFinished / 1000));
                                    }

                                    //倒數結束
                                    public void onFinish() {
                                        tf_second.setText("時間到囉!");
                                        String end = "---結標線---";
                                        ref = FirebaseDatabase.getInstance().getReference("money").child(fuser.getUid());
                                        ref.push().setValue(new Chat("-1", end));
                                        gdata();
                                        ref = FirebaseDatabase.getInstance().getReference("clock").child(fuser.getUid()).child("sec");
                                        ref.setValue("未設定");
                                    }
                                }.start();
                            }
                        })
                        .show();
            }
        });
    }

    private View.OnClickListener checklistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ref = FirebaseDatabase.getInstance().getReference("chat").child(fuser.getUid());
            ref.push().setValue(new Chat(muser.get(0).getNickname(), edittext.getText().toString()));
            edittext.setText("");
        }
    };

    private View.OnClickListener moneylistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            item = LayoutInflater.from(sell_live_bidding.this).inflate(R.layout.dialog_title, null);
            new AlertDialog.Builder(sell_live_bidding.this)
                    .setTitle("競標設定")
                    .setView(item)
                    .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText editText2 = (EditText) item.findViewById(R.id.money);
                            max = Integer.parseInt(editText2.getText().toString());
                            textview3.setText("起標價-->" + max);
                            ref = FirebaseDatabase.getInstance().getReference("money").child(fuser.getUid());
                            ref.push().setValue(new Chat(fuser.getUid(), "" + max));
                            EditText editText = (EditText) item.findViewById(R.id.title);
                            String name = editText.getText().toString();
                            getSupportActionBar().setTitle(name);
                            order order = new order("姓名", "數量", "總計");
                            gData.add(name);
                            ref = FirebaseDatabase.getInstance().getReference("order_blive").child(fuser.getUid());
                            ref.child(name).child("0").setValue(order);

                        }
                    })
                    .show();
        }
    };

    public void gdata() {
        ref = FirebaseDatabase.getInstance().getReference("order_blive").child(fuser.getUid())
                .child(getSupportActionBar().getTitle().toString());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    order order = ds.getValue(order.class);
                    lData.add(order);
                    iData.add(lData);
                }
                adapter.notifyDataSetChanged();
                exlist.collapseGroup(0);
                exlist.expandGroup(0);
                lData = new ArrayList<>();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void database() {
        //                取買家個人資料
        ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user user;
                user = dataSnapshot.getValue(user.class);
                muser.add(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void chat() {
        //                取得聊天紀錄
        ref = FirebaseDatabase.getInstance().getReference("chat").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textview1.setText("");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat;
                    chat = ds.getValue(Chat.class);
                    mchat.add(chat);
                    textview1.append(chat.getName() + ":" + chat.getMsg() + "\n");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sell() {
        //                取買家個人資料
        ref = FirebaseDatabase.getInstance().getReference("live_room").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                live_room room;
                room = dataSnapshot.getValue(live_room.class);
                rooms.add(room);
                shop.setText(rooms.get(0).getRoom_name());
                Glide.with(sell_live_bidding.this)
                        .load(rooms.get(0).getRoom_pic())
                        .into(shopimg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void money() {
//        取得競標金額紀錄
        ref = FirebaseDatabase.getInstance().getReference("money").child(fuser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                textview2.setText("");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Chat chat;
                    chat = ds.getValue(Chat.class);
                    if (chat != null) {
                        if (chat.getName().equals("-1")) {
                            textview2.append(chat.getMsg() + "\n");
                            continue;
                        }
                        int p = Integer.parseInt(chat.getMsg());
                        if (chat.getName().equals(fuser.getUid())) {
                            max = p;
                            textview3.setText("起標價-->" + max);
                        } else {
                            if (max < p) {
                                max = p;
                                textview3.setText("" + chat.getName() + ":" + max);
                            }
                            textview2.append(chat.getName() + ":" + chat.getMsg() + "\n");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class ViewHolderGroup {
        private TextView tv_group_name;
    }

    class ViewHolderItem {
        private TextView tv_name;
        private TextView tv_count;
        private TextView tv_price;
    }

    public class MyBaseExpandableListAdapter extends BaseExpandableListAdapter {

        private Context mContext;

        public MyBaseExpandableListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        public int getGroupCount() {
            return gData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return iData.get(groupPosition).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return gData.get(groupPosition);
        }

        @Override
        public order getChild(int groupPosition, int childPosition) {
            return iData.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        //取得用于显示给定分组的视图. 这个方法仅返回分组的视图对象
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            ViewHolderGroup groupHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.buy_live_bidding_live_group, parent, false);
                groupHolder = new ViewHolderGroup();
                groupHolder.tv_group_name = (TextView) convertView.findViewById(R.id.textView1);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (ViewHolderGroup) convertView.getTag();
            }
            groupHolder.tv_group_name.setText(gData.get(groupPosition).toString());
            return convertView;
        }

        //取得显示给定分组给定子位置的数据用的视图
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            ViewHolderItem itemHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.buy_live_bidding_live_item, parent, false);
                itemHolder = new ViewHolderItem();
//                itemHolder.img_icon = (TextView) convertView.findViewById(R.id.img_icon);
                itemHolder.tv_name = (TextView) convertView.findViewById(R.id.textView1);
                itemHolder.tv_count = (TextView) convertView.findViewById(R.id.textView2);
                itemHolder.tv_price = (TextView) convertView.findViewById(R.id.textView3);
                convertView.setTag(itemHolder);
            } else {
                itemHolder = (ViewHolderItem) convertView.getTag();
            }
//            itemHolder.img_icon.setImageResource(iData.get(groupPosition).get(childPosition).getiId());
            itemHolder.tv_name.setText(iData.get(groupPosition).get(childPosition).getName());
            itemHolder.tv_count.setText(iData.get(groupPosition).get(childPosition).getCount());
            itemHolder.tv_price.setText(iData.get(groupPosition).get(childPosition).getTotal());
            return convertView;
        }

        //设置子列表是否可选中
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }
}
