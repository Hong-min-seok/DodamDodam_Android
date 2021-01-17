package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DTO.DeviceDataDTO;
import com.example.myapplication.Service.BackService;
import com.example.myapplication.Tasks.MainTask;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    TextView temptv, moistv, humitv, memberInfo;
    String tempvalue, humivalue, moisvalue;
    String device_id, fnick, f_shape;
    Button btnFname, btnMygarden, btndecorate, btnRegisdevice, btnCalendar;
    ExtendedFloatingActionButton imgMic, imgVideo, imgMain;

    ConstraintLayout layout_drawer;

    TextView txtInMsg;

    ImageView flower, ivEmoji;

    Button btnOpen;
    FrameLayout frameLayout;
    Boolean check;
    Animation animation;

    private DrawerLayout drawerLayout;
    private Intent serviceIntent;
    public static Context mContext;
    private Animation fab_open, fab_close;
    private boolean isToggle = false;

    Intent sttIntent;
    SpeechRecognizer mRecognizer;
    TextToSpeech tts;
    final int PERMISSION = 1;

    final String TAG = "BackService";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnOpen = findViewById(R.id.btnOpen);
        frameLayout = findViewById(R.id.frameLayout);
        check = false;

        btnOpen.setOnClickListener(new toogle());
        frameLayout.setOnClickListener(new toogle());

        layout_drawer = findViewById(R.id.layout_drawer);

        mContext = this;

        temptv = findViewById(R.id.tempText);
        moistv = findViewById(R.id.moisText);
        humitv = findViewById(R.id.humiText);
        btnFname = findViewById(R.id.btn_fname);

        btnMygarden = findViewById(R.id.btnMygarden);
        btndecorate = findViewById(R.id.btndecorate);
        btnRegisdevice = findViewById(R.id.btnRegisdevice);
        btnCalendar = findViewById(R.id.btnCalendar);

        memberInfo = findViewById(R.id.memberInfo);

        txtInMsg = findViewById(R.id.txtInMsg);

        flower = findViewById(R.id.flower);
        ivEmoji = findViewById(R.id.ivEmoji);


        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale);

        flower.setOnClickListener(new SayHello());

        Intent intent = getIntent();

        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);

        device_id = intent.getStringExtra("device_id");
        fnick = intent.getStringExtra("fnick");
        String user_id = pref.getString("id", null);
        f_shape = pref.getString("f_shape", "pot1");
        memberInfo.setText(user_id);

        setBackGround(pref.getString("theme", "back1"));
        setPotImg(f_shape);

        imgMic = findViewById(R.id.mic);
        imgVideo = findViewById(R.id.video);
        imgMain = findViewById(R.id.fabMain);

        btnMygarden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("userid", pref.getString("id", null));
                startActivity(intent);
            }
        });

        btndecorate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DesignActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        btnRegisdevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WifiActivity.class);
                intent.putExtra("userid", pref.getString("id", null));
                startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CalActivity.class);
                intent.putExtra("device_id", device_id);
                startActivity(intent);
            }
        });


        //회원의 장치아이디들(식물정보들) 가져와야함

        try {
            //해당 아이디를 넘겨야함
            MainTask networkMainTask = new MainTask(handler);
            Map<String, String> params = new HashMap<String, String>();
            params.put("device_id", device_id.trim());

            networkMainTask.execute(params);
        } catch (Exception e) {
            Log.e("MainActivity Exception발생", e.toString());
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarWifi);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24); //뒤로가기 버튼 이미지 지정

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return false;
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("ddd", "logout");

                SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit();

                /* 실행중인 process 목록 보기*/
                ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
                List<ActivityManager.RunningAppProcessInfo> appList = am.getRunningAppProcesses();
                ActivityManager.RunningAppProcessInfo r;
                for (int i = 0; i < appList.size(); i++) {
                    ActivityManager.RunningAppProcessInfo rapi = appList.get(i);
                    Log.d("run Process", "Package Name : " + rapi.processName);
                }
                r = appList.get(0);
                Log.d("run Process", "Package Name : " + r.processName);


                finishAffinity();
                System.exit(0);
//                moveTaskToBack(true);
//                finishAndRemoveTask();
                android.os.Process.killProcess(r.pid);

                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.cancelAll();

            }
        });


        // 오디오, 카메라 권한설정
        if (Build.VERSION.SDK_INT >= 23) {
            // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }

        // STT, TTS 로드
        speechInit();

        imgMain.setOnClickListener(new FabToggle());

        imgMic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechStart();
            }
        });

        imgVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(in);
            }
        });
    }

    class FabToggle implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            toggle_fab();

        }
    }

    private void toggle_fab() {

        if (isToggle) {

            imgMain.setIconResource(R.drawable.ic_baseline_add_24);
            imgVideo.startAnimation(fab_close);
            imgMic.startAnimation(fab_close);
            imgVideo.setClickable(false);
            imgMic.setClickable(false);
            isToggle = false;
        } else {

            imgMain.setIconResource(R.drawable.ic_baseline_close_24);
            imgVideo.startAnimation(fab_open);
            imgMic.startAnimation(fab_open);
            imgVideo.setClickable(true);
            imgMic.setClickable(true);
            isToggle = true;

        }

    }

    private void immortalService() {
        Log.d("BackService", "in immortalService()");
        //        --------------백그라운드 처리용 부분------------
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);
        boolean isWhiteListing = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d(TAG, "immortalService: if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)");
            isWhiteListing = pm.isIgnoringBatteryOptimizations(getApplicationContext().getPackageName());
        }

        if (!isWhiteListing) {
            Log.d(TAG, "immortalService: !isWhiteListing");
            Intent in = new Intent();
            in.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
            in.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivity(in);
        }

        if (BackService.serviceIntent == null) {
            Log.d(TAG, "immortalService: if (BackService.serviceIntent == null)");
            serviceIntent = new Intent(getApplicationContext(), BackService.class);
            startService(serviceIntent);
        } else {
            serviceIntent = BackService.serviceIntent;
            Log.d("service", "service is already");
        }
