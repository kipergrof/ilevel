package com.android.ilevel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.adapter.Drawer_Adapter;
import com.android.application.ILevelApplication;
import com.android.application.NetworkThread;
import com.android.ilevel.button.IlevelButton;
import com.ideasthatfloat.ilevel.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"HandlerLeak"})
public class HomeScreen extends Activity implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {
    public static boolean IGNITION_OFF_COMMAND = false;
    private boolean ABORT_CALIBRATION = false;
    final long[] CrcTable = {0, 79764919, 159529838, 222504665, 319059676, 398814059, 445009330, 507990021, 638119352, 583659535, 797628118, 726387553, 890018660, 835552979, 1015980042, 944750013};
    /* access modifiers changed from: private */
    public boolean EXIT_PRESSED = false;
    /* access modifiers changed from: private */
    public boolean IS_IGNITION_ON;
    private int POSITION_SAVED;
    /* access modifiers changed from: private */
    public String TAG = "HomeScreen";
    private boolean TRIM_PRESSED = false;
    private Drawer_Adapter adapter;
    /* access modifiers changed from: private */
    public AlertDialog alertDialog;
    /* access modifiers changed from: private */
    public AnimationDrawable all_down_flash;
    /* access modifiers changed from: private */
    public ILevelApplication application;
    Button btnStartProgress;
    /* access modifiers changed from: private */
    public IlevelButton btn_01;
    /* access modifiers changed from: private */
    public boolean btn_01Pressed = false;
    /* access modifiers changed from: private */
    public IlevelButton btn_01_IgnitionOff;
    /* access modifiers changed from: private */
    public ImageView btn_01_IgnitionOff_highlight;
    /* access modifiers changed from: private */
    public ImageView btn_01_highlight;
    /* access modifiers changed from: private */
    public IlevelButton btn_02;
    /* access modifiers changed from: private */
    public boolean btn_02Pressed = false;
    /* access modifiers changed from: private */
    public IlevelButton btn_02_IgnitionOff;
    /* access modifiers changed from: private */
    public ImageView btn_02_IgnitionOff_highlight;
    /* access modifiers changed from: private */
    public ImageView btn_02_highlight;
    /* access modifiers changed from: private */
    public IlevelButton btn_03;
    /* access modifiers changed from: private */
    public boolean btn_03Pressed = false;
    /* access modifiers changed from: private */
    public IlevelButton btn_03_IgnitionOff;
    /* access modifiers changed from: private */
    public ImageView btn_03_IgnitionOff_highlight;
    /* access modifiers changed from: private */
    public ImageView btn_03_highlight;
    /* access modifiers changed from: private */
    public IlevelButton btn_AllDown;
    /* access modifiers changed from: private */
    public IlevelButton btn_AllDown_IgnitionOff;
    /* access modifiers changed from: private */
    public ImageView btn_AllDown_IgnitionOff_highlight;
    /* access modifiers changed from: private */
    public ImageView btn_AllDown_highlight;
    private IlevelButton btn_BackDown;
    private IlevelButton btn_BackUp;
    private IlevelButton btn_Dot;
    private boolean btn_DotPressed = false;
    private ImageView btn_Dot_highlight;
    private IlevelButton btn_FrontDown;
    private IlevelButton btn_FrontUp;
    private IlevelButton btn_LeftBackDown;
    /* access modifiers changed from: private */
    public ImageView btn_LeftBackDown_highlight;
    private IlevelButton btn_LeftBackUp;
    /* access modifiers changed from: private */
    public ImageView btn_LeftBackUp_highlight;
    private IlevelButton btn_LeftFrontDown;
    /* access modifiers changed from: private */
    public ImageView btn_LeftFrontDown_highlight;
    private IlevelButton btn_LeftFrontUp;
    /* access modifiers changed from: private */
    public ImageView btn_LeftFrontUp_highlight;
    private Button btn_Menu;
    private IlevelButton btn_RightBackDown;
    /* access modifiers changed from: private */
    public ImageView btn_RightBackDown_highlight;
    private IlevelButton btn_RightBackUp;
    /* access modifiers changed from: private */
    public ImageView btn_RightBackUp_highlight;
    private IlevelButton btn_RightFrontDown;
    /* access modifiers changed from: private */
    public ImageView btn_RightFrontDown_highlight;
    private IlevelButton btn_RightFrontUp;
    /* access modifiers changed from: private */
    public ImageView btn_RightFrontUp_highlight;
    /* access modifiers changed from: private */
    public int buttonPress = 0;
    private boolean buttonPressLockout = false;
    /* access modifiers changed from: private */
    public boolean calibrationErrorFlag = false;
    private BroadcastReceiver checkIgnitionStatus;
    /* access modifiers changed from: private */
    public Dialog dialog;
    private BroadcastReceiver errorDialogReceiver;
    private BroadcastReceiver errorIndicatorReceiver;
    private long fileSize = 0;
    /* access modifiers changed from: private */
    public Handler handler;
    private BroadcastReceiver heightIndicatorReceiver;
    /* access modifiers changed from: private */
    public HomeScreenCounter homeScreenCounter;
    /* access modifiers changed from: private */
    public ImageView img_wifi_icon;
    private AnimationIn in;
    /* access modifiers changed from: private */
    public Intent intent;
    /* access modifiers changed from: private */
    public boolean isVisible = false;
    /* access modifiers changed from: private */
    public int lastButtonPressed = 0;
    /* access modifiers changed from: private */
    public int lastErrorCode = 0;
    private AnimationDrawable led_flash;
    private int led_status = 0;
    /* access modifiers changed from: private */
    public int mCounter = 0;
    /* access modifiers changed from: private */
    public boolean mDown = false;
    /* access modifiers changed from: private */
    public DrawerLayout mDrawerLayout;
    /* access modifiers changed from: private */
    public ListView mDrawerList;
    /* access modifiers changed from: private */
    public Handler mHandlerLongPressed = new Handler();
    private final Runnable mRunnable_btn_01Pressed = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd00000029000012340000000000000000000000007b7d7b7d8d35c9d4");
            this.sendCommandToSocket.run();
            boolean unused = HomeScreen.this.btn_01Pressed = true;
            HomeScreen.this.application.sendButtonReleaseCommand();
        }
    };
    private final Runnable mRunnable_btn_01_IgnitionOff = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a505b00");
            this.sendCommandToSocket.run();
            HomeScreen.IGNITION_OFF_COMMAND = true;
            HomeScreen.this.homeScreenCounter.cancel();
            HomeScreen.this.homeScreenCounter.start();
        }
    };
    private final Runnable mRunnable_btn_02Pressed = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd0000002a000012340000000000000000000000007b7d7b7db5409a33");
            this.sendCommandToSocket.run();
            boolean unused = HomeScreen.this.btn_02Pressed = true;
            HomeScreen.this.application.sendButtonReleaseCommand();
        }
    };
    private final Runnable mRunnable_btn_02_IgnitionOff = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a505c00");
            this.sendCommandToSocket.run();
            HomeScreen.IGNITION_OFF_COMMAND = true;
            HomeScreen.this.homeScreenCounter.cancel();
            HomeScreen.this.homeScreenCounter.start();
        }
    };
    private final Runnable mRunnable_btn_03Pressed = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd0000002b000012340000000000000000000000007b7d7b7da293ab6e");
            this.sendCommandToSocket.run();
            boolean unused = HomeScreen.this.btn_03Pressed = true;
            HomeScreen.this.application.sendButtonReleaseCommand();
        }
    };
    private final Runnable mRunnable_btn_03_IgnitionOff = new Runnable() {
        ILevelApplication.SendCommandToSocket sendCommandToSocket;

        public void run() {
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            this.sendCommandToSocket = new ILevelApplication.SendCommandToSocket("a55a0a505d00");
            this.sendCommandToSocket.run();
            HomeScreen.IGNITION_OFF_COMMAND = true;
            HomeScreen.this.homeScreenCounter.cancel();
            HomeScreen.this.homeScreenCounter.start();
        }
    };
    private final Runnable mRunnable_btn_AllDown_IgnitionOff = new Runnable() {
        public void run() {
            HomeScreen.this.vibrator.vibrate(500);
            if (HomeScreen.this.mCounter == 0) {
                ILevelApplication access$3500 = HomeScreen.this.application;
                access$3500.getClass();
                new ILevelApplication.SendCommandToSocket("a55a0a505a00").run();
                HomeScreen.this.application.sendButtonReleaseCommand();
            }
            HomeScreen.access$3108(HomeScreen.this);
            if (HomeScreen.this.mCounter < 2) {
                Log.d(HomeScreen.this.TAG, Integer.toString(HomeScreen.this.mCounter));
                HomeScreen.this.mHandlerLongPressed.postDelayed(this, 1000);
            }
        }
    };
    private final Runnable mRunnable_btn_All_Down = new Runnable() {
        public void run() {
            HomeScreen.access$3108(HomeScreen.this);
            if (HomeScreen.this.mCounter < 3) {
                Log.d(HomeScreen.this.TAG, Integer.toString(HomeScreen.this.mCounter));
                HomeScreen.this.vibrator.vibrate(500);
                HomeScreen.this.mHandlerLongPressed.postDelayed(this, 1000);
            } else if (HomeScreen.this.mCounter == 3) {
                HomeScreen.this.vibrator.vibrate(500);
                ILevelApplication access$3500 = HomeScreen.this.application;
                access$3500.getClass();
                new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd0000002c000012340000000000000000000000007b7d7b7dc5aa3dfd").run();
                HomeScreen.this.application.sendButtonReleaseCommand();
            }
        }
    };
    private final Runnable mRunnable_btn_Dot = new Runnable() {
        public void run() {
            HomeScreen.this.toast.show();
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            new ILevelApplication.SendCommandToSocket("a55a0a5001240d0d21210000abcd00000058000012340000000000000000000000007b7d7b7df3f9dc0b").run();
            HomeScreen.this.mHandlerLongPressed.postDelayed(this, 100);
        }
    };
    private final Runnable mRunnable_valveControl = new Runnable() {
        public void run() {
            if (HomeScreen.this.mDrawerLayout.isDrawerOpen((View) HomeScreen.this.mDrawerList)) {
                int unused = HomeScreen.this.buttonPress = 0;
                Log.i(HomeScreen.this.valveTag, "Menu extended.  Button press cancelled.");
            }
            if (HomeScreen.this.buttonPress == 0) {
                Log.i(HomeScreen.this.valveTag, "No Button pressed " + HomeScreen.this.buttonPress);
                boolean unused2 = HomeScreen.this.mDown = false;
                HomeScreen.this.cancelLongPress();
                HomeScreen.this.application.sendButtonReleaseCommand();
                int unused3 = HomeScreen.this.lastButtonPressed = 0;
                return;
            }
            HomeScreen.access$3108(HomeScreen.this);
            String command = "a55a0a5001240d0d21210000abcd0000000000001234000000" + String.format("%02x", new Object[]{Integer.valueOf(HomeScreen.this.buttonPress)}) + "00000000000000007b7d7b7d";
            String command2 = command + String.format("%08x", new Object[]{Long.valueOf(HomeScreen.this.getCRC(command).longValue() & -1)});
            Log.i(HomeScreen.this.valveTag, "Button pressed " + HomeScreen.this.buttonPress + " - " + command2);
            ILevelApplication access$3500 = HomeScreen.this.application;
            access$3500.getClass();
            new ILevelApplication.SendCommandToSocket(command2).run();
            HomeScreen.this.mHandlerLongPressed.postDelayed(this, 100);
        }
    };
    /* access modifiers changed from: private */
    public Context myContext;
    private BroadcastReceiver networkErrorDialog;
    private BroadcastReceiver passwordErrorReceiver;
    /* access modifiers changed from: private */
    public Dialog positionSavedDialog;
    /* access modifiers changed from: private */
    public int previousErrorCode = 50000;
    private Handler progressBarHandler = new Handler();
    private int progressBarStatus = 0;
    private RelativeLayout rLayoutFor_IgnitionOFF;
    private RelativeLayout rLayoutFor_IgnitionON;
    private BroadcastReceiver removeErrorDialogReceiver;
    private boolean scalingComplete = false;
    private BroadcastReceiver screenStateReceiver;
    /* access modifiers changed from: private */
    public boolean showError = false;
    /* access modifiers changed from: private */
    public int tempErrorCode;
    private Timer timerForWi_Fi;
    /* access modifiers changed from: private */
    public Toast toast;
    private BroadcastReceiver updatePasswordReceiver;
    /* access modifiers changed from: private */
    public String valveTag = "valve";
    /* access modifiers changed from: private */
    public Vibrator vibrator;
    BroadcastReceiver wifiStatusReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("WiFi", "checking wifi state...");
            SupplicantState supState = ((WifiManager) HomeScreen.this.getApplicationContext().getSystemService("wifi")).getConnectionInfo().getSupplicantState();
            Log.d("WiFi", "supplicant state: " + supState);
            if (supState.equals(SupplicantState.COMPLETED)) {
                Log.d("WiFi", "wifi enabled and connected");
                NetworkThread networkThread = HomeScreen.this.application.networkThread;
                ILevelApplication unused = HomeScreen.this.application;
                String str = ILevelApplication.SERVERIP;
                ILevelApplication unused2 = HomeScreen.this.application;
                networkThread.startConnection(str, (int) ILevelApplication.SERVERPORT);
            } else if (supState.equals(SupplicantState.SCANNING)) {
                Log.d("WiFi", "wifi scanning");
            } else if (supState.equals(SupplicantState.DISCONNECTED)) {
                Log.d("WiFi", "wifi disonnected");
            } else {
                Log.d("WiFi", "wifi connecting");
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean wifi_connected = false;

    static /* synthetic */ int access$3108(HomeScreen x0) {
        int i = x0.mCounter;
        x0.mCounter = i + 1;
        return i;
    }

    public void callOtherMethod(View view) {
        FirmwareUpdate.forceFirmwareUpdate(view);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(128);
        setContentView(R.layout.demo1);
        Log.i("status", "HomeScreen onCreate");
        this.mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        this.mDrawerList = (ListView) findViewById(R.id.right_drawer);
        LayoutInflater lf = getLayoutInflater();
        View headerView = lf.inflate(R.layout.drawer_header, this.mDrawerList, false);
        View inflate = lf.inflate(R.layout.drawer_footer, this.mDrawerList, false);
        this.mDrawerList.addHeaderView(headerView, (Object) null, false);
        ArrayList<String> alist = new ArrayList<>();
        alist.add("Calibration");
        alist.add("Save Heights");
        alist.add("Settings");
        alist.add("Password");
        this.mDrawerList.setAdapter(new Drawer_Adapter(this, alist));
        this.mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        setupUI();
        this.handler = new Handler();
        this.lastErrorCode = 0;
        if (!this.application.isTablet()) {
            setRequestedOrientation(1);
        }
        this.homeScreenCounter = new HomeScreenCounter(300000, 1000);
        this.timerForWi_Fi = new Timer();
        this.timerForWi_Fi.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                HomeScreen.this.handler.post(new Runnable() {
                    public void run() {
                        if (ILevelApplication.VERSION_INFO) {
                            HomeScreen.this.img_wifi_icon.setBackgroundResource(R.drawable.wifi);
                            boolean unused = HomeScreen.this.wifi_connected = true;
                            return;
                        }
                        HomeScreen.this.img_wifi_icon.setBackgroundResource(R.drawable.wififault);
                        boolean unused2 = HomeScreen.this.wifi_connected = false;
                    }
                });
            }
        }, 500, 2500);
        this.application.mainView = findViewById(16908290);
    }

    private long Crc32Fast(long Crc, long Data) {
        long Crc2 = Crc ^ Data;
        long Crc3 = ((Crc2 << 4) ^ this.CrcTable[(int) ((Crc2 >>> 28) & 15)]) & 4294967295L;
        long Crc4 = ((Crc3 << 4) ^ this.CrcTable[(int) ((Crc3 >>> 28) & 15)]) & 4294967295L;
        long Crc5 = ((Crc4 << 4) ^ this.CrcTable[(int) ((Crc4 >>> 28) & 15)]) & 4294967295L;
        long Crc6 = ((Crc5 << 4) ^ this.CrcTable[(int) ((Crc5 >>> 28) & 15)]) & 4294967295L;
        long Crc7 = ((Crc6 << 4) ^ this.CrcTable[(int) ((Crc6 >>> 28) & 15)]) & 4294967295L;
        long Crc8 = ((Crc7 << 4) ^ this.CrcTable[(int) ((Crc7 >>> 28) & 15)]) & 4294967295L;
        long Crc9 = ((Crc8 << 4) ^ this.CrcTable[(int) ((Crc8 >>> 28) & 15)]) & 4294967295L;
        return ((Crc9 << 4) ^ this.CrcTable[(int) ((Crc9 >>> 28) & 15)]) & 4294967295L;
    }

    /* access modifiers changed from: private */
    public Long getCRC(String dataIn) {
        return Long.valueOf(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(Crc32Fast(4294967295L, Long.parseLong(dataIn.substring(21, 28), 16)), Long.parseLong(dataIn.substring(29, 36), 16)), Long.parseLong(dataIn.substring(37, 44), 16)), Long.parseLong(dataIn.substring(45, 52), 16)), Long.parseLong(dataIn.substring(53, 60), 16)), Long.parseLong(dataIn.substring(61, 68), 16)));
    }

    public boolean isLandscape() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        if (metrics.widthPixels > metrics.heightPixels) {
            return true;
        }
        return false;
    }

    private void setupUI() {
        this.myContext = this;
        this.application = ILevelApplication.application;
        this.in = new AnimationIn();
        this.vibrator = (Vibrator) getApplicationContext().getSystemService("vibrator");
        this.img_wifi_icon = (ImageView) findViewById(R.id.img_Wi_Fi);
        this.btn_Menu = (Button) findViewById(R.id.btn_Menu);
        this.btn_01 = (IlevelButton) findViewById(R.id.btn_01);
        this.btn_02 = (IlevelButton) findViewById(R.id.btn_02);
        this.btn_03 = (IlevelButton) findViewById(R.id.btn_03);
        this.btn_Dot = (IlevelButton) findViewById(R.id.btn_Dot);
        this.btn_AllDown = (IlevelButton) findViewById(R.id.btn_AllDown);
        this.btn_FrontUp = (IlevelButton) findViewById(R.id.btn_FrontUp);
        this.btn_FrontDown = (IlevelButton) findViewById(R.id.btn_FrontDown);
        this.btn_RightFrontUp = (IlevelButton) findViewById(R.id.btn_RightFrontUp);
        this.btn_RightFrontDown = (IlevelButton) findViewById(R.id.btn_RightFrontDown);
        this.btn_LeftFrontUp = (IlevelButton) findViewById(R.id.btn_LeftFrontUp);
        this.btn_LeftFrontDown = (IlevelButton) findViewById(R.id.btn_LeftFrontDown);
        this.btn_BackUp = (IlevelButton) findViewById(R.id.btn_BackUp);
        this.btn_BackDown = (IlevelButton) findViewById(R.id.btn_BackDown);
        this.btn_RightBackUp = (IlevelButton) findViewById(R.id.btn_RightBackUp);
        this.btn_RightBackDown = (IlevelButton) findViewById(R.id.btn_RightBackDown);
        this.btn_LeftBackUp = (IlevelButton) findViewById(R.id.btn_LeftBackUp);
        this.btn_LeftBackDown = (IlevelButton) findViewById(R.id.btn_LeftBackDown);
        this.btn_01_highlight = (ImageView) findViewById(R.id.btn_01_highlight);
        this.btn_02_highlight = (ImageView) findViewById(R.id.btn_02_highlight);
        this.btn_03_highlight = (ImageView) findViewById(R.id.btn_03_highlight);
        this.btn_AllDown_highlight = (ImageView) findViewById(R.id.btn_alldown_highlight);
        this.btn_LeftFrontUp_highlight = (ImageView) findViewById(R.id.btn_LeftFrontUp_highlight);
        this.btn_RightFrontUp_highlight = (ImageView) findViewById(R.id.btn_RightFrontUp_highlight);
        this.btn_LeftFrontDown_highlight = (ImageView) findViewById(R.id.btn_LeftFrontDown_highlight);
        this.btn_RightFrontDown_highlight = (ImageView) findViewById(R.id.btn_RightFrontDown_highlight);
        this.btn_LeftBackUp_highlight = (ImageView) findViewById(R.id.btn_LeftBackUp_highlight);
        this.btn_RightBackUp_highlight = (ImageView) findViewById(R.id.btn_RightBackUp_highlight);
        this.btn_LeftBackDown_highlight = (ImageView) findViewById(R.id.btn_LeftBackDown_highlight);
        this.btn_RightBackDown_highlight = (ImageView) findViewById(R.id.btn_RightBackDown_highlight);
        this.btn_01_IgnitionOff_highlight = (ImageView) findViewById(R.id.btn_01_highlight_IgnitionOff);
        this.btn_02_IgnitionOff_highlight = (ImageView) findViewById(R.id.btn_02_highlight_IgnitionOff);
        this.btn_03_IgnitionOff_highlight = (ImageView) findViewById(R.id.btn_03_highlight_IgnitionOff);
        this.btn_AllDown_IgnitionOff_highlight = (ImageView) findViewById(R.id.btn_alldown_highlight_IgnitionOff);
        this.btn_01_IgnitionOff = (IlevelButton) findViewById(R.id.btn_01_IgnitionOff);
        this.btn_02_IgnitionOff = (IlevelButton) findViewById(R.id.btn_02_IgnitionOff);
        this.btn_03_IgnitionOff = (IlevelButton) findViewById(R.id.btn_03_IgnitionOff);
        this.btn_AllDown_IgnitionOff = (IlevelButton) findViewById(R.id.btn_AllDown_IgnitionOff);
        this.rLayoutFor_IgnitionOFF = (RelativeLayout) findViewById(R.id.rLayoutFor_IgnitionOff);
        this.rLayoutFor_IgnitionON = (RelativeLayout) findViewById(R.id.rLayoutFor_IgnitionOn);
        this.btn_Menu.setOnClickListener(this);
        this.btn_01.setOnTouchListener(this);
        this.btn_02.setOnTouchListener(this);
        this.btn_03.setOnTouchListener(this);
        this.btn_Dot.setOnTouchListener(this);
        this.btn_Dot.setSampleLongpress(this);
        this.btn_AllDown.setOnLongClickListener(this);
        this.btn_01_IgnitionOff.setOnClickListener(this);
        this.btn_02_IgnitionOff.setOnClickListener(this);
        this.btn_03_IgnitionOff.setOnClickListener(this);
        this.btn_01_IgnitionOff.setOnTouchListener(this);
        this.btn_02_IgnitionOff.setOnTouchListener(this);
        this.btn_03_IgnitionOff.setOnTouchListener(this);
        this.btn_AllDown_IgnitionOff.setOnLongClickListener(this);
        this.btn_FrontUp.setOnTouchListener(this);
        this.btn_FrontUp.setSampleLongpress(this);
        this.btn_FrontDown.setOnTouchListener(this);
        this.btn_FrontDown.setSampleLongpress(this);
        this.btn_LeftFrontUp.setOnTouchListener(this);
        this.btn_LeftFrontUp.setSampleLongpress(this);
        this.btn_LeftFrontDown.setOnTouchListener(this);
        this.btn_LeftFrontDown.setSampleLongpress(this);
        this.btn_RightFrontUp.setOnTouchListener(this);
        this.btn_RightFrontUp.setSampleLongpress(this);
        this.btn_RightFrontDown.setOnTouchListener(this);
        this.btn_RightFrontDown.setSampleLongpress(this);
        this.btn_BackUp.setOnTouchListener(this);
        this.btn_BackUp.setSampleLongpress(this);
        this.btn_BackDown.setOnTouchListener(this);
        this.btn_BackDown.setSampleLongpress(this);
        this.btn_LeftBackUp.setOnTouchListener(this);
        this.btn_LeftBackUp.setSampleLongpress(this);
        this.btn_LeftBackDown.setOnTouchListener(this);
        this.btn_LeftBackDown.setSampleLongpress(this);
        this.btn_RightBackUp.setOnTouchListener(this);
        this.btn_RightBackUp.setSampleLongpress(this);
        this.btn_RightBackDown.setOnTouchListener(this);
        this.btn_RightBackDown.setSampleLongpress(this);
        this.led_flash = (AnimationDrawable) this.btn_01_highlight.getBackground();
        this.all_down_flash = (AnimationDrawable) this.btn_AllDown_highlight.getBackground();
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        File imgFile = new File(Environment.getExternalStorageDirectory().getPath() + "/ilevel_background.jpg");
        new File(Environment.getExternalStorageDirectory().getPath() + "/ilevel_background_landscape.jpg");
        if (imgFile.exists()) {
            ((ImageView) findViewById(R.id.ivMainScreen)).setImageBitmap(BitmapFactory.decodeFile(imgFile.getAbsolutePath()));
        }
        if (!this.scalingComplete) {
            scaleContents(findViewById(R.id.rLayoutFor_MainScreen), findViewById(R.id.drawer_layout));
            this.scalingComplete = true;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (!this.showError) {
            this.application.stopTimerTask();
            if (this.homeScreenCounter != null) {
                this.homeScreenCounter.cancel();
            }
            System.exit(0);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 82) {
            return super.onKeyDown(keyCode, event);
        }
        if (this.mDrawerLayout.isDrawerOpen(5)) {
            this.mDrawerLayout.closeDrawer(5);
        } else {
            this.mDrawerLayout.openDrawer(5);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        Log.i("status", "HomeScreen onResume isCancelled = false");
        this.application.isCancelled = false;
        if (this.application.networkThread.getSocket() == null) {
            this.application.checkWifiStatus();
        }
        if (this.application.networkThread.isClosed()) {
            NetworkThread networkThread = this.application.networkThread;
            ILevelApplication iLevelApplication = this.application;
            String str = ILevelApplication.SERVERIP;
            ILevelApplication iLevelApplication2 = this.application;
            networkThread.startConnection(str, (int) ILevelApplication.SERVERPORT);
        }
        Intent intent2 = getIntent();
        if (intent2 != null) {
            this.ABORT_CALIBRATION = intent2.getBooleanExtra("ABORT_CALIBRATION_ERROR", false);
            this.POSITION_SAVED = intent2.getIntExtra("POSITION_SAVED", 0);
            if (this.POSITION_SAVED > 0) {
                showPositionSavedDialog(this.POSITION_SAVED);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if (HomeScreen.this.positionSavedDialog != null && HomeScreen.this.positionSavedDialog.isShowing()) {
                            HomeScreen.this.positionSavedDialog.dismiss();
                        }
                    }
                }, 4000);
            }
            getIntent().removeExtra("ABORT_CALIBRATION_ERROR");
            getIntent().removeExtra("POSITION_SAVED");
        }
        this.lastErrorCode = 0;
        IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        this.screenStateReceiver = new ScreenStateReceiver();
        registerReceiver(this.screenStateReceiver, filter);
        this.removeErrorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String error = intent.getStringExtra("ERROR_CODE");
                if (error.trim().length() > 0 && error.equalsIgnoreCase("00") && HomeScreen.this.dialog != null && HomeScreen.this.dialog.isShowing()) {
                    Log.i("calE", "1 cal flag= " + HomeScreen.this.calibrationErrorFlag);
                    if (!HomeScreen.this.calibrationErrorFlag) {
                        HomeScreen.this.dialog.dismiss();
                    }
                }
                int unused = HomeScreen.this.lastErrorCode = 0;
                HomeScreen.this.all_down_flash.stop();
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_LeftFrontUp_highlight.setVisibility(4);
                HomeScreen.this.btn_RightFrontUp_highlight.setVisibility(4);
                HomeScreen.this.btn_LeftFrontDown_highlight.setVisibility(4);
                HomeScreen.this.btn_RightFrontDown_highlight.setVisibility(4);
                HomeScreen.this.btn_LeftBackUp_highlight.setVisibility(4);
                HomeScreen.this.btn_RightBackUp_highlight.setVisibility(4);
                HomeScreen.this.btn_LeftBackDown_highlight.setVisibility(4);
                HomeScreen.this.btn_RightBackDown_highlight.setVisibility(4);
            }
        };
        this.networkErrorDialog = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (HomeScreen.this.alertDialog == null) {
                    HomeScreen.this.showNetworkError();
                } else if (HomeScreen.this.alertDialog != null && !HomeScreen.this.alertDialog.isShowing()) {
                    HomeScreen.this.showNetworkError();
                }
            }
        };
        this.checkIgnitionStatus = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    boolean unused = HomeScreen.this.IS_IGNITION_ON = intent.getBooleanExtra("IGNITION", false);
                    HomeScreen.this.setUIIgnitionOnOff(HomeScreen.this.IS_IGNITION_ON);
                }
            }
        };
        this.heightIndicatorReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    HomeScreen.this.setButtonHighlited(intent.getIntExtra("BUTTON", 1000));
                }
            }
        };
        this.errorIndicatorReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    int error = intent.getIntExtra("ERROR", 1000);
                    int globalCornerIndicator = 0;
                    if (error == 2 || error == 7) {
                        globalCornerIndicator = (int) intent.getLongExtra("GLOBAL_CORNER_INDICATOR", 0);
                    }
                    int unused = HomeScreen.this.tempErrorCode = error;
                    if (!(HomeScreen.this.tempErrorCode == HomeScreen.this.previousErrorCode || HomeScreen.this.dialog == null || !HomeScreen.this.dialog.isShowing())) {
                        Log.i("calE", "2 cal flag= " + HomeScreen.this.calibrationErrorFlag);
                        if (!HomeScreen.this.calibrationErrorFlag) {
                            HomeScreen.this.dialog.dismiss();
                        }
                    }
                    int unused2 = HomeScreen.this.previousErrorCode = error;
                    if (!HomeScreen.this.EXIT_PRESSED) {
                        HomeScreen.this.showError(error, globalCornerIndicator);
                    }
                }
            }
        };
        this.errorDialogReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String error = intent.getStringExtra("ERROR_DIALOG");
                    if (HomeScreen.this.alertDialog == null) {
                        HomeScreen.this.showErrorMessage(error);
                        boolean unused = HomeScreen.this.showError = true;
                    } else if (HomeScreen.this.alertDialog != null && !HomeScreen.this.alertDialog.isShowing()) {
                        boolean unused2 = HomeScreen.this.showError = true;
                        HomeScreen.this.showErrorMessage(error);
                    }
                }
            }
        };
        this.passwordErrorReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                HomeScreen.this.showWrongPasswordError();
            }
        };
        this.updatePasswordReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                HomeScreen.this.changePassword();
            }
        };
        IntentFilter noError = new IntentFilter("com.iLevel.NoError");
        registerReceiver(this.removeErrorDialogReceiver, noError);
        IntentFilter networkError = new IntentFilter("com.iLevel.NETWORK_ERROR");
        registerReceiver(this.networkErrorDialog, networkError);
        IntentFilter intentFilter = new IntentFilter("com.iLevel.ScreenToShow");
        registerReceiver(this.checkIgnitionStatus, intentFilter);
        IntentFilter heightIndicator = new IntentFilter("com.iLevel.Button.Highlited");
        registerReceiver(this.heightIndicatorReceiver, heightIndicator);
        IntentFilter errorIndicator = new IntentFilter("com.iLevel.ErrorIndicator");
        registerReceiver(this.errorIndicatorReceiver, errorIndicator);
        IntentFilter errorDialog = new IntentFilter("com.iLevel.error");
        registerReceiver(this.errorDialogReceiver, errorDialog);
        IntentFilter passwordFilter = new IntentFilter("com.iLevel.Password.Incorrect");
        registerReceiver(this.passwordErrorReceiver, passwordFilter);
        IntentFilter updatePassword = new IntentFilter("com.iLevel.Password.Update");
        registerReceiver(this.updatePasswordReceiver, updatePassword);
        IntentFilter wifiFilter = new IntentFilter("android.net.wifi.supplicant.STATE_CHANGE");
        registerReceiver(this.wifiStatusReceiver, wifiFilter);
        Log.i("Version", "On Resume request version");
        this.application.STOP_VERSION_REQUEST = false;
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.removeErrorDialogReceiver != null) {
                unregisterReceiver(this.removeErrorDialogReceiver);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.networkErrorDialog != null) {
                unregisterReceiver(this.networkErrorDialog);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (this.checkIgnitionStatus != null) {
                unregisterReceiver(this.checkIgnitionStatus);
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        try {
            if (this.heightIndicatorReceiver != null) {
                unregisterReceiver(this.heightIndicatorReceiver);
            }
        } catch (Exception e6) {
            e6.printStackTrace();
        }
        try {
            if (this.errorIndicatorReceiver != null) {
                unregisterReceiver(this.errorIndicatorReceiver);
            }
        } catch (Exception e7) {
            e7.printStackTrace();
        }
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
        try {
            if (this.passwordErrorReceiver != null) {
                unregisterReceiver(this.passwordErrorReceiver);
            }
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        try {
            if (this.updatePasswordReceiver != null) {
                unregisterReceiver(this.updatePasswordReceiver);
            }
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        try {
            if (this.wifiStatusReceiver != null) {
                unregisterReceiver(this.wifiStatusReceiver);
            }
        } catch (Exception e11) {
            e11.printStackTrace();
        }
        Log.i("System", "HomeScreen - onPause - isCancelled=true");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.application.networkThread.stopConnection();
        this.application.stopTimerTask();
        if (this.timerForWi_Fi != null) {
            this.timerForWi_Fi.cancel();
            this.timerForWi_Fi.purge();
        }
        if (this.homeScreenCounter != null) {
            this.homeScreenCounter.cancel();
        }
        try {
            if (this.removeErrorDialogReceiver != null) {
                unregisterReceiver(this.removeErrorDialogReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (this.networkErrorDialog != null) {
                unregisterReceiver(this.networkErrorDialog);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            if (this.checkIgnitionStatus != null) {
                unregisterReceiver(this.checkIgnitionStatus);
            }
        } catch (Exception e4) {
            e4.printStackTrace();
        }
        try {
            if (this.heightIndicatorReceiver != null) {
                unregisterReceiver(this.heightIndicatorReceiver);
            }
        } catch (Exception e5) {
            e5.printStackTrace();
        }
        try {
            if (this.errorIndicatorReceiver != null) {
                unregisterReceiver(this.errorIndicatorReceiver);
            }
        } catch (Exception e6) {
            e6.printStackTrace();
        }
        try {
            if (this.errorDialogReceiver != null) {
                unregisterReceiver(this.errorDialogReceiver);
            }
        } catch (Exception e7) {
            e7.printStackTrace();
        }
        try {
            if (this.passwordErrorReceiver != null) {
                unregisterReceiver(this.passwordErrorReceiver);
            }
        } catch (Exception e8) {
            e8.printStackTrace();
        }
        try {
            if (this.updatePasswordReceiver != null) {
                unregisterReceiver(this.updatePasswordReceiver);
            }
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e10) {
            e10.printStackTrace();
        }
        try {
            if (this.screenStateReceiver != null) {
                unregisterReceiver(this.screenStateReceiver);
            }
        } catch (Exception e11) {
            e11.printStackTrace();
        }
        super.onDestroy();
    }

    @SuppressLint({"NewApi"})
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == 20) {
            this.application.stopTimerTask();
            if (this.homeScreenCounter != null) {
                this.homeScreenCounter.cancel();
            }
        }
    }

    public void onClick(View view) {
        if (view == this.btn_Menu && !this.showError) {
            this.mDrawerLayout.openDrawer(5);
        }
    }

    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_AllDown:
                this.mCounter = 0;
                this.mHandlerLongPressed.post(this.mRunnable_btn_All_Down);
                break;
            case R.id.btn_AllDown_IgnitionOff:
                this.mCounter = 0;
                this.mHandlerLongPressed.post(this.mRunnable_btn_AllDown_IgnitionOff);
                IGNITION_OFF_COMMAND = true;
                this.homeScreenCounter.cancel();
                this.homeScreenCounter.start();
                break;
        }
        return true;
    }

    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == 0) {
            switch (view.getId()) {
                case R.id.btn_01:
                    this.btn_01.setBackgroundResource(R.drawable.selected_button_1);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_01Pressed);
                    break;
                case R.id.btn_01_IgnitionOff:
                    this.btn_01_IgnitionOff.setBackgroundResource(R.drawable.selected_button_1);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_01_IgnitionOff);
                    break;
                case R.id.btn_02:
                    this.btn_02.setBackgroundResource(R.drawable.selected_button_2);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_02Pressed);
                    break;
                case R.id.btn_02_IgnitionOff:
                    this.btn_02_IgnitionOff.setBackgroundResource(R.drawable.selected_button_2);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_02_IgnitionOff);
                    break;
                case R.id.btn_03:
                    this.btn_03.setBackgroundResource(R.drawable.selected_button_3);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_03Pressed);
                    break;
                case R.id.btn_03_IgnitionOff:
                    this.btn_03_IgnitionOff.setBackgroundResource(R.drawable.selected_button_3);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_03_IgnitionOff);
                    break;
                case R.id.btn_BackDown:
                    this.buttonPress |= 160;
                    this.btn_BackDown.setBackgroundResource(R.drawable.selected_button_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_BackUp:
                    this.buttonPress |= 80;
                    this.btn_BackUp.setBackgroundResource(R.drawable.selected_button_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_Dot:
                    this.btn_DotPressed = true;
                    this.TRIM_PRESSED = true;
                    this.btn_Dot.setBackgroundResource(R.drawable.selected_button_dot);
                    this.mHandlerLongPressed.post(this.mRunnable_btn_Dot);
                    if (this.btn_01_highlight.getVisibility() != 0) {
                        if (this.btn_02_highlight.getVisibility() != 0) {
                            if (this.btn_03_highlight.getVisibility() == 0) {
                                this.toast = Toast.makeText(this.myContext, "Trimming", 0);
                                this.toast.setGravity(48, 20, this.btn_03.getTop() + 20);
                                this.toast.show();
                                break;
                            }
                        } else {
                            this.toast = Toast.makeText(this.myContext, "Trimming", 0);
                            this.toast.setGravity(48, 20, this.btn_02.getTop() + 20);
                            this.toast.show();
                            break;
                        }
                    } else {
                        this.toast = Toast.makeText(this.myContext, "Trimming", 0);
                        this.toast.setGravity(48, 20, this.btn_01.getTop() + 20);
                        this.toast.show();
                        break;
                    }
                    break;
                case R.id.btn_FrontDown:
                    this.buttonPress |= 10;
                    this.btn_FrontDown.setBackgroundResource(R.drawable.selected_button_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_FrontUp:
                    this.buttonPress |= 5;
                    this.btn_FrontUp.setBackgroundResource(R.drawable.selected_button_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftBackDown:
                    this.buttonPress |= 32;
                    this.btn_LeftBackDown.setBackgroundResource(R.drawable.button_selected_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftBackUp:
                    this.buttonPress |= 16;
                    this.btn_LeftBackUp.setBackgroundResource(R.drawable.button_selected_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftFrontDown:
                    this.buttonPress |= 2;
                    this.btn_LeftFrontDown.setBackgroundResource(R.drawable.button_selected_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_LeftFrontUp:
                    this.buttonPress |= 1;
                    this.btn_LeftFrontUp.setBackgroundResource(R.drawable.button_selected_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightBackDown:
                    this.buttonPress |= 128;
                    this.btn_RightBackDown.setBackgroundResource(R.drawable.button_selected_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightBackUp:
                    this.buttonPress |= 64;
                    this.btn_RightBackUp.setBackgroundResource(R.drawable.button_selected_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightFrontDown:
                    this.buttonPress |= 8;
                    this.btn_RightFrontDown.setBackgroundResource(R.drawable.button_selected_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
                case R.id.btn_RightFrontUp:
                    this.buttonPress |= 4;
                    this.btn_RightFrontUp.setBackgroundResource(R.drawable.button_selected_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                    }
                    this.lastButtonPressed = this.buttonPress;
                    break;
            }
        }
        if (event.getAction() == 1 || event.getAction() == 3) {
            Log.i(this.valveTag, "Action UP or CANCEL");
            switch (view.getId()) {
                case R.id.btn_01:
                    this.btn_01.setBackgroundResource(R.drawable.button_1);
                    break;
                case R.id.btn_01_IgnitionOff:
                    this.btn_01_IgnitionOff.setBackgroundResource(R.drawable.button_1);
                    break;
                case R.id.btn_02:
                    this.btn_02.setBackgroundResource(R.drawable.button_2);
                    break;
                case R.id.btn_02_IgnitionOff:
                    this.btn_02_IgnitionOff.setBackgroundResource(R.drawable.button_2);
                    break;
                case R.id.btn_03:
                    this.btn_03.setBackgroundResource(R.drawable.button_3);
                    break;
                case R.id.btn_03_IgnitionOff:
                    this.btn_03_IgnitionOff.setBackgroundResource(R.drawable.button_3);
                    break;
                case R.id.btn_BackDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -161);
                    this.btn_BackDown.setBackgroundResource(R.drawable.button_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_BackUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -81);
                    this.btn_BackUp.setBackgroundResource(R.drawable.button_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_Dot:
                    this.btn_Dot.setBackgroundResource(R.drawable.button_dot);
                    this.mHandlerLongPressed.removeCallbacks(this.mRunnable_btn_Dot);
                    cancelLongPress();
                    this.application.sendButtonReleaseCommand();
                    break;
                case R.id.btn_FrontDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -11);
                    this.btn_FrontDown.setBackgroundResource(R.drawable.button_down);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_FrontUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -6);
                    this.btn_FrontUp.setBackgroundResource(R.drawable.button_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_LeftBackDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -33);
                    this.btn_LeftBackDown.setBackgroundResource(R.drawable.button_middle_dn);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_LeftBackUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -17);
                    this.btn_LeftBackUp.setBackgroundResource(R.drawable.button_middle_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_LeftFrontDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -3);
                    this.btn_LeftFrontDown.setBackgroundResource(R.drawable.button_middle_dn);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_LeftFrontUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -2);
                    this.btn_LeftFrontUp.setBackgroundResource(R.drawable.button_middle_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_RightBackDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -129);
                    this.btn_RightBackDown.setBackgroundResource(R.drawable.button_middle_dn);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_RightBackUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -65);
                    this.btn_RightBackUp.setBackgroundResource(R.drawable.button_middle_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_RightFrontDown:
                    this.buttonPress = (int) (((long) this.buttonPress) & -9);
                    this.btn_RightFrontDown.setBackgroundResource(R.drawable.button_middle_dn);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
                case R.id.btn_RightFrontUp:
                    this.buttonPress = (int) (((long) this.buttonPress) & -5);
                    this.btn_RightFrontUp.setBackgroundResource(R.drawable.button_middle_up);
                    if (this.lastButtonPressed == 0) {
                        this.mHandlerLongPressed.post(this.mRunnable_valveControl);
                        break;
                    }
                    break;
            }
        }
        return true;
    }

    public void cancelLongPress() {
        this.mDown = false;
        this.mCounter = 0;
        if (this.btn_DotPressed) {
            this.btn_DotPressed = false;
            if (this.toast != null) {
                this.toast.cancel();
            }
        }
    }

    /* access modifiers changed from: private */
    public void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        View view = LayoutInflater.from(this.myContext).inflate(R.layout.password_dialog, (ViewGroup) null);
        builder.setView(view);
        builder.setCancelable(false);
        final EditText etPassword = (EditText) view.findViewById(R.id.etUpdatePassword);
        builder.setTitle("Update Password");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
            }
        });
        builder.setNegativeButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String password = etPassword.getText().toString();
                if (password.trim().length() > 0) {
                    HomeScreen.this.application.stopTimerTask();
                    HomeScreen.this.application.savePassword(password);
                    HomeScreen.this.alertDialog.dismiss();
                    ILevelApplication access$3500 = HomeScreen.this.application;
                    access$3500.getClass();
                    new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                    return;
                }
                Toast.makeText(HomeScreen.this.myContext, "Please enter password.", 1).show();
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    /* access modifiers changed from: private */
    public void setUIIgnitionOnOff(boolean ignitionState) {
        if (ignitionState) {
            this.rLayoutFor_IgnitionON.setVisibility(0);
            this.btn_Menu.setVisibility(0);
            this.rLayoutFor_IgnitionOFF.setVisibility(8);
            this.mDrawerLayout.setDrawerLockMode(0);
        } else if (this.wifi_connected) {
            this.rLayoutFor_IgnitionON.setVisibility(8);
            this.btn_Menu.setVisibility(8);
            this.rLayoutFor_IgnitionOFF.setVisibility(0);
            this.mDrawerLayout.setDrawerLockMode(1);
        }
    }

    /* access modifiers changed from: protected */
    public void showWrongPasswordError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("Your password appears to be incorrect. Please go to menu Setting and change it.");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
                Intent unused = HomeScreen.this.intent = new Intent(HomeScreen.this, PasswordAlt.class);
                HomeScreen.this.mDrawerLayout.closeDrawer(5);
                if (!HomeScreen.this.showError) {
                    HomeScreen.this.startActivity(HomeScreen.this.intent);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    /* access modifiers changed from: protected */
    public void setButtonHighlited(int heightIndicator) {
        Log.i(this.TAG, "Button pressed " + heightIndicator);
        switch (heightIndicator) {
            case 0:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    break;
                }
                break;
            case 1:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(0);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(0);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    this.btn_01_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                    this.btn_01_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                    break;
                }
                break;
            case 2:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(0);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(0);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                    this.btn_02_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                    break;
                }
                break;
            case 3:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(0);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(0);
                    this.btn_03_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                    this.btn_03_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                    break;
                }
                break;
            case 101:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(0);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(0);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    if (this.IS_IGNITION_ON) {
                        this.btn_01_highlight.setBackgroundResource(R.drawable.btn1_flash);
                        this.btn_01_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_01_highlight.getBackground();
                    } else {
                        this.btn_01_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                        this.btn_01_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn1_flash);
                        this.led_flash = (AnimationDrawable) this.btn_01_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            case 102:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(0);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(0);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    if (this.IS_IGNITION_ON) {
                        this.btn_02_highlight.setBackgroundResource(R.drawable.btn2_flash);
                        this.btn_02_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_02_highlight.getBackground();
                    } else {
                        this.btn_02_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                        this.btn_02_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn2_flash);
                        this.led_flash = (AnimationDrawable) this.btn_02_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            case 103:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(0);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(0);
                    if (this.IS_IGNITION_ON) {
                        this.btn_03_highlight.setBackgroundResource(R.drawable.btn3_flash);
                        this.btn_03_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_03_highlight.getBackground();
                    } else {
                        this.btn_03_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                        this.btn_03_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn3_flash);
                        this.led_flash = (AnimationDrawable) this.btn_03_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            case 201:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(0);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(0);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    if (this.IS_IGNITION_ON) {
                        this.btn_01_highlight.setBackgroundResource(R.drawable.btn1_fast_flash);
                        this.btn_01_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_01_highlight.getBackground();
                    } else {
                        this.btn_01_highlight.setBackgroundResource(R.drawable.button_1_highlited);
                        this.btn_01_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn1_fast_flash);
                        this.led_flash = (AnimationDrawable) this.btn_01_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            case 202:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(0);
                    this.btn_03_highlight.setVisibility(4);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(0);
                    this.btn_03_IgnitionOff_highlight.setVisibility(4);
                    if (this.IS_IGNITION_ON) {
                        this.btn_02_highlight.setBackgroundResource(R.drawable.btn2_fast_flash);
                        this.btn_02_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_02_highlight.getBackground();
                    } else {
                        this.btn_02_highlight.setBackgroundResource(R.drawable.button_2_highlited);
                        this.btn_02_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn2_fast_flash);
                        this.led_flash = (AnimationDrawable) this.btn_02_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            case 203:
                if (this.led_status != heightIndicator) {
                    this.led_flash.stop();
                    this.btn_01_highlight.setVisibility(4);
                    this.btn_02_highlight.setVisibility(4);
                    this.btn_03_highlight.setVisibility(0);
                    this.btn_01_IgnitionOff_highlight.setVisibility(4);
                    this.btn_02_IgnitionOff_highlight.setVisibility(4);
                    this.btn_03_IgnitionOff_highlight.setVisibility(0);
                    if (this.IS_IGNITION_ON) {
                        this.btn_03_highlight.setBackgroundResource(R.drawable.btn3_fast_flash);
                        this.btn_03_IgnitionOff_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                        this.led_flash = (AnimationDrawable) this.btn_03_highlight.getBackground();
                    } else {
                        this.btn_03_highlight.setBackgroundResource(R.drawable.button_3_highlited);
                        this.btn_03_IgnitionOff_highlight.setBackgroundResource(R.drawable.btn3_fast_flash);
                        this.led_flash = (AnimationDrawable) this.btn_03_IgnitionOff_highlight.getBackground();
                    }
                    this.led_flash.start();
                    break;
                }
                break;
            default:
                this.led_flash.stop();
                this.btn_01_highlight.setVisibility(4);
                this.btn_02_highlight.setVisibility(4);
                this.btn_03_highlight.setVisibility(4);
                this.btn_01_IgnitionOff_highlight.setVisibility(4);
                this.btn_02_IgnitionOff_highlight.setVisibility(4);
                this.btn_03_IgnitionOff_highlight.setVisibility(4);
                break;
        }
        this.led_status = heightIndicator;
    }

    /* access modifiers changed from: private */
    public void showError(int errorCode, int globalCornerIndicator) {
        if (errorCode != 99) {
            this.all_down_flash.stop();
            this.btn_AllDown_highlight.setVisibility(4);
            this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
        }
        switch (errorCode) {
            case 1:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(1);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(1);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(1);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(1);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 2:
                if (errorCode != this.lastErrorCode) {
                    if (this.dialog == null) {
                        if (this.ABORT_CALIBRATION) {
                            showAlertAbortCalibration(2);
                            this.lastErrorCode = errorCode;
                        } else {
                            showAlert(2);
                            this.lastErrorCode = errorCode;
                        }
                    } else if (!this.dialog.isShowing()) {
                        if (this.ABORT_CALIBRATION) {
                            showAlertAbortCalibration(2);
                            this.lastErrorCode = errorCode;
                        } else {
                            showAlert(2);
                            this.lastErrorCode = errorCode;
                        }
                    }
                }
                Log.i(this.TAG, "setUIForHomeScreen Fault 2 or 7 - corner indicator=" + globalCornerIndicator);
                if ((globalCornerIndicator & 256) != 0) {
                    this.btn_LeftFrontUp_highlight.setVisibility(0);
                } else {
                    this.btn_LeftFrontUp_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 512) != 0) {
                    this.btn_RightFrontUp_highlight.setVisibility(0);
                } else {
                    this.btn_RightFrontUp_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 4096) != 0) {
                    this.btn_LeftFrontDown_highlight.setVisibility(0);
                } else {
                    this.btn_LeftFrontDown_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 8192) != 0) {
                    this.btn_RightFrontDown_highlight.setVisibility(0);
                } else {
                    this.btn_RightFrontDown_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 1024) != 0) {
                    this.btn_LeftBackUp_highlight.setVisibility(0);
                } else {
                    this.btn_LeftBackUp_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 2048) != 0) {
                    this.btn_RightBackUp_highlight.setVisibility(0);
                } else {
                    this.btn_RightBackUp_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 16384) != 0) {
                    this.btn_LeftBackDown_highlight.setVisibility(0);
                } else {
                    this.btn_LeftBackDown_highlight.setVisibility(4);
                }
                if ((32768 & globalCornerIndicator) != 0) {
                    this.btn_RightBackDown_highlight.setVisibility(0);
                    return;
                } else {
                    this.btn_RightBackDown_highlight.setVisibility(4);
                    return;
                }
            case 7:
                if (errorCode != this.lastErrorCode) {
                    if (this.dialog == null) {
                        if (this.ABORT_CALIBRATION) {
                            showAlertAbortCalibration(7);
                            this.lastErrorCode = errorCode;
                        } else {
                            showAlert(7);
                            this.lastErrorCode = errorCode;
                        }
                    } else if (!this.dialog.isShowing()) {
                        if (this.ABORT_CALIBRATION) {
                            showAlertAbortCalibration(7);
                            this.lastErrorCode = errorCode;
                        } else {
                            showAlert(7);
                            this.lastErrorCode = errorCode;
                        }
                    }
                }
                Log.i(this.TAG, "setUIForHomeScreen Fault 2 or 7 - corner indicator=" + globalCornerIndicator);
                if ((globalCornerIndicator & 1) != 0) {
                    this.btn_LeftFrontUp_highlight.setVisibility(0);
                    this.btn_LeftFrontDown_highlight.setVisibility(0);
                } else {
                    this.btn_LeftFrontUp_highlight.setVisibility(4);
                    this.btn_LeftFrontDown_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 2) != 0) {
                    this.btn_RightFrontUp_highlight.setVisibility(0);
                    this.btn_RightFrontDown_highlight.setVisibility(0);
                } else {
                    this.btn_RightFrontUp_highlight.setVisibility(4);
                    this.btn_RightFrontDown_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 4) != 0) {
                    this.btn_LeftBackUp_highlight.setVisibility(0);
                    this.btn_LeftBackDown_highlight.setVisibility(0);
                } else {
                    this.btn_LeftBackUp_highlight.setVisibility(4);
                    this.btn_LeftBackDown_highlight.setVisibility(4);
                }
                if ((globalCornerIndicator & 8) != 0) {
                    this.btn_RightBackUp_highlight.setVisibility(0);
                    this.btn_RightBackDown_highlight.setVisibility(0);
                    return;
                }
                this.btn_RightBackUp_highlight.setVisibility(4);
                this.btn_RightBackDown_highlight.setVisibility(4);
                return;
            case 8:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(8);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(8);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(8);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(8);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 11:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(11);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(11);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(11);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(11);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 55:
                this.homeScreenCounter.cancel();
                IGNITION_OFF_COMMAND = false;
                this.IS_IGNITION_ON = false;
                this.application.IS_REGULAR_UPDATE = false;
                setUIIgnitionOnOff(this.IS_IGNITION_ON);
                if (this.in != null && this.in.getView() != null) {
                    this.in.clearAnimation();
                    return;
                }
                return;
            case 67:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(67);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(67);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(67);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(67);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 68:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(68);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(68);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(68);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(68);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 89:
                if (errorCode == this.lastErrorCode) {
                    return;
                }
                if (this.dialog == null) {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(89);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(89);
                    this.lastErrorCode = errorCode;
                    return;
                } else if (this.dialog.isShowing()) {
                    return;
                } else {
                    if (this.ABORT_CALIBRATION) {
                        showAlertAbortCalibration(89);
                        this.lastErrorCode = errorCode;
                        return;
                    }
                    showAlert(89);
                    this.lastErrorCode = errorCode;
                    return;
                }
            case 99:
                if (this.dialog != null && this.dialog.isShowing()) {
                    Log.i("calE", "3 cal flag= " + this.calibrationErrorFlag);
                    if (!this.calibrationErrorFlag) {
                        this.dialog.dismiss();
                    }
                }
                setButtonHighlited(0);
                if (this.IS_IGNITION_ON) {
                    this.btn_AllDown_highlight.setVisibility(0);
                    this.all_down_flash = (AnimationDrawable) this.btn_AllDown_highlight.getBackground();
                    this.all_down_flash.start();
                    return;
                }
                this.btn_AllDown_IgnitionOff_highlight.setVisibility(0);
                this.all_down_flash = (AnimationDrawable) this.btn_AllDown_IgnitionOff_highlight.getBackground();
                this.all_down_flash.start();
                return;
            default:
                return;
        }
    }

    private void showAlert(int i) {
        this.dialog = new Dialog(this.myContext);
        this.dialog.requestWindowFeature(1);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.setContentView(R.layout.error_alert);
        TextView txtErrorTitle = (TextView) this.dialog.findViewById(R.id.txt_ErrorName);
        TextView txtErrorDiscription = (TextView) this.dialog.findViewById(R.id.txt_ErrorDiscription);
        final TextView txtErrorDetailDiscription = (TextView) this.dialog.findViewById(R.id.txt_ErrorDetailDscription);
        Button btnInfo = (Button) this.dialog.findViewById(R.id.btn_MoreInfo);
        switch (i) {
            case 1:
                txtErrorTitle.setText(getResources().getString(R.string.error_1_tank_pressure_warning));
                txtErrorDiscription.setText(getResources().getString(R.string.error_1_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_1_detail_discription));
                break;
            case 2:
                txtErrorTitle.setText(getResources().getString(R.string.error_2_volve_operation_warning));
                txtErrorDiscription.setText(getResources().getString(R.string.error_2_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_2_detail_discription));
                break;
            case 7:
                txtErrorTitle.setText(getResources().getString(R.string.error_7_ride_height));
                txtErrorDiscription.setText(getResources().getString(R.string.error_7_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_7_detail_discription));
                break;
            case 8:
                txtErrorTitle.setText(getResources().getString(R.string.error_8_Communication));
                txtErrorDiscription.setText(getResources().getString(R.string.error_8_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_8_detail_discription));
                break;
            case 11:
                txtErrorTitle.setText(getResources().getString(R.string.error_11_pressure_senser));
                txtErrorDiscription.setText(getResources().getString(R.string.error_11_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_11_detail_discription));
                break;
            case 67:
                txtErrorTitle.setText(getResources().getString(R.string.error_67_high_voltage));
                txtErrorDiscription.setText(getResources().getString(R.string.error_67_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_67_detail_discription));
                break;
            case 68:
                txtErrorTitle.setText(getResources().getString(R.string.error_68_low_voltage));
                txtErrorDiscription.setText(getResources().getString(R.string.error_68_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_68_detail_discription));
                break;
            case 89:
                txtErrorTitle.setText(getResources().getString(R.string.error_89_height_sensor_travel_warnign));
                txtErrorDiscription.setText(getResources().getString(R.string.error_89_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_89_detail_discription));
                break;
        }
        btnInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (HomeScreen.this.isVisible) {
                    txtErrorDetailDiscription.setVisibility(8);
                    boolean unused = HomeScreen.this.isVisible = false;
                    return;
                }
                txtErrorDetailDiscription.setVisibility(0);
                boolean unused2 = HomeScreen.this.isVisible = true;
            }
        });
        this.dialog.getWindow().setGravity(48);
        this.dialog.show();
    }

    private void showAlertAbortCalibration(int i) {
        this.dialog = new Dialog(this.myContext);
        this.dialog.requestWindowFeature(1);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialog.setContentView(R.layout.abort_calibration_error);
        TextView txtErrorTitle = (TextView) this.dialog.findViewById(R.id.txt_ErrorName_AbortCalibrationError);
        TextView txtErrorDiscription = (TextView) this.dialog.findViewById(R.id.txt_ErrorDiscription_AbortCalibrationError);
        TextView txtErrorDetailDiscription = (TextView) this.dialog.findViewById(R.id.txt_ErrorDetailDscription_AbortCalibrationError);
        Button btnCancle = (Button) this.dialog.findViewById(R.id.btnCancle_AbortCalibrationError);
        this.calibrationErrorFlag = true;
        this.lastErrorCode = i;
        switch (i) {
            case 1:
                txtErrorTitle.setText(getResources().getString(R.string.error_1_tank_pressure_warning));
                txtErrorDiscription.setText(getResources().getString(R.string.error_1_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_1_detail_discription));
                break;
            case 2:
                txtErrorTitle.setText(getResources().getString(R.string.error_2_volve_operation_warning));
                txtErrorDiscription.setText(getResources().getString(R.string.error_2_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_2_detail_discription));
                break;
            case 7:
                txtErrorTitle.setText(getResources().getString(R.string.error_7_ride_height));
                txtErrorDiscription.setText(getResources().getString(R.string.error_7_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_7_detail_discription));
                break;
            case 8:
                txtErrorTitle.setText(getResources().getString(R.string.error_8_Communication));
                txtErrorDiscription.setText(getResources().getString(R.string.error_8_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_8_detail_discription));
                break;
            case 11:
                txtErrorTitle.setText(getResources().getString(R.string.error_11_pressure_senser));
                txtErrorDiscription.setText(getResources().getString(R.string.error_11_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_11_detail_discription));
                break;
            case 67:
                txtErrorTitle.setText(getResources().getString(R.string.error_67_high_voltage));
                txtErrorDiscription.setText(getResources().getString(R.string.error_67_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_67_detail_discription));
                break;
            case 68:
                txtErrorTitle.setText(getResources().getString(R.string.error_68_low_voltage));
                txtErrorDiscription.setText(getResources().getString(R.string.error_68_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_68_detail_discription));
                break;
            case 89:
                txtErrorTitle.setText(getResources().getString(R.string.error_89_height_sensor_travel_warnign));
                txtErrorDiscription.setText(getResources().getString(R.string.error_89_discription));
                txtErrorDetailDiscription.setText(getResources().getString(R.string.error_89_detail_discription));
                break;
        }
        btnCancle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean unused = HomeScreen.this.calibrationErrorFlag = false;
                HomeScreen.this.dialog.cancel();
                boolean unused2 = HomeScreen.this.EXIT_PRESSED = true;
            }
        });
        this.dialog.getWindow().setGravity(48);
        this.dialog.show();
    }

    private void showPositionSavedDialog(int POSITION_SAVED2) {
        this.positionSavedDialog = new Dialog(this.myContext);
        this.positionSavedDialog.requestWindowFeature(1);
        this.positionSavedDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.positionSavedDialog.setContentView(R.layout.position_saved_dialog);
        ((TextView) this.positionSavedDialog.findViewById(R.id.txtPosition)).setText("Position " + POSITION_SAVED2 + " Saved !");
        this.positionSavedDialog.getWindow().setGravity(48);
        this.positionSavedDialog.show();
    }

    private class AnimationIn implements Animation.AnimationListener {
        Animation in;
        Animation out;
        private View view;

        private AnimationIn() {
            this.in = AnimationUtils.loadAnimation(HomeScreen.this.myContext, R.anim.fade_in);
            this.out = AnimationUtils.loadAnimation(HomeScreen.this.myContext, R.anim.fade_out);
        }

        public void setView(View view2) {
            this.view = view2;
        }

        public void setAnimaton() {
            this.view.setAnimation(this.in);
            this.view.startAnimation(this.in);
        }

        public View getView() {
            if (this.view == null) {
                this.view = null;
            }
            return this.view;
        }

        public void clearAnimation() {
            this.view.clearAnimation();
            this.in.cancel();
            this.out.cancel();
            if (this.view == HomeScreen.this.btn_01) {
                HomeScreen.this.btn_01_highlight.setVisibility(0);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_02) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(0);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_03) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(0);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_01_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_02_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_03_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_AllDown) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(0);
            } else if (this.view == HomeScreen.this.btn_AllDown_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(0);
            }
        }

        public void onAnimationEnd(Animation animation) {
            this.view.setAnimation(this.out);
            this.view.startAnimation(this.out);
            if (animation != this.out) {
                return;
            }
            if (this.view == HomeScreen.this.btn_01) {
                HomeScreen.this.btn_01_highlight.setVisibility(0);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_02) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(0);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_03) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(0);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_01_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_02_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_03_IgnitionOff) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(0);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(4);
            } else if (this.view == HomeScreen.this.btn_AllDown) {
                HomeScreen.this.btn_01_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_02_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_03_IgnitionOff_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_IgnitionOff_highlight.setVisibility(0);
            } else if (this.view == HomeScreen.this.btn_AllDown_IgnitionOff) {
                HomeScreen.this.btn_01_highlight.setVisibility(4);
                HomeScreen.this.btn_02_highlight.setVisibility(4);
                HomeScreen.this.btn_03_highlight.setVisibility(4);
                HomeScreen.this.btn_AllDown_highlight.setVisibility(0);
            }
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationStart(Animation animation) {
        }
    }

    /* access modifiers changed from: private */
    public void showErrorMessage(String error) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("There was an error while attempting to make a connection: The operation couldn't be completed :-" + error);
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
                ILevelApplication access$3500 = HomeScreen.this.application;
                access$3500.getClass();
                new ILevelApplication.OpenSocketConnection().execute(new String[0]);
                boolean unused = HomeScreen.this.showError = false;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
                ILevelApplication.VERSION_INFO = false;
                boolean unused = HomeScreen.this.showError = false;
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    public void showNetworkError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.myContext);
        builder.setTitle("Error");
        builder.setMessage("Please check your network connectivity.");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
                HomeScreen.this.application.checkWifiStatus();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                HomeScreen.this.alertDialog.dismiss();
            }
        });
        this.alertDialog = builder.create();
        this.alertDialog.show();
    }

    private class HomeScreenCounter extends CountDownTimer {
        public HomeScreenCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            HomeScreen.IGNITION_OFF_COMMAND = false;
        }

        public void onTick(long millisUntilFinished) {
        }
    }

    private class ScreenStateReceiver extends BroadcastReceiver {
        private ScreenStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
                HomeScreen.this.application.stopTimerTask();
            } else if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
                HomeScreen.this.application.checkWifiStatus();
            }
        }
    }

    private class DrawerItemClickListener implements AdapterView.OnItemClickListener {
        private DrawerItemClickListener() {
        }

        public void onItemClick(AdapterView parent, View view, int position, long id) {
            if (position == 1) {
                Intent unused = HomeScreen.this.intent = new Intent(HomeScreen.this, Calibration.class);
                HomeScreen.this.mDrawerLayout.closeDrawer(5);
                if (!HomeScreen.this.showError) {
                    HomeScreen.this.startActivity(HomeScreen.this.intent);
                }
            } else if (position == 2) {
                Intent unused2 = HomeScreen.this.intent = new Intent(HomeScreen.this, Save_Heights.class);
                HomeScreen.this.mDrawerLayout.closeDrawer(5);
                if (!HomeScreen.this.showError) {
                    HomeScreen.this.startActivity(HomeScreen.this.intent);
                }
            } else if (position == 3) {
                Intent unused3 = HomeScreen.this.intent = new Intent(HomeScreen.this, Settings.class);
                HomeScreen.this.mDrawerLayout.closeDrawer(5);
                if (!HomeScreen.this.showError) {
                    HomeScreen.this.startActivity(HomeScreen.this.intent);
                }
            } else if (position == 4) {
                if (HomeScreen.this.wifi_connected) {
                    Intent unused4 = HomeScreen.this.intent = new Intent(HomeScreen.this, Password.class);
                } else {
                    Intent unused5 = HomeScreen.this.intent = new Intent(HomeScreen.this, PasswordAlt.class);
                }
                HomeScreen.this.mDrawerLayout.closeDrawer(5);
                if (!HomeScreen.this.showError) {
                    HomeScreen.this.startActivity(HomeScreen.this.intent);
                }
            }
        }
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void scaleContents(View rootView, View container) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getApplicationContext().getApplicationContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        float scale = Math.min(((float) screenWidth) / 1300.0f, ((float) screenHeight) / 2350.0f);
        scaleViewAndChildren(rootView, scale);
        Log.i("notcloud", "Container Width=" + container.getWidth() + " view Width=" + screenWidth);
        Log.i("notcloud", "Container Height=" + container.getHeight() + " view Height=" + screenHeight);
        Log.i("notcloud", "Scaling Factor=" + scale);
    }

    public static void scaleViewAndChildren(View root, float scale) {
        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
        if (!(layoutParams.width == -1 || layoutParams.width == -2)) {
            layoutParams.width = (int) (((float) layoutParams.width) * scale);
        }
        if (!(layoutParams.height == -1 || layoutParams.height == -2)) {
            layoutParams.height = (int) (((float) layoutParams.height) * scale);
        }
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) layoutParams;
            marginParams.leftMargin = (int) (((float) marginParams.leftMargin) * scale);
            marginParams.rightMargin = (int) (((float) marginParams.rightMargin) * scale);
            marginParams.topMargin = (int) (((float) marginParams.topMargin) * scale);
            marginParams.bottomMargin = (int) (((float) marginParams.bottomMargin) * scale);
        }
        root.setLayoutParams(layoutParams);
        root.setPadding((int) (((float) root.getPaddingLeft()) * scale), (int) (((float) root.getPaddingTop()) * scale), (int) (((float) root.getPaddingRight()) * scale), (int) (((float) root.getPaddingBottom()) * scale));
        if (root instanceof TextView) {
            TextView textView = (TextView) root;
            Log.d("Calculator", "Scaling text size from " + textView.getTextSize() + " to " + (textView.getTextSize() * scale));
            textView.setTextSize(textView.getTextSize() * scale);
        }
        if (root instanceof ViewGroup) {
            ViewGroup groupView = (ViewGroup) root;
            for (int cnt = 0; cnt < groupView.getChildCount(); cnt++) {
                scaleViewAndChildren(groupView.getChildAt(cnt), scale);
            }
        }
    }
}
