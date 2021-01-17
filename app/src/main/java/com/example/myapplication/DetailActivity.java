package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DTO.DetailDTO;
import com.example.myapplication.Tasks.DetailTask;

import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    TextView f_nick;
    TextView f_name;
    ImageView f_shape;
    String device_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        f_nick = findViewById(R.id.f_nick);
        f_name = findViewById(R.id.f_name);
        f_shape = findViewById(R.id.f_shape);

        Intent intent = getIntent();
        device_id = intent.getStringExtra("device_id");
        f_name.setMovementMethod(new ScrollingMovementMethod());

        try {
            DetailTask networkDetailTask = new DetailTask(handler);

            Map<String, String> params = new HashMap<String, String>();
            params.put("device_id", device_id);

            networkDetailTask.execute(params);

        }catch (Exception e){

            Toast.makeText(DetailActivity.this, "에러 발생!.", Toast.LENGTH_SHORT).show();

        }



    }


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){

            switch (msg.what){

                case -1:
                    Toast.makeText(DetailActivity.this, "잘못 불러왔어요!!.", Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    DetailDTO detail = (DetailDTO) msg.obj;
                    String result = detail.getF_nick();
                    String result1 = detail.getF_name();
                    String result2 = detail.getF_shape();
                    String result3 = detail.getF_lang();
                    String result4 = detail.getF_use();
                    Log.d("ddd", result);
                    Log.d("ddd", result1);
                    Log.d("ddd", result2);


                    f_nick.setText(detail.getF_nick());
                    f_name.setText("* 종 명: "+detail.getF_name()+"\n\n* 꽃말: "+result3+"\n\n* 사용법: "+result4);
//                    f_shape.setImageResource(R.drawable.pot3);
                    switch(detail.getF_shape()){
                        case "pot1":
                            f_shape.setImageResource(R.drawable.pot1);
                            break;
                        case "pot2":
                            f_shape.setImageResource(R.drawable.pot2);
                            break;
                        case "pot3":
                            f_shape.setImageResource(R.drawable.pot3);
                            break;
                        case "pot4":
                            f_shape.setImageResource(R.drawable.pot4);
                            break;
                        case "pot5":
                            f_shape.setImageResource(R.drawable.pot5);
                            break;
                        case "pot6":
                            f_shape.setImageResource(R.drawable.pot6);
                            break;
                        case "pot7":
                            f_shape.setImageResource(R.drawable.pot7);
                            break;


                    }


                    break;

            }

        }
    };

}
