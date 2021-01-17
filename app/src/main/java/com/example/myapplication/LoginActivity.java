package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.*;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Tasks.LoginTask;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText edtID, edtPwd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        edtID = findViewById(R.id.edtUserID);
        edtPwd = findViewById(R.id.edtPassword);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    LoginTask networkLoginTask = new LoginTask(handler);

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userid", edtID.getText().toString());
                    params.put("pw", edtPwd.getText().toString());

                    networkLoginTask.execute(params);

                } catch (Exception e) {

                    Toast.makeText(LoginActivity.this, "아이디 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:
                    Toast.makeText(LoginActivity.this, "아이디 패스워드를 확인하세요.", Toast.LENGTH_SHORT).show();
                    break;
                case 0:

                    SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);

                    SharedPreferences.Editor editor = pref.edit();

                    editor.putString("id", edtID.getText().toString());
                    editor.putString("pw", edtPwd.getText().toString());

                    editor.commit();

                    String prefDevice = pref.getString("device_id", null);
                    String prefFnick = pref.getString("fnick", null);

                    if (prefDevice == null) {
                        Intent intent = new Intent(LoginActivity.this, WifiActivity.class);
                        intent.putExtra("userid", edtID.getText().toString());
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("device_id", prefDevice);
                        intent.putExtra("fnick", prefFnick);
                        startActivity(intent);
                        finish();
                    }

                    break;

            }

        }
    };


}