//        -----------------------------------------------
    }

    private void setBackGround(String theme) {

        int intHour = getHours();
        int[] imgBack1 = {R.drawable.back1_morning, R.drawable.back1_afternoon, R.drawable.back1_night};
        int[] imgBack2 = {R.drawable.back2_morning, R.drawable.back2_afternoon, R.drawable.back2_night};
        int[] imgBack3 = {R.drawable.back3_morning, R.drawable.back3_afternoon, R.drawable.back3_night};
        int index = 0;

        if (intHour >= 6 && intHour < 16) {
            index = 0;
        } else if (intHour >= 16 && intHour < 20) {
            index = 1;
        } else {
            index = 2;
        }

        Log.d(TAG, "setBackGround: " + intHour);

        switch (theme) {

            case "back1":
                layout_drawer.setBackgroundResource(imgBack1[index]);
                break;
            case "back2":
                layout_drawer.setBackgroundResource(imgBack2[index]);
                break;
            case "back3":
                layout_drawer.setBackgroundResource(imgBack3[index]);
                break;
            case "back4":
                layout_drawer.setBackgroundResource(R.drawable.back4);
                break;

        }
    }

    private void setPotImg(String f_shape) {
        switch (f_shape) {
            case "pot1":
                flower.setImageResource(R.drawable.pot1);
                break;
            case "pot2":
                flower.setImageResource(R.drawable.pot2);
                break;
            case "pot3":
                flower.setImageResource(R.drawable.pot3);
                break;
            case "pot4":
                flower.setImageResource(R.drawable.pot4);
                break;
            case "pot5":
                flower.setImageResource(R.drawable.pot5);
                break;
            case "pot6":
                flower.setImageResource(R.drawable.pot6);
                break;
            case "pot7":
                flower.setImageResource(R.drawable.pot7);
                break;
        }
    }

    private int getHours() {

        SimpleDateFormat sdfNow = new SimpleDateFormat("HH");
        String stringHour = sdfNow.format(new Date(System.currentTimeMillis()));
        Log.d(TAG, "getHours: " + stringHour);
        return Integer.parseInt(stringHour);
    }

    class SayHello implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            funcVoiceOut("안녕 나는 " + fnick + "이야. 만나서 반가워");

