package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoadingActivity extends AppCompatActivity {

    TextView tvLoadingName;
    TextView tvLoadingContent;
    String key[] = {"수선화", "제비꽃", "장미", "해바라기", "코스모스", "민들레", "개나리", "수국", "물망초", "다알리아", "라넌큘러스", "아이리스"};
    String value[] = {"<수선화-류시화>\n\n여기 수선화가 있다, 남몰래\n숨겨 놓은 신부가\n\n나는 제주 바닷가에 핀\n흰 수선화 곁을 지나간다\n\n오래 전에 누군가 숨겨 놓고는 잊어 버린\n신부 곁을"
            , "<제비꽃- 나태주>\n\n그대 떠난 자리에\n나 혼자 남아\n쓸쓸한 날\n제비꽃이 피었습니다\n다른 날보다 더 예쁘게\n피었습니다."
            , "<장미꽃-서윤덕>\n\n태양보다 더 밝다\n초록을 더 초록으로 빛내주는 장미\n맑은날도 흐린날도 변함없이\n초록과 어울려 예쁜색과 고운향을 선물한다\n나는 장미꽃같은 사람이 좋다"
            , "<해바라기- 오순택>\n\n벌과 나비\n앉으라고\n노란 방석\n펴 놓았죠"
            , "<코스모스-윤동주 中>\n\n코스모스는\n귀뚜리 울음에도 수줍어지고,\n\n코스모스 앞에 선 나는\n어렸을 적처럼 부끄러워지나니,\n\n내 마음은 코스모스의 마음이요\n코스모스의 마음은 내 마음이다."
            , "<민들레- 이해인>\n\n꽃씨만한 행복을 이마에 얹고\n바람에게 준 마음 후회 없어라\n혼자서 생각하다 혼자서 별을 헤다\n땅에서 하늘에서 다시 피는 민들레"
            , "<개나리-이은상>\n\n매화꽃 졌다 하신 편지를 받자옵고,\n개나리 한창이라 대답을 보내었소.\n둘이 다 봄이란 말은 차마 쓰지 못하고"
            , "<수국을 보며-이해인 中>\n\n혼자서 여름을 앓던\n내 안에도 오늘은\n푸르디 푸른\n한다발의 희망이 피네\n\n수국처럼 둥근 웃음\n내 이웃들의 웃음이\n꽃무더기로 쏟아지네"
            , "<물망초-김남조 中>\n\n당신이 간 후\n바람곁에 내버린 꽃빛 연보라는\n못 잊어 넋을 우는 물망초지만\n기억해 주어요\n지금은 눈도 먼 물망초지만."
            , "당신의 마음을 알아 기쁩니다"
            , "매혹,매력,당신은 매력적입니다"
            , "사랑의 메시지, 기쁜 소식"};

    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
    Map<String, String> map = new HashMap<>();

    LinearLayout llLoading;
    boolean flag = true;
    int cnt = 0;
    final String TAG = "BackService";

    Handler handler;

    ImageView ivLoading;

    private Intent serviceIntent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);

        llLoading = findViewById(R.id.llLoading);

        ivLoading = findViewById(R.id.ivLoadingGif);
        Glide.with(this).load(R.raw.loading_gif).override(25,25).into(ivLoading);

        ImageView ivLoading02 = findViewById(R.id.ivLoading02);
        ivLoading02.setAnimation(animation);


        handler = new Handler();

        for (int i = 0; i < key.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(key[i], value[i]);
            list.add(map);
        }
        Map<String, String> text = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            int random = (int) (Math.random() * list.size());

            text = list.get(random);

        }
        tvLoadingName = findViewById(R.id.tvLoadingName);
        tvLoadingContent = findViewById(R.id.tvLoadingContent);

        Set set = text.keySet();

        String key = "";
        for (Object o : set) {
            key = o.toString();
            Log.d("ddd", key);
        }
        tvLoadingName.setText(key);
        tvLoadingContent.setText(text.get(key));

        startLoading();

//
//            int random = (int) (Math.random() * 10);
//        }
//            Map<String,String> m=list.get(random);
        // Log.d("ddd", String.valueOf(m));

    }

    private void startLoading() {

        SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
        String prefId = pref.getString("id", null);
        String prefPw = pref.getString("pw", null);
        String prefFnick = pref.getString("fnick", null);
        String prefDevice = pref.getString("device_id", null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                if (prefId == null || prefPw == null) {

                    Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
                    startActivity(intent);

                } else {

                    Log.d("loading", prefId);
                    Log.d("loading", prefPw);

                    if (prefDevice == null) {
                        Log.d("loading", "prefDevice is null");
                        Intent intent = new Intent(LoadingActivity.this, WifiActivity.class);
                        intent.putExtra("userid", prefId);
                        startActivity(intent);
                        finish();
                    } else if (prefFnick == null) {

                        Intent intent = new Intent(LoadingActivity.this, ListActivity.class);
                        intent.putExtra("userid", prefId);
                        startActivity(intent);
                        finish();

                    } else {

//                        immortalService();
                        Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                        intent.putExtra("device_id", prefDevice);
                        intent.putExtra("fnick", prefFnick);
                        startActivity(intent);
                        finish();
                    }

                }

            }

        }, 1000*5);

//        Thread thread = new Thread(null, runnable);
//        thread.start();


    }

//    protected Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//
//            while (flag) {
//                try {
//
//                    cnt++;
//
////                    runOnUiThread(new Runnable() {
////                        @Override
////                        public void run() {
////                            ImageView iv = new ImageView(getApplicationContext());
////                            iv.setImageResource(R.drawable.flower_loading);
////                            LayoutParams params = new LayoutParams(70, 70);
////                            params.setMargins(10, 10, 10, 10);
////                            iv.setLayoutParams(params);
//////                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
//////                            iv.setAnimation(animation);
////                            llLoading.addView(iv);
////
////                        }
////                    });
//
//
//                    Log.d(TAG, "run: " + cnt);
//
//                    if (cnt == 5) {
//                        flag = false;
//                    }
//
//                    Log.d(TAG, "run: " + flag);
//
//                    Thread.sleep(1000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            handler.post(in);
//
//        }
//    };
//
//    protected Runnable in = new Runnable() {
//        @Override
//        public void run() {
//
//            SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
//            String prefId = pref.getString("id", null);
//            String prefPw = pref.getString("pw", null);
//            String prefFnick = pref.getString("fnick", null);
//            String prefDevice = pref.getString("device_id", null);
//
//
//            if (prefId == null || prefPw == null) {
//
//                Intent intent = new Intent(LoadingActivity.this, LoginActivity.class);
//                startActivity(intent);
//
//            } else {
//
//                Log.d("loading", prefId);
//                Log.d("loading", prefPw);
//
//                if (prefDevice == null) {
//                    Log.d("loading", "prefDevice is null");
//                    Intent intent = new Intent(LoadingActivity.this, WifiActivity.class);
//                    intent.putExtra("userid", prefId);
//                    startActivity(intent);
//                    finish();
//                } else if (prefFnick == null) {
//
//                    Intent intent = new Intent(LoadingActivity.this, ListActivity.class);
//                    intent.putExtra("userid", prefId);
//                    startActivity(intent);
//                    finish();
//
//                } else {
//
////                        immortalService();
//                    Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
//                    intent.putExtra("device_id", prefDevice);
//                    intent.putExtra("fnick", prefFnick);
//                    startActivity(intent);
//                    finish();
//                }
//
//
//            }
//
//
//        }
//    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: onDestroy()");
    }
}