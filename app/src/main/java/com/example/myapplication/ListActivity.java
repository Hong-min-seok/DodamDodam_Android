package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.DTO.flowerDTO;
import com.example.myapplication.Tasks.ListTask;

import me.relex.circleindicator.CircleIndicator3;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ListActivity extends AppCompatActivity {
    private ViewPager2 f_shape;
    private CircleIndicator3 mIndicator;
    TextView f_nick;
    TextView f_divice_id;
    private String userid;
    Button btnGoMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
//        init();

        Intent intent = getIntent();
        userid = intent.getStringExtra("userid");
        Log.d("ddd",userid);

        btnGoMain = findViewById(R.id.btnGoMain);

        f_nick = findViewById(R.id.f_nick);
        f_shape = findViewById(R.id.f_shape);
        // f_divice_id = findViewById(R.id.f_device_id);
        try {

            ListTask listTask = new ListTask(handler);
            Map<String, String> params = new HashMap<String, String>();
            Log.d("ddd", userid);
            params.put("userid", userid);

            listTask.execute(params);

        }catch (Exception e){



        }
    }

    public void init(Map<String, flowerDTO> registerDeviceInfo){
        ArrayList<Map<String, String>> list = new ArrayList<>();
        Set set = registerDeviceInfo.keySet();

        for (Object o : set) {
            String key = o.toString();
            Log.d("ddd", key);
            list.add((Map<String, String>)registerDeviceInfo.get(key));

        }
//        list.add(new flowerDTO(R.drawable.pot3.png));

        f_shape = findViewById(R.id.f_shape);
        f_shape.setAdapter(new ViewPagerAdapter(list));

        mIndicator = (CircleIndicator3) findViewById(R.id.indicator);
        mIndicator.setViewPager(f_shape);
        mIndicator.createIndicators(registerDeviceInfo.size(),0);

        f_shape.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mIndicator.animatePageSelected(position);
            }
        });
    }

    public class ViewPagerAdapter extends RecyclerView.Adapter<ViewHolderPage> {
        private ArrayList<Map<String, String>> listData;
        ViewPagerAdapter(ArrayList<Map<String, String>> data) {
            this.listData = data;
        }
        @Override
        public ViewHolderPage onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_viewpager, parent,false);
            return new ViewHolderPage(view);
        }
        @Override
        public void onBindViewHolder(ViewHolderPage holder, int position) {
            if(holder instanceof ViewHolderPage){
                ViewHolderPage viewHolder = (ViewHolderPage) holder;
                viewHolder.onBind((Map<String, String>)listData.get(position));
            }
        }
        @Override
        public int getItemCount() {
            return listData.size();
        }
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case -1:

                    break;
                case 0:
                    Map<String, flowerDTO> registerDeviceInfo = (Map<String, flowerDTO>) msg.obj;

                    init(registerDeviceInfo);

                    btnGoMain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences pref = getSharedPreferences("userData", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            TextView tvNick = f_shape.findViewById(R.id.tvViewPage);

                            String device_id = null;
                            String shape = null;

                            Set set = registerDeviceInfo.keySet();
                            for( Object o : set){
                                Log.d("ddd", o.toString());
                                if((((Map<String, String>)registerDeviceInfo.get(o.toString())).get("f_nick")).equals(tvNick.getText().toString())){
                                    device_id = o.toString();
                                    shape = (((Map<String, String>)registerDeviceInfo.get(o.toString())).get("f_shape"));
                                }
                            }

                            Log.d("device_id", device_id);
                            Log.d("f_shape", shape);

                            Log.d("ddd", tvNick.getText().toString());

                            editor.putString("f_shape", shape);
                            editor.putString("device_id", device_id);
                            editor.commit();

                            Intent intent = new Intent(ListActivity.this, MainActivity.class);
                            intent.putExtra("device_id", device_id);
                            intent.putExtra("f_shape", shape);
                            intent.putExtra("fnick", tvNick.getText().toString());
                            startActivity(intent);
                            finish();

                        }
                    });

                    break;

            }

        }
    };


}