//            flower.setAnimation(animation);
//            Log.d(TAG, "onClick: ");

        }
    }

    class toogle implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            ObjectAnimator animator;

            switch (v.getId()) {
                case R.id.btnOpen:
                    animator = ObjectAnimator.ofFloat(frameLayout, "translationX", 350f);
                    animator.setDuration(1000);
                    animator.start();
                    frameLayout.setVisibility(View.VISIBLE);
                    btnOpen.setVisibility(View.INVISIBLE);
                    break;
                case R.id.frameLayout:
                    animator = ObjectAnimator.ofFloat(frameLayout, "translationX", -350f);
                    animator.setDuration(1000);
                    animator.start();
                    frameLayout.setVisibility(View.INVISIBLE);
                    btnOpen.setVisibility(View.VISIBLE);
                    break;
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            String img = data.getStringExtra("img");

            int intHour = getHours();

            SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            int[] imgBack1 = {R.drawable.back1_morning, R.drawable.back1_afternoon, R.drawable.back1_night};
            int[] imgBack2 = {R.drawable.back2_morning, R.drawable.back2_afternoon, R.drawable.back2_night};
            int[] imgBack3 = {R.drawable.back3_morning, R.drawable.back3_afternoon, R.drawable.back3_night};
            int index = 0;

            if (intHour >= 6 && intHour < 16) {
                index = 0;
            } else if (intHour >= 16 && intHour < 20) {
                index = 1;
            } else {
                index = 2;
            }

            switch (img) {

                case "back1":
                    editor.putString("theme", "back1");
                    editor.commit();
                    layout_drawer.setBackgroundResource(imgBack1[index]);
                    break;
                case "back2":
                    editor.putString("theme", "back2");
                    editor.commit();
                    layout_drawer.setBackgroundResource(imgBack2[index]);
                    break;
                case "back3":
                    editor.putString("theme", "back3");
                    editor.commit();
                    layout_drawer.setBackgroundResource(imgBack3[index]);
                    break;
                case "back4":
                    editor.putString("theme", "back4");
                    editor.commit();
                    layout_drawer.setBackgroundResource(R.drawable.back4);
                    break;
            }
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:
                    Log.e("MainActivity-handler", "온습도 값을 가져오지 못했습니다.");
                    break;
                case 0:
                    Log.d("msg.obj", msg.obj.toString());
                    DeviceDataDTO dto = (DeviceDataDTO) msg.obj;

                    SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);

                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("fnick", fnick);
                    editor.commit();

                    tempvalue = String.valueOf(dto.getTemp());
                    humivalue = String.valueOf(dto.getHumi());
                    moisvalue = String.valueOf(dto.getMois());

                    btnFname.setText(fnick);
                    temptv.setText(tempvalue + "℃");
                    humitv.setText(humivalue + "%");
                    moistv.setText(moisvalue + "%");

                    int[] emojiId = {R.drawable.emoji_normal1, R.drawable.emoji_normal2, R.drawable.emoji_superhappy};

                    Random random = new Random();

                    if (dto.getTemp() >= 40) {
                        ivEmoji.setImageResource(R.drawable.emoji_veryhot);
                    } else if (dto.getTemp() < 40 && dto.getTemp() >= 30) {
                        ivEmoji.setImageResource(R.drawable.emoji_hot);
                    } else if (dto.getTemp() < 15 && dto.getTemp() >= 5) {
                        ivEmoji.setImageResource(R.drawable.emoji_cold);
                    } else if (dto.getTemp() < 5) {
                        ivEmoji.setImageResource(R.drawable.emoji_verycold);
                    } else {
                        ivEmoji.setImageResource(emojiId[random.nextInt(3)]);
                    }

                    immortalService();

                    btnFname.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                            intent.putExtra("device_id", device_id);
                            startActivity(intent);
                        }
                    });

                    break;

            }

        }
    };


    private void speechInit() {
        // stt 객체 생성, 초기화
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        // tts 객체 생성, 초기화
        tts = new TextToSpeech(MainActivity.this, this);
    }


    public void speechStart() {
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(mContext); // 음성인식 객체
        mRecognizer.setRecognitionListener(listener); // 음성인식 리스너 등록
        mRecognizer.startListening(sttIntent);
    }


    private RecognitionListener listener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(), "음성인식을 시작합니다.", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBeginningOfSpeech() {
        }

        @Override
        public void onRmsChanged(float rmsdB) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
        }

        @Override
        public void onError(int error) {
            String message;

            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT:
                    message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간초과";
                    break;
                default:
                    message = "알 수 없는 오류임";
                    break;
            }
            String guideStr = "에러가 발생하였습니다.";
            Toast.makeText(getApplicationContext(), guideStr + message, Toast.LENGTH_SHORT).show();
            funcVoiceOut(guideStr);
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            String resultStr = "";

            for (int i = 0; i < matches.size(); i++) {
                txtInMsg.setText(matches.get(i));
                resultStr += matches.get(i);
            }

            if (resultStr.length() < 1) return;
            resultStr = resultStr.replace(" ", "");

            moveActivity(resultStr);
        }

        @Override
        public void onPartialResults(Bundle partialResults) {
        }

        @Override
        public void onEvent(int eventType, Bundle params) {
        }
    };

    //입력 텍스트 구분
    public void moveActivity(String resultStr) {
        if (resultStr.contains("안녕")) {
            String guideStr = "반가워";
            Toast.makeText(getApplicationContext(), guideStr, Toast.LENGTH_SHORT).show();
            funcVoiceOut(guideStr);

        } else if (resultStr.contains("무슨일")) {
            String guideStr = "아무일 없어";

            List<String> guideArr = new ArrayList<>();
            String temp = temptv.getText().toString();
            String humi = humitv.getText().toString();

            float t = Float.parseFloat(temp.substring(0, temp.indexOf("℃")));
            int h = Integer.parseInt(humi.substring(0, humi.indexOf("%")));

            //온도가 15도 이하일 경우
            if (t <= 15.f) {
                guideStr = "너무 추워";
                guideArr.add(guideStr);
            } else if (t >= 36.f) {
                guideStr = "너무 더워";
                guideArr.add(guideStr);
            }

            //습도가 15% 이하일 경오
            if (h <= 15) {
                guideStr = "너무 건조해";
                guideArr.add(guideStr);
            } else if (h >= 80) {
                guideStr = "너무 습해";
                guideArr.add(guideStr);
            }

            if (guideArr.size() >= 1)
                funcVoiceOut(String.join(", 그리고", guideArr));
            else {
                funcVoiceOut(guideStr);
            }

        }
    }

    public void funcVoiceOut(String OutMsg) {
        if (OutMsg.length() < 1) return;
        if (!tts.isSpeaking()) {
            tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.KOREAN);
            tts.setPitch(1);
        } else {
            Log.e("TTS", "초기화 실패");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        if (mRecognizer != null) {
            mRecognizer.destroy();
            mRecognizer.cancel();
            mRecognizer = null;
        }

        if (serviceIntent != null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

}