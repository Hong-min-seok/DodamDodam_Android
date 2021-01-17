package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DesignActivity extends AppCompatActivity {

    RadioGroup rgDesign;
    RadioButton rb1, rb2, rb3, rb4;
    ImageView ivDesign;
    String img = "back1";
    Button btnDesign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design);

        rgDesign = findViewById(R.id.rgDesign);
        ivDesign = findViewById(R.id.ivDesign);

        GradientDrawable drawable = (GradientDrawable) getDrawable(R.drawable.img_round);

        ivDesign.setBackground(drawable);
        ivDesign.setClipToOutline(true);

        btnDesign = findViewById(R.id.btnDesign);

        rgDesign.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case R.id.rb1:
                        img = "back1";
                        ivDesign.setImageResource(R.drawable.back1_morning);
                        break;
                    case R.id.rb2:
                        img = "back2";
                        ivDesign.setImageResource(R.drawable.back2_morning);
                        break;
                    case R.id.rb3:
                        img = "back3";
                        ivDesign.setImageResource(R.drawable.back3_morning);
                        break;
                    case R.id.rb4:
                        img = "back4";
                        ivDesign.setImageResource(R.drawable.back4);
                        break;
                }

            }
        });

        btnDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.putExtra("img", img);
                Log.d("radio", "onClick: " + img);
                setResult(RESULT_OK, in);
                finish();

            }
        });

    }
}
