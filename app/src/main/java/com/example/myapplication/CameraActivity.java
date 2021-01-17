package com.example.myapplication;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class CameraActivity extends AppCompatActivity {
    WebView wvCCTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        wvCCTV = findViewById(R.id.wvCCTV);
        wvCCTV.setWebViewClient(new WebViewClient());
        wvCCTV.getSettings().setUseWideViewPort(true);
        wvCCTV.getSettings().setLoadWithOverviewMode(true);
        wvCCTV.getSettings().setSupportZoom(true);
        wvCCTV.loadUrl("http://192.168.0.125:5000/");

    }
}
